package semanticAnalyzer;

import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import utils.FileIOUtils;

public class SemanticDriver {
    private String creationOutputLocation = "src/output/semanticCreationOutput.txt";
    private String typeOutputLocation = "src/output/semanticTypeOutput.txt";
    private boolean hasError = true;

    public void start(ProgASTNode progASTNode) {
        //nothing to verify
        hasError = true;
        SemanticPhases semanticPhases = new SemanticPhases();
        semanticPhases.startPhases(progASTNode);
        FileIOUtils.writeOutput(semanticPhases.getCreationOutput(), creationOutputLocation);
        FileIOUtils.writeOutput(semanticPhases.getTypeOutput(), typeOutputLocation);
        hasError = semanticPhases.hasError();
    }

    public boolean hasError() {
        return hasError;
    }
}
