package node;

import java.util.LinkedList;

public class Block {
    private LinkedList<Node> instructions = new LinkedList<>();

    public void addInstruction(final Node instruction) {
        instructions.add(instruction);
    }

    public LinkedList<Node> getInstructions() {
        return instructions;
    }
}
