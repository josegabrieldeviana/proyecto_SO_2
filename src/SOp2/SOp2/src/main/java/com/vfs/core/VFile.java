package com.vfs.core;

import com.vfs.structures.CustomLinkedList;
import java.awt.Color;

public class VFile extends FileSystemItem {//los archivos del directorio
    private int startBlockId; //donde empieza el bloque de id
    private int fileSize; // En bloques para este requerimiento
    private Color fileColor; //el color del archivo
    private CustomLinkedList<Integer> assignedBlocks; //los bloques asignados de cada archivo

    public VFile(String name, String owner, int permissions, int numBlocks) {
        super(name, owner, permissions, false); //los atributos de todos los file
        this.startBlockId = -1;
        this.fileSize = numBlocks;
        this.assignedBlocks = new CustomLinkedList<>();
        // Generar color aleatorio para representación en el SD
        this.fileColor = new Color((int)(Math.random() * 0x1000000));
    }

    public void addBlock(int blockId) {
        if (assignedBlocks.isEmpty()) this.startBlockId = blockId; //si bloques asignados estan vacíos rnotnces  en el bloque que empieza
        assignedBlocks.add(blockId); //es el bloque id y se añade el bloque id
    }

    public CustomLinkedList<Integer> getAssignedBlocks() { return assignedBlocks; }
    public Color getFileColor() { return fileColor; }
    public int getStartBlockId() { return startBlockId; }
    public int getNumBlocks() { return fileSize; }
}
