package com.vfs.scheduling;

import com.vfs.structures.CustomLinkedList;

public class FIFOScheduler implements DiskScheduler {
    @Override
    public CustomLinkedList<Integer> schedule(int currentHead, CustomLinkedList<Integer> requests, int maxBlock) {
        return requests; // FIFO simplemente devuelve el orden de llegada
    }
}
