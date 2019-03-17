package syntacticAnalyzer.AST.semanticNodes;

import semanticAnalyzer.SymbolTable;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;

public class ProgASTNode extends ASTNode {
    private String value;
    private SymbolTable symbolTable;

    public String getValue() {
        return null;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
}
