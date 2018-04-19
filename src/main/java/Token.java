public class Token {
    private TokenType tokenType;
    private String stringValue;
    private int intValue;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.stringValue = value;
    }

    public Token(TokenType tokenType, int intValue) {
        this.tokenType = tokenType;
        this.intValue = intValue;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
