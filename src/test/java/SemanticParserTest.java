import input.InputManager;
import lexer.Lexer;
import node.Program;
import org.junit.Test;
import parser.Parser;
import semantic.SemanticParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SemanticParserTest {
    @Test
    public void shouldNotParseTwoMouseEvents() {
        try {
            String text = "MYSZ() {} MYSZ() {} ";
            Parser parser = new Parser(new Lexer(new InputManager(text)));
            Program program = parser.parse();
            new SemanticParser().check(program);
            fail();
        } catch (Exception e) {
            assertEquals("Program zawiera więcej niż jedno zdarzenie danego typu.", e.getMessage());
        }
    }

    @Test
    public void shouldNotAssignUninitializedVariable() {
        try {
            String text = "START() { " +
                    "x=y+1*3; }";
            Parser parser = new Parser(new Lexer(new InputManager(text)));
            Program program = parser.parse();
            new SemanticParser().check(program);
            fail();
        } catch (Exception e) {
            assertEquals("Użyto niezainicjalizowanej zmiennej.", e.getMessage());
        }
    }

    @Test
    public void shouldNotUseUninitializedVariableInFunctionArgument() {
        try {
            String text = "START() { " +
                    "czekaj(x); }";
            Parser parser = new Parser(new Lexer(new InputManager(text)));
            Program program = parser.parse();
            new SemanticParser().check(program);
            fail();
        } catch (Exception e) {
            assertEquals("Użyto niezainicjalizowanej zmiennej.", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenFunctionHasTooManyArguments() {
        try {
            String text = "START() { " +
                    "czekaj(4,5); }";
            Parser parser = new Parser(new Lexer(new InputManager(text)));
            Program program = parser.parse();
            new SemanticParser().check(program);
            fail();
        } catch (Exception e) {
            assertEquals("Funkcja powinna zawierać 1 argument", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenFunctionHasStringArgumentInsteadOfInt() {
        try {
            String text = "START() { " +
                    "idzLewo(\"kwiatek\"); }";
            Parser parser = new Parser(new Lexer(new InputManager(text)));
            Program program = parser.parse();
            new SemanticParser().check(program);
            fail();
        } catch (Exception e) {
            assertEquals("Argument powinien zawierać liczbę całkowitą.", e.getMessage());
        }
    }
}