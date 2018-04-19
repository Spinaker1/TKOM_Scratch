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

        if (isEoF()) {
            return new Token(TokenType.EOF);
        }

        //delete white spaces
        while (Character.isWhitespace(nextChar) && !isEoF()) {
            nextChar = getNextChar();
        }

        //delete comments
        if (nextChar == '/' && peek() == '/') {
            int lineNumber = inputManager.getLineNumber();

            while((lineNumber == inputManager.getLineNumber() || Character.isWhitespace(nextChar)) && !isEoF()) {
                nextChar = getNextChar();
            }
        }

        Token token;

        if (Character.isDigit(nextChar)) {
            token = processInteger(nextChar);
        } else if (nextChar == '"') {
            token = processString(nextChar);
        } else if (Character.isLetter(nextChar)) {
            token = processKeywordOrId(nextChar);
        }
        else {
            token = processOperator(nextChar);
        }

        if (token == null) {
            return new Token(TokenType.ERROR);
        }
        return token;
    }

    private Token processInteger(char nextChar) {
        String string = "";
        string += nextChar;

        while (Character.isDigit(peek()) && !isEoF()) {
            string += getNextChar();
        }

        return new Token(TokenType.INTEGER, Integer.parseInt(string));
    }

    private Token processString(char nextChar) {
        String string = "";

        while (peek() != '"' && !isEoF()) {
            string += getNextChar();
        }

        return new Token(TokenType.STRING, string);
    }

    private Token processOperator(char nextChar) {
        String string = "";
        string += nextChar;

        if (nextChar == '=' && peek() == '=')
            string += getNextChar();
        if (nextChar == '&' && peek() == '&')
            string += getNextChar();
        if (nextChar == '|' && peek() == '|')
            string += getNextChar();
        if (nextChar == '!' && peek() == '=')
            string += getNextChar();
        if (nextChar == '>' && peek() == '=')
            string += getNextChar();
        if (nextChar == '<' && peek() == '=')
            string += getNextChar();

        return KeywordsOperatorsHashmap.OPERATORS.get(string);
    }

    private Token processKeywordOrId(char nextChar) {
        String string = "";
        string += nextChar;

        while((Character.isLetter(peek()) || Character.isDigit(peek())) && !isEoF()) {
            string += getNextChar();
        }

        Token token = KeywordsOperatorsHashmap.KEYWORDS.get(string);
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