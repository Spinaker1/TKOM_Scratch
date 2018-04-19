package lexer;

import token.Token;
import token.TokenType;

import java.util.ArrayList;

class Functions {
    static final ArrayList<String> list = new ArrayList<String>() {{
        add("idz");
        add("idzDoMyszy");
        add("zmienKolor");
        add("zmienRozmiar");
        add("powiedz");
        add("czekaj");
        add("pobierzX");
        add("pobierzY");
        add("pobierzRotacje");
        add("idzLewo");
        add("idzPrawo");
        add("idzGora");
        add("idzDol");
        add("obrocPrawo");
        add("obrocLewo");
    }};

    static Token get(String s) {
        if (list.contains(s)) {
            return new Token(TokenType.FUNCTION,s);
        }

        return null;
    }
}
