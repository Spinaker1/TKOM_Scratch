package node;

import token.TokenType;

import java.util.LinkedList;

public class Condition extends Node{
    private LinkedList<TokenType> operators = new LinkedList<>();
    private LinkedList<Node> operands = new LinkedList<>();

    public Condition() { }

    public Condition(LinkedList<TokenType> operators, LinkedList<Node> operands) {
        this.operators = operators;
        this.operands = operands;
    }

    public void addOperator(final TokenType operator) {
        operators.add(operator);
    }

    public void addOperand(final Node operand) {
        operands.add(operand);
    }

    public LinkedList<Node> getOperands() {
        return operands;
    }

    public LinkedList<TokenType> getoperators() {
        return operators;
    }
}
