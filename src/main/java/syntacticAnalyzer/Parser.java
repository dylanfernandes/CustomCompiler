package syntacticAnalyzer;

import lexer.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Parser {

    private List<Token> tokenList;
    private int position;
    private Token lookahead;
    private String syntax;
    private boolean parseGood;
    private boolean foundError;

    public Parser() {
        position = 0;
        syntax = "";
        parseGood = false;
        foundError = false;
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
        } else if(idProd(false) && varDeclStatFuncRep()) {
            addToSyntax("varDeclStatFuncRep -> idProd varDeclStatFuncRep");
            return true;
        } else  if(statement() && varDeclStatFuncRep()) {
            addToSyntax("statement -> idProd varDeclStatFuncRep");
            return true;
        }
        return false;
    }

    private boolean varDeclNotId() {
        if(!skipErrors(Arrays.asList(Token.TokenType.FLOAT, Token.TokenType.INTEGER), Collections.<Token.TokenType>emptyList(),false)) {
            return false;
        }

        if(typeNotId() && match(Token.TokenType.ID) && varDeclNext()) {
            addToSyntax("varDeclNotId -> typeNotId 'id' varDeclNext");
            return true;
        }

        return false;
    }

    private boolean idProd(boolean write) {
        //error written by last prod
        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList(), write)) {
            return false;
        }

        if(match(Token.TokenType.ID) && idProdNext()) {
            addToSyntax("idProd -> 'id' idProdNext");
            return true;
        }
        return false;
    }

    private boolean idProdNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID, Token.TokenType.OSBRA, Token.TokenType.POIN, Token.TokenType.ASSGN, Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.OPAR) && aParams() && match(Token.TokenType.CPAR) && match(Token.TokenType.POIN) && idProd(true)) {
            addToSyntax("idProdNext -> '(' aParams ')' '.' idProd ");
            return  true;
        } else if(varDeclId()) {
            addToSyntax("idProdNext -> varDeclId");
            return  true;
        }
        else if(oldVarEndNest()) {
            addToSyntax("idProdNext -> oldVarEndNest");
            return  true;
        }
        return false;
    }

    private boolean oldVarEndNest() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.POIN, Token.TokenType.ASSGN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(indiceRep() && oldVarEndNestNext()){
            addToSyntax("oldVarEndNest -> indiceRep oldVarEndNestNext");
            return true;
        }
        return false;
    }

    private boolean oldVarEndNestNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ASSGN, Token.TokenType.POIN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.POIN) && idProd(true)) {
            addToSyntax("oldVarEndNestNext -> '.' idProd");
            return true;
        } else if (assignStatEnd()) {
            addToSyntax("oldVarEndNestNext -> assignStatEnd");
            return true;
        }
        return false;
    }

    private boolean assignStatEnd() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ASSGN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(assignOp() && expr() && match(Token.TokenType.SEMI)) {
            addToSyntax("assignStatEnd -> assignOp expr ';'");
            return true;
        }
        return false;
    }

    private boolean assignOp() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ASSGN), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.ASSGN)) {
            addToSyntax("assignOp -> '='");
            return true;
        }
        return false;
    }

    private boolean indiceRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.POIN, Token.TokenType.ASSGN))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.POIN, Token.TokenType.ASSGN))) {
            addToSyntax("indiceRep -> EPSILON");
            return true;
        }else if(indice() && indiceRep()) {
            addToSyntax("indiceRep -> indice indiceRep");
            return true;
        }
        return false;
    }

    private boolean indice() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA), Collections.<Token.TokenType>emptyList())) {
            return  false;
        }

        if(match(Token.TokenType.OSBRA) && arithExpr() && match(Token.TokenType.CSBRA)) {
            addToSyntax("indice -> '[' arithExpr ']'");
            return true;
        }
        return false;
    }

    private boolean expr() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(arithExpr() && exprNext()) {
            addToSyntax("expr -> arithExpr exprNext");
            return true;
        }
        return false;
    }

    private boolean exprNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.EQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.LESSEQ, Token.TokenType.LES, Token.TokenType.NEQ, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM))) {
            addToSyntax("exprNext -> EPSILON");
            return true;
        } else if(relOp() && arithExpr()) {
            addToSyntax("exprNext -> relOp arithExpr");
            return true;
        }
        return false;
    }

    private boolean relExpr() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

         if(arithExpr() && relOp() && arithExpr() ) {
            addToSyntax("relExpr -> arithExpr relOp arithExpr");
            return true;
        }
        return false;
    }

    private boolean relOp() {
        if(!skipErrors(Arrays.asList(Token.TokenType.NEQ, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.EQ), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.NEQ)) {
            addToSyntax("relOp -> 'eq'");
            return true;
        } else if( match(Token.TokenType.NEQ)) {
            addToSyntax("relOp -> 'neq'");
            return true;
        } else if(match(Token.TokenType.LES)) {
            addToSyntax("relOp -> 'lt'");
            return true;
        } else if(match(Token.TokenType.GRE)) {
            addToSyntax("relOp -> 'gt'");
            return true;
        } else if(match(Token.TokenType.LESSEQ)) {
            addToSyntax("relOp -> 'leq'");
            return true;
        } else if(match(Token.TokenType.GREEQ)) {
            addToSyntax("relOp -> 'geq'");
            return true;
        }
        return false;
    }

    private boolean arithExpr() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(term() && arithExprPrime()) {
            addToSyntax("arithExpr -> term arithExprPrime");
            return true;
        }
        return false;
    }

    private boolean arithExprPrime() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA))) {
            addToSyntax("arithExprPrime -> EPSILON");
            return true;
        } else if(addOp() && term() && arithExprPrime()) {
            addToSyntax("arithExprPrime -> addOp term arithExprPrime");
            return true;
        }
        return false;
    }

    private boolean addOp() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR) ,Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.ADD)) {
            addToSyntax("addOp -> '+'");
            return true;
        } else  if(match(Token.TokenType.SUB)) {
            addToSyntax("addOp -> '-'");
            return true;
        } else if(match(Token.TokenType.OR)) {
            addToSyntax("addOp -> 'or");
            return true;
        }
        return false;
    }

    private boolean term() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(factor() && termPrime()) {
            addToSyntax("term -> factor termPrime");
            return true;
        }
        return false;
    }

    private boolean termPrime() {
        if(!skipErrors(Arrays.asList(Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.AND, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GRE, Token.TokenType.GREEQ, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR))) {
            addToSyntax("termPrime -> EPSILON");
            return true;
        } else if(multOp() && factor() && termPrime()) {
            addToSyntax("termPrime -> multOp factor termPrime");
            return true;
        }
        return false;
    }

    private boolean multOp() {
        if(!skipErrors(Arrays.asList(Token.TokenType.AND, Token.TokenType.DIV, Token.TokenType.MULT), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.AND)) {
            addToSyntax("multOp -> 'and'");
            return true;
        } else if(match(Token.TokenType.DIV)) {
            addToSyntax("multOp -> '/'");
            return true;
        } else if(match(Token.TokenType.MULT)) {
            addToSyntax("multOp -> '*'");
            return true;
        }
        return false;
    }

    private boolean factor() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.ID, Token.TokenType.NOT, Token.TokenType.INT, Token.TokenType.FLO, Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.INT)) {
            addToSyntax("factor -> 'intNum'");
            return true;
        } else if(match(Token.TokenType.FLO)) {
            addToSyntax("factor -> 'floatNum'");
            return true;
        } else if(match(Token.TokenType.OPAR) && arithExpr() && match(Token.TokenType.CPAR)) {
            addToSyntax("'factor -> (' arithExpr ')'");
            return true;
        } else if(match(Token.TokenType.NOT) && factor()) {
            addToSyntax("factor -> 'not' factor");
            return true;
        } else if(sign() && factor()) {
            addToSyntax("factor -> sign factor");
            return true;
        } else  if(varOrFuncCall()) {
            addToSyntax("factor -> varOrFuncCall");
            return true;
        }
        return false;
    }

    private boolean sign() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList(), false)) {
            return false;
        }

        if(match(Token.TokenType.ADD)) {
            addToSyntax("sign -> '+'");
            return true;
        } else if(match(Token.TokenType.SUB)) {
            addToSyntax("sign -> '-'");
            return true;
        }
        return false;
    }

    private boolean varOrFuncCall() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID) ,Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.ID) && varOrFuncCallNext()) {
            addToSyntax("varOrFuncCall -> 'id' varOrFuncCallNext");
            return true;
        }
        return  false;
    }

    private boolean varOrFuncCallNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.POIN, Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LESSEQ, Token.TokenType.LES, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.AND))) {
            return false;
        }
        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LESSEQ, Token.TokenType.LES, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.AND))) {
            addToSyntax("varOrFuncCallNext -> EPSILON");
            return true;
        } else if(match(Token.TokenType.OPAR) && varFuncParams()) {
            addToSyntax("varOrFuncCallNext -> '(' varFuncParams");
            return true;
        } else if(match(Token.TokenType.POIN) && varStart()) {
            addToSyntax("varOrFuncCallNext -> '.' varStart");
            return true;
        } else if(varIndice()) {
            addToSyntax("varOrFuncCallNext -> varIndice");
            return true;
        }
        return false;
    }

    private boolean varIndice() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(indice() && varIndiceNext()) {
            addToSyntax("varIndice -> indice varIndiceNext");
            return true;
        }
        return false;
    }

    private boolean varIndiceNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.POIN, Token.TokenType.EPSILON, Token.TokenType.OSBRA), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.ADD, Token.TokenType.ASSGN))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.ADD, Token.TokenType.ASSGN))) {
            addToSyntax(" varIndiceNext -> EPSILON");
            return true;
        } else if(varIndice()) {
            addToSyntax(" varIndiceNext -> varIndice");
            return true;
        } else if(varIndiceNextNext()) {
            addToSyntax("varIndiceNext -> varIndiceNextNext");
            return true;
        }
        return false;
    }

    private boolean varIndiceNextNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.POIN, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.ADD, Token.TokenType.ASSGN))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.ADD, Token.TokenType.ASSGN))) {
            addToSyntax(" varIndiceNextNext -> EPSILON");
            return true;
        } else if(match(Token.TokenType.POIN) && varStart()) {
            addToSyntax("varIndiceNextNext -> '.' varStart");
            return true;
        }
        return false;
    }

    private boolean varStart() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList() )) {
            return false;
        }

        if(match(Token.TokenType.ID) && varNext()) {
            addToSyntax("varStart -> 'id' varNext");
            return true;
        }
        return false;
    }

    private boolean varNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.POIN, Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.ADD, Token.TokenType.ASSGN))) {
            return false;
        }

        if(peekListMatch( Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LES, Token.TokenType.LESSEQ, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.ADD, Token.TokenType.ASSGN))) {
            addToSyntax("varNext -> EPSILON");
            return true;
        } else if(match(Token.TokenType.OPAR) && varAParams()) {
            addToSyntax("varNext -> '(' varAParams");
            return true;
        } else if(match(Token.TokenType.POIN) && varStart()) {
            addToSyntax("varNext -> '.' varStart");
            return true;
        } else if(varIndice()) {
            addToSyntax("");
            return true;
        }
        return false;
    }

    private boolean varAParams() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(aParams() && match(Token.TokenType.CPAR) && match(Token.TokenType.POIN) && varStart()) {
            addToSyntax("varAParams ->  aParams ')' '.' varStart");
            return true;
        }
        return false;
    }

    private boolean varFuncParams() {
        if(!skipErrors(Arrays.asList( Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB),Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(aParams() && match(Token.TokenType.CPAR) && varFuncParamsNext()) {
            addToSyntax("varFuncParams ->  aParams ')' varFuncParamsNext");
            return true;
        }
        return false;
    }

    private boolean varFuncParamsNext() {
        if(!skipErrors(Arrays.asList( Token.TokenType.POIN,  Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LESSEQ, Token.TokenType.LES, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.AND))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM, Token.TokenType.EQ, Token.TokenType.GREEQ, Token.TokenType.GRE, Token.TokenType.LESSEQ, Token.TokenType.LES, Token.TokenType.NEQ, Token.TokenType.CSBRA, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.OR, Token.TokenType.MULT, Token.TokenType.DIV, Token.TokenType.AND))) {
            addToSyntax("varFuncParamsNext -> EPSILON");
            return true;
        } else if(match(Token.TokenType.POIN) && varOrFuncCall()) {
            addToSyntax("varFuncParamsNext -> '.' varOrFuncCall");
            return true;
        }
        return false;
    }

    private boolean varDeclId() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID),Collections.<Token.TokenType>emptyList(), false)) {
            return false;
        }

        if(match(Token.TokenType.ID) && varDeclNext()) {
            addToSyntax("varDeclId -> 'id' varDeclNext");
            return true;
        }
        return false;
    }

    private boolean varDeclNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.SEMI), Collections.<Token.TokenType>emptyList())) {
            return  false;
        }

        if(arraySizeRep() && match(Token.TokenType.SEMI)) {
            addToSyntax("varDeclNext -> arraySizeRep ';'");
            return  true;
        }
        return false;
    }

    private boolean aParams() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OPAR, Token.TokenType.FLO, Token.TokenType.INT, Token.TokenType.NOT, Token.TokenType.ID, Token.TokenType.ADD, Token.TokenType.SUB, Token.TokenType.COMM), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

         if(expr() && aParamsTailRep()) {
            addToSyntax("aParams -> expr aParamsTailRep");
            return true;
        }
        return false;
    }

    private boolean aParamsTailRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.COMM, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CPAR))) {
            return  false;
        }

        if(peekMatch(Token.TokenType.CPAR)) {
            addToSyntax("aParamsTailRep -> EPSILON");
            return true;
        } else if(aParamsTail() && aParamsTailRep()) {
            addToSyntax("aParamsTailRep -> aParamsTail aParamsTailRep");
            return true;
        }
        return false;
    }

    private boolean aParamsTail() {
        if(!skipErrors(Arrays.asList(Token.TokenType.COMM), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.COMM) && expr()) {
            addToSyntax("aParamsTail -> ',' expr");
            return true;
        }
        return false;
    }

    private boolean statement() {
        if(!skipErrors(Arrays.asList(Token.TokenType.WRITE, Token.TokenType.RETURN, Token.TokenType.READ, Token.TokenType.IF, Token.TokenType.FOR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(match(Token.TokenType.IF) && match(Token.TokenType.OPAR) && expr() && match(Token.TokenType.CPAR) && match(Token.TokenType.THEN) && statBlock() && match(Token.TokenType.ELSE) && statBlock() && match(Token.TokenType.SEMI)) {
            addToSyntax("statement -> 'if' '(' expr ')' 'then' statBlock 'else' statBlock ';'");
            return true;
        } else if(match(Token.TokenType.FOR) && match(Token.TokenType.OPAR) && type() && match(Token.TokenType.ID) && assignOp() && expr() && relExpr() && match(Token.TokenType.SEMI) && assignStat() && match(Token.TokenType.CPAR) && statBlock() &&match (Token.TokenType.SEMI)) {
            addToSyntax("statement -> 'for' '(' type 'id' assignOp expr ';' relExpr ';' assignStat ')' statBlock ';'");
            return true;
        } else if(match(Token.TokenType.READ) && match(Token.TokenType.OPAR) && varStart() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI)) {
            addToSyntax("statement -> 'read' '(' variable ')' ';'");
            return true;
        } else if(match(Token.TokenType.WRITE) && match(Token.TokenType.OPAR) && expr() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI)) {
            addToSyntax("'write' '(' expr ')' ';'");
            return  true;
        } else if(match(Token.TokenType.RETURN) && match(Token.TokenType.OPAR) && expr() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI)) {
            addToSyntax("'return' '(' expr ')' ';'");
            return true;
        }
        return false;
    }


    private boolean assignStat() {
        if(!skipErrors(Arrays.asList(Token.TokenType.ID), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(varStart() && assignOp() && expr()) {
            addToSyntax("assignStat -> varStart assignOp expr");
            return true;
        }
        return false;
    }

    private boolean statBlock() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OBRA, Token.TokenType.FOR, Token.TokenType.IF, Token.TokenType.READ, Token.TokenType.RETURN, Token.TokenType.WRITE, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.SEMI, Token.TokenType.ELSE))) {
            return false;
        }

        if(peekListMatch(Arrays.asList(Token.TokenType.SEMI, Token.TokenType.ELSE))) {
            addToSyntax("statBlock -> EPSILON");
            return true;
        } else if (match(Token.TokenType.OBRA) && statementRep() && match(Token.TokenType.CBRA)) {
            addToSyntax("statBlock -> '{' statementRep '}'");
            return true;
        } else if(statement()) {
            addToSyntax("statBlock -> statement");
            return true;
        }
        return false;
    }

    private boolean statementRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.FOR, Token.TokenType.IF, Token.TokenType.READ, Token.TokenType.RETURN, Token.TokenType.WRITE, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.CBRA))) {
            return false;
        }

        if(peekMatch(Token.TokenType.CBRA)) {
            addToSyntax("statementRep -> EPSILON");
            return true;
        } else if(statement() && statementRep()) {
            addToSyntax("statementRep -> statement statementRep");
            return true;
        }

        return false;
    }


    private boolean funcDefRep() {
        if(!skipErrors(Arrays.asList(Token.TokenType.EPSILON, Token.TokenType.ID, Token.TokenType.INTEGER, Token.TokenType.FLOAT), Arrays.asList(Token.TokenType.MAIN))){
            return false;
        }
        if(peekMatch(Token.TokenType.MAIN)) {
            addToSyntax("funcDefRep -> EPSILON");
            return true;
        } else if(funcDef() && funcDefRep()) {
            addToSyntax("funcDefRep -> funcDef funcDefRep");
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

        if(peekMatch(Token.TokenType.ID) || peekMatch(Token.TokenType.FLOAT) || peekMatch(Token.TokenType.INTEGER) || peekMatch(Token.TokenType.MAIN)){
            addToSyntax("classDeclRep -> EPSILON");
            return true;
        }else if(classDecl() && classDeclRep()) {
            addToSyntax("classDeclRep -> classDecl classDeclRep");
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
        else if(match(Token.TokenType.FLOAT)) {
            addToSyntax("typeNotId -> 'float' ");
            return true;
        }
        return false;
    }

    public boolean varCheckNext() {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.SEMI, Token.TokenType.OPAR), Collections.<Token.TokenType>emptyList())) {
            return false;
        }

        if(arraySizeRep(false) && match(Token.TokenType.SEMI) && varOrFuncCheck()) {
            addToSyntax("varCheckNext -> arraySizeRep varOrFuncCheck  ';'");
            return true;
        }else if(match(Token.TokenType.OPAR) && fParams() && match(Token.TokenType.CPAR) && match(Token.TokenType.SEMI) && funcDeclRep()) {
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

        if (peekMatch(Token.TokenType.CBRA) || peekMatch(Token.TokenType.SEMI)) {
            addToSyntax("funcDeclRep -> EPSILON");
            return true;
        }else if (funcDecl() && funcDeclRep()) {
            addToSyntax("funcDeclRep -> funcDecl funcDeclRep");
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
        return arraySizeRep(true);
    }

    private boolean arraySizeRep(boolean write) {
        if(!skipErrors(Arrays.asList(Token.TokenType.OSBRA, Token.TokenType.EPSILON), Arrays.asList(Token.TokenType.ID, Token.TokenType.FLOAT, Token.TokenType.INTEGER, Token.TokenType.SEMI, Token.TokenType.CPAR, Token.TokenType.COMM), write)) {
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
