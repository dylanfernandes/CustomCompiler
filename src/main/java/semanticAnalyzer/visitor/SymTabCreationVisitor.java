package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.*;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

public class SymTabCreationVisitor extends Visitor {
    public void visit(StringASTNode astNode) {

    }

    public void visit(TokenASTNode astNode) {

    }

    public void visit(ProgASTNode astNode) {
        SymbolTable globalTable = new SymbolTable("global");
        SymbolTable mainTable = new SymbolTable("main");
        ASTNode classDeclRep = astNode.getFirstChild();
        ASTNode funcDefRep = classDeclRep.getRightSibling();
        VarDeclStatFuncRepASTNode varDeclStatFuncRep = (VarDeclStatFuncRepASTNode) funcDefRep.getRightSibling().getRightSibling().getFirstChild().getRightSibling();

        ClassDeclASTNode classNode;
        FuncDefASTNode functionNode;

        while(classDeclRep.getValue().equals("classDeclRep") ) {
            classDeclRep = classDeclRep.getFirstChild();
            if(classDeclRep.getClass() == ClassDeclASTNode.class) {
                classNode = (ClassDeclASTNode) classDeclRep;
                classNode.accept(this);
                globalTable.addEntry(classNode.getEntry());
                classDeclRep = classNode.getRightSibling();
            }
        }

        while(funcDefRep.getValue().equals("funcDefRep") ) {
            funcDefRep = funcDefRep.getFirstChild();
            if(funcDefRep.getClass() == FuncDefASTNode.class) {
                functionNode = (FuncDefASTNode) funcDefRep;
                functionNode.accept(this);
                globalTable.addEntry(functionNode.getEntry());
                funcDefRep = functionNode.getRightSibling();
            }
        }

        varDeclStatFuncRep.accept(this);
        mainTable.addEntries(varDeclStatFuncRep.getEntries());

        globalTable.addEntry(new SymbolTableEntry("main", EntryKind.FUNCTION, null, mainTable));
        astNode.setGlobalTable(globalTable);

    }


    public void visit(ClassDeclASTNode astNode) {
        SymbolTable classTable;
        SymbolTableEntry symbolTableEntry;
        SymbolTableEntry inheritance;
        ASTNode child = astNode.getFirstChild();
        VarOrFuncCheckASTNode varOrFuncCheck;
        String value = "";
        String inheritName;


        if(child.getValue().equals("class")) {
            child = child.getRightSibling();
            value = child.getValue();
        }
        varOrFuncCheck = (VarOrFuncCheckASTNode) child.getRightSibling().getRightSibling().getRightSibling();
        classTable = new SymbolTable(value);

        //get inheritance of class
        if(child.getRightSibling().getValue().equals("classExOpt")) {
            child = child.getRightSibling();
            if(child.getFirstChild().getValue().equals(":")) {
                inheritance = getInheritanceEntry(child);
                classTable.addEntry(inheritance);

                //get several inheritance
                child = child.getRightSibling();
                while (child.getFirstChild().getValue().equals(",")) {
                    inheritance = getInheritanceEntry(child);
                    classTable.addEntry(inheritance);
                    child = child.getRightSibling();
                }
            }
        }
        //TODO class body
        varOrFuncCheck.accept(this);
        classTable.addEntries(varOrFuncCheck.getEntries());

        symbolTableEntry = new SymbolTableEntry(value, EntryKind.CLASS, null, classTable);
        astNode.setEntry(symbolTableEntry);
    }


    public void visit(FuncDefASTNode astNode) {
        SymbolTable funcTable;
        SymbolTableEntry symbolTableEntry;
        SymbolTableEntry classDef;
        EntryType funcTypes;
        String funcName;
        String typeStr;

        ASTNode head = astNode.getFirstChild();
        VarDeclStatFuncRepASTNode varDeclStatFuncRepASTNode = (VarDeclStatFuncRepASTNode) head.getRightSibling().getFirstChild().getRightSibling();
        ASTNode type = head.getFirstChild();
        ASTNode id = type.getRightSibling();
        ASTNode headerChoice = id.getRightSibling();
        FParamsASTNode fParams;

        typeStr = getType(type);
        funcTypes = new EntryType(typeStr);
        //free function definition
        if(headerChoice.getFirstChild().getValue().equals("(")) {
            fParams = (FParamsASTNode) id.getRightSibling().getFirstChild().getRightSibling();
            funcName = id.getValue();
            funcTable = new SymbolTable(funcName);
        } else {
            //class function definition
            //id was id of class
            classDef = new SymbolTableEntry(id.getValue(), EntryKind.INHERIT, null, null);
            id = headerChoice.getFirstChild().getRightSibling();
            funcName = id.getValue();
            fParams = (FParamsASTNode) id.getRightSibling().getRightSibling();
            funcTable = new SymbolTable(funcName);
            funcTable.addEntry(classDef);
        }


        //add function paramters to function table
        if(!fParams.getFirstChild().getValue().equals("EPSILON")) {
            fParams.accept(this);
            funcTable.addEntries(fParams.getEntries());
        }

        varDeclStatFuncRepASTNode.accept(this);
        funcTable.addEntries(varDeclStatFuncRepASTNode.getEntries());

        symbolTableEntry = new SymbolTableEntry(funcName, EntryKind.FUNCTION, funcTypes, funcTable);
        astNode.setEntry(symbolTableEntry);

    }

    public void visit(FParamsASTNode astNode) {
        SymbolTableEntry symbolTableEntry;
        String paramName;
        String paramTypeStr;
        EntryType paramType;
        VariableType param;

        ASTNode type;
        ASTNode id;
        ASTNode array;
        ASTNode head;

        head = astNode;

        while(!head.getFirstChild().getValue().equals("EPSILON")) {

            if(head.getValue().equals("fParams")) {
                type = head.getFirstChild();
                id = type.getRightSibling();
                array = id.getRightSibling();
                head = array.getRightSibling();
            }
            else {
                type = head.getFirstChild().getFirstChild().getRightSibling();
                id = type.getRightSibling();
                array = id.getRightSibling();
                head = head.getFirstChild().getRightSibling();
            }



            paramName = id.getValue();
            paramTypeStr = getType(type);

            paramType = getArray(array, paramTypeStr);
            symbolTableEntry = new SymbolTableEntry(paramName, EntryKind.PARAMETER, paramType, null);
            astNode.addEntry(symbolTableEntry);
        }
    }


    public void visit(VarOrFuncCheckASTNode astNode) {
        SymbolTableEntry symbolTableEntry;
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

            //element is variable
            if (varCheckNext.getFirstChild().getValue().equals("arraySizeRep")) {
                array = varCheckNext.getFirstChild();
                head = varCheckNext.getFirstChild().getRightSibling().getRightSibling();

                elementType = getArray(array, elementTypeStr);

                symbolTableEntry = new SymbolTableEntry(id.getValue(), EntryKind.VARIABLE, elementType, null);
                astNode.addEntry(symbolTableEntry);
            }
            else if(varCheckNext.getFirstChild().getValue().equals("(")) {
                //Element is a function declaration
                head = varCheckNext.getFirstChild();
                fParamsASTNode = (FParamsASTNode) head.getRightSibling();
                astNode.addEntry(getParamFuncTable(fParamsASTNode, id.getValue(), elementTypeStr));
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
                astNode.addEntry(getParamFuncTable(fParamsASTNode, id.getValue(),elementTypeStr));
                head = head.getFirstChild().getRightSibling();
            }
        }

    }

    public void visit(VarDeclStatFuncRepASTNode astNode) {
        ASTNode head = astNode;
        SymbolTableEntry symbolTableEntry;
        ASTNode var;
        ASTNode array;
        String type = "";
        String id = "";
        EntryType varEntry;

        while(head != null && head.getValue().equals("varDeclStatFuncRep")) {
            if(head.getFirstChild().getValue().equals("varDeclNotId")) {
                var = head.getFirstChild().getFirstChild();
                type = var.getFirstChild().getValue();
                id = var.getRightSibling().getValue();
                array = var.getRightSibling().getRightSibling().getFirstChild();
                varEntry = getArray(array, type);
                symbolTableEntry = new SymbolTableEntry(id, EntryKind.VARIABLE, varEntry,null);
                astNode.addEntry(symbolTableEntry);
            }
            head = head.getFirstChild().getRightSibling();
        }

    }



}
