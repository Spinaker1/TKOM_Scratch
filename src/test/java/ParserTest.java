import input.InputManager;
import node.*;
import lexer.Lexer;
import parser.Parser;

import java.util.LinkedList;

import static org.junit.Assert.*;

import org.junit.Test;
import token.TokenType;

public class ParserTest {

    @Test
    public void shouldParseEvents() {
        try {
            String text = "MYSZ() {} START(){} " +
                    "SCIANA (){} KOLIZJA(kangurek){} ";
            Parser parser = new Parser(new Lexer(new InputManager(text)));

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
            Parser parser = new Parser(new Lexer(new InputManager("MYSZ() { powtorz(4) { pobierzY(); } }")));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            RepeatStatement repeatStatement = (RepeatStatement) block.getInstructions().get(0);
            Block block2 = repeatStatement.getCodeBlock();
            Function function = (Function)block2.getInstructions().get(0);

            assertEquals("pobierzY", function.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseIfStatementAndFunction() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager("KOLIZJA(auto) { " +
                    "jezeli() { " +
                    "idzLewo(10); " +
                    "} " +
                    "}")));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            IfStatement ifStatement = (IfStatement) block.getInstructions().get(0);
            Block block2 = ifStatement.getCodeBlock();
            Function function = (Function)block2.getInstructions().get(0);
            Expression expression1 = (Expression) function.getArguments().get(0);
            Expression expression2 = (Expression) expression1.getOperands().get(0);
            IntLiteral intLiteral = (IntLiteral) expression2.getOperands().get(0);

            assertEquals("idzLewo", function.getName());
            assertEquals(10, intLiteral.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignVariable() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager("START() { x = y; }")));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Assignment assignment = (Assignment) block.getInstructions().get(0);
            Variable var1 = assignment.getVariable();
            Expression expression1 = (Expression) assignment.getValue();
            Expression expression2 = (Expression) expression1.getOperands().get(0);
            Variable var2 = (Variable) expression2.getOperands().get(0);

            assertEquals("x", var1.getName());
            assertEquals("y", var2.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignIntegerAndAssignString() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager("START() { x = -4; y = \"kurak\"; }")));

            Program program = parser.parse();

            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Assignment assignment = (Assignment) block.getInstructions().get(0);
            Variable var1 = assignment.getVariable();
            Expression expression1 = (Expression) assignment.getValue();
            Expression expression2 = (Expression) expression1.getOperands().get(0);
            IntLiteral intLiteral = (IntLiteral) expression2.getOperands().get(0);

            assertEquals("x", var1.getName());
            assertEquals(-4, intLiteral.getValue());

            Assignment assignment2 = (Assignment) block.getInstructions().get(1);
            Variable var2 = assignment2.getVariable();
            StringLiteral stringLiteral = (StringLiteral) assignment2.getValue();

            assertEquals("y", var2.getName());
            assertEquals("kurak", stringLiteral.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignString() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager("START() { x = \"dom\"; }")));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Assignment assignment = (Assignment) block.getInstructions().get(0);
            Variable var1 = assignment.getVariable();
            StringLiteral stringLiteral = (StringLiteral) assignment.getValue();

            assertEquals("x", var1.getName());
            assertEquals("dom", stringLiteral.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignMultiplicativeExpression() {
        try {
            StringBuilder builder = new StringBuilder(" START() { x = 4*y; }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Assignment assignment = (Assignment) block.getInstructions().get(0);
            Variable var1 = assignment.getVariable();
            Expression expression1 = (Expression) assignment.getValue();
            Expression expression2 = (Expression) expression1.getOperands().get(0);
            IntLiteral intLiteral = (IntLiteral) expression2.getOperands().get(0);
            Variable var2 = (Variable) expression2.getOperands().get(1);
            TokenType operation = expression2.getOperations().get(0);

            assertEquals("x", var1.getName());
            assertEquals(4, intLiteral.getValue());
            assertEquals(TokenType.MULTIPLY, operation);
            assertEquals("y", var2.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}