package node;

public class Assignment extends Node {
    private Variable variable;
    private Assignable value;

    public Assignment(Variable variable, Assignable value) {
        this.nodeType = NodeType.ASSIGNMENT;
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public Assignable getValue() {
        return value;
    }
}
