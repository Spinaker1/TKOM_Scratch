package node;

import token.TokenType;

import java.util.LinkedList;

public class Condition extends Node{
    private LinkedList<TokenType> operations = new LinkedList<>();
    private LinkedList<Node> operands = new LinkedList<>();

    public void addOperator(final TokenType operator) {
        operations.add(operator);
    }

    public void addOperand(final Node operand) {
        operands.add(operand);
    }

    public LinkedList<Node> getOperands() {
        return operands;
    }

    public LinkedList<TokenType> getOperations() {
        return operations;
    }
}
