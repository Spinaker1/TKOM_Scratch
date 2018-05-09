package node;

public class Assignment extends Node {
    private Variable variable;
    private Node value;

    public Assignment(Variable variable, Node value) {
        this.variable = variable;
        this.value = value;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public void setValue(Node value) {
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public Node getValue() {
        return value;
    }
}
