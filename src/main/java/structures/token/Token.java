package structures.token;

import input.InputManager;
import structures.node.event.EventType;
import structures.node.assignable.function.FunctionType;

public class Token {
    private TokenType tokenType;
    private int intValue;
    private String stringValue;
    private EventType eventType;
    private FunctionType functionType;

    private int positionInLine;
    private int lineNumber;
    private int positionInFile;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token(TokenType tokenType, InputManager source) {
        this(tokenType);
        this.positionInLine = source.getPositionInLine();
        this.lineNumber = source.getLineNumber();
        this.positionInFile = source.getPositionInFile();
    }

    public Token(TokenType tokenType, int intValue, InputManager source) {
        this(tokenType, source);
        this.intValue = intValue;
    }

    public Token(TokenType tokenType, String stringValue, InputManager source) {
        this(tokenType, source);
        this.stringValue = stringValue;
    }

    public Token(TokenType tokenType, EventType eventType) {
        this(tokenType);
        this.eventType = eventType;
    }

    public Token(TokenType tokenType, FunctionType functionType) {
        this(tokenType);
        this.functionType = functionType;
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


    public EventType getEventType() {
        return eventType;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getPositionInFile() {
        return positionInFile;
    }

    public void setPosition(InputManager source) {
        this.positionInLine = source.getPositionInLine();
        this.lineNumber = source.getLineNumber();
        this.positionInFile = source.getPositionInFile();
    }
}
