package input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InputManager {
    private InputStream inputStream;

    private int positionInLine = 0;
    private int lineNumber = 1;

    private char currentChar;
    private char nextChar;

    public InputManager(String string) {
         inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
         next();
    }

    public char getCurrentChar() {
        return currentChar;
    }

    public char getNextChar() {
        return nextChar;
    }

    public void next() {
        char character='d';

        try {
            character = (char) inputStream.read();
            while(character == '\n') {
                character = (char) inputStream.read();
                lineNumber++;
                positionInLine = 0;
            }

            positionInLine++;
        }
        catch (IOException e) {
        }

        currentChar = nextChar;
        nextChar = character;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean isEoF() {
        return (int) currentChar == -1;
    }
}
