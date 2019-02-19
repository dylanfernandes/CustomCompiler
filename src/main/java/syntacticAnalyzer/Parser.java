package syntacticAnalyzer;

import lexer.Token;

import java.util.List;

public class Parser {

    private List<Token> tokenList;
    private int position;
    private Token lookahead;
    private String syntax;

    public Parser() {
        position = 0;
        syntax = "";
    }

    public Parser(List<Token> tokens) {
        new Parser();
        tokenList = tokens;
        if(tokenList.size() > 0) {
            lookahead = tokenList.get(0);
        }
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
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
        if(lookahead != null && lookahead.getType() == expectedTokenType) {
            lookahead = nextToken();
            return true;
        }
        return  false;
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
        if(funcDef() && funcDefRep()) {
            addToSyntax("funcDefRep -> funcDef funcDefRep");
            return true;
        }
        else if(peekMatch(Token.TokenType.MAIN)) {
            addToSyntax("funcDefRep -> EPSILON");
            return true;
        }
        return false;
    }

    private boolean funcDef() {
        return false;
    }

    public boolean classDeclRep() {
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

    public boolean classDecl() {
        if(match(Token.TokenType.CLASS) && match(Token.TokenType.ID) && classExOpt() && match(Token.TokenType.OBRA) && varOrFuncCheck() && match(Token.TokenType.CBRA) && match(Token.TokenType.SEMI)) {
            addToSyntax("classDecl -> 'class' 'id' classExOpt '{' varOrFuncCheck '}' ';'");
        }
        return false;
    }

    private boolean varOrFuncCheck() {
        if(type() && match(Token.TokenType.ID) && varCheckNext()) {
            addToSyntax("varOrFuncCheck -> type 'id' varCheckNext");
            return true;
        } else if(peekMatch(Token.TokenType.CBRA) || peekMatch(Token.TokenType.SEMI)) {
            addToSyntax("varOrFuncCheck -> EPSILON");
            return true;
        }
        return false;
    }

    private boolean type() {
        if(typeNotId()) {
            addToSyntax("type -> typeNotId");
            return true;
        }
        else if(match(Token.TokenType.ID)) {
            addToSyntax("type -> 'id'");
            return true;
        }
        return false;
    }

    private boolean typeNotId() {
        if(match(Token.TokenType.INTEGER)) {
            addToSyntax("typeNotId -> 'integer'");
            return true;
        }
        else if(match(Token.TokenType.FLO)) {
            addToSyntax("typeNotId -> 'float' ");
            return true;
        }
        return false;
    }

    private boolean varCheckNext() {
        return false;
    }

    private boolean classExOpt() {
        if(match(Token.TokenType.COLO) && match(Token.TokenType.ID) && classExMoreRep()) {
            addToSyntax("classExOpt ->  ':' 'id' classExMoreRep");
            return true;
        }
        else if(peekMatch(Token.TokenType.OPAR)) {
            addToSyntax("classExOpt -> EPSILON");
            return true;
        }
        return false;
    }

    private boolean classExMoreRep() {
        if(match(Token.TokenType.COMM) && match(Token.TokenType.ID) && classExMoreRep()) {
            return true;
        }
        else if(peekMatch(Token.TokenType.OPAR)) {
            addToSyntax("classExMoreRep -> EPSILON");
            return true;
        }
        return false;
    }

}
