package structures.node.assignable.literal;

import structures.node.NodeType;
import structures.node.assignable.Operand;

public class IntLiteral extends Operand {
    private int value;

    public IntLiteral(int value) {
        this.nodeType = NodeType.INT_LITERAL;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
