package node;

import java.util.LinkedList;

public class Block {
    private LinkedList<Node> instructions;

    public Block(LinkedList<Node> instructions) {
        this.instructions = instructions;
    }

    public LinkedList<Node> getInstructions() {
        return instructions;
    }
}
