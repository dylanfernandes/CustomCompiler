package semanticAnalyzer.SymbolTable;

public class SymbolTableEntry {
    String name;
    EntryKind entryKind;
    EntryType entryType;
    SymbolTableEntry link;
    boolean hasLink;

    public SymbolTableEntry(String name, EntryKind entryKind, EntryType entryType, SymbolTableEntry link) {
        this.name = name;
        this.entryKind = entryKind;
        this.entryType = entryType;
        this.link = link;
        hasLink();
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

    public SymbolTableEntry getLink() {
        return link;
    }

    public void setLink(SymbolTableEntry link) {
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
