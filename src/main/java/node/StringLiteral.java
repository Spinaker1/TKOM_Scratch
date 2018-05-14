package node;

public class StringLiteral extends Assignable {
    private String value;

    public StringLiteral(String value) {
        this.nodeType = NodeType.STRING_LITERAL;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
