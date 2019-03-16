package semanticAnalyzer;

import java.util.Collections;
import java.util.List;

public class EntryType {
    String elementType;
    List<String> parameterTypes;

    public EntryType(String elementType, List<String> parameterTypes) {
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

    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
