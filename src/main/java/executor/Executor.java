package executor;

import gui.Sprite;
import node.*;
import node.Event;
import semantic.FunctionArgumentsHashMap;
import token.EventType;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;

public class Executor {
    private Sprite sprite;

    public void execute(Sprite sprite, EventType eventType) throws Exception {
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

    private void executeEvent(Event event) throws Exception {
        executeBlock(event.getCodeBlock());
    }

    private void executeBlock(Block block) throws Exception {
        Scope scope = block.getScope();

        for (Node instruction : block.getInstructions()) {
            switch (instruction.getNodeType()) {
                case FUNCTION:
                    executeFunction((Function) instruction, scope);
                    break;

                case ASSIGNMENT:
                    executeAssignment((Assignment) instruction, scope);
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

    private void checkArgumentsType(Function function, Scope scope) throws Exception {
        int i = 0;
        VariableType[] expectedVariableTypes = FunctionArgumentsHashMap.FUNCTION_ARGUMENTS.get(function.getFunctionType());
        for (Assignable argument : function.getArguments()) {
            if (expectedVariableTypes[i] == VariableType.STRING) {
                if (argument.getNodeType() == NodeType.STRING_LITERAL) {
                    return;
                } else {
                    String value;
                    if ((value = checkStringVariable(argument, scope)) != null) {
                        function.getArguments().set(i, new StringLiteral(value));
                        return;
                    } else {
                        throw new Exception("Argument powinien zawierać napis.");
                    }
                }
            }
            if (expectedVariableTypes[i] == VariableType.INT) {
                if (argument.getNodeType() == NodeType.STRING_LITERAL) {
                    throw new Exception("Argument powinien zawierać liczbę całkowitą.");
                } else {
                    executeAssignable(argument, scope);
                }
            }
        }
    }

    private void executeFunction(Function function, Scope scope) throws Exception {
        checkArgumentsType(function, scope);

        LinkedList<Assignable> arguments = function.getArguments();

        switch (function.getFunctionType()) {
            case GO_TO_MOUSE:
                sprite.moveToMouse();
                break;

            case GO_LEFT:
                sprite.moveLeft(((Expression) arguments.get(0)).getValue());
                break;

            case GO_RIGHT:
                sprite.moveRight(((Expression) arguments.get(0)).getValue());
                break;

            case GO_UP:
                sprite.moveUp(((Expression) arguments.get(0)).getValue());
                break;

            case GO_DOWN:
                sprite.moveDown(((Expression) arguments.get(0)).getValue());
                break;

            case ROTATE_LEFT:
                sprite.rotateLeft(((Expression) arguments.get(0)).getValue());
                break;

            case ROTATE_RIGHT:
                sprite.rotateRight(((Expression) arguments.get(0)).getValue());
                break;

            case CHANGE_SIZE:
                int size = (((Expression) arguments.get(0)).getValue());

                if (size < 0) {
                    throw new Exception("Funkcja zmienKolor() nie przyjmuje ujemnych argumentów.");
                }

                sprite.changeSize(size);
                break;

            case CHANGE_COLOR:
                int r = ((Expression) arguments.get(0)).getValue();
                int g = ((Expression) arguments.get(1)).getValue();
                int b = ((Expression) arguments.get(2)).getValue();

                if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
                    throw new Exception("Podany argument jest poza zakresem RGB 0-255");
                }

                sprite.changeColor(r, g, b);
                break;

            case WAIT:
                int seconds = (((Expression) arguments.get(0)).getValue());

                if (seconds < 0) {
                    throw new Exception("Funkcja czekaj() nie przyjmuje ujemnych argumentów.");
                }

                sprite.sleep(seconds);
                break;

            case GO:
                sprite.move(((Expression) arguments.get(0)).getValue(),
                        ((Expression) arguments.get(1)).getValue());
                break;

            case TALK:
                String value = "";
                if (arguments.get(0).getNodeType() == NodeType.STRING_LITERAL) {
                    StringLiteral stringLiteral = (StringLiteral) arguments.get(0);
                    value = stringLiteral.getValue();
                }

                sprite.talk(value);
                break;
        }
    }

    private void executeAssignment(Assignment assignment, Scope scope) throws Exception {
        Assignable assignable = assignment.getValue();
        Variable variable = scope.getVariable(assignment.getVariable().getName());

        if (assignable.getNodeType() == NodeType.STRING_LITERAL) {
            StringLiteral stringLiteral = (StringLiteral) assignable;
            variable.setVariableType(VariableType.STRING);
            variable.setStringValue(stringLiteral.getValue());
        } else {
            Expression expression = (Expression) assignable;

            String value;
            if ((value = checkStringVariable(expression, scope)) != null) {
                variable.setVariableType(VariableType.STRING);
                variable.setStringValue(value);
            } else {
                variable.setIntValue(executeAssignable(expression, scope));
                variable.setVariableType(VariableType.INT);
            }
        }
    }

    private void executeIfStatement(IfStatement ifStatement, Scope scope) throws Exception {
        boolean result = executeCondition(ifStatement.getCondition(), scope);
        System.out.println(result);
        if (result) {
            executeBlock(ifStatement.getCodeBlock());
        }
    }

    private void executeRepeatStatement(RepeatStatement repeatStatement) throws Exception {
        Block block = repeatStatement.getCodeBlock();

        for (int i = 1; i <= repeatStatement.getRepeatingCount(); i++) {
            executeBlock(block);
        }
    }

    private void executeRepeatIfStatement(RepeatIfStatement repeatIfStatement, Scope scope) throws Exception {
        while (executeCondition(repeatIfStatement.getCondition(), scope)) {
            executeBlock(repeatIfStatement.getCodeBlock());
        }
    }

    private int executeAssignable(Assignable assignable, Scope scope) throws Exception {
        if (assignable.getNodeType() == NodeType.INT_LITERAL) {
            return ((IntLiteral) assignable).getValue();
        }

        if (assignable.getNodeType() == NodeType.FUNCTION) {
            Function function = (Function) assignable;
            switch (function.getFunctionType()) {
                case GET_X:
                    return (int) sprite.getX();
                case GET_Y:
                    return (int) sprite.getY();
                case GET_ROTATION:
                    return (int) sprite.getRotate();
            }
        }

        if (assignable.getNodeType() == NodeType.EXPRESSION) {
            Expression expression = (Expression) assignable;
            return executeExpression(expression, scope);
        }

        if (assignable.getNodeType() == NodeType.VARIABLE) {
            Variable variable = (Variable) assignable;
            variable = scope.getVariable(variable.getName());
            return variable.getIntValue();
        }

        return 0;
    }

    private int executeExpression(Expression expression, Scope scope) throws Exception {
        for (Assignable operand : expression.getOperands()) {
            if (operand.getNodeType() == NodeType.VARIABLE) {
                Variable variable = (Variable) operand;
                variable = scope.getVariable(variable.getName());
                System.out.println(variable.getVariableType());
                if (variable.getVariableType() == VariableType.STRING) {
                    throw new Exception("Zmienna powinna zawierać liczbę.");
                }
            }
        }

        int i = -1;
        int value = 0;

        for (Assignable operand : expression.getOperands()) {
            if (i == -1) {
                value = executeAssignable(operand, scope);
            } else {
                switch (expression.getOperators().get(i)) {
                    case ADD:
                        value += executeAssignable(operand, scope);
                        break;
                    case MINUS:
                        value -= executeAssignable(operand, scope);
                        break;
                    case MULTIPLY:
                        value *= executeAssignable(operand, scope);
                        break;
                    case DIVIDE:
                        value /= executeAssignable(operand, scope);
                        break;
                }
            }

            i++;
        }

        expression.setValue(value);
        return value;
    }

    private boolean executeCondition(Expression condition, Scope scope) throws Exception {
        if (condition.getOperators().size() == 0) {
            return executeCondition((Expression) condition.getOperands().get(0), scope) ^ condition.isNegated();
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
                return (x1 == x2) ^ condition.isNegated();
            case NOT_EQUAL:
                return (x1 != x2) ^ condition.isNegated();
            case LESS:
                return x1 < x2 ^ condition.isNegated();
            case LESS_EQUAL:
                return x1 <= x2 ^ condition.isNegated();
            case GREATER:
                return x1 > x2 ^ condition.isNegated();
            case GREATER_EQUAL:
                return x1 >= x2 ^ condition.isNegated();
            case OR:
                for (Node node : condition.getOperands()) {
                    Expression condition1 = (Expression) node;
                    if (executeCondition(condition1, scope)) {
                        return !condition.isNegated();
                    }
                }
                return condition.isNegated();

            case AND:
                for (Node node : condition.getOperands()) {
                    Expression condition1 = (Expression) node;
                    if (!executeCondition(condition1, scope)) {
                        return condition.isNegated();
                    }
                }
                return !condition.isNegated();
        }

        return true;
    }

    private String checkStringVariable(Assignable assignable, Scope scope) {
        while (assignable.getNodeType() == NodeType.EXPRESSION) {
            LinkedList<Operand> operands = ((Expression) assignable).getOperands();
            if (operands.size() != 1) {
                return null;
            }
            assignable = operands.get(0);
        }

        if (assignable.getNodeType() != NodeType.VARIABLE) {
            return null;
        }

        Variable variable = (Variable) assignable;
        if (!scope.containsVariable(variable.getName())) {
            return null;
        }

        variable = scope.getVariable(variable.getName());
        if (variable.getVariableType() != VariableType.STRING) {
            return null;
        }

        return variable.getStringValue();
    }

}
