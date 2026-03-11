package com.vfs.gui;

import com.vfs.core.FileSystemItem;
import com.vfs.core.VFile;
import com.vfs.structures.CustomLinkedList;
import javax.swing.table.AbstractTableModel;

public class AllocationTableModel extends AbstractTableModel {
    private String[] columnNames = {"Nombre", "Dueño", "Cant. Bloques", "Inicio", "Bloques"};
    private CustomLinkedList<VFile> files = new CustomLinkedList<>();

    public void setFiles(CustomLinkedList<VFile> files) {
        this.files = files;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return files.size(); }
    @Override
    public int getColumnCount() { return columnNames.length; }
    @Override
    public String getColumnName(int col) { return columnNames[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        VFile file = files.get(row);
        switch (col) {
            case 0: return file.getName();
            case 1: return file.getOwner();
            case 2: return file.getNumBlocks();
            case 3: return file.getStartBlockId();
            case 4: return formatBlocks(file.getAssignedBlocks());
            default: return null;
        }
    }

    private String formatBlocks(CustomLinkedList<Integer> list) {
        StringBuilder sb = new StringBuilder("[");
        for (Integer b : list) sb.append(b).append(",");
        if (sb.length() > 1) sb.setLength(sb.length() - 1);
        return sb.append("]").toString();
    }
}
