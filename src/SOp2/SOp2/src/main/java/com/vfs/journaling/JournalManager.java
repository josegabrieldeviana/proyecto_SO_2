package com.vfs.journaling;

import com.vfs.structures.CustomLinkedList;
import com.vfs.core.FileSystemManager;

public class JournalManager {
    private CustomLinkedList<JournalEntry> logs = new CustomLinkedList<>();
    private int nextId = 1;
    
    
    
    public synchronized JournalEntry logStart(String operation, String path) {
        JournalEntry entry = new JournalEntry(nextId++, operation, path); //cada log con su id operación y entru
        logs.add(entry); //en los logs voy a poner el entry
        return entry; //regreso el entry
    }

    public synchronized void simulateCrash() {
        System.out.println("!!! CRASH SIMULADO !!! Iniciando recuperación...");
        recover();
    }

    private void recover() {
        for (JournalEntry entry : logs) {
            if (entry.getStatus() == TransactionStatus.PENDING) {
                System.out.println("UNDO: Revirtiendo operación incompleta -> " + entry.getOperation() + " en " + entry.getTargetPath());
                entry.abort(); //el abort cambia de pending a aborted, esto puede puede ser con los ENUM
                // Aquí se llamaría a la lógica específica para liberar bloques o eliminar nodos huérfanos
            }
        }
    }

    public CustomLinkedList<JournalEntry> getLogs() { return logs; }
}
