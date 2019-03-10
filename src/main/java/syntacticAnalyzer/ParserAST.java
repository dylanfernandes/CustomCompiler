package syntacticAnalyzer;

import lexer.Token;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.ASTNodeFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserAST {

    private List<Token> tokenList;
    private int position;
    private Token lookahead;
    private String syntax;
    private boolean parseGood;
    private boolean foundError;
    private ASTNode root;

    public ParserAST() {
        position = 0;
        syntax = "";
        parseGood = false;
        foundError = false;
        root = null;
    }

    public ParserAST(List<Token> tokens) {
        new Parser();
        tokenList = tokens;
        if(tokenList.size() > 0) {
            lookahead = tokenList.get(0);
        }
    }

    public String getSyntax() {
        return syntax;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
        lookahead = tokenList.get(0);
    }

    public boolean isParseGood() {
        return parseGood;
    }

    public boolean isFoundError() {
        return foundError;
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
                if(write) {
                    addToSyntax("Syntax error at line: " + lookahead.getLineNumber());
                    foundError = true;
                    //if in follow, current production not good, can get epsilon if in first in parse method
                    while (!first.contains(lookahead.getType()) && !follow.contains(lookahead.getType())) {
                        lookahead = nextToken();
                        if (lookahead == null)
                            break;
                        //production not properly completed
                        if (first.contains(Token.TokenType.EPSILON) && follow.contains(lookahead.getType()))
                            return false;
                    }
                    if (lookahead != null)
                        return true;
                }
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

    public boolean matchAndSave( Token.TokenType expectedTokenType, Token storageToken) {
        if(lookahead != null && lookahead.getType() == expectedTokenType) {
            storageToken.setToken(lookahead);
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
        root = ASTNodeFactory.getASTNode("prog");
        ASTNode classDeclRepNode = ASTNodeFactory.getASTNode("classDeclRep");
        ASTNode funcDefRepNode = ASTNodeFactory.getASTNode("funcDefRep");
        ASTNode funcBodyNode = ASTNodeFactory.getASTNode("funcBody");
        Token main = new Token();
        Token semi = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.INTEGER, Token.TokenType.FLOAT, Token.TokenType.CLASS, Token.TokenType.ID, Token.TokenType.MAIN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if (classDeclRep(classDeclRepNode) && funcDefRep(funcDefRepNode) && matchAndSave(Token.TokenType.MAIN, main) && funcBody(funcBodyNode) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("prog -> classDeclRep funcDefRep 'main' funcBody ';'");
//            root.makeFamily(classDeclRepNode, ASTNodeFactory.getASTNode(main),funcDefRepNode, funcBodyNode, ASTNodeFactory.getASTNode(semi));
//            return true;
//        }
            return false;
    }

    public boolean classDeclRep(ASTNode currentRoot) {
        ASTNode classDeclNode = ASTNodeFactory.getASTNode("classDecl");
        ASTNode classDeclRepNode = ASTNodeFactory.getASTNode("classDeclRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.CLASS), Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.MAIN))) {
            return false;
        }

        if(peekMatch(Token.TokenType.ID) || peekMatch(Token.TokenType.FLOAT) || peekMatch(Token.TokenType.INTEGER) || peekMatch(Token.TokenType.MAIN)){
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            addToSyntax("classDeclRep -> EPSILON");
            return true;
        }else if(classDecl(classDeclNode) && classDeclRep(classDeclRepNode)) {
            currentRoot.makeFamily(classDeclNode, classDeclRepNode);
            addToSyntax("classDeclRep -> classDecl classDeclRep");
            return true;
        }
        return false;
    }



    public boolean classDecl(ASTNode classDeclNode) {
        Token classT = new Token();
        Token id = new Token();
        Token oBra = new Token();
        Token cBra = new Token();
        Token semi = new Token();

        ASTNode classExOptNode = ASTNodeFactory.getASTNode("classExOpt");
        ASTNode varOrFuncCheckNode = ASTNodeFactory.getASTNode("varOrFuncCheck");

        if(!skipErrors(Arrays.asList(Token.TokenType.CLASS), Collections.<Token.TokenType>emptyList())) {
            return false;
        }
//        if(matchAndSave(Token.TokenType.CLASS, classT) && matchAndSave(Token.TokenType.ID, id) && classExOpt(classExOptNode) && matchAndSave(Token.TokenType.OBRA, oBra) && varOrFuncCheck(varOrFuncCheckNode) && matchAndSave(Token.TokenType.CBRA, cBra) && matchAndSave(Token.TokenType.SEMI, semi)) {
//
//            addToSyntax("classDecl -> 'class' 'id' classExOpt '{' varOrFuncCheck '}' ';'");
//            classDeclNode.makeFamily(ASTNodeFactory.getASTNode(classT), ASTNodeFactory.getASTNode(id), classExOptNode, ASTNodeFactory.getASTNode(oBra), varOrFuncCheckNode, ASTNodeFactory.getASTNode(cBra), ASTNodeFactory.getASTNode(semi));
//            return true;
//        }
        return false;
    }

    private boolean funcDefRep(ASTNode currentRoot) {
        ASTNode funcDefNode = ASTNodeFactory.getASTNode("funcDef");
        ASTNode funcDefRepNode = ASTNodeFactory.getASTNode("funcDefRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.ID, Token.TokenType.INTEGER, Token.TokenType.FLOAT), Arrays.asList(Token.TokenType.MAIN))){
            return false;
        }
//        if(peekMatch(Token.TokenType.MAIN)) {
//            addToSyntax("funcDefRep -> EPSILON");
//            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
//            return true;
//        } else if(funcDef(funcDefNode) && funcDefRep(funcDefRepNode)) {
//            addToSyntax("funcDefRep -> funcDef funcDefRep");
//            currentRoot.makeFamily(funcDefNode, funcDefRepNode);
//            return true;
//        }

        return false;
    }


    private boolean funcBody(ASTNode currentRoot) {
        ASTNode varDeclStatFuncRepNode = ASTNodeFactory.getASTNode("varDeclStatFuncRep");
        Token oBra = new Token();
        Token cBra = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.OBRA), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.OBRA, oBra) && varDeclStatFuncRep(varDeclStatFuncRepNode) && matchAndSave(Token.TokenType.CBRA,cBra)) {
//            addToSyntax("funcBody -> '{' varDeclStatFuncRep '}'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(oBra), varDeclStatFuncRepNode, ASTNodeFactory.getASTNode(cBra));
//            return true;
//        }
        return false;
    }
}
