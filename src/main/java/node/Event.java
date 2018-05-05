package node;

public class Event extends Node {
    private String name;
    private String argument;
    private Block codeBlock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(Block codeBlock) {
        this.codeBlock = codeBlock;
    }
}
