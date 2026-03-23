package com.vfs.core;

import com.vfs.structures.CustomLinkedList;

import java.awt.Color;

public class SimulatedDisk {
    private Block[] blocks;
    /*
    representa array de bloques
    */
    private int blockSize;
    /*
    tamaño de bloques
    */
    private boolean[] freeBlocks;
    /*
    ¿hay bloques libres?
    */
    private Color[] blockColors;
    /*
    array de colores para los bloques de archivos
    */

    public SimulatedDisk(int totalBlocks, int blockSize) {
        this.blocks = new Block[totalBlocks]; //un array de bloques con la cantidad total de bloques
        this.freeBlocks = new boolean[totalBlocks]; //un array de booleanos con la cantidad total de bloques
        this.blockColors = new Color[totalBlocks]; //igual con colores
        this.blockSize = blockSize; //cantidad de bloques
        /*
        cada que se inicialice, con "totalblocks" el disco se inicializa, blocksize es el tamño de bloques
        */
        for (int i = 0; i < totalBlocks; i++) {
            blocks[i] = new Block(i, blockSize);
            freeBlocks[i] = true;
            blockColors[i] = Color.WHITE;
        }
    }

    public synchronized int allocateBlock(Color color) {
        /*
        para cada uno de los bloques libres entonces decimos que el bloque libre no esta
        y que es del color que se necesita, para eso es allocateblock
        */
        for (int i = 0; i < freeBlocks.length; i++) {
            if (freeBlocks[i]) {
                freeBlocks[i] = false;
                blockColors[i] = color;
                return i; //regreso ese bloque en específico
            }
        }
        return -1; //regreso el ultimo bloque.
    }

    /*
El id es mayor que 0 y no supera el tamaño de bloques libres     
    */
    public synchronized void freeBlock(int id) {
        if (id >= 0 && id < freeBlocks.length) { 
            freeBlocks[id] = true; //bloques libres en true para esta id
            blockColors[id] = Color.WHITE;
            blocks[id].setNextBlockId(-1);
        }
    }

    public Color getBlockColor(int id) {
        return blockColors[id]; //consigue el color del bloque
    }

    public Block getBlock(int id) { //consigue el bloque 
        return (id >= 0 && id < blocks.length) ? blocks[id] : null;
    }

    public int getBlockSize() { return blockSize; }
    public int getTotalBlocks() { return blocks.length; }
    public boolean[] getFreeBlocksStatus() { return freeBlocks; }
}
