package com.vfs.process;

import com.vfs.structures.CustomQueue;
import com.vfs.structures.CustomLinkedList;

public class ProcessManager {
    private CustomQueue<VProcess> newQueue = new CustomQueue<>();
    private CustomQueue<VProcess> readyQueue = new CustomQueue<>();
    private CustomLinkedList<VProcess> blockedList = new CustomLinkedList<>();
    private CustomLinkedList<VProcess> terminatedList = new CustomLinkedList<>();
    private VProcess runningProcess;

    public synchronized void addProcess(VProcess process) {
        process.setState(ProcessState.NEW);
        newQueue.enqueue(process);
    }

    public synchronized void admitProcesses() {
        while (!newQueue.isEmpty()) {
            VProcess p = newQueue.dequeue();
            p.setState(ProcessState.READY);
            readyQueue.enqueue(p);
        }
    }

    public synchronized VProcess getNextReady() {
        if (readyQueue.isEmpty()) return null;
        runningProcess = readyQueue.dequeue();
        runningProcess.setState(ProcessState.RUNNING);
        return runningProcess;
    }

    public synchronized void blockProcess(VProcess p) {
        p.setState(ProcessState.BLOCKED);
        blockedList.add(p);
        runningProcess = null;
    }

    public synchronized void terminateProcess(VProcess p) {
        p.setState(ProcessState.TERMINATED);
        terminatedList.add(p);
        runningProcess = null;
    }

    public CustomQueue<VProcess> getReadyQueue() { return readyQueue; }
    public CustomLinkedList<VProcess> getBlockedList() { return blockedList; }
    public CustomLinkedList<VProcess> getTerminatedList() { return terminatedList; }
    public VProcess getRunningProcess() { return runningProcess; }
}
