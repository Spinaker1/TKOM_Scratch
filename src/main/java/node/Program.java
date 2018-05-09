package node;

import java.util.LinkedList;

public class Program {
    private LinkedList<Event> events;

    public Program(LinkedList<Event> events) {
        this.events = events;
    }

    public LinkedList<Event> getEvents() {
        return events;
    }
}
