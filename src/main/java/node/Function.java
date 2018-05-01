package node;

import java.util.LinkedList;

public class Function extends Node {
    private String name;
    private LinkedList<Node> arguments = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void addArgument(Node argument) {
        arguments.add(argument);
    }

    public LinkedList<Node> getArguments() {
        return arguments;
    }
}
