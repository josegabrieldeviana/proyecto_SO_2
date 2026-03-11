package com.vfs.core;

import com.vfs.journaling.JournalManager;
import com.vfs.journaling.JournalEntry;
import com.vfs.process.LockManager;
import com.vfs.structures.CustomLinkedList;

public class FileSystemManager {
    private static FileSystemManager instance;
    private SimulatedDisk disk;
    private VDirectory root;
    private JournalManager journal;
    private String currentUser = "Admin";
    private boolean isAdmin = true;

    private FileSystemManager() {
        this.disk = new SimulatedDisk(100, 1024);
        this.root = new VDirectory("/", "Admin", 777);
        this.journal = new JournalManager();
    }

    public static FileSystemManager getInstance() {
        if (instance == null) instance = new FileSystemManager();
        return instance;
    }

    public synchronized boolean createFile(String name, int numBlocks, VDirectory parent) {
        if (!isAdmin) return false;
        
        JournalEntry entry = journal.logStart("CREATE", parent.getName() + name);
        if (!LockManager.acquireLock(name, true)) return false;

        try {
            VFile newFile = new VFile(name, currentUser, 755, numBlocks);
            for (int i = 0; i < numBlocks; i++) {
                int blockId = disk.allocateBlock(newFile.getFileColor());
                if (blockId == -1) {
                    entry.abort();
                    return false;
                }
                newFile.addBlock(blockId);
            }

            parent.addChild(newFile);
            entry.commit();
            return true;
        } finally {
            LockManager.releaseLock(name);
        }
    }

    public synchronized void deleteResource(FileSystemItem item, VDirectory parent) {
        if (!isAdmin) return;
        if (item.isDirectory()) {
            VDirectory dir = (VDirectory) item;
            CustomLinkedList<FileSystemItem> children = new CustomLinkedList<>();
            for (FileSystemItem child : dir.getChildren()) children.add(child);
            for (FileSystemItem child : children) deleteResource(child, dir);
            parent.removeChild(dir);
        } else {
            deleteFile((VFile) item, parent);
        }
    }

    private void deleteFile(VFile file, VDirectory parent) {
        for (Integer blockId : file.getAssignedBlocks()) {
            disk.freeBlock(blockId);
        }
        parent.removeChild(file);
    }

    public synchronized boolean renameResource(FileSystemItem item, String newName) {
        if (!isAdmin) return false;
        
        JournalEntry entry = journal.logStart("RENAME", item.getName() + " -> " + newName);
        if (!LockManager.acquireLock(item.getName(), true)) {
            entry.abort();
            return false;
        }

        try {
            item.setName(newName);
            entry.commit();
            return true;
        } catch (Exception e) {
            entry.abort();
            return false;
        } finally {
            LockManager.releaseLock(item.getName());
        }
    }

    public synchronized boolean renameItem(FileSystemItem item, String newName) {
        if (!isAdmin) return false;
        if (newName == null || newName.trim().isEmpty()) return false;

        String oldName = item.getName();
        JournalEntry entry = journal.logStart("RENAME", oldName + " -> " + newName.trim());
        if (!LockManager.acquireLock(oldName, true)) {
            entry.abort();
            return false;
        }
        try {
            item.setName(newName.trim());
            entry.commit();
            return true;
        } finally {
            LockManager.releaseLock(oldName);
        }
    }
    
    
    public void switchMode(boolean admin) { this.isAdmin = admin; }
    public boolean isAdmin() { return isAdmin; }
    public VDirectory getRoot() { return root; }
    public SimulatedDisk getDisk() { return disk; }
    public JournalManager getJournal() { return journal; }
}
