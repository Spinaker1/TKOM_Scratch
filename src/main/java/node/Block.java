package node;

import java.util.LinkedList;

public class Block extends Node {
    private LinkedList<Node> instructions;

    public Block(LinkedList<Node> instructions) {
        this.instructions = instructions;
        this.nodeType = NodeType.BLOCK;
    }

    public LinkedList<Node> getInstructions() {
        return instructions;
    }
}
