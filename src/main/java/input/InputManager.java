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

    public InputManager(String string) {
         inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

    public char getNextChar() {
        char character = 'd';

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

        currentChar = character;

        return character;
    }

    public char peek() {
        try {
            inputStream.mark(1);

            char character = (char) inputStream.read();

            while(character == '\n') {
                inputStream.mark(1);

                character = (char) inputStream.read();
            }

            inputStream.reset();

            return character;
        } catch (IOException e) {
            return 'a';
        }
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
