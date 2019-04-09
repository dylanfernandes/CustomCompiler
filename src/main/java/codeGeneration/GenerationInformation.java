package codeGeneration;

public class GenerationInformation {
    private int size;
    private int offset;

    public GenerationInformation() {
        size = -1;
        offset = -1;
    }

    public GenerationInformation(int size, int offset) {
        this.size = size;
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
