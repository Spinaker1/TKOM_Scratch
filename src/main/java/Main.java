import input.InputManager;
import lexer.Lexer;
import parser.Parser;

public class Main {
    public static void main(String Args[]) {
        try {
            InputManager inputManager = new InputManager("START() { xx = pobierzX() }");
            Lexer lexer = new Lexer(inputManager);
            Parser parser = new Parser(lexer);
            parser.parse();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
