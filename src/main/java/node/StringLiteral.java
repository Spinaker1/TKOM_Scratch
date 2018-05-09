package node;

public class StringLiteral extends Assignable {
    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
