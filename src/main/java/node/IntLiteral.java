package node;

public class IntLiteral extends Operand {
    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
