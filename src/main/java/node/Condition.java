package node;

import token.TokenType;

import java.util.LinkedList;

public class Condition extends Node{
    private LinkedList<TokenType> operators;
    private LinkedList<Node> operands;

    public Condition(LinkedList<TokenType> operators, LinkedList<Node> operands) {
        this.operators = operators;
        this.operands = operands;
        this.nodeType = NodeType.CONDITION;
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

    public LinkedList<TokenType> getOperators() {
        return operators;
    }
}
