package com.vfs.process;

import com.vfs.structures.CustomLinkedList;
import com.vfs.structures.Node;

public class LockManager {
    // Lista de archivos bloqueados actualmente
    private static CustomLinkedList<FileLock> locks = new CustomLinkedList<>();

    public static synchronized boolean acquireLock(String fileName, boolean exclusive) {
        for (FileLock lock : locks) {
            if (lock.fileName.equals(fileName)) {
                if (exclusive || lock.isExclusive) return false; // Bloqueo denegado
            }
        }
        locks.add(new FileLock(fileName, exclusive));
        return true;
    }

    public static synchronized void releaseLock(String fileName) {
        FileLock toRemove = null;
        for (FileLock lock : locks) {
            if (lock.fileName.equals(fileName)) {
                toRemove = lock;
                break;
            }
        }
        if (toRemove != null) locks.remove(toRemove);
    }

    private static class FileLock {
        String fileName;
        boolean isExclusive;
        public FileLock(String fileName, boolean isExclusive) {
            this.fileName = fileName;
            this.isExclusive = isExclusive;
        }
    }
}
