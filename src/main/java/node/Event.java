package node;

public class Event extends Node {
    private String name;
    private String argument;
    private Block codeBlock;

    public Event(String name, String argument, Block codeBlock) {
        this.name = name;
        this.argument = argument;
        this.codeBlock = codeBlock;
    }

    public String getName() {
        return name;
    }

    public String getArgument() {
        return argument;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }
}
