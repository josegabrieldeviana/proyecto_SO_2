package com.vfs.scheduling;

public class IORequest {
    private int blockId;
    private String operation; // READ, WRITE, DELETE
    private int processId;

    public IORequest(int blockId, String op, int pid) {
        this.blockId = blockId;
        this.operation = op;
        this.processId = pid;
    }

    public int getBlockId() { return blockId; }
    public String getOperation() { return operation; }
    public int getProcessId() { return processId; }
}
