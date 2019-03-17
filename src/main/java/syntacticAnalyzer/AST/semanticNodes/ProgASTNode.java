package syntacticAnalyzer.AST.semanticNodes;

import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;

public class ProgASTNode extends ASTNode {
    private String value;
    private SymbolTable globalTable;

    public String getValue() {
        return null;
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
