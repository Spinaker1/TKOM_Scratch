package node;

public class Variable extends Operand {
    private String name;
    private VariableType variableType;
    private String stringValue;
    private int intValue;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariableType getVariableType() {
        return variableType;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
