import input.InputManager;
import node.Function;
import org.junit.Test;
import token.EventType;
import token.FunctionType;
import token.Token;
import token.TokenType;
import lexer.Lexer;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void shouldParseAddOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("+"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.ADD, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseMinusOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("-"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.MINUS, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseMultiplyOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("*"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.MULTIPLY, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseDivideOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("/"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.DIVIDE, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseComma() {
        try {
            Lexer lexer = new Lexer(new InputManager(","));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.COMMA, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseModuloOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("%"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.MODULO, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void shouldParseSemicolon() {
        try {
            Lexer lexer = new Lexer(new InputManager(";"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.SEMICOLON, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseEqualOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("=="));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.EQUAL, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseNotEqualOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("!="));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.NOT_EQUAL, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseBracketOpen() {
        try {
            Lexer lexer = new Lexer(new InputManager("{"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.BRACKET_OPEN, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseBracketClose() {
        try {
            Lexer lexer = new Lexer(new InputManager("}"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.BRACKET_CLOSE, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseParenthesisOpen() {
        try {
            Lexer lexer = new Lexer(new InputManager("("));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.PARENTHESIS_OPEN, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseParenthesisClose() {
        try {
            Lexer lexer = new Lexer(new InputManager(")"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.PARENTHESIS_CLOSE, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseAndOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("&&"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.AND, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseOrOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("||"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.OR, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseLessOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("<"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.LESS, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseGreaterOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager(">"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.GREATER, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseLessEqualOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("<="));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.LESS_EQUAL, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseGreaterEqualOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager(">="));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.GREATER_EQUAL, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseAssignmentOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("="));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.ASSIGNMENT, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseNegationOperator() {
        try {
            Lexer lexer = new Lexer(new InputManager("!"));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.NEGATION, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseInt() {
        try {
            String value = "123";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.INTEGER, token.getTokenType());
            assertEquals(123, token.getIntValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldNotParseIntWhichStartsAtZero() {
        try {
            String value = "00123";
            Lexer lexer = new Lexer(new InputManager(value));

            lexer.getNextToken();

            fail();
        } catch (Exception e) {
            assertEquals("Liczba nie powinna zaczynać się od 0.",e.getMessage());
        }
    }

    @Test
    public void shouldParseString() {
        try {
            String value = "\"abc\"";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.STRING, token.getTokenType());
            assertEquals("abc", token.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldEscapeQutationMarkInString() {
        try {
            String value = "\"a\\\"bc\\\"\"";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.STRING, token.getTokenType());
            assertEquals("a\"bc\"", token.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseVariable() {
        try {
            String value = "kangurek1";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.VARIABLE, token.getTokenType());
            assertEquals("kangurek1", token.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldDeleteComments() {
        try {
            String value = (
                    " # komentarz  \n" +
                            "  69 #456 \n" +
                            " \"kwoka\"");
            Lexer lexer = new Lexer(new InputManager(value));

            Token token1 = lexer.getNextToken();
            Token token2 = lexer.getNextToken();

            assertEquals(TokenType.INTEGER, token1.getTokenType());
            assertEquals(69, token1.getIntValue());
            assertEquals(TokenType.STRING, token2.getTokenType());
            assertEquals("kwoka", token2.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseIfKeyword() {
        try {
            String value = "jezeli";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.IF, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseRepeatKeyword() {
        try {
            String value = "powtorz";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.REPEAT, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseRepeatIfKeyword() {
        try {
            String value = "powtorzJezeli";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.REPEAT_IF, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseEvents() {
        try {
            String value = "SCIANA START KOLIZJA MYSZ";
            Lexer lexer = new Lexer(new InputManager(value));

            Token token1 = lexer.getNextToken();
            Token token2 = lexer.getNextToken();
            Token token3 = lexer.getNextToken();
            Token token4 = lexer.getNextToken();

            assertEquals(TokenType.EVENT, token1.getTokenType());
            assertEquals(EventType.WALL, token1.getEventType());
            assertEquals(TokenType.EVENT, token2.getTokenType());
            assertEquals(EventType.START, token2.getEventType());
            assertEquals(TokenType.EVENT, token3.getTokenType());
            assertEquals(EventType.COLLISION, token3.getEventType());
            assertEquals(TokenType.EVENT, token4.getTokenType());
            assertEquals(EventType.MOUSE, token4.getEventType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldParseFunctions() {
        try {
            String value = "idz idzDoMyszy zmienKolor zmienRozmiar powiedz czekaj " +
                    "pobierzX pobierzY pobierzRotacje idzLewo idzPrawo idzGora idzDol " +
                    "obrocPrawo obrocLewo";

            FunctionType[] functionTypes = FunctionType.values();

            Lexer lexer = new Lexer(new InputManager(value));

            int i = 0;
            Token token;
            while ((token = lexer.getNextToken()).getTokenType() != TokenType.EOF) {
                assertEquals(TokenType.FUNCTION, token.getTokenType());
                assertEquals(functionTypes[i], token.getFunctionType());
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldShowError() {
        try {
            Lexer lexer = new Lexer(new InputManager("^"));

            Token token = lexer.getNextToken();

            fail();
        } catch (Exception e) {
            assertEquals("Nieprawidłowe wyrażenie", e.getMessage());
        }
    }

    @Test
    public void shouldCorrectlyParseAllTokens() {
        try {
            String value = "idzLewo(50); #Idę w lewo 50 pikseli \n" +
                    "jezeli (a == 4) { c = \"kwiat\"; }";
            InputManager source = new InputManager(value);
            Lexer lexer = new Lexer(source);

            Token token = lexer.getNextToken();
            assertEquals(TokenType.FUNCTION, token.getTokenType());
            assertEquals(FunctionType.GO_LEFT, token.getFunctionType());

            token = lexer.getNextToken();
            assertEquals(TokenType.PARENTHESIS_OPEN, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.INTEGER, token.getTokenType());
            assertEquals(50, token.getIntValue());

            token = lexer.getNextToken();
            assertEquals(TokenType.PARENTHESIS_CLOSE, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.SEMICOLON, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.IF, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.PARENTHESIS_OPEN, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.VARIABLE, token.getTokenType());
            assertEquals("a", token.getStringValue());

            token = lexer.getNextToken();
            assertEquals(TokenType.EQUAL, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.INTEGER, token.getTokenType());
            assertEquals(4, token.getIntValue());

            token = lexer.getNextToken();
            assertEquals(TokenType.PARENTHESIS_CLOSE, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.BRACKET_OPEN, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.VARIABLE, token.getTokenType());
            assertEquals("c", token.getStringValue());

            token = lexer.getNextToken();
            assertEquals(TokenType.ASSIGNMENT, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.STRING, token.getTokenType());
            assertEquals("kwiat", token.getStringValue());

            token = lexer.getNextToken();
            assertEquals(TokenType.SEMICOLON, token.getTokenType());

            token = lexer.getNextToken();
            assertEquals(TokenType.BRACKET_CLOSE, token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}