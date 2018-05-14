package semantic;

import token.EventType;
import token.Token;
import node.*;
import token.TokenType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SemanticParser {
    private Map<String, Token> variables = new HashMap<>();

    public void check(Program program) throws Exception {
        for (Event event : program.getEvents()) {
            checkEvent(event);
        }
    }

    private void checkEvent(Event event) throws Exception {
        if (event.getEventType() != EventType.COLLISION && event.getArgument() != null) {
            throw new Exception("Zdarzenie tego typu nie powinno mieć argumentu.");
        }

        checkBlock(event.getCodeBlock());
    }

    private void checkBlock(Block block) throws Exception {
        for (Node instruction : block.getInstructions()) {
            switch (instruction.getNodeType()) {
                case FUNCTION:
                    checkFunction((Function) instruction);
                    break;

                case ASSIGMENT:
                    checkAssignment((Assignment) instruction);
                    break;

                case IF_STATEMENT:
                    checkIfStatement((IfStatement) instruction);
                    break;

                case REPEAT_IF_STATEMENT:
                    checkRepeatIfStatement((RepeatIfStatement) instruction);
                    break;

                case REPEAT_STATEMENT:
                    checkRepeatStatement((RepeatStatement) instruction);
                    break;
            }
        }
    }

    private void checkArgumentsCount(LinkedList<Assignable> arguments, int argumentsCount) throws Exception {
        if (arguments.size() != argumentsCount) {
            switch (argumentsCount) {
                case 0:
                    throw new Exception("Funkcja nie powinna zawierać argumentów");
                case 1:
                    throw new Exception("Funkcja powinna zawierać 1 argument");
                case 2:
                case 3:
                case 4:
                    throw new Exception("Funkcja powinna zawierać " + argumentsCount + " argumenty");
                default:
                    throw new Exception("Funkcja powinna zawierać " + argumentsCount + " argumentów");
            }
        }
    }

    private void checkArgumentsType(LinkedList<Assignable> arguments, NodeType[] nodeTypes) {
    }

    private void checkFunction(Function function) throws Exception {
        LinkedList<Assignable> arguments = function.getArguments();

        switch (function.getFunctionType()) {
            case CHANGE_COLOR:
                checkArgumentsCount(arguments, 3);
                break;

            case GO:
                checkArgumentsCount(arguments, 2);
                break;

            case GO_LEFT:
            case GO_RIGHT:
            case GO_UP:
            case GO_DOWN:
            case ROTATE_RIGHT:
            case ROTATE_LEFT:
            case CHANGE_SIZE:
            case WAIT:
                checkArgumentsCount(arguments, 1);
                break;

            case TALK:
                checkArgumentsCount(arguments, 1);
                break;

            case GET_X:
            case GET_Y:
            case GET_ROTATION:
            case GO_TO_MOUSE:
                checkArgumentsCount(arguments, 0);
                break;
        }
    }

    private void checkAssignment(Assignment assignment) throws Exception {
        System.out.println("assigment");
        Assignable assignable = assignment.getValue();
        Variable variable = assignment.getVariable();

        if (assignable.getNodeType() == NodeType.STRING_LITERAL) {
            variable.setVariableType(VariableType.STRING);
            variable.setStringValue(((StringLiteral) assignable).getValue());
        }
        else {
            Expression expression = (Expression) assignable;
            checkExpression(expression);
        }
    }

    private void checkIfStatement(IfStatement ifStatement) throws Exception {

    }

    private void checkRepeatStatement(RepeatStatement repeatStatement) throws Exception {

    }

    private void checkRepeatIfStatement(RepeatIfStatement repeatIfStatement) throws Exception {

    }

    private void checkExpression(Expression expression) throws Exception {
        System.out.println("expression");
    }
}
