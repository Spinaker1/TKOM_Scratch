public class Lexer {
    private InputManager inputManager;

    Lexer(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    private char getNextChar() {
        return inputManager.getNextChar();
    }

    private char peek() {
        return inputManager.peek();
    }

    public Token getNextToken() {
        char nextChar = getNextChar();

        //delete white spaces
        while (Character.isWhitespace(nextChar)) {
            nextChar = getNextChar();
        }

        //delete comments
        if (nextChar == '/' && peek() == '/') {
            int lineNumber = inputManager.getLineNumber();
            while(lineNumber == inputManager.getLineNumber() || Character.isWhitespace(nextChar)) {
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

        return token;
    }

    private Token processInteger(char nextChar) {
        String string = "";
        string += nextChar;

        while (Character.isDigit(peek())) {
            string += getNextChar();
        }

        return new Token(TokenType.INTEGER, Integer.parseInt(string));
    }

    private Token processString(char nextChar) {
        String string = "";

        while (peek() != '"') {
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

        while(Character.isLetter(peek()) || Character.isDigit(peek())) {
            string += getNextChar();
        }

        Token token = KeywordsOperatorsHashmap.KEYWORDS.get(string);

        if (token != null)
            return token;
        else
            return new Token(TokenType.VARIABLE,string);
    }
}