
import input.InputManager;
import org.junit.Test;
import token.Token;
import token.TokenType;
import lexer.Lexer;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void shouldParseAddOperator() {
        Lexer lexer = new Lexer(new InputManager("+"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.ADD, token.getTokenType());
    }

    @Test
    public void shouldParseMinusOperator() {
        Lexer lexer = new Lexer(new InputManager("-"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void shouldParseMultiplyOperator() {
        Lexer lexer = new Lexer(new InputManager("*"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.MULTIPLY, token.getTokenType());
    }

    @Test
    public void shouldParseDivideOperator() {
        Lexer lexer = new Lexer(new InputManager("/"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.DIVIDE, token.getTokenType());
    }

    @Test
    public void shouldParseModuloOperator() {
        Lexer lexer = new Lexer(new InputManager("%"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.MODULO, token.getTokenType());
    }


    @Test
    public void shouldParseSemicolon() {
        Lexer lexer = new Lexer(new InputManager(";"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.SEMICOLON, token.getTokenType());
    }

    @Test
    public void shouldParseEqualOperator() {
        Lexer lexer = new Lexer(new InputManager("=="));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.EQUAL, token.getTokenType());
    }

    @Test
    public void shouldParseNotEqualOperator() {
        Lexer lexer = new Lexer(new InputManager("!="));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.NOT_EQUAL, token.getTokenType());
    }

    @Test
    public void shouldParseBracketOpen() {
        Lexer lexer = new Lexer(new InputManager("{"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.BRACKET_OPEN, token.getTokenType());
    }

    @Test
    public void shouldParseBracketClose() {
        Lexer lexer = new Lexer(new InputManager("}"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.BRACKET_CLOSE, token.getTokenType());
    }

    @Test
    public void shouldParseParenthesisOpen() {
        Lexer lexer = new Lexer(new InputManager("("));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.PARENTHESIS_OPEN, token.getTokenType());
    }

    @Test
    public void shouldParseParenthesisClose() {
        Lexer lexer = new Lexer(new InputManager(")"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.PARENTHESIS_CLOSE, token.getTokenType());
    }

    @Test
    public void shouldParseAndOperator() {
        Lexer lexer = new Lexer(new InputManager("&&"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void shouldParseOrOperator() {
        Lexer lexer = new Lexer(new InputManager("||"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.OR, token.getTokenType());
    }

    @Test
    public void shouldParseLessOperator() {
        Lexer lexer = new Lexer(new InputManager("<"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.LESS, token.getTokenType());
    }

    @Test
    public void shouldParseGreaterOperator() {
        Lexer lexer = new Lexer(new InputManager(">"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.GREATER, token.getTokenType());
    }

    @Test
    public void shouldParseLessEqualOperator() {
        Lexer lexer = new Lexer(new InputManager("<="));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.LESS_EQUAL, token.getTokenType());
    }

    @Test
    public void shouldParseGreaterEqualOperator() {
        Lexer lexer = new Lexer(new InputManager(">="));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.GREATER_EQUAL, token.getTokenType());
    }

    @Test
    public void shouldParseAssignmentOperator() {
        Lexer lexer = new Lexer(new InputManager("="));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.ASSIGNMENT, token.getTokenType());
    }

    @Test
    public void shouldParseNegationOperator() {
        Lexer lexer = new Lexer(new InputManager("!"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.NEGATION, token.getTokenType());
    }

    @Test
    public void shouldParseInt() {
        String value = "123";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.INTEGER, token.getTokenType());
        assertEquals(123,token.getIntValue());
    }

    @Test
    public void shouldNotParseIntWhichStartsAtZero() {
        String value = "00123";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void shouldParseString() {
        String value = "\"abc\"";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.STRING, token.getTokenType());
        assertEquals("abc", token.getStringValue());
    }

    @Test
    public void shouldParseVariable() {
        String value = "kangurek1";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("kangurek1", token.getStringValue());
    }

    @Test
    public void shouldDeleteComments() {
        String value = (
                "# komentarz\n" +
                "69 #456 \n" +
                " \"kwoka\"");
        Lexer lexer = new Lexer(new InputManager(value));

        Token token1 = lexer.getNextToken();
        Token token2 = lexer.getNextToken();

        assertEquals(TokenType.INTEGER, token1.getTokenType());
        assertEquals(69,token1.getIntValue());
        assertEquals(TokenType.STRING, token2.getTokenType());
        assertEquals("kwoka", token2.getStringValue());
    }

    @Test
    public void shouldParseIfKeyword() {
        String value = "jezeli";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.IF, token.getTokenType());
    }

    @Test
    public void shouldParseElseKeyword() {
        String value = "inaczej";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.ELSE, token.getTokenType());
    }

    @Test
    public void shouldParseRepeatKeyword() {
        String value = "powtorz";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.REPEAT, token.getTokenType());
    }

    @Test
    public void shouldParseRepeatIfKeyword() {
        String value = "powtorzJezeli";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.REPEAT_IF, token.getTokenType());
    }

    @Test
    public void shouldParseEvents() {
        String value = "SCIANA START KOLIZJA MYSZ";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token1 = lexer.getNextToken();
        Token token2 = lexer.getNextToken();
        Token token3 = lexer.getNextToken();
        Token token4 = lexer.getNextToken();

        assertEquals(TokenType.EVENT, token1.getTokenType());
        assertEquals("SCIANA", token1.getStringValue());
        assertEquals(TokenType.EVENT, token2.getTokenType());
        assertEquals("START", token2.getStringValue());
        assertEquals(TokenType.EVENT, token3.getTokenType());
        assertEquals("KOLIZJA", token3.getStringValue());
        assertEquals(TokenType.EVENT, token4.getTokenType());
        assertEquals("MYSZ", token4.getStringValue());
    }

    @Test
    public void shouldParseFunctions() {
        String[] functionNames = { "idz",	"idzDoMyszy",	"zmienKolor",	"zmienRozmiar",	"powiedz",	"czekaj",
                "pobierzX",	"pobierzY",	"pobierzRotacje",	"idzLewo",	"idzPrawo",	"idzGora",	"idzDol",
                "obrocPrawo",	"obrocLewo"};

        for (String functionName : functionNames) {
            Lexer lexer = new Lexer(new InputManager(functionName));

            Token token = lexer.getNextToken();

            assertEquals(TokenType.FUNCTION, token.getTokenType());
            assertEquals(functionName, token.getStringValue());
        }
    }

    @Test
    public void shouldShowError() {
        Lexer lexer = new Lexer(new InputManager("^"));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void shouldCorrectlyParseAllTokens() {
        String value = "idzLewo(50); #Idę w lewo 50 pikseli \n" +
                "jezeli (a == 4) { c = \"kwiat\"; }\n";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();
        assertEquals(TokenType.FUNCTION, token.getTokenType());
        assertEquals("idzLewo", token.getStringValue());

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
    }
}