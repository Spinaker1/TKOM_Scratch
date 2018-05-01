import input.InputManager;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import lexer.Lexer;
import org.junit.rules.ExpectedException;
import parser.Parser;

public class ParserTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowExceptionAfterIf() {
        Parser parser = new Parser(new Lexer(new InputManager("jezeli a == b")));

        try {
            parser.parse();
            Assert.fail( "My method didn't throw when I expected it to" );
        } catch (Exception e) { }
    }
}