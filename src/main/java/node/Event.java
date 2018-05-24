package node;

import token.EventType;

public class Event extends Node {
    private EventType eventType;
    private Block codeBlock;

    public Event(EventType eventType, Block codeBlock) {
        this.eventType = eventType;
        this.codeBlock = codeBlock;
        this.nodeType = NodeType.EVENT;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }
}
