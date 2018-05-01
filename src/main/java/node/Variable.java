package node;

public class Variable extends Node {
    private String name;
    private VariableType variableType;
    private String stringValue;
    private int intValue;

    public void setName(String name) {
        this.name = name;
    }

    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
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
