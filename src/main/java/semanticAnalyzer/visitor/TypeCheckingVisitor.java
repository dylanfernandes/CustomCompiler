package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.EntryType;
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
                verifyClass(getInheritanceEntry(child, temp).getName());
                child = temp;


                //verify several inheritance
                child = child.getRightSibling();
                while (child.getFirstChild().getValue().equals(",")) {
                    verifyClass(getInheritanceEntry(child, temp).getName());
                    child = temp;
                    child = child.getRightSibling();
                }
            }
        }

        varOrFuncCheck.accept(this);
    }

    private void verifyClass(String classCheck){
        if (globalSymbolTable.find(classCheck, EntryKind.CLASS) == -1){
            hasError = true;
            errorOutput += "Class "+ classCheck + "is not defined \n";
        }

    }

    public void visit(FuncDefASTNode astNode) {

    }

    public void visit(FParamsASTNode astNode) {

    }

    public void visit(VarOrFuncCheckASTNode astNode) {
        String elementTypeStr;
        EntryType elementType;
        FParamsASTNode fParamsASTNode;

        ASTNode type;
        ASTNode id;
        ASTNode array;
        ASTNode head;
        ASTNode varCheckNext;

        head = astNode;

        while(head.getValue().equals("varOrFuncCheck") && !head.getFirstChild().getValue().equals("EPSILON")) {
            type = head.getFirstChild();
            id = type.getRightSibling();
            varCheckNext = id.getRightSibling();

            elementTypeStr = getType(type);

            if (!(elementTypeStr.equals("integer") || elementTypeStr.equals("float"))) {
                verifyClass(elementTypeStr);
            }

            //element is variable
            if (varCheckNext.getFirstChild().getValue().equals("arraySizeRep")) {
                array = varCheckNext.getFirstChild();
                head = varCheckNext.getFirstChild().getRightSibling().getRightSibling();

                elementType = getArray(array, elementTypeStr);

            }
            else if(varCheckNext.getFirstChild().getValue().equals("(")) {
                //Element is a function declaration
                head = varCheckNext.getFirstChild();
                fParamsASTNode = (FParamsASTNode) head.getRightSibling();

                //point to function delaration repitition
                head = varCheckNext.getFirstChild().getRightSibling().getRightSibling().getRightSibling().getRightSibling();
            }
        }

        if(head != null && head.getValue().equals("funcDeclRep")) {
            while(head.getValue().equals("funcDeclRep") && !head.getFirstChild().getValue().equals("EPSILON")) {
                type = head.getFirstChild().getFirstChild();
                elementTypeStr = getType(type);
                id = type.getRightSibling();
                fParamsASTNode = (FParamsASTNode) id.getRightSibling().getRightSibling();

                head = head.getFirstChild().getRightSibling();
            }
        }

    }

    public void visit(VarDeclStatFuncRepASTNode astNode) {

    }
}
