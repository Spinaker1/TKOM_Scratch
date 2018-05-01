package node;

public class Assignment extends Node {
    private Variable variable;
    private Node value;

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public void setValue(Node value) {
        this.value = value;
    }
}
