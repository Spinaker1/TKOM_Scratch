package node;

import java.util.LinkedList;

public class Program {
    private LinkedList<Event> events = new LinkedList<>();

    public void addEvent(Event event) {
        events.add(event);
    }

    public LinkedList<Event> getArguments() {
        return events;
    }
}
