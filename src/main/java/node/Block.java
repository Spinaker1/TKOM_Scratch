package node;

import java.util.LinkedList;

public class Block extends Node {
    private LinkedList<Node> instructions;
    private Scope scope;

    public Block(LinkedList<Node> instructions) {
        this.instructions = instructions;
        this.nodeType = NodeType.BLOCK;
    }

    public LinkedList<Node> getInstructions() {
        return instructions;
    }

    public void setInstructions(LinkedList<Node> instructions) {
        this.instructions = instructions;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
