package lexer;

import token.Token;
import token.TokenType;

import java.util.ArrayList;

class Events {
    static final ArrayList<String> list = new ArrayList<String>() {{
        add("START");
        add("SCIANA");
        add("MYSZ");
        add("KOLIZJA");
    }};

    static Token get(String s) {
        if (list.contains(s)) {
            return new Token(TokenType.EVENT,s);
        }

        return null;
    }
}
