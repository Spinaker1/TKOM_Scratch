package token;

public class Token {
    private TokenType tokenType;
    private int intValue;
    private String stringValue;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token(TokenType tokenType, int intValue) {
        this(tokenType);
        this.intValue = intValue;
    }

    public Token(TokenType tokenType, String stringValue) {
        this(tokenType);
        this.stringValue = stringValue;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
