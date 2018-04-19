package lexer;

import token.Token;
import token.TokenType;

import java.util.HashMap;
import java.util.Map;

class KeywordsOperatorsHashmap {
    static Map<String, Token> OPERATORS = new HashMap<String, Token>() {{
        put("+", new Token(TokenType.ADD));
        put("-", new Token(TokenType.MINUS));
        put("*", new Token(TokenType.MULTIPLY));
        put("/", new Token(TokenType.DIVIDE));
        put(";", new Token(TokenType.SEMICOLON));
        put(".", new Token(TokenType.DOT));
        put("{", new Token(TokenType.BRACKET_OPEN));
        put("}", new Token(TokenType.BRACKET_CLOSE));
        put("(", new Token(TokenType.PARENTHESIS_OPEN));
        put(")", new Token(TokenType.PARENTHESIS_CLOSE));
        put("==", new Token(TokenType.EQUAL));
        put("!=", new Token(TokenType.NOT_EQUAL));
        put("&&", new Token(TokenType.AND));
        put("||", new Token(TokenType.OR));
        put("<", new Token(TokenType.LESS));
        put(">", new Token(TokenType.GREATER));
        put("<=", new Token(TokenType.LESS_EQUAL));
        put(">=", new Token(TokenType.GREATER_EQUAL));
        put("=", new Token(TokenType.ASSIGNMENT));
    }};

    static Map<String, Token> KEYWORDS = new HashMap<String, Token>() {{
        put("jezeli", new Token(TokenType.IF));
        put("inaczej", new Token(TokenType.ELSE));
        put("powtorz", new Token(TokenType.REPEAT));
        put("powtorzJezeli", new Token(TokenType.REPEAT_IF));
    }};
}
