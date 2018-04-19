import input.InputManager;
import lexer.Lexer;

public class Main {
    public static void main(String Args[]) {
        InputManager inputManager = new InputManager("123 //456 \n 789");
        Lexer lexer = new Lexer(inputManager);
        System.out.println(lexer.getNextToken().getIntValue());
        System.out.println(lexer.getNextToken().getIntValue());
    }
}
