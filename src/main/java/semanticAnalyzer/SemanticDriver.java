package semanticAnalyzer;

import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import utils.FileIOUtils;

public class SemanticDriver {
    private String outputLocation = "src/output/semanticOutput.txt";

    public void start(ProgASTNode progASTNode) {
        SemanticPhases semanticPhases = new SemanticPhases();
        semanticPhases.startPhases(progASTNode);
        FileIOUtils.writeOutput(progASTNode.getGlobalTable().print(), outputLocation);
    }
}
