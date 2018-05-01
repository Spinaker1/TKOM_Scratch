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

    public void parse() throws Exception {
        Program program = new Program();

        Event event = parseEvent();

        program.addEvent(event);
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

        event.setName(getToken().getStringValue());

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE))
            throw new Exception("Oczekiwane wyrażenie: )");

        parseBlock();

        return event;
    }

    private void parseAssignment() throws Exception {
        Assignment assignment = new Assignment();

        Variable variable = new Variable();
        assignment.setVariable(variable);
        variable.setName(currentToken.getStringValue());

        if (!checkTokenType(getToken(), TokenType.ASSIGNMENT))
            throw new Exception("Oczekiwane wyrażenie: =");

        assignment.setValue(parseAssignable());
    }

    private Node parseAssignable() throws Exception {
        if (checkTokenType(getToken(), TokenType.FUNCTION)) {
            return parseFunction();
        }
        throw new Exception("");
    }

    private void parseRepeatStatement() {

    }

    private void parseIfStatement() throws Exception {
        IfStatement ifStatement = new IfStatement();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_OPEN))
            throw new Exception("Oczekiwane wyrażenie: (");

        parseCondition();

        if (!checkTokenType(getToken(), TokenType.PARENTHESIS_CLOSE))
            throw new Exception("Oczekiwane wyrażenie: )");

        parseBlock();

        if (checkTokenType(getToken(), TokenType.ELSE)) {
            ifStatement.setElse(true);
            parseBlock();
        }
    }

    private void parseBlock() throws Exception {
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
                if (!checkTokenType(currentToken, TokenType.INTEGER)) {
                    throw new Exception("Nieprawidłowe wyrażenie");
                }

                Variable var = new Variable();
                var.setIntValue(currentToken.getIntValue());

                function.addArgument(var);

                switch (getToken().getTokenType()) {
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
}

