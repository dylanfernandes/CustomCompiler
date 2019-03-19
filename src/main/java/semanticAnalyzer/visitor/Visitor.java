package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.*;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

public abstract class Visitor {
    public abstract void  visit(StringASTNode astNode);
    public abstract void  visit(TokenASTNode astNode);
    public abstract void  visit(ProgASTNode astNode);
    public abstract void  visit(ClassDeclASTNode astNode);
    public abstract void  visit(FuncDefASTNode astNode);
    public abstract void  visit(FParamsASTNode astNode);
    public abstract void  visit(VarOrFuncCheckASTNode astNode);
    public abstract void  visit(VarDeclStatFuncRepASTNode astNode);

    protected SymbolTableEntry getInheritanceEntry(ASTNode child, ASTNode endLocation){
        String inheritName;
        //For movement around tree
        ASTNode temp;
        temp = child.getFirstChild();
        temp = temp.getRightSibling();
        //set instance to new location
        endLocation.set(temp);
        inheritName = temp.getValue();
        return new SymbolTableEntry(inheritName, EntryKind.INHERIT,null, null);

    }

    protected String getType(ASTNode typeRoot){
        while (typeRoot.getFirstChild() != null) {
            typeRoot = typeRoot.getFirstChild();
        }
        return typeRoot.getValue();
    }

    protected EntryType getArray(ASTNode array, String type) {
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

    protected SymbolTableEntry getParamFuncTable(FParamsASTNode fParamsASTNode, String idVal, String elementTypeStr){
        SymbolTable funcTable;
        EntryType elementType;

        fParamsASTNode.accept(this);
        funcTable = new SymbolTable(idVal);
        funcTable.addEntries(fParamsASTNode.getEntries());
        elementType = new EntryType(elementTypeStr);
        return new SymbolTableEntry(idVal, EntryKind.FUNCTION, elementType, funcTable);
    }

}
