package node;

import token.EventType;

public class Event extends Node {
    private EventType eventType;
    private String argument;
    private Block codeBlock;

    public Event(EventType eventType, String argument, Block codeBlock) {
        this.eventType = eventType;
        this.argument = argument;
        this.codeBlock = codeBlock;
        this.nodeType = NodeType.EVENT;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getArgument() {
        return argument;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }
}
