package com.vfs.scheduling;

import com.vfs.structures.CustomLinkedList;

public interface DiskScheduler {
    CustomLinkedList<Integer> schedule(int currentHead, CustomLinkedList<Integer> requests, int maxBlock);
}
