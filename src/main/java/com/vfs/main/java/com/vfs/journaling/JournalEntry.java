package com.vfs.journaling;

import com.vfs.structures.CustomLinkedList;
import java.util.Date;

public class JournalEntry {
    private int transactionId;
    private String operation; // "CREATE", "DELETE"
    private String targetPath;
    private TransactionStatus status;
    private Date timestamp;
    private CustomLinkedList<Integer> affectedBlocks;

    public JournalEntry(int id, String op, String path) {
        this.transactionId = id;
        this.operation = op;
        this.targetPath = path;
        this.status = TransactionStatus.PENDIENTE;
        this.timestamp = new Date();
        this.affectedBlocks = new CustomLinkedList<>();
    }

    public void addAffectedBlock(int blockId) {
        affectedBlocks.add(blockId);
    }

    public CustomLinkedList<Integer> getAffectedBlocks() {
        return affectedBlocks;
    }

    public void commit() { this.status = TransactionStatus.CONFIRMADO; }
    public void abort() { this.status = TransactionStatus.UNDO; }
    
    public TransactionStatus getStatus() { return status; }
    public String getOperation() { return operation; }
    public String getTargetPath() { return targetPath; }
    
    @Override
    public String toString() {
        return "[" + timestamp + "] ID:" + transactionId + " | OP:" + operation + " | STATUS:" + status + " | PATH:" + targetPath;
    }
}
