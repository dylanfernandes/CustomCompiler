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
        position = -1;
        syntax = "";

    }

    private boolean hasNextToken() {
        return position + 1 < tokenList.size();
    }

    public Token nextToken() {
        Token token;
        if(hasNextToken()) {
            position++;
            token = tokenList.get(position);
            return  token;
        }
        return  null;
    }

    public Token peekToken() {
        if(hasNextToken()) {
            return tokenList.get(position+1);
        }
        return null;
    }

    public boolean match( Token.TokenType expectedTokenType) {
        lookahead = nextToken();
        return  lookahead != null && lookahead.getType() == expectedTokenType;
    }

    public boolean peekMatch(Token.TokenType expectedTokenType) {
        lookahead = peekToken();
        return lookahead != null && lookahead.getType() == expectedTokenType;
    }

    public String parse() {
        prog();
        return syntax;
    }


    public boolean prog() {
        if(classDeclRep() && funcDefRep() && match(Token.TokenType.MAIN) && funcBody() && match(Token.TokenType.SEMI)) {
            addToSyntax("prog -> classDeclRep funcDefRep 'main' funcBody ';'");
            return true;
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
        if(classDecl() && classDeclRep()) {
            addToSyntax("classDeclRep -> classDecl classDeclRep");
            return true;
        }
        else if(peekMatch(Token.TokenType.ID) || peekMatch(Token.TokenType.FLOAT) || peekMatch(Token.TokenType.INTEGER) || peekMatch(Token.TokenType.MAIN)){
            addToSyntax("classDeclRep -> EPSILON");
            return true;
        }
        return false;
    }

    private boolean classDecl() {
        return false;
    }

}
