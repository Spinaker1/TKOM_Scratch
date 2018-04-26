package lexer;

import token.Token;
import token.TokenType;

import input.InputManager;

public class Lexer {
    private InputManager source;

    public Lexer(InputManager inputManager) {
        this.source = inputManager;
    }

    private boolean isEoF() { return source.isEoF(); }

    public Token getNextToken() {
        source.next();

        while (deleteComments() || deleteWhitespaces());

        if (isEoF()) {
            return new Token(TokenType.EOF);
        }

        if (Character.isDigit(source.getCurrentChar()))
            return processInteger();
        if (source.getCurrentChar() == '"') {
            return processString();
        }
        if (Character.isLetter(source.getCurrentChar())) {
            return processKeywordOrId();
        }
        else {
            return processOperator();
        }
    }

    private Token processInteger() {
        if (source.getCurrentChar() == '0')
            return new Token(TokenType.ERROR);

        int number = source.getCurrentChar()-'0';

        while (Character.isDigit(source.getNextChar()) && !isEoF()) {
            source.next();
            int digit = source.getCurrentChar() - '0';
            number = number*10 + digit;
        }

        return new Token(TokenType.INTEGER, number);
    }

    private Token processString() {
        StringBuilder builder = new StringBuilder();

        while (source.getNextChar() != '"' && !isEoF()) {
            source.next();

            if (source.getCurrentChar() == '\\' && source.getNextChar() == '"') {
                source.next();
            }

            builder.append(source.getCurrentChar());
        }

        source.next();

        return new Token(TokenType.STRING, builder.toString());
    }

    private Token processOperator() {
        switch (source.getCurrentChar()) {
            case '=':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.EQUAL);
                }
                return new Token(TokenType.ASSIGNMENT);

            case '&':
                if (source.getNextChar() == '&') {
                    source.next();
                    return new Token(TokenType.AND);
                }
                return new Token(TokenType.ERROR);

            case '|':
                if (source.getNextChar() == '|') {
                    source.next();
                    return new Token(TokenType.OR);
                }
                return new Token(TokenType.ERROR);

            case '!':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.NOT_EQUAL);
                }
                return new Token(TokenType.NEGATION);

            case '>':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.GREATER_EQUAL);
                }
                return new Token(TokenType.GREATER);

            case '<':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.LESS_EQUAL);
                }
                return new Token(TokenType.LESS);

            case '+':
                return new Token(TokenType.ADD);

            case '-':
                return new Token(TokenType.MINUS);

            case '*':
                return new Token(TokenType.MULTIPLY);

            case '/':
                return new Token(TokenType.DIVIDE);

            case '%':
                return new Token(TokenType.MODULO);

            case ';':
                return new Token(TokenType.SEMICOLON);

            case '{':
                return new Token(TokenType.BRACKET_OPEN);

            case '}':
                return new Token(TokenType.BRACKET_CLOSE);

            case '(':
                return new Token(TokenType.PARENTHESIS_OPEN);

            case ')':
                return new Token(TokenType.PARENTHESIS_CLOSE);
        }

        return new Token(TokenType.ERROR);
    }

    private Token processKeywordOrId() {
        StringBuilder builder = new StringBuilder();
        builder.append(source.getCurrentChar());

        while((Character.isLetter(source.getNextChar()) || Character.isDigit(source.getNextChar())) && !isEoF()) {
            source.next();
            builder.append(source.getCurrentChar());
        }

        String string = builder.toString();

        Token token = KeywordsHashmap.KEYWORDS.get(string);
        if (token != null)
            return token;

        token = Events.get(string);
        if (token != null)
            return token;

        token = Functions.get(string);
        if (token != null)
            return token;

        return new Token(TokenType.VARIABLE,string);
    }

    private boolean deleteComments() {
        boolean isComment = false;

        if (source.getCurrentChar() == '#') {
            isComment = true;

            int lineNumber = source.getLineNumber();

            do {
                source.next();
            } while (lineNumber == source.getLineNumber()  && !isEoF());
        }

        return isComment;
    }

    private boolean deleteWhitespaces() {
        boolean isWhitespace = false;

        if (Character.isWhitespace(source.getCurrentChar())) {
            source.next();
            isWhitespace = true;
        }

        return isWhitespace;
    }
}