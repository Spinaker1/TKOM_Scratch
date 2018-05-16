package lexer;

import com.sun.org.apache.xpath.internal.axes.WalkingIterator;
import node.Function;
import token.EventType;
import token.FunctionType;
import token.Token;
import token.TokenType;

import java.util.HashMap;
import java.util.Map;

class KeywordsHashmap {
    static final Map<String, Token> KEYWORDS = new HashMap<String, Token>() {{
        put("jezeli", new Token(TokenType.IF));
        put("powtorz", new Token(TokenType.REPEAT));
        put("powtorzJezeli", new Token(TokenType.REPEAT_IF));

        put("START", new Token(TokenType.EVENT, EventType.START));
        put("MYSZ", new Token(TokenType.EVENT, EventType.MOUSE));
        put("KOLIZJA", new Token(TokenType.EVENT, EventType.COLLISION));
        put("SCIANA", new Token(TokenType.EVENT, EventType.WALL));

        put("idz", new Token(TokenType.FUNCTION, FunctionType.GO));
        put("idzDoMyszy", new Token(TokenType.FUNCTION, FunctionType.GO_TO_MOUSE));
        put("zmienKolor", new Token(TokenType.FUNCTION, FunctionType.CHANGE_COLOR));
        put("zmienRozmiar", new Token(TokenType.FUNCTION, FunctionType.CHANGE_SIZE));
        put("powiedz", new Token(TokenType.FUNCTION, FunctionType.TALK));
        put("czekaj", new Token(TokenType.FUNCTION, FunctionType.WAIT));
        put("pobierzX", new Token(TokenType.FUNCTION, FunctionType.GET_X));
        put("pobierzY", new Token(TokenType.FUNCTION, FunctionType.GET_Y));
        put("pobierzRotacje", new Token(TokenType.FUNCTION, FunctionType.GET_ROTATION));
        put("idzLewo", new Token(TokenType.FUNCTION, FunctionType.GO_LEFT));
        put("idzPrawo", new Token(TokenType.FUNCTION, FunctionType.GO_RIGHT));
        put("idzGora", new Token(TokenType.FUNCTION, FunctionType.GO_UP));
        put("idzDol", new Token(TokenType.FUNCTION, FunctionType.GO_DOWN));
        put("obrocPrawo", new Token(TokenType.FUNCTION, FunctionType.ROTATE_RIGHT));
        put("obrocLewo", new Token(TokenType.FUNCTION, FunctionType.ROTATE_LEFT));
    }};
}
