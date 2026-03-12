package com.vfs.core;

public class Block {
    private int id;
    private byte[] data;
    private int nextBlockId; // -1 indica fin de cadena

    public Block(int id, int size) {
        this.id = id;
        this.data = new byte[size];
        this.nextBlockId = -1;
    }

    public int getId() { return id; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
    public int getNextBlockId() { return nextBlockId; }
    public void setNextBlockId(int nextBlockId) { this.nextBlockId = nextBlockId; }
}
