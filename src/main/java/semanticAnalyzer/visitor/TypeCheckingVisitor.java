package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

public class TypeCheckingVisitor extends Visitor {
    private boolean hasError;
    private String errorOutput;
    private SymbolTable globalSymbolTable;

    public TypeCheckingVisitor() {
        hasError = false;
        errorOutput = "";
        globalSymbolTable = null;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public void visit(StringASTNode astNode) {

    }

    public void visit(TokenASTNode astNode) {

    }

    public void visit(ProgASTNode astNode) {
        ASTNode classDeclRep = astNode.getFirstChild();
        ASTNode funcDefRep = classDeclRep.getRightSibling();
        VarDeclStatFuncRepASTNode varDeclStatFuncRep = (VarDeclStatFuncRepASTNode) funcDefRep.getRightSibling().getRightSibling().getFirstChild().getRightSibling();

        ClassDeclASTNode classNode;
        FuncDefASTNode functionNode;

        globalSymbolTable = astNode.getGlobalTable();

        while(classDeclRep.getValue().equals("classDeclRep") ) {
            classDeclRep = classDeclRep.getFirstChild();
            if(classDeclRep.getClass() == ClassDeclASTNode.class) {
                classNode = (ClassDeclASTNode) classDeclRep;
                classNode.accept(this);
                classDeclRep = classNode.getRightSibling();
            }
        }

        while(funcDefRep.getValue().equals("funcDefRep") ) {
            funcDefRep = funcDefRep.getFirstChild();
            if(funcDefRep.getClass() == FuncDefASTNode.class) {
                functionNode = (FuncDefASTNode) funcDefRep;
                functionNode.accept(this);
                funcDefRep = functionNode.getRightSibling();
            }
        }

    }

    public void visit(ClassDeclASTNode astNode) {
        ASTNode child = astNode.getFirstChild();
        ASTNode temp = new StringASTNode("");
        VarOrFuncCheckASTNode varOrFuncCheck;

        if(child.getValue().equals("class")) {
            child = child.getRightSibling();
        }

        varOrFuncCheck = (VarOrFuncCheckASTNode) child.getRightSibling().getRightSibling().getRightSibling();

        //verify inheritance of class
        if(child.getRightSibling().getValue().equals("classExOpt")) {
            child = child.getRightSibling();
            if(child.getFirstChild().getValue().equals(":")) {
                verifyInheritance(getInheritanceEntry(child, temp));
                child = temp;


                //verify several inheritance
                child = child.getRightSibling();
                while (child.getFirstChild().getValue().equals(",")) {
                    verifyInheritance(getInheritanceEntry(child, temp));
                    child = temp;
                    child = child.getRightSibling();
                }
            }
        }

        varOrFuncCheck.accept(this);
    }

    private void verifyInheritance(SymbolTableEntry inheritance){
        if (globalSymbolTable.find(inheritance.getName(), EntryKind.CLASS) == -1){
            hasError = true;
            errorOutput += "Class "+ inheritance.getName() + "is not defined \n";
        }

    }

    public void visit(FuncDefASTNode astNode) {

    }

    public void visit(FParamsASTNode astNode) {

    }

    public void visit(VarOrFuncCheckASTNode astNode) {

    }

    public void visit(VarDeclStatFuncRepASTNode astNode) {

    }
}
