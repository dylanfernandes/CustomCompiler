package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.*;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

import java.util.List;

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
        List<SymbolTableEntry> symbolTableEntries;
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
        symbolTableEntries = varOrFuncCheck.getEntries();
        //check if declared functions are defined
        for(int i = 0; i < symbolTableEntries.size(); i++) {
            if(symbolTableEntries.get(i).getEntryKind() == EntryKind.FUNCTION) {
                verifyClassFunction(astNode.getEntry().getName(), symbolTableEntries.get(i).getName());
            }
        }
    }

    private int verifyClass(String classCheck){
        int status = globalSymbolTable.find(classCheck, EntryKind.CLASS);

        if (classCheck.equals("integer") || classCheck.equals("float")) {
            //base types
            return -2;
        }
        if (status == -1){
            hasError = true;
            errorOutput += "Class "+ classCheck + "is not defined \n";
        }
        return status;
    }

    private int verifyClassFunction(String classCheck, String functioName){
        SymbolTable classTable;
        SymbolTable funcTable;
        int status = -1;


        if (verifyClass(classCheck) != -1){
            classTable = globalSymbolTable.search(classCheck, EntryKind.CLASS).getLink();
            status = classTable.find(functioName, EntryKind.FUNCTION);
            if(status == -1) {
                hasError = true;
                errorOutput += "Class function "+ functioName + "is not declared \n";
            } else {
                funcTable = classTable.search(functioName, EntryKind.FUNCTION).getLink();
                status = funcTable.find(classCheck, EntryKind.INHERIT);
                if(status == -1){
                    hasError = true;
                    errorOutput += "Class function "+ functioName + "is not defined \n";
                }
            }
        }
        return status;
    }

    public void visit(FuncDefASTNode astNode) {
        String typeStr;

        ASTNode head = astNode.getFirstChild();
        VarDeclStatFuncRepASTNode varDeclStatFuncRepASTNode = (VarDeclStatFuncRepASTNode) head.getRightSibling().getFirstChild().getRightSibling();
        ASTNode type = head.getFirstChild();
        ASTNode id = type.getRightSibling();
        ASTNode headerChoice = type.getRightSibling().getRightSibling();
        FParamsASTNode fParams;

        typeStr = getType(type);
        verifyClass(typeStr);
        //free function definition
        if(headerChoice.getFirstChild().getValue().equals("(")) {
            fParams = (FParamsASTNode) id.getRightSibling().getFirstChild().getRightSibling();
        } else {
            //class function definition
            //id was id of class
            id = headerChoice.getFirstChild().getRightSibling();
            fParams = (FParamsASTNode) id.getRightSibling().getRightSibling();
        }


        //add function paramters to function table
        if(!fParams.getFirstChild().getValue().equals("EPSILON")) {
            fParams.accept(this);
        }

        varDeclStatFuncRepASTNode.accept(this);

    }

    public void visit(FParamsASTNode astNode) {
        String paramTypeStr;

        ASTNode type;
        ASTNode head;

        head = astNode;

        while(!head.getFirstChild().getValue().equals("EPSILON")) {

            if(head.getValue().equals("fParams")) {
                type = head.getFirstChild();
                head = type.getRightSibling().getRightSibling().getRightSibling();
            }
            else {
                type = head.getFirstChild().getFirstChild().getRightSibling();
                head = head.getFirstChild().getRightSibling();
            }

            paramTypeStr = getType(type);
            verifyClass(paramTypeStr);
        }

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

            }
            else if(varCheckNext.getFirstChild().getValue().equals("(")) {
                //Element is a function declaration
                head = varCheckNext.getFirstChild();
                fParamsASTNode = (FParamsASTNode) head.getRightSibling();
                fParamsASTNode.accept(this);
                //point to function delaration repitition
                head = varCheckNext.getFirstChild().getRightSibling().getRightSibling().getRightSibling().getRightSibling();
            }
        }

        if(head != null && head.getValue().equals("funcDeclRep")) {
            while(head.getValue().equals("funcDeclRep") && !head.getFirstChild().getValue().equals("EPSILON")) {
                type = head.getFirstChild().getFirstChild();
                elementTypeStr = getType(type);
                if (!(elementTypeStr.equals("integer") || elementTypeStr.equals("float"))) {
                    verifyClass(elementTypeStr);
                }
                id = type.getRightSibling();
                fParamsASTNode = (FParamsASTNode) id.getRightSibling().getRightSibling();
                fParamsASTNode.accept(this);
                head = head.getFirstChild().getRightSibling();
            }
        }

    }

    public void visit(VarDeclStatFuncRepASTNode astNode) {

    }
}
