import input.InputManager;
import node.*;
import org.junit.Test;
import lexer.Lexer;
import parser.Parser;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void shouldParseEvents() {
        try {
            StringBuilder builder = new StringBuilder("MYSZ() {} START(){} SCIANA (){} KOLIZJA(kangurek){} ");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            Program program = parser.parse();

            LinkedList<Event> events = program.getEvents();
            assertEquals("MYSZ", events.get(0).getName());
            assertEquals("START", events.get(1).getName());
            assertEquals("SCIANA", events.get(2).getName());
            assertEquals("KOLIZJA", events.get(3).getName());
            assertEquals("kangurek", events.get(3).getArgument());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseRepeatStatement() {
        try {
            StringBuilder builder = new StringBuilder("{ powtorz(4) {} }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            parser.parseBlock();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseIfStatement() {
        try {
            StringBuilder builder = new StringBuilder("{ jezeli() {  } }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            parser.parseBlock();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignVariable() {
        try {
            StringBuilder builder = new StringBuilder(" { x = y; }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            parser.parseBlock();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignInteger() {
        try {
            StringBuilder builder = new StringBuilder(" { x = 4; }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            parser.parseBlock();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignMultiplicativeExpression() {
        try {
            StringBuilder builder = new StringBuilder(" { x = 4*2; }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            parser.parseBlock();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}