package parser;

import lexer.Lexer;
import node.*;
import token.*;

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

    private Token getToken() throws Exception {
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

        event.setCodeBlock(parseBlock());

        return event;
    }

    private Assignment parseAssignment() throws Exception {
        Assignment assignment = new Assignment();

        assignment.setVariable(parseVariable());

        if (!checkTokenType(getToken(), TokenType.ASSIGNMENT)) {
            throw new Exception("Oczekiwane wyrażenie: =");
        }

        getToken();
        assignment.setValue(parseAssignable());

        if (!checkTokenType(currentToken, TokenType.SEMICOLON)) {
            throw new Exception("Oczekiwane wyrażenie: ;");
        }

        return assignment;
    }

    private Node parseAssignable() throws Exception {
        switch (currentToken.getTokenType()) {
            case FUNCTION:
                return parseFunction();

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
        RepeatStatement repeatStatement = new RepeatStatement();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN)) {
            throw new Exception("Oczekiwane wyrażenie: (");
        }

        if (!checkTokenType(getToken(), TokenType.INTEGER)) {
            throw new Exception("Oczekiwane wyrażenie: liczba całkowita");
        }

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE)) {
            throw new Exception("Oczekiwane wyrażenie: )");
        }

        repeatStatement.setCodeBlock(parseBlock());

        return repeatStatement;
    }

    private IfStatement parseIfStatement() throws Exception {
        IfStatement ifStatement = new IfStatement();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

        getToken();
        ifStatement.setCondition(parseCondition());

        if (!checkTokenType(currentToken, TokenType.PARENTHESIS_CLOSE))
            throw new Exception("Oczekiwane wyrażenie: )");

        ifStatement.setCodeBlock(parseBlock());

        if (checkTokenType(currentToken, TokenType.ELSE)) {
            ifStatement.setElse(true);
            ifStatement.setElseCodeBlock(parseBlock());
        }

        return ifStatement;
    }

    private RepeatIfStatement parseRepeatIfStatement() throws Exception {
        RepeatIfStatement repeatIfStatement = new RepeatIfStatement();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

        repeatIfStatement.setCondition(parseCondition());

        if (!checkTokenType(currentToken, TokenType.PARENTHESIS_CLOSE))
            throw new Exception("Oczekiwane wyrażenie: )");

        repeatIfStatement.setCodeBlock(parseBlock());

        return repeatIfStatement;
    }

    private Block parseBlock() throws Exception {
        Block block = new Block();

        if (!checkTokenType(getToken(), TokenType.BRACKET_OPEN))
            throw new Exception("Oczekiwane wyrażenie: {");

        while (!checkTokenType(getToken(), TokenType.BRACKET_CLOSE)) {
            switch (currentToken.getTokenType()) {
                case IF:
                    block.addInstruction(parseIfStatement());
                    break;

                case REPEAT:
                    block.addInstruction(parseRepeatStatement());
                    break;

                case FUNCTION:
                    block.addInstruction(parseFunction());
                    break;

                case VARIABLE:
                    block.addInstruction(parseAssignment());
                    break;

                    default:
                        throw new Exception("Oczekiwane wyrażenie: }");
            }
        }

        return block;
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

        condition.addOperand(parseEqualityCondition());
        while (checkTokenType(currentToken,TokenType.AND )) {
            condition.addOperator(currentToken.getTokenType());
            getToken();
            condition.addOperand(parseEqualityCondition());
        }

        return condition;
    }

    private Condition parseEqualityCondition() throws Exception {
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

        if (!checkTokenType(getToken(),TokenType.SEMICOLON)) {
            throw new Exception("Oczekiwanie wyrażenie: ;");
        }

        return function;
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
        Variable variable = new Variable();
        variable.setName(currentToken.getStringValue());

        return variable;
    }
}

