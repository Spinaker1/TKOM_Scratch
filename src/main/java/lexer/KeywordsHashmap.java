package lexer;

import token.Token;
import token.TokenType;

import java.util.HashMap;
import java.util.Map;

class KeywordsHashmap {
    static final Map<String, Token> KEYWORDS = new HashMap<String, Token>() {{
        put("jezeli", new Token(TokenType.IF));
        put("inaczej", new Token(TokenType.ELSE));
        put("powtorz", new Token(TokenType.REPEAT));
        put("powtorzJezeli", new Token(TokenType.REPEAT_IF));
    }};
}
