package semanticAnalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SymbolTable {
    List<SymbolTableEntry> symbolTableEntries;
    String name;

    public SymbolTable(String name) {
        this.name = name;
        symbolTableEntries = new ArrayList<SymbolTableEntry>();
    }

    public SymbolTable(List<SymbolTableEntry> symbolTableEntries, String name) {
        this.symbolTableEntries = symbolTableEntries;
        this.name = name;
    }

    public void addEntry(SymbolTableEntry newEntry) {
        symbolTableEntries.add(newEntry);
    }

    public int find(String name){
        SymbolTableEntry temp;
        for (int i  = 0; i < symbolTableEntries.size(); i++){
            temp = symbolTableEntries.get(i);
            if(temp.getName() == name) {
                return i;
            }
        }
        return -1;
    }

    public SymbolTableEntry search(String name) {
        int index = find(name);
        if(index > 0 && index < symbolTableEntries.size()) {
            return symbolTableEntries.get(index);
        }
        return null;
    }

    public SymbolTableEntry delete(String name) {
        int index = find(name);
        if(index > 0 && index < symbolTableEntries.size()) {
            return symbolTableEntries.remove(index);
        }
        return null;
    }


}
