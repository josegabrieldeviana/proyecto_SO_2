package com.vfs.core;

import com.vfs.structures.CustomLinkedList;
import java.awt.Color;

public class VFile extends FileSystemItem {
    private int startBlockId;
    private int fileSize; // En bloques para este requerimiento
    private Color fileColor;
    private CustomLinkedList<Integer> assignedBlocks;

    public VFile(String name, String owner, int permissions, int numBlocks) {
        super(name, owner, permissions, false);
        this.startBlockId = -1;
        this.fileSize = numBlocks;
        this.assignedBlocks = new CustomLinkedList<>();
        // Generar color aleatorio para representación en el SD
        this.fileColor = new Color((int)(Math.random() * 0x1000000));
    }

    public void addBlock(int blockId) {
        if (assignedBlocks.isEmpty()) this.startBlockId = blockId;
        assignedBlocks.add(blockId);
    }

    public CustomLinkedList<Integer> getAssignedBlocks() { return assignedBlocks; }
    public Color getFileColor() { return fileColor; }
    public int getStartBlockId() { return startBlockId; }
    public int getNumBlocks() { return fileSize; }
}
