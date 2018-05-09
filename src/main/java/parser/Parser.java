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

        return new Program(eventList);
    }

    private Token getToken() throws Exception {
        Token token = lexer.getNextToken();

        System.out.println(token.getTokenType());

        if (checkTokenType(token,TokenType.ERROR)) {
            if (token.getStringValue() != null)
                throw new Exception(token.getStringValue());
            else
                throw new Exception();
        }

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

        String name = currentToken.getStringValue();

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

        return new Event(name, argument, codeBlock);
    }

    private Block parseBlock() throws Exception {
        Block block = new Block();

        if (!checkTokenType(getToken(), TokenType.BRACKET_OPEN))
            return null;

        while (!checkTokenType(getToken(), TokenType.BRACKET_CLOSE)) {
            switch (currentToken.getTokenType()) {
                case IF:
                    block.addInstruction(parseIfStatement());
                    break;

                case REPEAT:
                    block.addInstruction(parseRepeatStatement());
                    break;

                case REPEAT_IF:
                    block.addInstruction(parseRepeatIfStatement());
                    break;

                case FUNCTION:
                    block.addInstruction(parseFunction());
                    accept(getToken(), TokenType.SEMICOLON, "Oczekiwane wyrażenie: ;");
                    break;

                case VARIABLE:
                    block.addInstruction(parseAssignment());
                    accept(currentToken, TokenType.SEMICOLON, "Oczekiwane wyrażenie: ;");
                    break;

                default:
                    throw new Exception("Oczekiwane wyrażenie: }");
            }
        }

        return block;
    }

    private Function parseFunction() throws Exception {
        Function function = new Function();

        function.setName(currentToken.getStringValue());

        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE)) {

            loop:
            while (true) {
                Node node = parseAssignable();
                function.addArgument(node);

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

        return function;
    }

    private Assignment parseAssignment() throws Exception {
        Variable variable = parseVariable();

        accept(getToken(), TokenType.ASSIGNMENT, "=");

        getToken();
        Node assignable = parseAssignable();

        return new Assignment(variable, assignable);
    }

    private Node parseAssignable() throws Exception {
        switch (currentToken.getTokenType()) {
            case FUNCTION:
            case MINUS:
            case VARIABLE:
            case INTEGER:
            case PARENTHESIS_OPEN:
                return parseAdditiveExpression();

            case STRING:
                return parseStringLiteral();

                default:
                    throw new Exception("");
        }
    }

    private RepeatStatement parseRepeatStatement() throws Exception {
        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        accept(getToken(), TokenType.INTEGER, "Oczekiwane wyrażenie: liczba całkowita");

        int repeatingCount = currentToken.getIntValue();

        accept(getToken(), TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock = parseBlock();

        return new RepeatStatement(codeBlock,repeatingCount);
    }

    private IfStatement parseIfStatement() throws Exception {
        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        getToken();
        Condition condition = parseCondition();

        accept(currentToken, TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock = parseBlock();

        Block elseCodeBlock = null;
        if (checkTokenType(currentToken, TokenType.ELSE)) {
            elseCodeBlock = parseBlock();
        }

        return new IfStatement(codeBlock, elseCodeBlock, condition);
    }

    private RepeatIfStatement parseRepeatIfStatement() throws Exception {
        accept(getToken(), TokenType.PARENTHESIS_OPEN, "Oczekiwane wyrażenie: (");

        Condition condition = parseCondition();

        accept(currentToken, TokenType.PARENTHESIS_CLOSE, "Oczekiwane wyrażenie: )");

        Block codeBlock = parseBlock();

        return new RepeatIfStatement(codeBlock, condition);
    }

    private Condition parseCondition() throws Exception {
        Condition condition = new Condition();

        condition.addOperand(parseAndCondition());
        while (checkTokenType(currentToken,TokenType.OR )) {
            condition.addOperator(currentToken.getTokenType());
            getToken();
            condition.addOperand(parseAndCondition());
        }

        return condition;
    }

    private Condition parseAndCondition() throws Exception {
        Condition condition = new Condition();

        condition.addOperand(parseRelationalCondition());
        while (checkTokenType(currentToken,TokenType.AND )) {
            condition.addOperator(currentToken.getTokenType());
            getToken();
            condition.addOperand(parseRelationalCondition());
        }

        return condition;
    }

    private Condition parseRelationalCondition() throws Exception {
        Condition condition = new Condition();

        condition.addOperand(parsePrimaryCondition());
        if (checkTokenType(currentToken,TokenType.EQUAL) ||
                checkTokenType(currentToken, TokenType.NOT_EQUAL) ||
                checkTokenType(currentToken, TokenType.LESS) ||
                checkTokenType(currentToken, TokenType.LESS_EQUAL) ||
                checkTokenType(currentToken, TokenType.GREATER) ||
                checkTokenType(currentToken, TokenType.GREATER_EQUAL)
                ) {
            condition.addOperator(currentToken.getTokenType());
            getToken();
            condition.addOperand(parsePrimaryCondition());
        }

        else {
            throw new Exception();
        }

        return condition;
    }

    private Condition parsePrimaryCondition() throws Exception {
        Condition condition = new Condition();

        condition.addOperand(parseAdditiveExpression());

        return condition;
    }

    private Node parseAdditiveExpression() throws Exception {
        Expression expression = new Expression();

        expression.addOperand(parseMultiplicativeExpression());
        while (checkTokenType(currentToken, TokenType.ADD) ||
                checkTokenType(currentToken, TokenType.MINUS) ) {
            expression.addOperator(currentToken.getTokenType());
            getToken();
            expression.addOperand(parseMultiplicativeExpression());
        }

        return expression;
    }

    private Expression parseMultiplicativeExpression() throws Exception {
        Expression expression = new Expression();

        expression.addOperand(parsePrimaryExpression());
        while (checkTokenType(currentToken, TokenType.MULTIPLY) ||
                checkTokenType(currentToken, TokenType.DIVIDE) ) {
            expression.addOperator(currentToken.getTokenType());
            getToken();
            expression.addOperand(parsePrimaryExpression());
        }

        return expression;
    }

    private Node parsePrimaryExpression() throws Exception {
        Node expression = null;

        switch(currentToken.getTokenType()) {
            case VARIABLE:
                expression = parseVariable();
                break;

            case FUNCTION:
                expression = parseFunction();
                break;

            case MINUS:
            case INTEGER:
                    expression = parseIntLiteral();
                    break;

            case PARENTHESIS_OPEN:
                getToken();

                expression = parseAdditiveExpression();
                if (!checkTokenType(currentToken,TokenType.PARENTHESIS_CLOSE))
                    throw new Exception();
        }

        getToken();

        return expression;
    }

    private IntLiteral parseIntLiteral() throws Exception {
        IntLiteral intLiteral = new IntLiteral();

        boolean isNegative = false;
        if (checkTokenType(currentToken,TokenType.MINUS)) {
            isNegative = true;
            getToken();
        }

        int value = currentToken.getIntValue();
        if (isNegative) {
            value = -value;
        }
        intLiteral.setValue(value);

        return intLiteral;
    }

    private StringLiteral parseStringLiteral() throws Exception {
        StringLiteral stringLiteral = new StringLiteral();

        stringLiteral.setValue(currentToken.getStringValue());

        getToken();

        return stringLiteral;
    }

    private Variable parseVariable() {
        return new Variable(currentToken.getStringValue());
    }
}

