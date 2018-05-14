package input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InputManager {
    private InputStream inputStream;
    private int fileLength;

    private int positionInLine = 0;
    private int lineNumber = 1;
    private int positionInFile = 0;

    private char currentChar;
    private char nextChar;

    public InputManager() {
        inputStream = null;
        fileLength = 0;
    }

    public InputManager(String string) {
         inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
         fileLength = string.length()+1;
         next();
    }

    public void setInputStream(String string) {
        positionInLine = 0;
        lineNumber = 1;
        positionInFile = 0;
        inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        fileLength = string.length() + 1;
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
            positionInFile++;
            positionInLine++;
            while(character == '\n') {
                character = (char) inputStream.read();
                lineNumber++;
                positionInFile++;
                positionInLine = 0;
            }
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

    public int getPositionInFile() {
        return positionInFile;
    }

    public boolean isEoF() {
        if (positionInFile > fileLength)
            return true;
        return (int) currentChar == -1;
    }
}
