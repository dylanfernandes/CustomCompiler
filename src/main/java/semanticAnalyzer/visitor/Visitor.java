package semanticAnalyzer.visitor;

import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.ClassDeclASTNode;
import syntacticAnalyzer.AST.semanticNodes.FParamsASTNode;
import syntacticAnalyzer.AST.semanticNodes.FuncDefASTNode;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public interface Visitor {
    public void  visit(StringASTNode astNode);
    public void  visit(TokenASTNode astNode);
    public void  visit(ProgASTNode astNode);
    public void  visit(ClassDeclASTNode astNode);
    public void  visit(FuncDefASTNode astNode);
    public void  visit(FParamsASTNode astNode);
}
