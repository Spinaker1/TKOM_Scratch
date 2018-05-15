import gui.Sprite;
import node.*;

import java.util.LinkedList;

public class Executor {
    private Sprite sprite;

    public void execute(Sprite sprite, Program program)  {
        this.sprite = sprite;

        for (Event event : program.getEvents()) {
            executeEvent(event);
        }
    }

    private void executeEvent(Event event)  {
        executeBlock(event.getCodeBlock());
    }

    private void executeBlock(Block block) {
        for (Node instruction : block.getInstructions()) {
            switch (instruction.getNodeType()) {
                case FUNCTION:
                    executeFunction((Function) instruction);
                    break;

                case ASSIGMENT:
                    executeAssignment((Assignment) instruction);
                    break;

                case IF_STATEMENT:
                    executeIfStatement((IfStatement) instruction);
                    break;

                case REPEAT_IF_STATEMENT:
                    executeRepeatIfStatement((RepeatIfStatement) instruction);
                    break;

                case REPEAT_STATEMENT:
                    executeRepeatStatement((RepeatStatement) instruction);
                    break;
            }
        }
    }


    private void executeFunction(Function function)  {
        System.out.println("function");
        LinkedList<Assignable> arguments = function.getArguments();

        for(Assignable argument: arguments) {
            executeAssignable(argument);
        }

        switch (function.getFunctionType()) {
            case GO_LEFT:
                sprite.moveLeft(((Expression)arguments.get(0)).getValue());
                break;

            case GO_RIGHT:
                sprite.moveRight(((Expression)arguments.get(0)).getValue());
                break;

            case GO_UP:
                sprite.moveUp(((Expression)arguments.get(0)).getValue());
                break;

            case GO_DOWN:
                sprite.moveDown(((Expression)arguments.get(0)).getValue());
                break;
        }
    }

    private void executeAssignment(Assignment assignment)  {
        System.out.println("assigment");
        Assignable assignable = assignment.getValue();
        Variable variable = assignment.getVariable();

        if (assignable.getNodeType() == NodeType.STRING_LITERAL) {
            variable.setVariableType(VariableType.STRING);
            variable.setStringValue(((StringLiteral) assignable).getValue());
        }
        else {
            Expression expression = (Expression) assignable;
            executeAssignable(expression);
        }
    }

    private void executeIfStatement(IfStatement ifStatement)  {

    }

    private void executeRepeatStatement(RepeatStatement repeatStatement)  {
        Block block = repeatStatement.getCodeBlock();

        for (int i = 1; i <= repeatStatement.getRepeatingCount(); i++) {
            executeBlock(block);
        }
    }

    private void executeRepeatIfStatement(RepeatIfStatement repeatIfStatement)  {

    }

    private int executeAssignable(Assignable assignable)  {
        System.out.println("assignable");
        if (assignable.getNodeType() == NodeType.INT_LITERAL) {
            System.out.println(((IntLiteral)assignable).getValue());
            return ((IntLiteral)assignable).getValue();
        }

        else if (assignable.getNodeType() == NodeType.EXPRESSION) {
            Expression expression = (Expression) assignable;
            int i = -1;
            int value = 0;

            for (Assignable operand: expression.getOperands()) {
                if (i == -1) {
                    value = executeAssignable(operand);
                }
                else {
                    switch(expression.getOperations().get(i)) {
                        case ADD:
                            value+=executeAssignable(operand);
                            break;
                        case MINUS:
                            value-=executeAssignable(operand);
                            break;
                        case MULTIPLY:
                            value*=executeAssignable(operand);
                            break;
                        case DIVIDE:
                            value/=executeAssignable(operand);
                            break;
                    }
                }

                i++;
            }

            expression.setValue(value);
            return value;
        }

        return 0;
    }
}
