package semanticAnalyzer;

public class SymbolTableEntry {
    String name;
    EntryKind entryKind;
    EntryType entryType;
    SymbolTableEntry link;

    public SymbolTableEntry(String name, EntryKind entryKind, EntryType entryType, SymbolTableEntry link) {
        this.name = name;
        this.entryKind = entryKind;
        this.entryType = entryType;
        this.link = link;
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
        this.link = link;
    }
}
