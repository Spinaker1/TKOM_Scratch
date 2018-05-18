package executor;

import gui.Sprite;
import node.*;
import node.Event;
import token.EventType;

import java.awt.*;
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
                    executeIfStatement((IfStatement) instruction, scope);
                    break;

                case REPEAT_IF_STATEMENT:
                    executeRepeatIfStatement((RepeatIfStatement) instruction, scope);
                    break;

                case REPEAT_STATEMENT:
                    executeRepeatStatement((RepeatStatement) instruction);
                    break;
            }
        }
    }

    private void executeFunction(Function function, Scope scope)  {
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
                String value="";
                if (arguments.get(0).getNodeType() == NodeType.STRING_LITERAL) {
                    StringLiteral stringLiteral = (StringLiteral) arguments.get(0);
                    value = stringLiteral.getValue();
                }

                sprite.talk(value);
                break;
        }
    }

    private void executeAssignment(Assignment assignment, Scope scope)  {
        Assignable assignable = assignment.getValue();
        Variable variable = scope.getVariable(assignment.getVariable().getName());

        if (assignable.getNodeType() == NodeType.STRING_LITERAL) {
            StringLiteral stringLiteral = (StringLiteral) assignable;
            variable.setVariableType(VariableType.STRING);
            variable.setStringValue(stringLiteral.getValue());
        }
        else {
            Expression expression = (Expression) assignable;
            variable.setIntValue(executeAssignable(expression,scope));
            variable.setVariableType(VariableType.INT);
        }
    }

    private void executeIfStatement(IfStatement ifStatement, Scope scope)  {
        boolean result = executeCondition(ifStatement.getCondition(), scope);
        System.out.println(result);
        if (result) {
            executeBlock(ifStatement.getCodeBlock());
        }
    }

    private void executeRepeatStatement(RepeatStatement repeatStatement)  {
        Block block = repeatStatement.getCodeBlock();

        for (int i = 1; i <= repeatStatement.getRepeatingCount(); i++) {
            executeBlock(block);
        }
    }

    private void executeRepeatIfStatement(RepeatIfStatement repeatIfStatement, Scope scope)  {
        while (executeCondition(repeatIfStatement.getCondition(),scope)) {
            executeBlock(repeatIfStatement.getCodeBlock());
        }
    }

    private int executeAssignable(Assignable assignable, Scope scope)  {
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

    private boolean executeCondition(Condition condition, Scope scope) {
        if (condition.getOperators().size() == 0) {
            return executeCondition((Condition) condition.getOperands().get(0), scope);
        }

        int x1 = 0;
        int x2 = 0;
        switch (condition.getOperators().get(0)) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
                x1 = executeExpression((Expression) condition.getOperands().get(0), scope);
                x2 = executeExpression((Expression) condition.getOperands().get(1), scope);
        }

        switch (condition.getOperators().get(0)) {
            case EQUAL:
                return x1 == x2;
            case NOT_EQUAL:
                return x1 != x2;
            case LESS:
                return x1 < x2;
            case LESS_EQUAL:
                return x1 <= x2;
            case GREATER:
                return x1 > x2;
            case GREATER_EQUAL:
                return x1 >= x2;
            case OR:
                for (Node node: condition.getOperands()) {
                    Condition condition1 = (Condition) node;
                    if (executeCondition(condition1,scope)) {
                        return true;
                    }
                }
                return false;

            case AND:
                for (Node node: condition.getOperands()) {
                    Condition condition1 = (Condition) node;
                    if (!executeCondition(condition1,scope)) {
                        return false;
                    }
                }
                return true;
        }

        return true;
    }
}
