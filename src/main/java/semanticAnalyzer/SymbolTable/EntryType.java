package semanticAnalyzer.SymbolTable;

import java.util.Collections;
import java.util.List;

public class EntryType {
    String elementType;
    List<VariableType> parameterTypes;

    public EntryType(String elementType, List<VariableType> parameterTypes) {
        this.elementType = elementType;
        this.parameterTypes = parameterTypes;
    }

    public EntryType(String elementType) {
        this.elementType = elementType;
        this.parameterTypes = Collections.emptyList();
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String returnValue) {
        this.elementType = returnValue;
    }

    public List<VariableType> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<VariableType> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String print() {
        String type = "";
        type += getElementType();
        if(parameterTypes.size() > 0) {
            type += ": " + printParameters();
        }
        return type;
    }
    private String printParameters() {
        String parameter = "";
        for(int i = 0; i <parameterTypes.size();i++) {
            parameter += parameterTypes.get(i);
            if (parameterTypes.size() > 1 &&  i  < parameterTypes.size() - 1) {
                parameter += ",";
            }
        }
        return parameter;
    }
}
