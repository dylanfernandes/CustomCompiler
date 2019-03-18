package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.*;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.ClassDeclASTNode;
import syntacticAnalyzer.AST.semanticNodes.FParamsASTNode;
import syntacticAnalyzer.AST.semanticNodes.FuncDefASTNode;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

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
        String value = "";
        String inheritName;


        if(child.getValue().equals("class")) {
            child = child.getRightSibling();
            value = child.getValue();
        }

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
        symbolTableEntry = new SymbolTableEntry(value, EntryKind.CLASS, null, classTable);
        //TODO class body
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

        ASTNode head = astNode.getFirstChild();
        ASTNode type = head.getFirstChild();
        ASTNode id = type.getRightSibling();
        FParamsASTNode fParams = (FParamsASTNode) id.getRightSibling().getFirstChild().getRightSibling();

        while(type.getFirstChild() != null) {
            type = type.getFirstChild();
        }
        funcTypes = new EntryType(type.getValue());
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



            while (type.getFirstChild() != null) {
                type = type.getFirstChild();
            }

            paramName = id.getValue();
            paramTypeStr = type.getValue();

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


}
