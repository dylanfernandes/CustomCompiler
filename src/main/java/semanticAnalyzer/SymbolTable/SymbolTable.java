package semanticAnalyzer.SymbolTable;

import java.util.ArrayList;
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

    public String getName() {
        return name;
    }

    public void addEntry(SymbolTableEntry newEntry) {
        symbolTableEntries.add(newEntry);
    }

    public void addEntries(List<SymbolTableEntry> entries){
        for (int i = 0; i < entries.size(); i++) {
            addEntry(entries.get(i));
        }
    }

    public int find(String name){
        SymbolTableEntry temp;
        for (int i  = 0; i < symbolTableEntries.size(); i++){
            temp = symbolTableEntries.get(i);
            if(temp.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public SymbolTableEntry search(String name) {
        int index = find(name);
        if(index > -1 && index < symbolTableEntries.size()) {
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

    public String print() {
        SymbolTableEntry current;
        List<Integer> links = new ArrayList<Integer>();
        String table = "";
        table += "Symbol Table: " + this.name + "\n";
        table += "name\t kind\t type\t link \n";

        for (int i = 0;i < symbolTableEntries.size(); i++) {
            current = symbolTableEntries.get(i);
            table += current.print();
            if(current.hasLink()) {
                links.add(i);
            }
        }

        //print linked tables
        for (int j = 0; j < links.size(); j++) {
            table += "\n";
            table += symbolTableEntries.get(links.get(j)).getLink().print();
        }
        return table;
    }


}
