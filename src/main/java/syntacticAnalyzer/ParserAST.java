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


//    public boolean match( Token.TokenType expectedTokenType) {
//        if(lookahead != null && lookahead.getType() == expectedTokenType) {
//            lookahead = nextToken();
//            return true;
//        }
//        return  false;
//    }

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



    public boolean classDecl(ASTNode currentRoot) {
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
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(classT), ASTNodeFactory.getASTNode(id), classExOptNode, ASTNodeFactory.getASTNode(oBra), varOrFuncCheckNode, ASTNodeFactory.getASTNode(cBra), ASTNodeFactory.getASTNode(semi));
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

    public boolean varOrFuncCheck(ASTNode currentRoot) {
        Token id = new Token();
        ASTNode typeNode = ASTNodeFactory.getASTNode("type");
        ASTNode varCheckNextNode = ASTNodeFactory.getASTNode("varCheckNext");

        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER), Arrays.asList(Token.TokenType.CBRA))) {
            return false;
        }
//        if(peekMatch(Token.TokenType.CBRA)) {
//            addToSyntax("varOrFuncCheck -> EPSILON");
//            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
//            return true;
//        } else if(type(typeNode) && matchAndSave(Token.TokenType.ID, id) && varCheckNext(varCheckNextNode)) {
//            addToSyntax("varOrFuncCheck -> type 'id' varCheckNext");
//            currentRoot.makeFamily(typeNode, ASTNodeFactory.getASTNode(id), varCheckNextNode);
//            return true;
//        }
        return false;
    }

    public boolean varCheckNext(ASTNode currentRoot) {
        Token opar = new Token();
        Token cpar = new Token();
        Token semi = new Token();

        ASTNode arraySizeRepNode = ASTNodeFactory.getASTNode("arraySizeRep");
        ASTNode varOrFuncCheckNode = ASTNodeFactory.getASTNode("varOrFuncCheck");

        ASTNode fParamsNode = ASTNodeFactory.getASTNode("fParams");
        ASTNode funcDeclRepNode = ASTNodeFactory.getASTNode("funcDeclRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.SEMI, Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(arraySizeRep(false, arraySizeRepNode) && matchAndSave(Token.TokenType.SEMI, semi) && varOrFuncCheck(varOrFuncCheckNode)) {
//            addToSyntax("varCheckNext -> arraySizeRep varOrFuncCheck  ';'");
//            currentRoot.makeFamily(arraySizeRepNode, ASTNodeFactory.getASTNode(semi), varOrFuncCheckNode);
//            return true;
//        }else if(matchAndSave(Token.TokenType.OPAR, opar) && fParams(fParamsNode) && matchAndSave(Token.TokenType.CPAR, cpar) && matchAndSave(Token.TokenType.SEMI, semi) && funcDeclRep(funcDeclRepNode)) {
//            addToSyntax("varCheckNext -> '(' fParams ')' ';' funcDeclRep ");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(opar), fParamsNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(semi), funcDeclRepNode);
//            return true;
//        }

        return false;
    }

    public boolean classExOpt(ASTNode currentRoot) {
        Token colo = new Token();
        Token id = new Token();

        ASTNode classExMoreRepNode = ASTNodeFactory.getASTNode("classExMoreRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.COLO, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.OBRA))) {
            return false;
        }

        if(peekMatch(Token.TokenType.OBRA)) {
            addToSyntax("classExOpt -> EPSILON");
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            return true;
        } else if(matchAndSave(Token.TokenType.COLO, colo) && matchAndSave(Token.TokenType.ID, id) && classExMoreRep(classExMoreRepNode)) {
            addToSyntax("classExOpt ->  ':' 'id' classExMoreRep");
            currentRoot.makeFamily(ASTNodeFactory.getASTNode(colo), ASTNodeFactory.getASTNode(id), classExMoreRepNode);
            return true;
        }
        return false;
    }

    public boolean classExMoreRep(ASTNode currentRoot) {
        Token comm = new Token();
        Token id = new Token();

        ASTNode classExMoreRepNode = ASTNodeFactory.getASTNode("classExMoreRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.COMM, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.OBRA))) {
            return false;
        }
        if(peekMatch(Token.TokenType.OBRA)) {
            addToSyntax("classExMoreRep -> EPSILON");
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            return true;
        } else if(matchAndSave(Token.TokenType.COMM, comm) && matchAndSave(Token.TokenType.ID, id) && classExMoreRep(classExMoreRepNode)) {
            addToSyntax("classExMoreRep -> ',' 'id' classExMoreRep");
            currentRoot.makeFamily(ASTNodeFactory.getASTNode(comm), ASTNodeFactory.getASTNode(id), classExMoreRepNode);
            return true;
        }
        return false;
    }

    private boolean funcDeclRep(ASTNode currentRoot) {
        ASTNode funcDeclNode = ASTNodeFactory.getASTNode("funcDecl");
        ASTNode funcDeclRepNode = ASTNodeFactory.getASTNode("funcDeclRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CBRA))) {
            return false;
        }

        if (peekMatch(Token.TokenType.CBRA)) {
            addToSyntax("funcDeclRep -> EPSILON");
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            return true;
        }else if (funcDecl(funcDeclNode) && funcDeclRep(funcDeclRepNode)) {
            addToSyntax("funcDeclRep -> funcDecl funcDeclRep");
            currentRoot.makeFamily(funcDeclNode, funcDeclRepNode);
            return true;
        }

        return false;
    }

    private boolean funcDecl(ASTNode currentRoot) {
        Token id = new Token();
        Token opar = new Token();
        Token cpar = new Token();
        Token semi = new Token();

        ASTNode typeNode = ASTNodeFactory.getASTNode("type");
        ASTNode fParamsNode = ASTNodeFactory.getASTNode("fParams");

        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.EPSILON), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(type(typeNode) && matchAndSave(Token.TokenType.ID, id) && matchAndSave(Token.TokenType.OPAR, opar) && fParams(fParamsNode) && matchAndSave(Token.TokenType.CPAR, cpar) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("funcDecl -> type 'id' '(' fParams ')' ';' ");
//            currentRoot.makeFamily(typeNode, ASTNodeFactory.getASTNode(id), ASTNodeFactory.getASTNode(opar), fParamsNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(semi));
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

    private boolean funcHead(ASTNode currentRoot) {
        ASTNode typeNode = ASTNodeFactory.getASTNode("type");
        ASTNode funcHeadChoiceNode = ASTNodeFactory.getASTNode("funcHeadChoice");

        Token id = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(type(typeNode) && matchAndSave(Token.TokenType.ID, id) && funcHeadChoice(funcHeadChoiceNode)) {
//            addToSyntax("funcHead -> type 'id' funcHeadChoice");
//            currentRoot.makeFamily(typeNode, ASTNodeFactory.getASTNode(id),funcHeadChoiceNode);
//            return true;
//        }
        return false;
    }

    private boolean funcHeadChoice(ASTNode currentRoot) {
        ASTNode fParamsNode = ASTNodeFactory.getASTNode("fParams");

        Token dcolo = new Token();
        Token id = new Token();
        Token opar = new Token();
        Token cpar = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.DCOLO), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(match(Token.TokenType.DCOLO) && matchAndSave(Token.TokenType.ID, id) && matchAndSave(Token.TokenType.OPAR, opar) && fParams(fParamsNode) && matchAndSave(Token.TokenType.CPAR, cpar)) {
//            addToSyntax("funcHeadChoice -> 'sr' 'id' '(' fParams ')'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(dcolo), ASTNodeFactory.getASTNode(id), ASTNodeFactory.getASTNode(opar), fParamsNode, ASTNodeFactory.getASTNode(cpar));
//            return true;
//        } else if (matchAndSave(Token.TokenType.OPAR, opar) && fParams(fParamsNode) && matchAndSave(Token.TokenType.CPAR, cpar)) {
//            addToSyntax("funcHeadChoice -> '(' fParams ')'");
//            currentRoot.makeFamily( ASTNodeFactory.getASTNode(opar), fParamsNode, ASTNodeFactory.getASTNode(cpar));
//            return true;
//        }

        return false;
    }

    private boolean funcDef(ASTNode currentRoot) {
        ASTNode funcHeadNode = ASTNodeFactory.getASTNode("funcHead");
        ASTNode funcBodyNode = ASTNodeFactory.getASTNode("funcBody");

        Token semi = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(funcHead(funcHeadNode) && funcBody(funcBodyNode) && matchAndSave(Token.TokenType.SEMI, semi)) {
            addToSyntax("funcDef -> funcHead funcBody ';'");
            currentRoot.makeFamily(funcHeadNode, funcBodyNode, ASTNodeFactory.getASTNode(semi));
            return true;
        }
        return false;
    }

    private boolean varDeclStatFuncRep(ASTNode currentRoot) {
        ASTNode varDeclNotIdNode = ASTNodeFactory.getASTNode("varDeclNotId");
        ASTNode varDeclStatFuncRepNode = ASTNodeFactory.getASTNode("varDeclStatFuncRep");

        ASTNode idProdNode = ASTNodeFactory.getASTNode("idProd");
        ASTNode statementNoIdNode = ASTNodeFactory.getASTNode("statement");

        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.FOR, Token.TokenType.IF, Token.TokenType.READ, Token.TokenType.RETURN, Token.TokenType.WRITE, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CBRA))) {
            return false;
        }

//        if(peekMatch(Token.TokenType.CBRA)) {
//            addToSyntax("varDeclStatFuncRep -> EPSILON");
//            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
//            return  true;
//        } else if(varDeclNotId(false, varDeclNotIdNode) && varDeclStatFuncRep(varDeclStatFuncRepNode)) {
//            addToSyntax("varDeclStatFuncRep -> varDeclNotId varDeclStatFuncRep");
//            currentRoot.makeFamily(varDeclNotIdNode, varDeclStatFuncRepNode);
//            return true;
//        } else if(idProd(false, idProdNode) && varDeclStatFuncRep(varDeclStatFuncRepNode)) {
//            addToSyntax("varDeclStatFuncRep -> idProd varDeclStatFuncRep");
//            currentRoot.makeFamily(idProdNode, varDeclStatFuncRepNode);
//            return true;
//        } else  if(statementNoId(statementNoIdNode) && varDeclStatFuncRep(varDeclStatFuncRepNode)) {
//            addToSyntax("varDeclStatFuncRep -> statementNoId varDeclStatFuncRep");
//            currentRoot.makeFamily(statementNoIdNode, varDeclStatFuncRepNode);
//            return true;
//        }
        return false;
    }

    private boolean idProd(boolean write, ASTNode currentRoot) {
        ASTNode idProdNextNode = ASTNodeFactory.getASTNode("idProdNext");
        Token id = new Token();

        //error written by last prod
        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList(), write)) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.ID,id) && idProdNext(idProdNextNode)) {
//            addToSyntax("idProd -> 'id' idProdNext");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(id), idProdNextNode);
//            return true;
//        }
        return false;
    }

    private boolean idProdNext(ASTNode currentRoot) {
        ASTNode aParamsNode = ASTNodeFactory.getASTNode("aParams");
        ASTNode idProdNode = ASTNodeFactory.getASTNode("idProd");
        ASTNode varDeclIdNode = ASTNodeFactory.getASTNode("varDeclId");
        ASTNode oldVarEndNestNode = ASTNodeFactory.getASTNode("oldVarEndNest");

        Token opar = new Token();
        Token cpar = new Token();
        Token poin = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.OSBRA, Token.TokenType.POIN, Token.TokenType.ASSGN, Token.TokenType.OPAR, Token.TokenType.SEMI), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.OPAR, opar) && aParams(aParamsNode) && matchAndSave(Token.TokenType.CPAR, cpar) && matchAndSave(Token.TokenType.POIN, poin) && idProd(true, idProdNode)) {
//            addToSyntax("idProdNext -> '(' aParams ')' '.' idProd ");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(opar), aParamsNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(poin), idProdNode);
//            return  true;
//        } else if(varDeclId(false, varDeclIdNode)) {
//            addToSyntax("idProdNext -> varDeclId");
//            currentRoot.makeFamily(varDeclIdNode);
//            return  true;
//        }
//        else if(oldVarEndNest(oldVarEndNestNode)) {
//            addToSyntax("idProdNext -> oldVarEndNest");
//            currentRoot.makeFamily(oldVarEndNestNode);
//            return  true;
//        }

        return false;
    }

    private boolean oldVarEndNest(ASTNode currentRoot) {
        ASTNode indiceRepNode = ASTNodeFactory.getASTNode("indiceRep");
        ASTNode oldVarEndNestNextNode = ASTNodeFactory.getASTNode("oldVarEndNestNext");

        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.POIN, Token.TokenType.ASSGN, Token.TokenType.SEMI), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(indiceRep(indiceRepNode) && oldVarEndNestNext(oldVarEndNestNextNode)){
//            addToSyntax("oldVarEndNest -> indiceRep oldVarEndNestNext");
//            currentRoot.makeFamily(indiceRepNode, oldVarEndNestNextNode);
//            return true;
//        }
        return false;
    }

    private boolean oldVarEndNestNext(ASTNode currentRoot) {
        ASTNode idProdNode = ASTNodeFactory.getASTNode("idProd");
        ASTNode assignStatEndNode = ASTNodeFactory.getASTNode("assignStatEnd");

        Token poin = new Token();
        Token semi = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ASSGN, Token.TokenType.POIN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("oldVarEndNestNext -> ';'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(semi));
//            return true;
//        } else if(matchAndSave(Token.TokenType.POIN, poin) && idProd(true, idProdNode)) {
//            addToSyntax("oldVarEndNestNext -> '.' idProd");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(poin), idProdNode);
//            return true;
//        } else if (assignStatEnd(assignStatEndNode)) {
//            addToSyntax("oldVarEndNestNext -> assignStatEnd");
//            return true;
//        }

        return false;
    }

    private boolean varDeclId(ASTNode currentRoot) {
        ASTNode varDeclNextNode = ASTNodeFactory.getASTNode("varDeclNext");

        Token id = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ID),Collections.<Token.TokenType>emptyList(), false)) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.ID, id) && varDeclNext(varDeclNextNode)) {
//            addToSyntax("varDeclId -> 'id' varDeclNext");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(id), varDeclNextNode);
//            return true;
//        }

        return false;
    }

    private boolean assignStatEnd(ASTNode currentRoot) {
        ASTNode assignOpNode = ASTNodeFactory.getASTNode("assignOp");
        ASTNode exprNode = ASTNodeFactory.getASTNode("expr");

        Token semi = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ASSGN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(assignOp(assignOpNode) && expr(exprNode) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("assignStatEnd -> assignOp expr ';'");
//            currentRoot.makeFamily(assignOpNode, exprNode, ASTNodeFactory.getASTNode(semi));
//            return true;
//        }

        return false;
    }

    private boolean varDeclNotId(ASTNode currentRoot) {
        ASTNode typeNotIdNode = ASTNodeFactory.getASTNode("typeNotId");
        ASTNode varDeclNextNode = ASTNodeFactory.getASTNode("varDeclNext");

        Token id = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList(),false)) {
            return false;
        }

//        if(typeNotId(typeNotIdNode) && matchAndSave(Token.TokenType.ID, id) && varDeclNext(varDeclNextNode)) {
//            addToSyntax("varDeclNotId -> typeNotId 'id' varDeclNext");
//            currentRoot.makeFamily(typeNotIdNode, ASTNodeFactory.getASTNode(id), varDeclNextNode);
//            return true;
//        }

        return false;
    }

    private boolean varDeclNext(ASTNode currentRoot) {
        ASTNode arraySizeRepNode = ASTNodeFactory.getASTNode("arraySizeRep");

        Token semi = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.SEMI), Collections.<Token.TokenType>emptyList())) {
            return  false;
        }

//        if(arraySizeRep(arraySizeRepNode) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("varDeclNext -> arraySizeRep ';'");
//            currentRoot.makeFamily(arraySizeRepNode, ASTNodeFactory.getASTNode(semi));
//            return  true;
//        }
        return false;
    }

    private boolean arraySizeRep(ASTNode currentRoot) {
        return arraySizeRep(true, currentRoot);
    }

    private boolean arraySizeRep(boolean write, ASTNode currentRoot) {
        ASTNode arraySizeRepNode = ASTNodeFactory.getASTNode("arraySizeRep");
        ASTNode arraySizeNode = ASTNodeFactory.getASTNode("arraySize");

        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM), write)) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
            addToSyntax("arraySizeRep -> EPSILON");
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            return true;
        } else  if (arraySize(arraySizeNode) && arraySizeRep(arraySizeRepNode)) {
            addToSyntax("arraySizeRep -> arraySize arraySizeRep");
            currentRoot.makeFamily(arraySizeNode, arraySizeRepNode);
            return true;
        }
        return false;
    }

    private boolean statement(ASTNode currentRoot) {
        ASTNode statementNoIdNode = ASTNodeFactory.getASTNode("statementNoId");
        ASTNode statementIdNode = ASTNodeFactory.getASTNode("statementId");

        if(!skipErrors(Arrays.asList(Token.TokenType.WRITE, Token.TokenType.RETURN, Token.TokenType.READ, Token.TokenType.IF, Token.TokenType.FOR, Token.TokenType.ID), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(statementNoId(false, statementNoIdNode)) {
//            addToSyntax("statement -> statementNoId");
//            currentRoot.makeFamily(statementNoIdNode);
//            return true;
//
//        } else if (statementId(statementIdNode)) {
//            addToSyntax("statement -> statementIdNode");
//            currentRoot.makeFamily(statementIdNode);
//            return true;
//        }

        return false;
    }

    private boolean statementId(ASTNode currentRoot) {
        ASTNode assignStatNode = ASTNodeFactory.getASTNode("assignStat");

        Token semi = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(assignStat(assignStatNode) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("statementId -> assignStat");
//            currentRoot.makeFamily(assignStatNode, ASTNodeFactory.getASTNode(semi));
//            return true;
//        }

        return false;
    }

    private boolean statementNoId(ASTNode currentRoot) {

        ASTNode ifRestNode = ASTNodeFactory.getASTNode("ifRest");
        ASTNode forRestNode = ASTNodeFactory.getASTNode("forRest");
        ASTNode varStartNode = ASTNodeFactory.getASTNode("varStart");
        ASTNode exprNode = ASTNodeFactory.getASTNode("expr");

        Token opar = new Token();
        Token cpar = new Token();
        Token semi = new Token();

        Token reservedWord = new Token();


        if(!skipErrors(Arrays.asList(Token.TokenType.WRITE, Token.TokenType.RETURN, Token.TokenType.READ, Token.TokenType.IF, Token.TokenType.FOR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.IF, reservedWord) && ifRest(ifRestNode)) {
//            addToSyntax("statementNoId -> 'if' ifRest");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(reservedWord), ifRestNode);
//            return true;
//        } else if(matchAndSave(Token.TokenType.FOR, reservedWord) && forRest(forRestNode)) {
//            addToSyntax("statementNoId -> 'for' forRest");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(reservedWord), forRestNode);
//            return true;
//        } else if(matchAndSave(Token.TokenType.READ, reservedWord) && matchAndSave(Token.TokenType.OPAR, opar) && varStart(varStartNode) && matchAndSave(Token.TokenType.CPAR, cpar) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("statementNoId -> 'read' '(' variable ')' ';'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(reservedWord), ASTNodeFactory.getASTNode(opar),varStartNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(semi));
//            return true;
//        } else if(matchAndSave(Token.TokenType.WRITE, reservedWord) && matchAndSave(Token.TokenType.OPAR, opar) && expr(exprNode) && matchAndSave(Token.TokenType.CPAR, cpar) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("statementNoId -> 'write' '(' expr ')' ';'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(reservedWord), ASTNodeFactory.getASTNode(opar),exprNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(semi));
//            return  true;
//        } else if(matchAndSave(Token.TokenType.RETURN, reservedWord) && matchAndSave(Token.TokenType.OPAR, opar) && expr(exprNode) && matchAndSave(Token.TokenType.CPAR, cpar) && matchAndSave(Token.TokenType.SEMI, semi)) {
//            addToSyntax("statementNoId -> 'return' '(' expr ')' ';'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(reservedWord), ASTNodeFactory.getASTNode(opar),exprNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(semi));
//            return true;
//        }
        return false;
    }

    private boolean ifRest(ASTNode currentRoot) {
        ASTNode ifStatBlockNode = ASTNodeFactory.getASTNode("statBlock");
        ASTNode elseStatBlockNode = ASTNodeFactory.getASTNode("statBlock");
        ASTNode exprNode = ASTNodeFactory.getASTNode("expr");

        Token opar = new Token();
        Token cpar = new Token();
        Token semi = new Token();

        Token thenToken = new Token();
        Token elseToken = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.OPAR, opar) && expr(exprNode) && matchAndSave(Token.TokenType.CPAR, cpar) &&matchAndSave(Token.TokenType.THEN, thenToken) && statBlock(ifStatBlockNode) && matchAndSave(Token.TokenType.ELSE, elseToken) && statBlock(elseStatBlockNode) && matchAndSave(Token.TokenType.SEMI, semi)){
//            addToSyntax("ifRest -> '(' expr ')' 'then' statBlock 'else' statBlock ';'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(opar), exprNode, ASTNodeFactory.getASTNode(cpar), ASTNodeFactory.getASTNode(thenToken), ifStatBlockNode, ASTNodeFactory.getASTNode(elseToken), elseStatBlockNode,ASTNodeFactory.getASTNode(semi));
//            return true;
//        }
        return false;
    }

    private Boolean forRest(ASTNode currentRoot) {
        ASTNode typeNode = ASTNodeFactory.getASTNode("type");
        ASTNode assignOpNode = ASTNodeFactory.getASTNode("assignOp");
        ASTNode exprNode = ASTNodeFactory.getASTNode("expr");
        ASTNode relExprNode = ASTNodeFactory.getASTNode("relExpr");
        ASTNode assignStatNode = ASTNodeFactory.getASTNode("assignStat");
        ASTNode statBlockNode = ASTNodeFactory.getASTNode("statBlock");

        Token opar = new Token();
        Token cpar = new Token();
        Token semiVar = new Token();
        Token semiCond = new Token();
        Token semiEnd = new Token();
        Token id = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(matchAndSave(Token.TokenType.OPAR, opar) && type(typeNode) && matchAndSave(Token.TokenType.ID, id) && assignOp(assignOpNode) && expr(exprNode) && matchAndSave(Token.TokenType.SEMI, semiVar) && relExpr(relExprNode) && matchAndSave(Token.TokenType.SEMI, semiCond) && assignStat(assignStatNode) && matchAndSave(Token.TokenType.OPAR, opar) && statBlock(statBlockNode) && matchAndSave(Token.TokenType.SEMI, semiEnd)){
//            addToSyntax("forRest -> '(' type 'id' assignOp expr ';' relExpr ';' assignStat ')' statBlock ';'");
//            currentRoot.makeFamily(ASTNodeFactory.getASTNode(opar), typeNode, ASTNodeFactory.getASTNode(id), assignOpNode, exprNode, ASTNodeFactory.getASTNode(semiVar), relExprNode, ASTNodeFactory.getASTNode(semiCond), assignStatNode, ASTNodeFactory.getASTNode(opar), statBlockNode,ASTNodeFactory.getASTNode(semiEnd));
//            return true;
//        }
        return false;
    }

    private boolean statementRep(ASTNode currentRoot) {
        ASTNode statementNode = ASTNodeFactory.getASTNode("statement");
        ASTNode statementRepNode = ASTNodeFactory.getASTNode("statementRep");

        if(!skipErrors(Arrays.asList(Token.TokenType.FOR, Token.TokenType.IF, Token.TokenType.READ, Token.TokenType.RETURN, Token.TokenType.WRITE, Token.TokenType.ID,Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CBRA))) {
            return false;
        }

        if(peekMatch(Token.TokenType.CBRA)) {
            addToSyntax("statementRep -> EPSILON");
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            return true;
        } else if(statement(statementNode) && statementRep(statementRepNode)) {
            addToSyntax("statementRep -> statement statementRep");
            currentRoot.makeFamily(statementNode, statementRepNode);
            return true;
        }

        return false;
    }

    private boolean arraySize(ASTNode currentRoot) {
        Token osbra = new Token();
        Token intToken = new Token();
        Token csbra = new Token();
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(matchAndSave(Token.TokenType.OSBRA, osbra) && matchAndSave(Token.TokenType.INT, intToken) && matchAndSave(Token.TokenType.CSBRA, csbra)) {
            addToSyntax(" arraySize -> '[' 'intNum' ']'");
            currentRoot.makeFamily(ASTNodeFactory.getASTNode(osbra), ASTNodeFactory.getASTNode(intToken), ASTNodeFactory.getASTNode(csbra));
            return true;
        }
        return false;
    }

    private boolean assignStat(ASTNode currentRoot) {
        ASTNode varStartNode = ASTNodeFactory.getASTNode("varStart");
        ASTNode assignOpNode = ASTNodeFactory.getASTNode("assignOp");
        ASTNode exprNode = ASTNodeFactory.getASTNode("expr");

        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(varStart(varStartNode) && assignOp(assignOpNode) && expr(exprNode)) {
//            addToSyntax("assignStat -> varStart assignOp expr");
//            currentRoot.makeFamily(varStartNode, assignOpNode, exprNode);
//            return true;
//        }
        return false;
    }

    private boolean statBlock(ASTNode currentRoot) {
        ASTNode statementRepNode = ASTNodeFactory.getASTNode("statementRep");
        ASTNode statementNode = ASTNodeFactory.getASTNode("statement");

        Token obra = new Token();
        Token cbra = new Token();

        if(!skipErrors(Arrays.asList(Token.TokenType.OBRA, Token.TokenType.FOR, Token.TokenType.IF, Token.TokenType.READ, Token.TokenType.RETURN, Token.TokenType.WRITE, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.ELSE))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.ELSE))) {
            addToSyntax("statBlock -> EPSILON");
            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
            return true;
        } else if (matchAndSave(Token.TokenType.OBRA, obra) && statementRep(statementRepNode) && matchAndSave(Token.TokenType.CBRA,cbra)) {
            addToSyntax("statBlock -> '{' statementRep '}'");
            currentRoot.makeFamily(ASTNodeFactory.getASTNode(obra), statementRepNode, ASTNodeFactory.getASTNode(cbra));
            return true;
        } else if(statement(statementNode)) {
            addToSyntax("statBlock -> statement");
            currentRoot.makeFamily(statementNode);
            return true;
        }
        return false;
    }

    private boolean expr(ASTNode currentRoot) {
        ASTNode arithExprNode = ASTNodeFactory.getASTNode("arithExpr");
        ASTNode exprNextNode = ASTNodeFactory.getASTNode("exprNext");

        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

//        if(arithExpr(arithExprNode) && exprNext(exprNextNode)) {
//            addToSyntax("expr -> arithExpr exprNext");
//            currentRoot.makeFamily(arithExprNode, exprNextNode);
//            return true;
//        }
        return false;
    }

    private boolean exprNext(ASTNode currentRoot) {
        ASTNode relOpNode = ASTNodeFactory.getASTNode("relOp");
        ASTNode arithExprNode = ASTNodeFactory.getASTNode("arithExpr");

        if(!skipErrors(Arrays.asList( Token.TokenType.LESSEQ, Token.TokenType.LES, Token.TokenType.NEQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.EQ,Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
            return false;
        }

//        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
//            addToSyntax("exprNext -> EPSILON");
//            currentRoot.adoptChildren(ASTNodeFactory.getASTNode("EPSILON"));
//            return true;
//        } else if(relOp(relOpNode) && arithExpr(arithExprNode)) {
//            addToSyntax("exprNext -> relOp arithExpr");
//            return true;
//        }
        return false;
    }
}
