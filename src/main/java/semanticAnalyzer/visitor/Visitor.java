package semanticAnalyzer.visitor;

import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

public interface Visitor {
    public void  visit(StringASTNode astNode);
    public void  visit(TokenASTNode astNode);
    public void  visit(ProgASTNode astNode);
    public void  visit(ClassDeclASTNode astNode);
    public void  visit(FuncDefASTNode astNode);
    public void  visit(FParamsASTNode astNode);
    public void  visit(VarOrFuncCheckASTNode astNode);
}
