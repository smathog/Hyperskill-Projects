package metro;

import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

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
    }
}
