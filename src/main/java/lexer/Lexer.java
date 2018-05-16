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

    public Token getNextToken() throws Exception {
        source.next();

        while (deleteComments() || deleteWhitespaces());

        if (isEoF()) {
            return new Token(TokenType.EOF, source);
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

    private Token processInteger() throws Exception {
        if (source.getCurrentChar() == '0' && Character.isDigit(source.getNextChar()))
            throw new Exception("Liczba nie powinna zaczynać się od 0.");

        int number = source.getCurrentChar()-'0';

        while (Character.isDigit(source.getNextChar())) {
            source.next();
            int digit = source.getCurrentChar() - '0';
            number = number*10 + digit;
        }

        return new Token(TokenType.INTEGER, number, source);
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

        return new Token(TokenType.STRING, builder.toString(), source);
    }

    private Token processOperator() throws Exception {
        switch (source.getCurrentChar()) {
            case '=':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.EQUAL, source);
                }
                return new Token(TokenType.ASSIGNMENT, source);

            case '&':
                if (source.getNextChar() == '&') {
                    source.next();
                    return new Token(TokenType.AND, source);
                }
                throw new Exception("Nieprawidłowe wyrażenie");

            case '|':
                if (source.getNextChar() == '|') {
                    source.next();
                    return new Token(TokenType.OR);
                }
                throw new Exception("Nieprawidłowe wyrażenie");

            case '!':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.NOT_EQUAL, source);
                }
                return new Token(TokenType.NEGATION, source);

            case '>':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.GREATER_EQUAL, source);
                }
                return new Token(TokenType.GREATER, source);

            case '<':
                if (source.getNextChar() == '=') {
                    source.next();
                    return new Token(TokenType.LESS_EQUAL, source);
                }
                return new Token(TokenType.LESS, source);

            case '+':
                return new Token(TokenType.ADD, source);

            case '-':
                return new Token(TokenType.MINUS, source);

            case '*':
                return new Token(TokenType.MULTIPLY, source);

            case '/':
                return new Token(TokenType.DIVIDE, source);

            case '%':
                return new Token(TokenType.MODULO, source);

            case ';':
                return new Token(TokenType.SEMICOLON, source);

            case '{':
                return new Token(TokenType.BRACKET_OPEN, source);

            case '}':
                return new Token(TokenType.BRACKET_CLOSE, source);

            case '(':
                return new Token(TokenType.PARENTHESIS_OPEN, source);

            case ')':
                return new Token(TokenType.PARENTHESIS_CLOSE, source);

            case ',':
                return new Token(TokenType.COMMA, source);
        }

        throw new Exception("Nieprawidłowe wyrażenie");
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
        if (token != null) {
            token.setPosition(source);
            return token;
        }

        return new Token(TokenType.VARIABLE,string,source);
    }

    private boolean deleteComments() {
        boolean isComment = false;

        if (source.getCurrentChar() == '#') {
            isComment = true;

            int lineNumber = source.getLineNumber();

            while (lineNumber == source.getLineNumber()  && !isEoF())
            {
                source.next();
            }
            source.next();
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