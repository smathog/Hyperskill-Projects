package metro;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

public class Line {
    private final String name;
    private final Station depot;

    /**
     * Creates a new line of given name containing a depot.
     * Implemented as a circular linked list starting and ending at depot.
     * @param name of the new line
     */
    public Line(String name) {
        this.name = name;
        this.depot = new Station("depot");
        this.depot.setNext(this.depot);
        this.depot.setPrevious(this.depot);
    }

    /**
     * Appends a new station of given name to the end of the line.
     * @param name of the station to be appended
     */
    public void appendStation(String name) {
        Station previous = this.depot.getPrevious();
        Station newStation = new Station(name);
        previous.setNext(newStation);
        this.depot.setPrevious(newStation);
        newStation.setPrevious(previous);
        newStation.setNext(depot);
    }

    /**
     * Appends a new station of given name to the start of the line.
     * @param name of the station to be appended
     */
    public void addHead(String name) {
        Station next = this.depot.getNext();
        Station newStation = new Station(name);
        next.setPrevious(newStation);
        this.depot.setNext(newStation);
        newStation.setPrevious(this.depot);
        newStation.setNext(next);
    }

    /**
     * Removes a station and returns true if the station is in the line; otherwise returns false
     * @param name of the station to be removed
     * @return Whether the station was successfully removed
     */
    public boolean removeStation(String name) {
        var target = getStation(name);
        if (target.isPresent()) {
                Station previous = target.get().getPrevious();
                Station next = target.get().getNext();
                previous.setNext(next);
                next.setPrevious(previous);
                target.get().getTransfer().ifPresent(s -> s.setTransfer(null));
                return true;
        } else {
            return false;
        }
    }

    /**
     * Creates a transfer between the station of name in this line and the station transfer in line
     * @param name of the station in this line to create a transfer in
     * @param line containing station transfer
     * @param transfer to this station
     */
    public void addTransfer(String name, Line line, String transfer) {
        var target = getStation(name);
        var otherTarget = line.getStation(transfer);
        if (target.isPresent() && otherTarget.isPresent()) {
            var station = target.get();
            var transferStation = otherTarget.get();
            station.setTransfer(transferStation);
        }
    }

    public void printLine() {
        Station current = this.depot;
        System.out.println(current.getName());
        if (current.getNext() == this.depot) {
            return;
        }
        current = current.getNext();
        while (current != this.depot) {
            if (current.getTransfer().isPresent()) {
                System.out.printf("%s - %s - (%s)%n",
                        current.getName(),
                        current.getTransfer().get().getName(),
                        current.getTransfer().get().getLineName());
            } else {
                System.out.println(current.getName());
            }
            current = current.getNext();
        }
        System.out.println(current.getName());
    }

    public String getName() {
        return name;
    }

    /**
     * Determines if a route exists between the first line/station and second line/station
     * @param first line
     * @param start station
     * @param second line
     * @param end station
     * @return A list representing a route, if it exists
     */
    private static Optional<ArrayList<Direction>> route(Line first, String start, Line second, String end) {
        //Note in directions:
        // 0 is origin, 1 is next, 2 is previous, 3 is transfer
        class Route {
            final Station station;
            final ArrayList<Direction> directions;

            public Route(Station station, ArrayList<Direction> directions, Direction direction) {
                this.station = station;
                this.directions = new ArrayList<>(directions);
                if (direction != null)
                    this.directions.add(direction);
            }
        }

        ArrayDeque<Route> queue = new ArrayDeque<>();
        HashSet<Station> visited = new HashSet<>();
        var startStation = first.getStation(start);
        var endStation = second.getStation(end);
        if (startStation.isPresent() && endStation.isPresent()) {
            queue.add(new Route(startStation.get(), new ArrayList<>(), null));
            visited.add(startStation.get());
            startStation.get().getTransfer().ifPresent(visited::add);
            while (!queue.isEmpty()) {
                Route current = queue.pollFirst();
                if (current.station.equals(endStation.get())) {
                    return Optional.of(current.directions);
                } else {
                    Station currentStation = current.station;
                    if (!currentStation.getName().equals("depot")) { //depots are sinks on the graph
                        if (!visited.contains(currentStation.getNext())) {
                            queue.add(new Route(currentStation.getNext(),
                                    current.directions,
                                    Direction.Next));
                            visited.add(currentStation.getNext());
                            currentStation.getTransfer().ifPresent(visited::add);
                        }
                        if (!visited.contains(currentStation.getPrevious())) {
                            queue.add(new Route(currentStation.getPrevious(),
                                    current.directions,
                                    Direction.Previous));
                            visited.add(currentStation.getPrevious());
                            currentStation.getTransfer().ifPresent(visited::add);
                        }
                        if (currentStation.getTransfer().isPresent()) {
                            currentStation = currentStation.getTransfer().get();
                            ArrayList<Direction> transferList = new ArrayList<>(current.directions);
                            transferList.add(Direction.Transfer);
                            if (!visited.contains(currentStation.getNext())) {
                                queue.add(new Route(currentStation.getNext(),
                                        transferList,
                                        Direction.Next));
                                visited.add(currentStation.getNext());
                                currentStation.getTransfer().ifPresent(visited::add);
                            }
                            if (!visited.contains(currentStation.getPrevious())) {
                                queue.add(new Route(currentStation.getPrevious(),
                                        transferList,
                                        Direction.Previous));
                                visited.add(currentStation.getPrevious());
                                currentStation.getTransfer().ifPresent(visited::add);
                            }
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
            // Two gets are safe because otherwise route wouldn't be present
            Station current = first.getStation(start).get();
            for (Direction direction : route.get()) {
                System.out.println(current.getName());
                switch (direction) {
                    case Next:
                        current = current.getNext();
                        break;
                    case Previous:
                        current = current.getPrevious();
                        break;
                    case Transfer:
                        // Know this is safe because it is in the route
                        current = current.getTransfer().get();
                        System.out.printf("Transition to line %s%n", current.getLineName());
                        break;
                }
            }
            System.out.println(current.getName());
        } else {
            System.out.println("No route exists!");
        }
    }

    /**
     * Iterates through the line and finds the station matching name, if it exists.
     * @param name of the station to find
     * @return Optional of station if possible, else empty Optional.
     */
    private Optional<Station> getStation(String name) {
        Station currentStation = this.depot.getNext();
        while (currentStation != this.depot) {
            if (currentStation.getName().equals(name)) {
                return Optional.of(currentStation);
            }
            currentStation = currentStation.getNext();
        }
        return Optional.empty();
    }

    private enum Direction {
        Next,
        Previous,
        Transfer,
    }

    private class Station {
        private final String name;
        private Station previous;
        private Station next;
        private Station transfer;

        private Station(String name) {
            this.name = name;
            this.previous = null;
            this.next = null;
            this.transfer = null;
        }

        public String getName() {
            return name;
        }

        public String getLineName() {
            return Line.this.name;
        }

        public Station getPrevious() {
            return previous;
        }

        public void setPrevious(Station previous) {
            this.previous = previous;
        }

        public Station getNext() {
            return next;
        }

        public void setNext(Station next) {
            this.next = next;
        }

        public Optional<Station> getTransfer() {
            return Optional.ofNullable(this.transfer);
        }

        public void setTransfer(Station transfer) {
            this.transfer = transfer;
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
    }
}
