package com.vfs.scheduling;

import com.vfs.structures.CustomLinkedList;

public class SSTFScheduler implements DiskScheduler {
    @Override
    public CustomLinkedList<Integer> schedule(int currentHead, CustomLinkedList<Integer> requests, int maxBlock) {
        CustomLinkedList<Integer> result = new CustomLinkedList<>();
        CustomLinkedList<Integer> remaining = new CustomLinkedList<>();
        for (Integer r : requests) remaining.add(r);

        int current = currentHead;
        while (!remaining.isEmpty()) {
            Integer closest = null;
            int minDistance = Integer.MAX_VALUE;
            for (Integer r : remaining) {
                int dist = Math.abs(r - current);
                if (dist < minDistance) {
                    minDistance = dist;
                    closest = r;
                }
            }
            result.add(closest);
            remaining.remove(closest);
            current = closest;
        }
        return result;
    }
}
