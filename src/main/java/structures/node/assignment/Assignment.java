package structures.node.assignment;

import structures.node.Node;
import structures.node.NodeType;
import structures.node.assignable.Assignable;
import structures.node.assignable.variable.Variable;

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

    public void setValue(Assignable value) {
        this.value = value;
    }
}
