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
        EntryType funcTypes;
        String funcName;
        String typeStr;

        ASTNode head = astNode.getFirstChild();
        ASTNode type = head.getFirstChild();
        ASTNode id = type.getRightSibling();
        FParamsASTNode fParams = (FParamsASTNode) id.getRightSibling().getFirstChild().getRightSibling();

        typeStr = getType(type);
        funcTypes = new EntryType(typeStr);
        funcName = id.getValue();

        funcTable = new SymbolTable(funcName);

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

            if (!array.getFirstChild().getValue().equals("EPSILON")) {
                param = new VariableType(paramTypeStr);
                while (!array.getFirstChild().getValue().equals("EPSILON")) {
                    array = array.getFirstChild();
                    param.addArrayDimension(array.getFirstChild().getRightSibling().getValue());
                    array = array.getRightSibling();
                }
                paramType = new EntryType(param);
            } else {
                paramType = new EntryType(paramTypeStr);
            }
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

    public void visit(VarOrFuncCheckASTNode astNode) {
        SymbolTableEntry symbolTableEntry;
        String elementName;
        String elementTypeStr;
        EntryType elementType;
        VariableType element;

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

                if (!array.getFirstChild().getValue().equals("EPSILON")) {
                    elementType = new EntryType(elementTypeStr);
                } else {
                    elementType = new EntryType(elementTypeStr);
                }
                symbolTableEntry = new SymbolTableEntry(id.getValue(), EntryKind.VARIABLE, elementType, null);
                astNode.addEntry(symbolTableEntry);
            }
        }
    }


}
