package com.vfs.core;

import java.util.Date;

public abstract class FileSystemItem {
    protected String name;
    protected String owner;
    protected int permissions; // Representación simple (ej: 777, 755)
    protected Date creationDate;
    protected boolean isDirectory;

    public FileSystemItem(String name, String owner, int permissions, boolean isDirectory) {
        this.name = name;
        this.owner = owner;
        this.permissions = permissions;
        this.creationDate = new Date();
        this.isDirectory = isDirectory;
    }

    // Getters y Setters
    public String getName() { return name; }
    public String getOwner() { return owner; }
    public int getPermissions() { return permissions; }
    public boolean isDirectory() { return isDirectory; }
    public Date getCreationDate() { return creationDate; }
}
