package com.vfs.core;

public class Block { //clase block
    private int id; //aqui tenemos su id
    private byte[] data; //aquí van a estar sus datos
    private int nextBlockId; // -1 indica fin de cadena y representa el siguiente basicamente

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
