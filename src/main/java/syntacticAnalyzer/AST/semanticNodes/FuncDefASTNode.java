package syntacticAnalyzer.AST.semanticNodes;

import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.BasicASTNode;

public class FuncDefASTNode extends BasicASTNode {
    private SymbolTableEntry entry;

    public FuncDefASTNode(String value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        super(value, parent, rightSibling, firstSibling, firstChild);
    }

    public FuncDefASTNode(String value) {
        super(value);
    }

    public SymbolTableEntry getEntry() {
        return entry;
    }

    public void setEntry(SymbolTableEntry entry) {
        this.entry = entry;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
