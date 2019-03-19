package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.*;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

public class SymTabCreationVisitor implements Visitor {
    public void visit(StringASTNode astNode) {

    }

    public void visit(TokenASTNode astNode) {

    }

    public void visit(ProgASTNode astNode) {
        SymbolTable globalTable = new SymbolTable("global");
        ASTNode classDeclRep = astNode.getFirstChild();
        ASTNode funcDefRep = classDeclRep.getRightSibling();

        ClassDeclASTNode classNode;
        FuncDefASTNode functionNode;

        while(classDeclRep.getValue() == "classDeclRep" ) {
            classDeclRep = classDeclRep.getFirstChild();
            if(classDeclRep.getClass() == ClassDeclASTNode.class) {
                classNode = (ClassDeclASTNode) classDeclRep;
                classNode.accept(this);
                globalTable.addEntry(classNode.getEntry());
                classDeclRep = classNode.getRightSibling();
            }
        }

        //TODO Function
        while(funcDefRep.getValue() == "funcDefRep" ) {
            funcDefRep = funcDefRep.getFirstChild();
            if(funcDefRep.getClass() == FuncDefASTNode.class) {
                functionNode = (FuncDefASTNode) funcDefRep;
                functionNode.accept(this);
                globalTable.addEntry(functionNode.getEntry());
                funcDefRep = functionNode.getRightSibling();
            }
        }
        //TODO Main
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

    private SymbolTableEntry getInheritanceEntry(ASTNode child){
        String inheritName;
        //For movement around tree
        ASTNode temp;
        temp = child.getFirstChild();
        temp = temp.getRightSibling();
        //set instance to new location
        child.set(temp);
        inheritName = temp.getValue();
        return new SymbolTableEntry(inheritName,EntryKind.INHERIT,null, null);

    }

    public void visit(FuncDefASTNode astNode) {
        SymbolTable funcTable;
        SymbolTableEntry symbolTableEntry;
        SymbolTableEntry classDef;
        EntryType funcTypes;
        String funcName;
        String typeStr;

        ASTNode head = astNode.getFirstChild();
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

    private String getType(ASTNode typeRoot){
        while (typeRoot.getFirstChild() != null) {
            typeRoot = typeRoot.getFirstChild();
        }
        return typeRoot.getValue();
    }

    private EntryType getArray(ASTNode array, String type) {
        EntryType entryType;
        VariableType variableType;
        if (!array.getFirstChild().getValue().equals("EPSILON")) {
            variableType = new VariableType(type);
            while (!array.getFirstChild().getValue().equals("EPSILON")) {
                array = array.getFirstChild();
                variableType.addArrayDimension(array.getFirstChild().getRightSibling().getValue());
                array = array.getRightSibling();
            }
            entryType = new EntryType(variableType);
        } else {
            entryType = new EntryType(type);
        }
        return entryType;
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

    private SymbolTableEntry getParamFuncTable(FParamsASTNode fParamsASTNode, String idVal, String elementTypeStr){
        SymbolTable funcTable;
        EntryType elementType;

        fParamsASTNode.accept(this);
        funcTable = new SymbolTable(idVal);
        funcTable.addEntries(fParamsASTNode.getEntries());
        elementType = new EntryType(elementTypeStr);
        return new SymbolTableEntry(idVal, EntryKind.FUNCTION, elementType, funcTable);
    }


}
