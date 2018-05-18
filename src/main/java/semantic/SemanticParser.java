package semantic;

import node.*;
import token.EventType;
import token.FunctionType;

import java.util.LinkedList;

public class SemanticParser {
    public void check(Program program) throws Exception {
        LinkedList<EventType> eventTypesInProgram = new LinkedList<>();

        for (Event event : program.getEvents()) {
            switch (event.getEventType()) {
                case START:
                case MOUSE:
                case WALL:
                    if (eventTypesInProgram.contains(event.getEventType())) {
                        throw new Exception("Program zawiera więcej niż jedno zdarzenie danego typu.");
                    }
                    eventTypesInProgram.add(event.getEventType());
                    break;
            }

            checkEvent(event);
        }
    }

    private void checkEvent(Event event) throws Exception {
        if (event.getEventType() != EventType.COLLISION && event.getArgument() != null) {
            throw new Exception("Zdarzenie tego typu nie powinno mieć argumentu.");
        }

        Block block = event.getCodeBlock();
        block.setScope(new Scope(null));
        checkBlock(block);
    }

    private void checkBlock(Block block) throws Exception {
        Scope scope = block.getScope();

        for (Node instruction : block.getInstructions()) {
            switch (instruction.getNodeType()) {
                case FUNCTION:
                    checkFunction((Function) instruction, scope);
                    break;

                case ASSIGNMENT:
                    checkAssignment((Assignment) instruction, scope);
                    break;

                case IF_STATEMENT:
                    checkIfStatement((IfStatement) instruction, scope);
                    break;

                case REPEAT_IF_STATEMENT:
                    checkRepeatIfStatement((RepeatIfStatement) instruction, scope);
                    break;

                case REPEAT_STATEMENT:
                    checkRepeatStatement((RepeatStatement) instruction, scope);
                    break;
            }
        }
    }

    private void checkArgumentsCount(Function function) throws Exception {
        int argumentsCount = FunctionArgumentsHashMap.FUNCTION_ARGUMENTS.get(function.getFunctionType()).length;

        if (function.getArguments().size() != argumentsCount) {
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
                        function.getArguments().set(i,new StringLiteral(value));
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
                    checkAssignable(argument, scope);
                }
            }
        }
    }

    private void checkFunction(Function function, Scope scope) throws Exception {
        checkArgumentsCount(function);
        checkArgumentsType(function, scope);
    }

    private void checkAssignment(Assignment assignment, Scope scope) throws Exception {
        Assignable assignable = assignment.getValue();
        Variable variable = assignment.getVariable();

        if (!scope.containsVariable(variable.getName())) {
            scope.putVariable(variable);
        }
        else {
            variable = scope.getVariable(variable.getName());
        }

        if (assignable.getNodeType() == NodeType.STRING_LITERAL) {
            variable.setVariableType(VariableType.STRING);
            variable.setStringValue(((StringLiteral)assignable).getValue());
        } else {
            Expression expression = (Expression) assignable;
            String value;
            if ((value = checkStringVariable(expression, scope)) != null) {
                assignment.setValue(new StringLiteral(value));
                variable.setVariableType(VariableType.STRING);
                variable.setStringValue(value);
            } else {
                checkAssignable(expression, scope);
                variable.setVariableType(VariableType.INT);
            }
        }
    }

    private void checkIfStatement(IfStatement ifStatement, Scope scope) throws Exception {
        Block block = ifStatement.getCodeBlock();
        block.setScope(new Scope(scope));
        checkBlock(block);

        checkAssignable(ifStatement.getCondition(), scope);
    }

    private void checkRepeatStatement(RepeatStatement repeatStatement, Scope scope) throws Exception {
        Block block = repeatStatement.getCodeBlock();
        block.setScope(new Scope(scope));
        checkBlock(block);
    }

    private void checkRepeatIfStatement(RepeatIfStatement repeatIfStatement, Scope scope) throws Exception {
        Block block = repeatIfStatement.getCodeBlock();
        block.setScope(new Scope(scope));
        checkBlock(block);
        checkAssignable(repeatIfStatement.getCondition(), scope);
    }

    private void checkAssignable(Assignable assignable, Scope scope) throws Exception {
        if (assignable.getNodeType() == NodeType.EXPRESSION) {
            Expression expression = (Expression) assignable;
            for (Assignable operand : expression.getOperands()) {
                checkAssignable(operand, scope);
            }
        }

        if (assignable.getNodeType() == NodeType.VARIABLE) {
            Variable variable = (Variable) assignable;
            checkVariable(variable, scope);
        }

        if (assignable.getNodeType() == NodeType.FUNCTION) {
            Function function = (Function) assignable;
            FunctionType functionType = function.getFunctionType();
            if (functionType != FunctionType.GET_X && functionType != FunctionType.GET_Y &&
                    functionType != FunctionType.GET_ROTATION) {
                throw new Exception("Podana funkcja nie zwraca wartości.");
            }
        }
    }


    private void checkVariable(Variable variable, Scope scope) throws Exception {
        if (!scope.containsVariable(variable.getName())) {
            throw new Exception("Użyto niezainicjalizowanej zmiennej.");
        }

        variable = scope.getVariable(variable.getName());
        if (variable.getVariableType() != VariableType.INT) {
            throw new Exception("Zmienna musi zawierać liczbę całkowitą.");
        }
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

