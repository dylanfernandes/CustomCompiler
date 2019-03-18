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
        SymbolTableEntry symbolTableEntry;
        ASTNode child = astNode.getFirstChild();
        String value = "";
        if(child.getValue().equals("class")) {
            value = child.getRightSibling().getValue();
        }
        symbolTableEntry = new SymbolTableEntry(value, EntryKind.CLASS, null, null);
        //TODO class body
        astNode.setEntry(symbolTableEntry);
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
