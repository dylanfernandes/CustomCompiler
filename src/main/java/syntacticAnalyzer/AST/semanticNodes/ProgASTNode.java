package syntacticAnalyzer.AST.semanticNodes;

import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.BasicASTNode;

public class ProgASTNode extends BasicASTNode {
    private SymbolTable globalTable;

    public ProgASTNode(String value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        super(value, parent, rightSibling, firstSibling, firstChild);
    }

    public ProgASTNode(String value) {
        super(value);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public SymbolTable getGlobalTable() {
        return globalTable;
    }

    public void setGlobalTable(SymbolTable symbolTable) {
        this.globalTable = symbolTable;
    }

}
