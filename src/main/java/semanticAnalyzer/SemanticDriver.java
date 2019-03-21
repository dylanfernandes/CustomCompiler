package semanticAnalyzer;

import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import utils.FileIOUtils;

public class SemanticDriver {
    private String creationOutputLocation = "src/output/semanticCreationOutput.txt";
    private String typeOutputLocation = "src/output/semanticTypeOutput.txt";

    public void start(ProgASTNode progASTNode) {
        SemanticPhases semanticPhases = new SemanticPhases();
        semanticPhases.startPhases(progASTNode);
        FileIOUtils.writeOutput(semanticPhases.getCreationOutput(), creationOutputLocation);
        FileIOUtils.writeOutput(semanticPhases.getTypeOutput(), typeOutputLocation);
    }
}
