package node;

import token.FunctionType;

import java.util.LinkedList;

public class Function extends Operand {
    private FunctionType functionType;
    private LinkedList<Assignable> arguments;

    public Function(FunctionType functionType, LinkedList<Assignable> arguments) {
        this.functionType = functionType;
        this.arguments = arguments;
    }

    public LinkedList<Assignable> getArguments() {
        return arguments;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }
}
