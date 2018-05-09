package node;

import java.util.LinkedList;

public class Program {
    private LinkedList<Event> events = new LinkedList<>();

    public Program(LinkedList<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public LinkedList<Event> getEvents() {
        return events;
    }
}
