package parser;

import lexer.Lexer;
import node.*;
import token.*;

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
        while((event = parseEvent()) != null) {
            eventList.add(event);
        }

        accept(currentToken,TokenType.EOF,"Nieprawidłowe wyrażenie.");

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

        String argument = null;
        if (checkTokenType(getToken(), TokenType.VARIABLE)) {
            argument = currentToken.getStringValue();
            accept(getToken(), TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");
        }
        else  {
            accept(currentToken, TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");
        }

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new Event(eventType, argument, codeBlock);
    }

    private Block parseBlock() throws Exception {
        if (!checkTokenType(getToken(), TokenType.BRACKET_OPEN))
            return null;

        LinkedList<Node> instructions = new LinkedList<>();
        Node instruction;
        getToken();
        while((instruction = parseInstruction()) != null ||
                (instruction = parseIfStatement()) != null ||
                (instruction = parseRepeatIfStatement()) != null ||
                (instruction = parseRepeatStatement()) != null) {
           instructions.add(instruction);
           getToken();
        }

        accept(currentToken,TokenType.BRACKET_CLOSE,("Oczekiwane wyrażenie: }"));

        return new Block(instructions);
    }

    private Node parseInstruction() throws Exception {
        Node instruction;

        if ((instruction = parseAssignment()) != null) {
            accept(currentToken, TokenType.SEMICOLON, "Oczekiwane wyrażenie: ;");
        }
        else if ((instruction = parseFunction()) != null) {
            accept(getToken(), TokenType.SEMICOLON, "Oczekiwane wyrażenie: ;");
        }

        return instruction;
    }

    private Function parseFunction() throws Exception {
        if (!checkTokenType(currentToken,TokenType.FUNCTION)) {
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
        if (!checkTokenType(currentToken,TokenType.VARIABLE)) {
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
        if ((assignable = parseStringLiteral()) == null) {
            assignable = parseAdditiveExpression();
        }

        return assignable;
    }

    private RepeatStatement parseRepeatStatement() throws Exception {
        if (!checkTokenType(currentToken,TokenType.REPEAT)) {
            return null;
        }

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        accept(getToken(), TokenType.INTEGER, "Oczekiwane wyrażenie: liczba całkowita");
        int repeatingCount = currentToken.getIntValue();

        accept(getToken(), TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new RepeatStatement(codeBlock,repeatingCount);
    }

    private IfStatement parseIfStatement() throws Exception {
        if (!checkTokenType(currentToken,TokenType.IF)) {
            return null;
        }

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        getToken();
        Condition condition = parseCondition();

        accept(currentToken, TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        Block elseCodeBlock = null;
        if (checkTokenType(currentToken, TokenType.ELSE)) {
            if ((elseCodeBlock = parseBlock()) == null)
                throw new Exception();
        }

        return new IfStatement(codeBlock, elseCodeBlock, condition);
    }

    private RepeatIfStatement parseRepeatIfStatement() throws Exception {
        if (!checkTokenType(currentToken,TokenType.REPEAT_IF)) {
            return null;
        }

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        Condition condition = parseCondition();

        accept(currentToken, TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock;
        if ((codeBlock = parseBlock()) == null)
            throw new Exception();

        return new RepeatIfStatement(codeBlock, condition);
    }

    private Condition parseCondition() throws Exception {
        LinkedList<Node> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        operands.add(parseAndCondition());
        while (checkTokenType(currentToken,TokenType.OR )) {
            operators.add(currentToken.getTokenType());
            getToken();
            operands.add(parseAndCondition());
        }

        return new Condition(operators,operands);
    }

    private Condition parseAndCondition() throws Exception {
        LinkedList<Node> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        operands.add(parseRelationalCondition());
        while (checkTokenType(currentToken,TokenType.AND )) {
            operators.add(currentToken.getTokenType());
            getToken();
            operands.add(parseRelationalCondition());
        }

        return new Condition(operators,operands);
    }

    private Condition parseRelationalCondition() throws Exception {
        LinkedList<Node> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        operands.add(parsePrimaryCondition());
        if (checkTokenType(currentToken,TokenType.EQUAL) ||
                checkTokenType(currentToken, TokenType.NOT_EQUAL) ||
                checkTokenType(currentToken, TokenType.LESS) ||
                checkTokenType(currentToken, TokenType.LESS_EQUAL) ||
                checkTokenType(currentToken, TokenType.GREATER) ||
                checkTokenType(currentToken, TokenType.GREATER_EQUAL)
                ) {
            operators.add(currentToken.getTokenType());
            getToken();
            operands.add(parsePrimaryCondition());
        }
        else {
            throw new Exception();
        }

        return new Condition(operators,operands);
    }

    private Condition parsePrimaryCondition() throws Exception {
        LinkedList<Node> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        operands.add(parseAdditiveExpression());

        return new Condition(operators,operands);
    }

    private Expression parseAdditiveExpression() throws Exception {
        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        operands.add(parseMultiplicativeExpression());
        while (checkTokenType(currentToken, TokenType.ADD) ||
                checkTokenType(currentToken, TokenType.MINUS) ) {
            operators.add(currentToken.getTokenType());
            getToken();
            operands.add(parseMultiplicativeExpression());
        }

        return new Expression(operators, operands);
    }

    private Expression parseMultiplicativeExpression() throws Exception {
        LinkedList<Operand> operands = new LinkedList<>();
        LinkedList<TokenType> operators = new LinkedList<>();

        operands.add(parsePrimaryExpression());
        while (checkTokenType(getToken(), TokenType.MULTIPLY) ||
                checkTokenType(currentToken, TokenType.DIVIDE) ) {
            operators.add(currentToken.getTokenType());
            getToken();
            operands.add(parsePrimaryExpression());
        }

        return new Expression(operators, operands);
    }

    private Operand parsePrimaryExpression() throws Exception {
        switch(currentToken.getTokenType()) {
            case VARIABLE:
                return parseVariable();

            case FUNCTION:
                return parseFunction();

            case MINUS:
            case INTEGER:
                return parseIntLiteral();

            case PARENTHESIS_OPEN:
                getToken();

                Operand expression = parseAdditiveExpression();
                if (!checkTokenType(currentToken,TokenType.PARENTHESIS_CLOSE))
                    throw new Exception();

                return expression;

                default:
                    return null;
        }
    }

    private IntLiteral parseIntLiteral() throws Exception {
        boolean isNegative = false;
        if (checkTokenType(currentToken,TokenType.MINUS)) {
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
        if (!checkTokenType(currentToken,TokenType.STRING)) {
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

