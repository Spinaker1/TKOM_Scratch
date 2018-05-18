import input.InputManager;
import node.*;
import lexer.Lexer;
import parser.Parser;

import java.util.LinkedList;

import static org.junit.Assert.*;

import org.junit.Test;
import token.EventType;
import token.FunctionType;
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

            assertEquals(EventType.MOUSE, events.get(0).getEventType());
            assertEquals(EventType.START, events.get(1).getEventType());
            assertEquals(EventType.WALL, events.get(2).getEventType());
            assertEquals(EventType.COLLISION, events.get(3).getEventType());
            assertEquals("kangurek", events.get(3).getArgument());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseFunction() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager("MYSZ() { pobierzY(); }")));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Function function = (Function)block.getInstructions().get(0);

            assertEquals(FunctionType.GET_Y, function.getFunctionType());
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

            assertEquals(FunctionType.GET_Y, function.getFunctionType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void shouldParseIfStatementAndFunction() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager("KOLIZJA(auto) { " +
                    "jezeli[1+2*3 != 5 && 3 < 5 || y+2+3 > k*s] { " +
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

            assertEquals(FunctionType.GO_LEFT, function.getFunctionType());
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
            TokenType operation = expression2.getOperators().get(0);

            assertEquals("x", var1.getName());
            assertEquals(4, intLiteral.getValue());
            assertEquals(TokenType.MULTIPLY, operation);
            assertEquals("y", var2.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignExpressionInParentheses() {
        try {
            StringBuilder builder = new StringBuilder(" START() { x = (z+5)*y; }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Assignment assignment = (Assignment) block.getInstructions().get(0);
            Variable var1 = assignment.getVariable();

            Expression expression1 = (Expression) assignment.getValue();
            Expression expression2 = (Expression) expression1.getOperands().get(0);

            Expression expression3 = (Expression) expression2.getOperands().get(0);
            Expression expression4 = (Expression) expression3.getOperands().get(0);
            Variable var2 = (Variable) expression4.getOperands().get(0);
            Expression expression5 = (Expression) expression3.getOperands().get(1);
            IntLiteral intLiteral = (IntLiteral) expression5.getOperands().get(0);

            Variable var3 = (Variable) expression2.getOperands().get(1);

            assertEquals("x", var1.getName());
            assertEquals("z", var2.getName());
            assertEquals(5, intLiteral.getValue());
            assertEquals("y", var3.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldAssignFunction() {
        try {
            StringBuilder builder = new StringBuilder(" START() { x = pobierzX(); }");
            Parser parser = new Parser(new Lexer(new InputManager(builder.toString())));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            Assignment assignment = (Assignment) block.getInstructions().get(0);
            Variable var1 = assignment.getVariable();
            Expression expression1 = (Expression) assignment.getValue();
            Expression expression2 = (Expression) expression1.getOperands().get(0);
            Function function = (Function) expression2.getOperands().get(0);

            assertEquals("x", var1.getName());
            assertEquals(FunctionType.GET_X, function.getFunctionType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void ShouldParseIfStatementWithAndOperator() {
        try {
            Parser parser = new Parser(new Lexer(new InputManager(" START() { jezeli [4 == y+1 || w < 5] {}  }")));

            Program program = parser.parse();
            Event event = program.getEvents().get(0);
            Block block = event.getCodeBlock();
            IfStatement ifStatement = (IfStatement) block.getInstructions().get(0);
            Expression orCond = ifStatement.getCondition();
            Expression andCond = (Expression) orCond.getOperands().get(0);
            Expression relCond = (Expression) andCond.getOperands().get(0);
            Expression addExpr = (Expression) relCond.getOperands().get(0);
            Expression mulExpr = (Expression) addExpr.getOperands().get(0);
            IntLiteral int1 = (IntLiteral) mulExpr.getOperands().get(0);

            Expression addExpr2 = (Expression) relCond.getOperands().get(1);
            Expression mulExpr2 = (Expression) addExpr2.getOperands().get(0);
            Variable var1 = (Variable) mulExpr2.getOperands().get(0);

            Expression mulExpr3 = (Expression) addExpr2.getOperands().get(1);
            IntLiteral int2 = (IntLiteral) mulExpr3.getOperands().get(0);

            Expression andCond1 = (Expression) orCond.getOperands().get(1);
            Expression relCond2 = (Expression) andCond1.getOperands().get(0);
            Expression addExpr3 = (Expression) relCond2.getOperands().get(0);
            Expression mulExpr4 = (Expression) addExpr3.getOperands().get(0);
            Variable var2 = (Variable) mulExpr4.getOperands().get(0);

            Expression addExpr4 = (Expression) relCond2.getOperands().get(1);
            Expression mulExpr5 = (Expression) addExpr4.getOperands().get(0);
            IntLiteral int3 = (IntLiteral) mulExpr5.getOperands().get(0);

            assertEquals(4, int1.getValue());
            assertEquals("y", var1.getName());
            assertEquals(1, int2.getValue());
            assertEquals("w", var2.getName());
            assertEquals(5, int3.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldThrowExceptionWhereThereIsNotSemicolonAfterAssignment() {
        try {
            String value = " START() { x = 4  }";
            Parser parser = new Parser(new Lexer(new InputManager(value)));
            parser.parse();
            fail();
        } catch (Exception e) {
            assertEquals("Oczekiwane wyrażenie: ;",e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhereThereIsNotSemicolonAfterFunction() {
        try {
            String value = " START() { pobierzX()  }";
            Parser parser = new Parser(new Lexer(new InputManager(value)));
            parser.parse();
            fail();
        } catch (Exception e) {
            assertEquals("Oczekiwane wyrażenie: ;",e.getMessage());
        }
    }

    @Test
    public void shouldParseRepeatIfStatement() {
        try {
            String value = " START() { pobierzX()  }";
            Parser parser = new Parser(new Lexer(new InputManager(value)));
            parser.parse();
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("Oczekiwane wyrażenie: ;",e.getMessage());
        }
    }
}