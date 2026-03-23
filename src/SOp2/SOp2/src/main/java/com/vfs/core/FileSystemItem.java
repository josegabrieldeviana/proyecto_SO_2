package com.vfs.core;

import java.util.Date;

public abstract class FileSystemItem {
    protected String name; //el nombre de file sys
    protected String owner; //este es el nombre del dueño
    protected int permissions; // Representación simple (ej: 777, 755)
    protected Date creationDate; //eñ tiempo de creación
    protected boolean isDirectory; //para revisar si es directorio

    public FileSystemItem(String name, String owner, int permissions, boolean isDirectory) {
        this.name = name;
        this.owner = owner;
        this.permissions = permissions;
        this.creationDate = new Date();
        this.isDirectory = isDirectory;
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOwner() { return owner; }
    public int getPermissions() { return permissions; }
    public boolean isDirectory() { return isDirectory; }
    public Date getCreationDate() { return creationDate; }
}
