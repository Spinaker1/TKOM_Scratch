package node;

import java.util.LinkedList;

public class Function extends Operand {
    private String name;
    private LinkedList<Assignable> arguments = new LinkedList<>();

    public Function(String name, LinkedList<Assignable> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Assignable> getArguments() {
        return arguments;
    }
}
