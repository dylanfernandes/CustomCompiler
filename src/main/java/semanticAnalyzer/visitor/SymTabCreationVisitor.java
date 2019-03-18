package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.ClassDeclASTNode;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class SymTabCreationVisitor implements Visitor {
    public void visit(StringASTNode astNode) {

    }

    public void visit(TokenASTNode astNode) {

    }

    public void visit(ProgASTNode astNode) {
        SymbolTable globalTable = new SymbolTable("global");
        ASTNode classDeclRep = astNode.getFirstChild();
        ClassDeclASTNode classDeclASTNode;
        while(classDeclRep.getValue() == "classDeclRep" ) {
            classDeclRep = classDeclRep.getFirstChild();
            if(classDeclRep.getClass() == ClassDeclASTNode.class) {
                classDeclASTNode = (ClassDeclASTNode) classDeclRep;
                classDeclASTNode.accept(this);
                globalTable.addEntry(classDeclASTNode.getEntry());
                classDeclRep = classDeclASTNode.getRightSibling();
            }
        }
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
        astNode.setEntry(symbolTableEntry);

    }
}
