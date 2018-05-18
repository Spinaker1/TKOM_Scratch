package node;

import token.TokenType;

import java.util.LinkedList;
import java.util.ListIterator;

public class Expression extends Operand {
    private LinkedList<TokenType> operators;
    private LinkedList<Operand> operands;
    private int value;
    private boolean isNegated = false;

    public Expression(LinkedList<TokenType> operators, LinkedList<Operand> operands) {
        this.operators = operators;
        this.operands = operands;
        this.nodeType = NodeType.EXPRESSION;
    }

    public LinkedList<Operand> getOperands() {
        return operands;
    }

    public LinkedList<TokenType> getOperators() {
        return operators;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public void setNegated(boolean negated) {
        isNegated = negated;
    }
}