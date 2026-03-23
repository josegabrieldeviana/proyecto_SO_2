package com.vfs.journaling;

import com.vfs.structures.CustomLinkedList;
import com.vfs.core.FileSystemManager;
import com.vfs.core.SimulatedDisk;

public class JournalManager {
    private CustomLinkedList<JournalEntry> logs = new CustomLinkedList<>();
    private int nextId = 1;

    public synchronized JournalEntry logStart(String operation, String path) {
        JournalEntry entry = new JournalEntry(nextId++, operation, path);
        logs.add(entry);
        return entry;
    }

    public synchronized void simulateCrash() {
        System.out.println("!!! CRASH SIMULADO !!! Iniciando recuperación...");
        recover();
    }

    private void recover() {
        SimulatedDisk disk = FileSystemManager.getInstance().getDisk();
        for (JournalEntry entry : logs) {
            if (entry.getStatus() == TransactionStatus.PENDIENTE) {
                System.out.println("RECUPERACIÓN: UNDO en '" + entry.getTargetPath() + "' para operación " + entry.getOperation());
                
                // Deshacer cambios en el disco
                for (Integer blockId : entry.getAffectedBlocks()) {
                    disk.freeBlock(blockId);
                    System.out.println("    -> Bloque " + blockId + " liberado.");
                }
                
                entry.abort();
            }
        }
    }

    public CustomLinkedList<JournalEntry> getLogs() { return logs; }
}
