import input.InputManager;
import lexer.Lexer;
import node.Assignment;
import node.Expression;
import node.Function;
import node.IntLiteral;
import parser.Parser;

public class Main {
    public static void main(String Args[]) {
        try {
            InputManager inputManager = new InputManager("START() { x = 1 ; }");
            Lexer lexer = new Lexer(inputManager);
            Parser parser = new Parser(lexer);
            parser.parse();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
