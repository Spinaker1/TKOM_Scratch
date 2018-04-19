import input.InputManager;
import lexer.Lexer;
import org.junit.Test;
import token.Token;
import token.TokenType;

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
    public void shouldParseDot() {
        Lexer lexer = new Lexer(new InputManager("."));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.DOT, token.getTokenType());
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
    public void shouldParseInt() {
        String value = "123";
        Lexer lexer = new Lexer(new InputManager(value));

        Token token = lexer.getNextToken();

        assertEquals(TokenType.INTEGER, token.getTokenType());
        assertEquals(123,token.getIntValue());
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
        String value = ("//komentarz\n69 //456 \n \"kwoka\"");
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
}