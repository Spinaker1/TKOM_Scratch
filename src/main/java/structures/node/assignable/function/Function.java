package structures.node.assignable.function;

import structures.node.NodeType;
import structures.node.assignable.Assignable;
import structures.node.assignable.Operand;

import java.util.LinkedList;

public class Function extends Operand {
    private FunctionType functionType;
    private LinkedList<Assignable> arguments;

    public Function(FunctionType functionType, LinkedList<Assignable> arguments) {
        this.nodeType = NodeType.FUNCTION;
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
