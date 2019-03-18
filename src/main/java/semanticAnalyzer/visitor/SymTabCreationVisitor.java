package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.EntryType;
import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.ClassDeclASTNode;
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
        SymbolTableEntry symbolTableEntry;
        EntryType funcTypes;

        ASTNode head = astNode.getFirstChild();
        ASTNode type = head.getFirstChild();
        ASTNode id = type.getRightSibling();
        while(type.getFirstChild() != null) {
            type = type.getFirstChild();
        }
        funcTypes = new EntryType(type.getValue());
        symbolTableEntry = new SymbolTableEntry(id.getValue(), EntryKind.FUNCTION, funcTypes, null);
        astNode.setEntry(symbolTableEntry);
    }


}
