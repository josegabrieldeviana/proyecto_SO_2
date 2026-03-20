package com.vfs.core;

import com.vfs.structures.CustomLinkedList;

import java.awt.Color;

public class SimulatedDisk {
    private Block[] blocks;
    private int blockSize;
    private boolean[] freeBlocks;
    private Color[] blockColors;

    public SimulatedDisk(int totalBlocks, int blockSize) {
        this.blocks = new Block[totalBlocks];
        this.freeBlocks = new boolean[totalBlocks];
        this.blockColors = new Color[totalBlocks];
        this.blockSize = blockSize;
        for (int i = 0; i < totalBlocks; i++) {
            blocks[i] = new Block(i, blockSize);
            freeBlocks[i] = true;
            blockColors[i] = Color.WHITE;
        }
    }

    public synchronized int allocateBlock(Color color) {
        for (int i = 0; i < freeBlocks.length; i++) {
            if (freeBlocks[i]) {
                freeBlocks[i] = false;
                blockColors[i] = color;
                return i;
            }
        }
        return -1;
    }

    public synchronized void freeBlock(int id) {
        if (id >= 0 && id < freeBlocks.length) {
            freeBlocks[id] = true;
            blockColors[id] = Color.WHITE;
            blocks[id].setNextBlockId(-1);
        }
    }

    public Color getBlockColor(int id) {
        return blockColors[id];
    }

    public Block getBlock(int id) {
        return (id >= 0 && id < blocks.length) ? blocks[id] : null;
    }

    public int getBlockSize() { return blockSize; }
    public int getTotalBlocks() { return blocks.length; }
    public boolean[] getFreeBlocksStatus() { return freeBlocks; }

    public int getFreeBlockCount() {
        int count = 0;
        for (boolean isFree : freeBlocks) {
            if (isFree) count++;
        }
        return count;
    }
}
