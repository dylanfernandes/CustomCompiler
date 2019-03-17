package semanticAnalyzer;

import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import utils.FileIOUtils;

public class SemanticDriver {
    private String outputLocation = "src/output/semanticOutput.txt";

    public void start(ProgASTNode progASTNode) {
        SemanticPhases semanticPhases = new SemanticPhases();
        semanticPhases.creation(progASTNode);
        FileIOUtils.writeOutput(progASTNode.print(), outputLocation);
    }
}
