package syntacticAnalyzer.AST.semanticNodes;

import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.BasicASTNode;

import java.util.ArrayList;
import java.util.List;

public class VarDeclStatFuncRepASTNode extends BasicASTNode {
    private List<SymbolTableEntry> entries;

    public VarDeclStatFuncRepASTNode(String value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        super(value, parent, rightSibling, firstSibling, firstChild);
        entries = new ArrayList<SymbolTableEntry>();
    }

    public VarDeclStatFuncRepASTNode(String value) {
        super(value);
        entries = new ArrayList<SymbolTableEntry>();
    }

    public List<SymbolTableEntry> getEntries() {
        return entries;
    }

    public void addEntry(SymbolTableEntry entry) {
        this.entries.add(entry);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
