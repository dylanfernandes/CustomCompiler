package semanticAnalyzer.SymbolTable;

import codeGeneration.CodeGenerationVisitor;
import codeGeneration.GenerationInformation;

public class SymbolTableEntry {
    String name;
    EntryKind entryKind;
    EntryType entryType;
    SymbolTable link;
    boolean hasLink;
    GenerationInformation generationInformation;

    public SymbolTableEntry(String name, EntryKind entryKind, EntryType entryType, SymbolTable link) {
        this.name = name;
        this.entryKind = entryKind;
        this.entryType = entryType;
        this.link = link;
        hasLink();
        generationInformation = new GenerationInformation();
    }

    public void setGenerationInformation(GenerationInformation generationInformation) {
        this.generationInformation = generationInformation;
    }

    public void setSize(int size){
        generationInformation.setSize(size);
    }

    public void setOffset(int offset){
        generationInformation.setOffset(offset);
    }

    public int getSize() {
        return generationInformation.getSize();
    }

    public int getOffset() {
        return generationInformation.getOffset();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntryKind getEntryKind() {
        return entryKind;
    }

    public void setEntryKind(EntryKind entryKind) {
        this.entryKind = entryKind;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public SymbolTable getLink() {
        return link;
    }

    public void setLink(SymbolTable link) {
        hasLink();
        this.link = link;
    }

    public boolean hasLink() {
        if(link != null)
            hasLink = true;
        else
            hasLink = false;

        return hasLink;
    }


    public String print(){
        String entry = "";
        entry += getName() + "\t" + getEntryKind().toString();
        if(entryType != null)
            entry +=  "\t" + getEntryType().print();
        else
            entry +=  "\t null";
        if (link != null)
            entry += "\t" + link.getName();
        else
            entry +=  "\t null";
        entry += "\n";
        return entry;
    }
}
