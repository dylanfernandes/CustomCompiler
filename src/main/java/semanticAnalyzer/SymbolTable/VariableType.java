package semanticAnalyzer.SymbolTable;

import java.util.Collections;
import java.util.List;

public class VariableType {
    private String type;
    private List<Integer> arraySizes;

    public VariableType(String type) {
        this.type = type;
        this.arraySizes = Collections.emptyList();
    }

    public VariableType(String type, List<Integer> arraySizes) {
        this.type = type;
        this.arraySizes = arraySizes;
    }

    public boolean isArray() {
        return arraySizes.size() > 0;
    }

    public String print() {
        String variableType = "";
        variableType += type;
        variableType += printArray();
        return variableType;
    }

    private String printArray() {
        String arrayIndices = "";
        if(isArray()){
            for(int i =  0;i < arraySizes.size();i++) {
                arrayIndices += "[" + arraySizes.get(i) + "]";
            }
        }
        return arrayIndices;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
