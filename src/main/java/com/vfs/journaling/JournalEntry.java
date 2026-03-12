package com.vfs.journaling;

import java.util.Date;

public class JournalEntry {
    private int transactionId;
    private String operation; // "CREATE", "DELETE"
    private String targetPath;
    private TransactionStatus status;
    private Date timestamp;

    public JournalEntry(int id, String op, String path) {
        this.transactionId = id;
        this.operation = op;
        this.targetPath = path;
        this.status = TransactionStatus.PENDING;
        this.timestamp = new Date();
    }

    public void commit() { this.status = TransactionStatus.COMMITTED; }
    public void abort() { this.status = TransactionStatus.ABORTED; }
    public TransactionStatus getStatus() { return status; }
    public String getOperation() { return operation; }
    public String getTargetPath() { return targetPath; }
}
