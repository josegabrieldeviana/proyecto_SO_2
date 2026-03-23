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
        this.disk = new SimulatedDisk(200, 1024);
        this.root = new VDirectory("/", "Admin", 777);
        this.journal = new JournalManager();
    }

    public static FileSystemManager getInstance() {
        if (instance == null) instance = new FileSystemManager();
        return instance;
    }

    private boolean crashRequested = false;

    public void setCrashRequested(boolean requested) { this.crashRequested = requested; }

    public synchronized boolean createFile(String name, int numBlocks, VDirectory parent) {
        if (!isAdmin) return false;
        
        if (disk.getFreeBlockCount() < numBlocks) {
            System.err.println("WARNING: Not enough free blocks available. Requested: " + numBlocks + ", Free: " + disk.getFreeBlockCount());
            return false;
        }
        
        JournalEntry entry = journal.logStart("CREATE", parent.getName() + name);
        if (!LockManager.acquireLock(name, true)) {
            entry.abort();
            return false;
        }

        try {
            VFile newFile = new VFile(name, currentUser, 755, numBlocks);
            for (int i = 0; i < numBlocks; i++) {
                int blockId = disk.allocateBlock(newFile.getFileColor());
                if (blockId == -1) {
                    entry.abort();
                    return false;
                }
                newFile.addBlock(blockId);
                entry.addAffectedBlock(blockId); // REGISTRO PARA UNDO

                // Simulamos crash a mitad de la asignación si se solicitó
                if (crashRequested && i == (numBlocks / 2)) {
                    System.out.println("CRASH: Interrumpiendo asignación de bloques...");
                    throw new RuntimeException("Crash inducido por simulación");
                }
            }

            parent.addChild(newFile);
            entry.commit();
            return true;
        } catch (RuntimeException re) {
            // No hacemos commit, el journal queda PENDIENTE (o podemos abortar, pero PENDIENTE es mejor para mostrar la recuperación)
            System.err.println("ERROR: Operación interrumpida. El Journal contiene una transacción PENDIENTE.");
            return false;
        } finally {
            LockManager.releaseLock(name);
            crashRequested = false;
        }
    }

    public synchronized void deleteResource(FileSystemItem item, VDirectory parent) {
        if (!isAdmin) return;
        
        JournalEntry entry = journal.logStart("DELETE", parent.getName() + item.getName());
        if (!LockManager.acquireLock(item.getName(), true)) {
            entry.abort();
            return;
        }

        try {
            if (item.isDirectory()) {
                VDirectory dir = (VDirectory) item;
                CustomLinkedList<FileSystemItem> children = new CustomLinkedList<>();
                for (FileSystemItem child : dir.getChildren()) children.add(child);
                for (FileSystemItem child : children) deleteResource(child, dir);
                parent.removeChild(dir);
            } else {
                deleteFile((VFile) item, parent);
            }
            entry.commit();
        } catch (Exception e) {
            entry.abort();
            throw e;
        } finally {
            LockManager.releaseLock(item.getName());
        }
    }

    private void deleteFile(VFile file, VDirectory parent) {
        for (Integer blockId : file.getAssignedBlocks()) {
            disk.freeBlock(blockId);
        }
        parent.removeChild(file);
    }

    public void switchMode(boolean admin) { this.isAdmin = admin; }
    public boolean isAdmin() { return isAdmin; }
    public VDirectory getRoot() { return root; }
    public SimulatedDisk getDisk() { return disk; }
    public JournalManager getJournal() { return journal; }
}
