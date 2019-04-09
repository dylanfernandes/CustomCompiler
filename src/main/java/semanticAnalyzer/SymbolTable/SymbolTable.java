package semanticAnalyzer.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    //List sorted with order of declaration in code
    List<SymbolTableEntry> symbolTableEntries;
    List<SymbolTableEntry> parameters;
    List<SymbolTableEntry> inheritances;
    List<SymbolTableEntry> variables;
    List<SymbolTableEntry> classes;
    String name;

    public SymbolTable(String name) {
        this.name = name;
        symbolTableEntries = new ArrayList<SymbolTableEntry>();
        parameters = new ArrayList<SymbolTableEntry>();
        inheritances = new ArrayList<SymbolTableEntry>();
        variables = new ArrayList<SymbolTableEntry>();
        classes = new ArrayList<SymbolTableEntry>();
    }

    public SymbolTable(List<SymbolTableEntry> symbolTableEntries, String name) {
        this.symbolTableEntries = symbolTableEntries;
        parameters = new ArrayList<SymbolTableEntry>();
        inheritances = new ArrayList<SymbolTableEntry>();
        variables = new ArrayList<SymbolTableEntry>();
        classes = new ArrayList<SymbolTableEntry>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addEntry(SymbolTableEntry newEntry) {
        if(newEntry.entryKind.equals(EntryKind.PARAMETER)){
            parameters.add(newEntry);
        } else if(newEntry.entryKind.equals(EntryKind.INHERIT)){
            inheritances.add(newEntry);
        } else if(newEntry.entryKind.equals(EntryKind.VARIABLE)){
            variables.add(newEntry);
        } else if(newEntry.entryKind.equals(EntryKind.CLASS)){
            classes.add(newEntry);
        }
        symbolTableEntries.add(newEntry);
    }

    public void addEntries(List<SymbolTableEntry> entries){
        for (int i = 0; i < entries.size(); i++) {
            addEntry(entries.get(i));
        }
    }


    public List<SymbolTableEntry> getClasses() {
        return classes;
    }

    public List<SymbolTableEntry> getVariables() {
        return variables;
    }

    public List<SymbolTableEntry> getParameters() {
        return parameters;
    }

    public int getNumParams() {
        return parameters.size();
    }

    public List<SymbolTableEntry> getInheritances() {
        return inheritances;
    }

    public int getNumInheritances() {
        return inheritances.size();
    }

    public int getNumEntries() {
        return symbolTableEntries.size();
    }

    public SymbolTableEntry getEntryByRow(int row) {
        if(row < symbolTableEntries.size()){
            return symbolTableEntries.get(row);
        }
        return null;
    }


    public int find(String name, EntryKind entryKind){
        SymbolTableEntry temp;
        for (int i  = 0; i < symbolTableEntries.size(); i++){
            temp = symbolTableEntries.get(i);
            if(temp.getName().equals(name) && temp.getEntryKind().equals(entryKind)) {
                return i;
            }
        }
        return -1;
    }

    public int hasInheritance(){
        if(symbolTableEntries.size() > 0 && symbolTableEntries.get(0).getEntryKind() == EntryKind.INHERIT)
            return 0;
        return  -1;
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

    public SymbolTableEntry search(String name, EntryKind entryKind) {
        int index = find(name, entryKind);
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
