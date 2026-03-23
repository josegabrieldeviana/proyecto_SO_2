package com.vfs.core;

import com.vfs.structures.CustomLinkedList;

public class VDirectory extends FileSystemItem {
    private CustomLinkedList<FileSystemItem> children;

    public VDirectory(String name, String owner, int permissions) {
        super(name, owner, permissions, true);
        this.children = new CustomLinkedList<>();
    }

    public void addChild(FileSystemItem item) {
        children.add(item);
    }

    public void removeChild(FileSystemItem item) {
        children.remove(item);
    }

    public CustomLinkedList<FileSystemItem> getChildren() {
        return children;
    }
}
