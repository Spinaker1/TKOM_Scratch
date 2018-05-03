package parser;

import lexer.Lexer;
import node.*;
import node.Event;
import token.*;

import java.awt.*;

public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Program parse() throws Exception {
        Program program = new Program();

        Event event;
        while((event = parseEvent()) != null) {
            program.addEvent(event);
        }

        return program;
    }

    public Token getToken() throws Exception {
        Token token = lexer.getNextToken();

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

    private Event parseEvent() throws Exception {
        Event event = new Event();

        try {
            getToken();
        } catch (Exception e) {
            return null;
        }

        if (!checkTokenType(currentToken, TokenType.EVENT)) {
            throw new Exception("Oczekiwane wyrażenie: wydarzenie");
        }

        event.setName(currentToken.getStringValue());

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN)) {
            throw new Exception("Oczekiwane wyrażenie: (");
        }

        if (checkTokenType(getToken(), TokenType.VARIABLE)) {
            event.setArgument(currentToken.getStringValue());
            if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE)) {
                throw new Exception("Oczekiwane wyrażenie: )");
            }
        }
        else if (!checkTokenType(currentToken, TokenType.PARENTHESIS_CLOSE)) {
            throw new Exception("Oczekiwane wyrażenie: )");
        }

        parseBlock();

        return event;
    }

    public Assignment parseAssignment() throws Exception {
        Assignment assignment = new Assignment();

        assignment.setVariable(parseVariable());

        if (!checkTokenType(getToken(), TokenType.ASSIGNMENT))
            throw new Exception("Oczekiwane wyrażenie: =");

        getToken();
        assignment.setValue(parseAssignable());

        return assignment;
    }

    private Node parseAssignable() throws Exception {
        switch (currentToken.getTokenType()) {
            case FUNCTION:
                return parseFunction();

            case VARIABLE:
            case INTEGER:
                return parseAdditiveExpression();

                default:
                    throw new Exception("");
        }
    }

    private void parseRepeatStatement() throws Exception {
        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

        if (!checkTokenType(getToken(), TokenType.INTEGER))
            throw new Exception("Oczekiwane wyrażenie: liczba całkowita");

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE))
            throw new Exception("Oczekiwane wyrażenie: )");

        parseBlock();
    }

    private void parseIfStatement() throws Exception {
        IfStatement ifStatement = new IfStatement();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

        parseCondition();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE))
            throw new Exception("Oczekiwane wyrażenie: )");

        parseBlock();

        if (checkTokenType(currentToken, TokenType.ELSE)) {
            ifStatement.setElse(true);
            parseBlock();
        }
    }

    public void parseBlock() throws Exception {
        if (!checkTokenType(getToken(), TokenType.BRACKET_OPEN))
            throw new Exception("Oczekiwane wyrażenie: {");

        while (!checkTokenType(getToken(), TokenType.BRACKET_CLOSE)) {
            switch (currentToken.getTokenType()) {
                case IF:
                    parseIfStatement();
                    break;

                case REPEAT:
                    parseRepeatStatement();
                    break;

                case FUNCTION:
                    parseFunction();
                    break;

                case VARIABLE:
                    parseAssignment();
                    break;

                    default:
                        throw new Exception("Oczekiwane wyrażenie: }");
            }
        }
    }

    private void parseCondition() {

    }

    private Function parseFunction() throws Exception {
        Function function = new Function();

        function.setName(currentToken.getStringValue());

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

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

    private Expression parseMultiplicativeExpression() throws Exception {
        Expression expression = new Expression();

        expression.addOperand(parsePrimaryExpression());
        while (checkTokenType(getToken(), TokenType.MULTIPLY) ||
                checkTokenType(currentToken, TokenType.DIVIDE) ) {
            expression.addOperator(currentToken.getTokenType());
            getToken();
            expression.addOperand(parsePrimaryExpression());
        }

        return expression;
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

    private Node parsePrimaryExpression() throws Exception {
        Node expression = null;

        switch(currentToken.getTokenType()) {
            case VARIABLE:
                expression = parseVariable();
                break;

            case FUNCTION:
                expression = parseFunction();
                break;

                default:
                    expression = parseIntLiteral();
        }
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

    private Variable parseVariable() {
        Variable variable = new Variable();
        variable.setName(currentToken.getStringValue());

        return variable;
    }
}

