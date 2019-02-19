package syntacticAnalyzer;

import lexer.Token;

import java.util.List;

public class Parser {

    private List<Token> tokenList;
    private int position;
    private Token lookahead;
    private String syntax;


    public Parser(List<Token> tokens) {
        tokenList = tokens;
        position = 0;
        syntax = "";

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

    public boolean match( Token.TokenType expectedTokenType) {
        lookahead = nextToken();
        return  lookahead != null && lookahead.getType() == expectedTokenType;
    }

    public String parse() {
        prog();
        return syntax;
    }


    public boolean prog() {
        lookahead = nextToken();
        if(lookahead != null) {
            if(classDeclRep() && funcDefRep() && match(Token.TokenType.MAIN) && funcBody() && match(Token.TokenType.SEMI)) {
                addToSyntax("prog -> classDeclRep funcDefRep 'main' funcBody ';'");
                return true;
            }
        }
        return false;
    }

    public void addToSyntax(String newSyntax) {
        syntax = newSyntax + "\n" + syntax;
    }

    private boolean funcBody() {
        return false;
    }

    private boolean funcDefRep() {
        return false;
    }

    private boolean classDeclRep() {
        return false;
    }

}
