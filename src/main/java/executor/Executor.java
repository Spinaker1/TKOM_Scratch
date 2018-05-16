package executor;

import gui.Sprite;
import node.*;
import token.EventType;

import java.util.LinkedList;

public class Executor {
    private Sprite sprite;

    public void execute(Sprite sprite, EventType eventType)  {
        this.sprite = sprite;

        Program program;
        if ((program = sprite.getProgram()) == null) {
            return;
        }

        for (Event event : program.getEvents()) {
            if (event.getEventType() == eventType) {
                executeEvent(event);
                break;
            }
        }
    }

    private void executeEvent(Event event)  {
        executeBlock(event.getCodeBlock());
    }

    private void executeBlock(Block block) {
        Scope scope = block.getScope();

        for (Node instruction : block.getInstructions()) {
            switch (instruction.getNodeType()) {
                case FUNCTION:
                    executeFunction((Function) instruction, scope);
                    break;

                case ASSIGNMENT:
                    executeAssignment((Assignment) instruction,scope);
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

    private void executeFunction(Function function, Scope scope)  {
        System.out.println("function");
        LinkedList<Assignable> arguments = function.getArguments();

        for(Assignable argument: arguments) {
            executeAssignable(argument, scope);
        }

        switch (function.getFunctionType()) {
            case GO_TO_MOUSE:
                sprite.moveToMouse();
                break;

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

            case ROTATE_LEFT:
                sprite.rotateLeft(((Expression)arguments.get(0)).getValue());
                break;

            case ROTATE_RIGHT:
                sprite.rotateRight(((Expression)arguments.get(0)).getValue());
                break;

            case CHANGE_SIZE:
                sprite.changeSize((((Expression)arguments.get(0)).getValue()));
                break;

            case CHANGE_COLOR:
                sprite.changeColor(((Expression)arguments.get(0)).getValue(),
                        ((Expression)arguments.get(1)).getValue(),
                        ((Expression)arguments.get(2)).getValue());
                break;

            case WAIT:
                sprite.sleep(((Expression)arguments.get(0)).getValue());
                break;

            case GO:
                sprite.move(((Expression)arguments.get(0)).getValue(),
                        ((Expression)arguments.get(1)).getValue());
                break;

            case TALK:
                String value;
                StringLiteral stringLiteral = (StringLiteral) arguments.get(0);
                value = stringLiteral.getValue();
                sprite.talk(value);
                break;
        }
    }

    private void executeAssignment(Assignment assignment, Scope scope)  {
        System.out.println("assigment");
        Assignable assignable = assignment.getValue();
        Variable variable = scope.getVariable(assignment.getVariable().getName());

        if (assignable.getNodeType() == NodeType.STRING_LITERAL) {

        }
        else {
            Expression expression = (Expression) assignable;
            variable.setIntValue(executeAssignable(expression,scope));
            System.out.println(variable.getIntValue());
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

    private int executeAssignable(Assignable assignable, Scope scope)  {
        System.out.println("assignable");
        if (assignable.getNodeType() == NodeType.INT_LITERAL) {
            return ((IntLiteral)assignable).getValue();
        }

        if (assignable.getNodeType() == NodeType.FUNCTION) {
            Function function = (Function) assignable;
            switch (function.getFunctionType()) {
                case GET_X:
                    return (int)sprite.getX();
                case GET_Y:
                    return (int)sprite.getY();
                case GET_ROTATION:
                    return (int)sprite.getRotate();
            }
        }

        if (assignable.getNodeType() == NodeType.EXPRESSION) {
            Expression expression = (Expression) assignable;
            return executeExpression(expression,scope);
        }

        if (assignable.getNodeType() == NodeType.VARIABLE) {
            Variable variable = (Variable) assignable;
            variable = scope.getVariable(variable.getName());
            return variable.getIntValue();
        }

        return 0;
    }

    private int executeExpression(Expression expression, Scope scope) {
        int i = -1;
        int value = 0;

        for (Assignable operand: expression.getOperands()) {
            if (i == -1) {
                value = executeAssignable(operand,scope);
            }
            else {
                switch(expression.getOperators().get(i)) {
                    case ADD:
                        value+=executeAssignable(operand,scope);
                        break;
                    case MINUS:
                        value-=executeAssignable(operand,scope);
                        break;
                    case MULTIPLY:
                        value*=executeAssignable(operand,scope);
                        break;
                    case DIVIDE:
                        value/=executeAssignable(operand,scope);
                        break;
                }
            }

            i++;
        }

        expression.setValue(value);
        return value;
    }
}
