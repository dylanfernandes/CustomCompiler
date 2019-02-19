package syntacticAnalyzer;

import lexer.Token;

import java.util.List;

public class Parser {

    private List<Token> tokenList;
    private int position;

    public Parser(List<Token> tokens) {
        tokenList = tokens;
        position = 0;
    }

    private boolean hasNextToken() {
        return position < tokenList.size();
    }

    public Token nextToken() {
        Token token;
        if(hasNextToken()) {
            token = tokenList.get(position);
            return  token;
        }
        return  null;
    }

    public static boolean match(Token currentToken, Token.TokenType expectedTokenType) {
        return  currentToken.getType() == expectedTokenType;
    }

    public String parse() {
        String syntax = "";

        return syntax;
    }

}
