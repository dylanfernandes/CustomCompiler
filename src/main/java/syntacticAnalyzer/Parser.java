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
    boolean parseGood;

    public Parser() {
        position = 0;
        syntax = "";
        parseGood = false;
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

    public boolean isParseGood() {
        return parseGood;
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

    public boolean skipErrors(List<Token.TokenType> first, List<Token.TokenType> follow, boolean write) {
        if(lookahead != null) {
            if (first.contains(lookahead.getType()) || (first.contains(Token.TokenType.EPSILON) && follow.contains(lookahead.getType()))) {
                return true;
            } else {
                if(write)
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

    //write error in syntax automatically
    public boolean skipErrors(List<Token.TokenType> first, List<Token.TokenType> follow) {
        return skipErrors(first, follow, true);
    }


    public boolean match( Token.TokenType expectedTokenType) {
        if(lookahead != null && lookahead.getType() == expectedTokenType) {
            lookahead = nextToken();
            return true;
        }
        return  false;
    }

    public boolean peekListMatch(List<Token.TokenType> tokenTypes) {
        return lookahead != null && tokenTypes.contains(lookahead.getType());
    }

    public boolean peekMatch(Token.TokenType expectedTokenType) {
        return lookahead != null && lookahead.getType() == expectedTokenType;
    }

    public void addToSyntax(String newSyntax) {
        syntax = newSyntax + "\n" + syntax;
    }

    public String parse() {
        parseGood = prog();
        return syntax;
    }

    /******************************************************************
         Start of grammar production implementation
     *******************************************************************/


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
        if(!skipErrors(Arrays.asList(Token.TokenType.OBRA), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.OBRA) && varDeclStatFuncRep() && match(Token.TokenType.CBRA)) {
            addToSyntax("funcBody -> '{' varDeclStatFuncRep '}'");
            return true;
        }
        return false;
    }

    private boolean varDeclStatFuncRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.FOR, Token.TokenType.IF, Token.TokenType.READ, Token.TokenType.RETURN, Token.TokenType.WRITE, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CBRA))) {
            return false;
        }

        if(peekMatch(Token.TokenType.CBRA)) {
            addToSyntax("varDeclStatFuncRep -> EPSILON");
            return  true;
        } else if(varDeclNotId() && varDeclStatFuncRep()) {
            addToSyntax("varDeclStatFuncRep -> varDeclNotId varDeclStatFuncRep");
            return true;
        } else if(idProd() && varDeclStatFuncRep()) {
            addToSyntax("varDeclStatFuncRep -> idProd varDeclStatFuncRep");
            return true;
        } else  if(statement() && varDeclStatFuncRep()) {
            addToSyntax("statement -> idProd varDeclStatFuncRep");
            return true;
        }
        return false;
    }

    private boolean varDeclNotId() {
        return false;
    }

    private boolean idProd() {
        //error written by last prod
        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList(), false)) {
            return false;
        }

        if(match(Token.TokenType.ID) && idProdNext()) {
            addToSyntax("idProd -> 'id' idProdNext");
            return true;
        }
        return false;
    }

    private boolean idProdNext() {
        return false;
    }

    private boolean statement() {
        return false;
    }


    private boolean funcDefRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.ID, Token.TokenType.INTEGER, Token.TokenType.FLOAT), Arrays.asList(Token.TokenType.MAIN))){
            return false;
        }
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
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(funcHead() && funcBody() && match(Token.TokenType.SEMI)) {
            addToSyntax("funcDef -> funcHead funcBody ';'");
            return true;
        }
        return false;
    }

    private boolean funcHead() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(type() && match(Token.TokenType.ID) && funcHeadChoice()) {
            addToSyntax("funcHead -> type 'id' funcHeadChoice");
            return true;
        }
        return false;
    }

    private boolean funcHeadChoice() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.DCOLO), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.DCOLO) && match(Token.TokenType.ID) && match(Token.TokenType.OPAR) && fParams() && match(Token.TokenType.CPAR)) {
            addToSyntax("funcHeadChoice -> 'sr' 'id' '(' fParams ')'");
            return true;
        } else if (match(Token.TokenType.OPAR) && fParams() && match(Token.TokenType.CPAR)) {
            addToSyntax("funcHeadChoice -> '(' fParams ')'");
            return true;
        }
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
        if(!skipErrors(Arrays.asList(Token.TokenType.CLASS), Collections.<Token.TokenType>emptyList())) {
            return false;
        }
        if(match(Token.TokenType.CLASS) && match(Token.TokenType.ID) && classExOpt() && match(Token.TokenType.OBRA) && varOrFuncCheck() && match(Token.TokenType.CBRA) && match(Token.TokenType.SEMI)) {
            addToSyntax("classDecl -> 'class' 'id' classExOpt '{' varOrFuncCheck '}' ';'");
            return true;
        }
        return false;
    }

    public boolean varOrFuncCheck() {
        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER), Arrays.asList(Token.TokenType.CBRA, Token.TokenType.SEMI))) {
            return false;
        }
         if(peekMatch(Token.TokenType.CBRA) || peekMatch(Token.TokenType.SEMI)) {
            addToSyntax("varOrFuncCheck -> EPSILON");
            return true;
        } else if(type() && match(Token.TokenType.ID) && varCheckNext()) {
            addToSyntax("varOrFuncCheck -> type 'id' varCheckNext");
            return true;
        }
        return false;
    }

    public boolean type() {
        if(!skipErrors(Arrays.asList(Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.ID), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.ID)) {
            addToSyntax("type -> 'id'");
            return true;
        } else if(typeNotId()) {
            addToSyntax("type -> typeNotId");
            return true;
        }
        return false;
    }

    public boolean typeNotId() {
        if(!skipErrors(Arrays.asList(Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList())) {
            return false;
        }
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

    public boolean varCheckNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.SEMI, Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

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

    public boolean fParams() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CPAR))) {
            return  false;
        }

        if(peekMatch(Token.TokenType.CPAR)) {
            addToSyntax("fParams -> EPSILON");
            return true;
        }
        else if(type() && match(Token.TokenType.ID) && arraySizeRep() && fParamsTailRep()) {
            addToSyntax("fParams -> type 'id' arraySizeRep fParamsTailRep");
            return true;
        }
        return false;
    }

    private boolean fParamsTailRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.COMM, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CPAR))) {
            return  false;
        }

        if(peekMatch(Token.TokenType.CPAR)) {
            addToSyntax("fParamsTailRep -> EPSILON");
            return  true;
        } else if(fParamsTail() && fParamsTailRep()) {
            addToSyntax("fParamsTailRep -> fParamsTail fParamsTailRep");
            return  true;
        }
        return false;
    }

    private boolean fParamsTail() {
        if(!skipErrors(Arrays.asList(Token.TokenType.COMM), Collections.<Token.TokenType>emptyList())) {
            return  false;
        }

        if(match(Token.TokenType.COMM) && type() && match(Token.TokenType.ID) && arraySizeRep()) {
            addToSyntax("fParamsTail -> ',' type 'id' arraySizeRep");
            return true;
        }
        return false;
    }


    private boolean funcDeclRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CBRA, Token.TokenType.SEMI))) {
            return false;
        }

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
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.EPSILON), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(type() && match(Token.TokenType.ID) && match(Token.TokenType.OPAR) && fParams() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI)) {
            addToSyntax("funcDecl -> type 'id' '(' fParams ')' ';' ");
            return true;
        }
        return false;
    }

    private boolean arraySizeRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
            addToSyntax("arraySizeRep -> EPSILON");
            return true;
        } else  if (arraySize() && arraySizeRep()) {
            addToSyntax("arraySizeRep -> arraySize arraySizeRep");
            return true;
        }
        return false;
    }

    private boolean arraySize() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.OSBRA) && match(Token.TokenType.INT) && match(Token.TokenType.CSBRA)) {
            addToSyntax(" arraySize -> '[' 'intNum' ']'");
            return true;
        }
        return false;
    }

    public boolean classExOpt() {
        if(!skipErrors(Arrays.asList(Token.TokenType.COLO, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.OBRA))) {
            return false;
        }
        if(match(Token.TokenType.COLO) && match(Token.TokenType.ID) && classExMoreRep()) {
            addToSyntax("classExOpt ->  ':' 'id' classExMoreRep");
            return true;
        }
        else if(peekMatch(Token.TokenType.OBRA)) {
            addToSyntax("classExOpt -> EPSILON");
            return true;
        }
        return false;
    }

    public boolean classExMoreRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.COMM, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.OBRA))) {
            return false;
        }
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
