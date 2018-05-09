package node;

import token.TokenType;

import java.util.LinkedList;
import java.util.ListIterator;

public class Expression extends Operand {
    private LinkedList<TokenType> operators;
    private LinkedList<Operand> operands;

    public Expression(LinkedList<TokenType> operators, LinkedList<Operand> operands) {
        this.operators = operators;
        this.operands = operands;
    }

    public LinkedList<Operand> getOperands() {
        return operands;
    }

    public LinkedList<TokenType> getOperations() {
        return operators;
    }
}