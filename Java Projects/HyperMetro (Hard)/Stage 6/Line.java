package metro;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Line {
    private final String name;
    private final HashMap<String, Station> stationMap;

    /**
     * Creates a new line of given name containing a depot.
     * Implemented as a circular linked list starting and ending at depot.
     * @param name of the new line
     */
    public Line(String name) {
        this.name = name;
        this.stationMap = new HashMap<>();
    }

    /**
     * Creates a station for this line
     * @param name of the station
     * @param next value of time to go to a next station from this one
     */
    public void createStation(String name, int next) {
        stationMap.put(name, new Station(name, next));
    }

    public void setNext(String stationName, List<String> nextNames) {
        var nextSet = stationMap.get(stationName).getNext();
        nextSet.addAll(nextNames.stream()
                .map(stationMap::get)
                .collect(Collectors.toSet()));
    }

    public void setPrev(String stationName, List<String> prevNames) {
        var prevSet = stationMap.get(stationName).getPrevious();
        prevSet.addAll(prevNames.stream()
                .map(stationMap::get)
                .collect(Collectors.toSet()));
    }

    public void addTransfer(String name, Line other, String transferTo) {
        Station station = stationMap.get(name);
        station.getTransfer().add(other.stationMap.get(transferTo));
    }

    /**
     * Determines if a route exists between the first line/station and second line/station
     * @param first line
     * @param start station
     * @param second line
     * @param end station
     * @return A list representing a route, if it exists
     */
    private static Optional<ArrayList<Station>> route(Line first, String start, Line second, String end) {
        class Route {
            final Station station;
            final ArrayList<Station> directions;

            public Route(Station station, List<Station> directions) {
                this.station = station;
                this.directions = new ArrayList<>(directions);
                this.directions.add(station);
            }
        }

        ArrayDeque<Route> queue = new ArrayDeque<>();
        HashSet<Station> visited = new HashSet<>();
        var startStation = Optional.ofNullable(first.stationMap.get(start));
        var endStation = Optional.ofNullable(second.stationMap.get(end));
        if (startStation.isPresent() && endStation.isPresent()) {
            queue.add(new Route(startStation.get(), new ArrayList<>()));
            visited.add(startStation.get());
            visited.addAll(startStation.get().getTransfer());
            while (!queue.isEmpty()) {
                Route current = queue.pollFirst();
                if (current.station.equals(endStation.get())) {
                    return Optional.of(current.directions);
                } else {
                    Station currentStation = current.station;

                    // Functor to do next iteration of queue addition
                    BiConsumer<HashSet<Station>, List<Station>> addStations = (set, list) -> set.stream()
                                    .filter(Predicate.not(visited::contains)).forEach(s -> {
                                queue.add(new Route(s, list));
                                visited.add(s);
                                visited.addAll(s.getTransfer());
                            });

                    // Add next and previous stations to queue
                    addStations.accept(currentStation.getNext(), current.directions);
                    addStations.accept(currentStation.getPrevious(), current.directions);

                    // If this station is a transfer, include the other previous and next sets:
                    if (!currentStation.getTransfer().isEmpty()) {
                        for (Station transfer : currentStation.getTransfer()) {
                            ArrayList<Station> transferList = new ArrayList<>(current.directions);
                            transferList.add(transfer);

                            //Add next and previous stations to queue for this transfer:
                            addStations.accept(transfer.getNext(), transferList);
                            addStations.accept(transfer.getPrevious(), transferList);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static void printRoute(Line first, String start, Line second, String end) {
        var route = route(first, start, second, end);
        if (route.isPresent()) {
            printRoute(route.get());
        } else {
            System.out.println("No route exists!");
        }
    }

    private static void printRoute(List<Station> stationList) {
        String lineName = stationList.get(0).getLineName();
        for (Station station : stationList) {
            if (!lineName.equals(station.getLineName())) {
                System.out.printf("Transition to line %s%n", station.getLineName());
                lineName = station.getLineName();
            }
            System.out.println(station.getName());
        }
    }

    /**
     * Same as previous function but finds the fastest route weighted for time using Dijkstra's Algorithm.
     */
    private static Optional<Map<Integer, ArrayList<Station>>> fastestRoute(Line first, String start, Line second, String end) {
        class Route {
            final Station station;
            final ArrayList<Station> directions;
            final int time;

            public Route(Station station, List<Station> directions, int time) {
                this.station = station;
                this.directions = new ArrayList<>(directions);
                this.directions.add(station);
                this.time = time;
            }
        }

        PriorityQueue<Route> queue = new PriorityQueue<>(Comparator.comparingInt((Route r) -> r.time));
        HashSet<Station> visited = new HashSet<>();
        var startStation = Optional.ofNullable(first.stationMap.get(start));
        var endStation = Optional.ofNullable(second.stationMap.get(end));
        if (startStation.isPresent() && endStation.isPresent()) {
            queue.add(new Route(startStation.get(), new ArrayList<>(), 0));
            while (!queue.isEmpty()) {
                Route current = queue.poll();
                if (visited.contains(current.station)) {
                    // Do nothing
                    continue;
                } else if (current.station.equals(endStation.get())) {
                    return Optional.of(Collections.singletonMap(current.time, current.directions));
                } else {
                    Station currentStation = current.station;
                    visited.add(currentStation);

                    // Functor to manage next round of additions for priority queue
                    BiConsumer<HashSet<Station>, Character> addStations = (set, dir) -> set.stream()
                            .filter(Predicate.not(visited::contains))
                            .forEach(s -> queue.add(new Route(s, current.directions,
                                    current.time + (dir == 't' ? currentStation.getTransferTime()
                                            : (dir == 'n' ? currentStation.getNextTime() : s.getNextTime())))));

                    // Add unvisited next, previous, and transfer stations to priority queue
                    addStations.accept(currentStation.getNext(), 'n');
                    addStations.accept(currentStation.getPrevious(), 'p');
                    addStations.accept(currentStation.getTransfer(), 't');
                }
            }
        }
        return Optional.empty();
    }

    public static void printFastestRoute(Line first, String start, Line second, String end) {
        var route = fastestRoute(first, start, second, end);
        if (route.isPresent()) {
            // Two gets are safe because otherwise route wouldn't be present
            Map<Integer, ArrayList<Station>> solutionMap = route.get();
            solutionMap.forEach((key, value) -> {
                printRoute(value);
                System.out.printf("Total: %d minutes in the way%n", key);
            });
        } else {
            System.out.println("No route exists!");
        }
    }

    private class Station {
        private final String name;
        private final HashSet<Station> previous;
        // Time to travel between this station and previous
        private final HashSet<Station> next;
        // Time to travel between this station and next
        private final int nextTime;
        private final HashSet<Station> transfer;

        private Station(String name, int nextTime) {
            this.name = name;
            this.previous = new HashSet<>();
            this.next = new HashSet<>();
            this.transfer = new HashSet<>();
            this.nextTime = nextTime;
        }

        public String getName() {
            return name;
        }

        public String getLineName() {
            return Line.this.name;
        }

        public HashSet<Station> getPrevious() {
            return previous;
        }

        public HashSet<Station> getNext() {
            return next;
        }

        public HashSet<Station> getTransfer() {
            return transfer;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Line.this.name, this.name);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (other == null) {
                return false;
            } else if (this.getClass() != other.getClass()) {
                return false;
            } else {
                Station otherStation = (Station) other;
                return this.name.equals(otherStation.name)
                        && Line.this.name.equals(otherStation.getLineName());
            }
        }

        public int getTransferTime() {
            // Time to transfer lines at this station
            return 5;
        }

        public int getNextTime() {
            return nextTime;
        }
    }
}
