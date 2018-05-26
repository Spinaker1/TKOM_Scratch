package parser;

import lexer.Lexer;
import structures.node.assignable.expression.Expression;
import structures.node.assignable.literal.IntLiteral;
import structures.node.assignable.literal.StringLiteral;
import structures.node.assignment.Assignment;
import structures.node.event.EventType;
import structures.node.assignable.function.FunctionType;
import structures.node.*;
import structures.node.assignable.*;
import structures.node.assignable.function.Function;
import structures.node.assignable.variable.Variable;
import structures.node.block.Block;
import structures.node.event.Event;
import structures.node.ifstatement.IfStatement;
import structures.node.loop.RepeatIfStatement;
import structures.node.loop.RepeatStatement;
import structures.node.program.Program;
import structures.token.Token;
import structures.token.TokenType;

import java.util.LinkedList;

public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Program parse() throws Exception {
        LinkedList<Event> eventList = new LinkedList<>();

        Event event;
        while ((event = parseEvent()) != null) {
            eventList.add(event);
        }

        accept(currentToken, TokenType.EOF, "Nieprawidłowe wyrażenie.");

        return new Program(eventList);
    }

    private Token getToken() throws Exception {
        Token token = lexer.getNextToken();

        currentToken = token;

        return token;
    }

    private boolean checkTokenType(Token token, TokenType tokenType) {
        return tokenType.equals(token.getTokenType());
    }

    private void accept(Token token, TokenType tokenType, String exceptionText) throws Exception {
        if (!checkTokenType(token, tokenType)) {
            throw new Exception(exceptionText);
        }
    }

    private Event parseEvent() throws Exception {
        if (!checkTokenType(getToken(), TokenType.EVENT)) {
            return null;
        }

        EventType eventType = currentToken.getEventType();

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");
        accept(getToken(), TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new Event(eventType, codeBlock);
    }

    private Block parseBlock() throws Exception {
        if (!checkTokenType(getToken(), TokenType.BRACKET_OPEN))
            return null;

        LinkedList<Node> instructions = new LinkedList<>();
        Node instruction;
        getToken();
        while ((instruction = parseInstruction()) != null ||
                (instruction = parseIfStatement()) != null ||
                (instruction = parseRepeatIfStatement()) != null ||
                (instruction = parseRepeatStatement()) != null) {
            instructions.add(instruction);
            getToken();
        }

        accept(currentToken, TokenType.BRACKET_CLOSE, ("Oczekiwane wyrażenie: }"));

        return new Block(instructions);
    }

    private Node parseInstruction() throws Exception {
        Node instruction;

        if ((instruction = parseAssignment()) != null) {
            accept(currentToken, TokenType.SEMICOLON, "Oczekiwane wyrażenie: ;");
        } else if ((instruction = parseFunction()) != null) {
            accept(getToken(), TokenType.SEMICOLON, "Oczekiwane wyrażenie: ;");
        }

        return instruction;
    }

    private Function parseFunction() throws Exception {
        if (!checkTokenType(currentToken, TokenType.FUNCTION)) {
            return null;
        }

        FunctionType functionType = currentToken.getFunctionType();

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        LinkedList<Assignable> arguments = new LinkedList<>();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE)) {

            loop:
            while (true) {
                Assignable argument = parseAssignable();
                arguments.add(argument);

                switch (currentToken.getTokenType()) {
                    case PARENTHESIS_CLOSE:
                        break loop;

                    case COMMA:
                        getToken();
                        continue loop;

                    default:
                        throw new Exception("Nieprawidłowe wyrażenie");
                }
            }
        }

        return new Function(functionType, arguments);
    }

    private Assignment parseAssignment() throws Exception {
        if (!checkTokenType(currentToken, TokenType.VARIABLE)) {
            return null;
        }

        Variable variable = parseVariable();

        accept(getToken(), TokenType.ASSIGNMENT, "Oczekiwane wyrażenie: =");

        getToken();
        Assignable assignable = parseAssignable();

        return new Assignment(variable, assignable);
    }

    private Assignable parseAssignable() throws Exception {
        Assignable assignable;
        if (((assignable = parseStringLiteral()) == null) &&
                ((assignable = parseAdditiveExpression()) == null)) {
            throw new Exception("Nie podano operandu lub nieprawidłowy typ operandu.");
        }

        return assignable;
    }

    private RepeatStatement parseRepeatStatement() throws Exception {
        if (!checkTokenType(currentToken, TokenType.REPEAT)) {
            return null;
        }

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        accept(getToken(), TokenType.INTEGER, "Oczekiwane wyrażenie: liczba całkowita");
        int repeatingCount = currentToken.getIntValue();

        accept(getToken(), TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new RepeatStatement(codeBlock, repeatingCount);
    }

    private IfStatement parseIfStatement() throws Exception {
        if (!checkTokenType(currentToken, TokenType.IF)) {
            return null;
        }

        accept(getToken(), TokenType.SQUARE_BRACKET_OPEN, "Oczekiwane wyrażenie: [");

        getToken();

        Expression condition;
        if ((condition = parseCondition()) == null) {
            throw new Exception("Nie podano operandu lub nieprawidłowy typ operandu.");
        }

        accept(currentToken, TokenType.SQUARE_BRACKET_CLOSE, "Oczekiwane wyrażenie: ]");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new IfStatement(codeBlock, condition);
    }

    private RepeatIfStatement parseRepeatIfStatement() throws Exception {
        if (!checkTokenType(currentToken, TokenType.REPEAT_IF)) {
            return null;
        }

        accept(getToken(), TokenType.SQUARE_BRACKET_OPEN, "Oczekiwane wyrażenie: [");

        getToken();

        Expression condition;
        if ((condition = parseCondition()) == null) {
            throw new Exception("Nie podano operandu lub nieprawidłowy typ operandu.");
        }

        accept(currentToken, TokenType.SQUARE_BRACKET_CLOSE, "Oczekiwane wyrażenie: ]");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new RepeatIfStatement(codeBlock, condition);
    }

    private Expression parseCondition() throws Exception {
        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        Operand operand;
        if ((operand = parseAndCondition()) == null) {
            return null;
        } else {
            operands.add(operand);
        }

        while (checkTokenType(currentToken, TokenType.OR)) {
            operators.add(currentToken.getTokenType());
            getToken();

            if ((operand = parseAndCondition()) == null) {
                return null;
            } else {
                operands.add(operand);
            }
        }

        return new Expression(operators, operands);
    }

    private Expression parseAndCondition() throws Exception {
        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        Operand operand;
        if ((operand = parseRelationalCondition()) == null) {
            return null;
        } else {
            operands.add(operand);
        }

        while (checkTokenType(currentToken, TokenType.AND)) {
            operators.add(currentToken.getTokenType());
            getToken();

            if ((operand = parseRelationalCondition()) == null) {
                return null;
            } else {
                operands.add(operand);
            }
        }

        return new Expression(operators, operands);
    }

    private Expression parseRelationalCondition() throws Exception {
        if (checkTokenType(currentToken, TokenType.SQUARE_BRACKET_OPEN)) {
            getToken();

            Expression condition;
            if ((condition = parseCondition()) == null) {
                return null;
            }

            accept(currentToken, TokenType.SQUARE_BRACKET_CLOSE, "");
            getToken();

            return condition;
        }

        if (checkTokenType(currentToken, TokenType.NEGATION)) {
            accept(getToken(), TokenType.SQUARE_BRACKET_OPEN, "");
            getToken();

            Expression condition;
            if ((condition = parseCondition()) == null) {
                return null;
            }

            accept(currentToken, TokenType.SQUARE_BRACKET_CLOSE, "");
            getToken();

            condition.setNegated(true);
            return condition;
        }

        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        Operand operand;
        if ((operand = parseAdditiveExpression()) == null) {
            return null;
        } else {
            operands.add(operand);
        }

        if (checkTokenType(currentToken, TokenType.EQUAL) ||
                checkTokenType(currentToken, TokenType.NOT_EQUAL) ||
                checkTokenType(currentToken, TokenType.LESS) ||
                checkTokenType(currentToken, TokenType.LESS_EQUAL) ||
                checkTokenType(currentToken, TokenType.GREATER) ||
                checkTokenType(currentToken, TokenType.GREATER_EQUAL)
                ) {
            operators.add(currentToken.getTokenType());
            getToken();

            if ((operand = parseAdditiveExpression()) == null) {
                return null;
            } else {
                operands.add(operand);
            }
        } else {
            throw new Exception("Oczekiwany operator porównania.");
        }

        return new Expression(operators, operands);
    }

    private Expression parseAdditiveExpression() throws Exception {
        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        Operand operand;
        if ((operand = parseMultiplicativeExpression()) == null) {
            return null;
        } else {
            operands.add(operand);
        }

        while (checkTokenType(currentToken, TokenType.ADD) ||
                checkTokenType(currentToken, TokenType.MINUS)) {
            operators.add(currentToken.getTokenType());
            getToken();

            if ((operand = parseMultiplicativeExpression()) == null) {
                return null;
            } else {
                operands.add(operand);
            }
        }

        return new Expression(operators, operands);
    }

    private Expression parseMultiplicativeExpression() throws Exception {
        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        Operand operand;
        if ((operand = parsePrimaryExpression()) == null) {
            return null;
        } else {
            operands.add(operand);
        }

        while (checkTokenType(getToken(), TokenType.MULTIPLY) ||
                checkTokenType(currentToken, TokenType.DIVIDE) ||
                checkTokenType(currentToken, TokenType.MODULO)) {
            operators.add(currentToken.getTokenType());
            getToken();

            if ((operand = parsePrimaryExpression()) == null) {
                return null;
            } else {
                operands.add(operand);
            }
        }

        return new Expression(operators, operands);
    }

    private Operand parsePrimaryExpression() throws Exception {
        switch (currentToken.getTokenType()) {
            case VARIABLE:
                return parseVariable();

            case FUNCTION:
                return parseFunction();

            case MINUS:
            case INTEGER:
                return parseIntLiteral();

            case PARENTHESIS_OPEN:
                getToken();

                Operand expression;
                if ((expression = parseAdditiveExpression()) == null) {
                    return null;
                }

                accept(currentToken, TokenType.PARENTHESIS_CLOSE, "");

                return expression;

            default:
                return null;
        }
    }

    private IntLiteral parseIntLiteral() throws Exception {
        boolean isNegative = false;
        if (checkTokenType(currentToken, TokenType.MINUS)) {
            isNegative = true;
            getToken();
        }

        int value = currentToken.getIntValue();
        if (isNegative) {
            value = -value;
        }

        return new IntLiteral(value);
    }

    private StringLiteral parseStringLiteral() throws Exception {
        if (!checkTokenType(currentToken, TokenType.STRING)) {
            return null;
        }

        String value = currentToken.getStringValue();

        getToken();

        return new StringLiteral(value);
    }

    private Variable parseVariable() {
        return new Variable(currentToken.getStringValue());
    }
}

