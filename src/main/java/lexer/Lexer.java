package lexer;

import token.Token;
import token.TokenType;

import input.InputManager;

public class Lexer {
    private InputManager inputManager;

    public Lexer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    private char getNextChar() {
        return inputManager.getNextChar();
    }

    private char peek() {
        return inputManager.peek();
    }

    private boolean isEoF() { return inputManager.isEoF(); }

    public Token getNextToken() {
        char nextChar = getNextChar();

        //delete white spaces
        while (Character.isWhitespace(nextChar)) {
            nextChar = getNextChar();
        }

        if (isEoF()) {
            return new Token(TokenType.EOF);
        }

        //delete comments
        if (nextChar == '#') {
            int lineNumber = inputManager.getLineNumber();

            while((lineNumber == inputManager.getLineNumber() || Character.isWhitespace(nextChar)) && !isEoF()) {
                nextChar = getNextChar();
            }
        }

        if (Character.isDigit(nextChar))
            return processInteger(nextChar);
        if (nextChar == '"') {
            return processString(nextChar);
        }
        if (Character.isLetter(nextChar)) {
            return processKeywordOrId(nextChar);
        }
        else {
            return processOperator(nextChar);
        }
    }

    private Token processInteger(char nextChar) {
        if (nextChar == '0')
            return new Token(TokenType.ERROR);

        int number = nextChar-'0';

        while (Character.isDigit(peek()) && !isEoF()) {
            int digit = getNextChar() - '0';
            number = number*10 + digit;
        }

        return new Token(TokenType.INTEGER, number);
    }

    private Token processString(char nextChar) {
        StringBuilder builder = new StringBuilder();

        while (peek() != '"' && !isEoF()) {
            builder.append(getNextChar());
        }

        getNextChar();

        return new Token(TokenType.STRING, builder.toString());
    }

    private Token processOperator(char nextChar) {
        switch (nextChar) {
            case '=':
                if (peek() == '=') {
                    getNextChar();
                    return new Token(TokenType.EQUAL);
                }
                return new Token(TokenType.ASSIGNMENT);

            case '&':
                if (peek() == '&') {
                    getNextChar();
                    return new Token(TokenType.AND);
                }
                return new Token(TokenType.ERROR);

            case '|':
                if (peek() == '|') {
                    getNextChar();
                    return new Token(TokenType.OR);
                }
                return new Token(TokenType.ERROR);

            case '!':
                if (peek() == '=') {
                    getNextChar();
                    return new Token(TokenType.NOT_EQUAL);
                }
                return new Token(TokenType.NEGATION);

            case '>':
                if (peek() == '=') {
                    getNextChar();
                    return new Token(TokenType.GREATER_EQUAL);
                }
                return new Token(TokenType.GREATER);

            case '<':
                if (peek() == '=') {
                    getNextChar();
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

    private Token processKeywordOrId(char nextChar) {
        StringBuilder builder = new StringBuilder();
        builder.append(nextChar);

        while((Character.isLetter(peek()) || Character.isDigit(peek())) && !isEoF()) {
            builder.append(getNextChar());
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
}