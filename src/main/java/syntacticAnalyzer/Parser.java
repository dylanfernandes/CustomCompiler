package syntacticAnalyzer;

import lexer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        lookahead = tokenList.get(0);
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

    public boolean skipErrors(List<Token.TokenType> first, List<Token.TokenType> follow) {
        if(lookahead != null) {
            if (first.contains(lookahead.getType()) || (first.contains(Token.TokenType.EPSILON) && follow.contains(lookahead.getType()))) {
                return true;
            } else {
                addToSyntax("Syntax error at line: " + lookahead.getLineNumber());
                //if in follow, current production not good, can get epsilon if in first in parse method
                while (!first.contains(lookahead.getType()) && !follow.contains(lookahead.getType())) {
                    lookahead = nextToken();
                    if(lookahead == null)
                        break;
                    //production not properly completed
                    if (first.contains(Token.TokenType.EPSILON) && follow.contains(lookahead.getType()))
                        return false;
                }
                if(lookahead != null)
                    return true;
            }
        }
        return false;
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

    public void addToSyntax(String newSyntax) {
        syntax = newSyntax + "\n" + syntax;
    }

    public String parse() {
        prog();
        return syntax;
    }


    public boolean prog() {
        if(!skipErrors(Arrays.asList(Token.TokenType.INTEGER, Token.TokenType.FLOAT, Token.TokenType.CLASS, Token.TokenType.ID, Token.TokenType.MAIN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if (classDeclRep() && funcDefRep() && match(Token.TokenType.MAIN) && funcBody() && match(Token.TokenType.SEMI)) {
            addToSyntax("prog -> classDeclRep funcDefRep 'main' funcBody ';'");
            return true;
        }
        return false;
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
        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.CLASS), Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.MAIN))) {
            return false;
        }

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
        if(arraySizeRep() && varOrFuncCheck() && match(Token.TokenType.SEMI)) {
            addToSyntax("varCheckNext -> arraySizeRep varOrFuncCheck  ';'");
            return true;
        }
        else if(match(Token.TokenType.OPAR) && fParams() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI) && funcDeclRep()) {
            addToSyntax("varCheckNext -> '(' fParams ')' ';' funcDeclRep ");
            return true;
        }
        return false;
    }

    private boolean fParams() {
        return false;
    }

    private boolean funcDeclRep() {
        if (funcDecl() && funcDeclRep()) {
            addToSyntax("funcDeclRep -> funcDecl funcDeclRep");
            return true;
        } else if (peekMatch(Token.TokenType.CBRA) || peekMatch(Token.TokenType.SEMI)) {
            addToSyntax("funcDeclRep -> EPSILON");
            return true;
        }
        return false;
    }

    private boolean funcDecl() {
        if(type() && match(Token.TokenType.ID) && match(Token.TokenType.OPAR) && fParams() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI)) {
            addToSyntax("funcDecl -> type 'id' '(' fParams ')' ';' ");
            return true;
        }
        return false;
    }

    private boolean arraySizeRep() {
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
