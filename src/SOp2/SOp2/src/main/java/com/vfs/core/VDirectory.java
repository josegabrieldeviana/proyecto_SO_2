package com.vfs.core;

import com.vfs.structures.CustomLinkedList;

public class VDirectory extends FileSystemItem { //hereda filesys
    private CustomLinkedList<FileSystemItem> children; 
    //lista de filesysitems (que puede o no ser directorio)
    public VDirectory(String name, String owner, int permissions) {
        
        //va a heredar de filesys con super
        super(name, owner, permissions, true);
        this.children = new CustomLinkedList<>(); //inicializamos la lista
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
