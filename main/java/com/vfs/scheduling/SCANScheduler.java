package com.vfs.scheduling;

import com.vfs.structures.CustomLinkedList;

public class SCANScheduler implements DiskScheduler {
    @Override
    public CustomLinkedList<Integer> schedule(int currentHead, CustomLinkedList<Integer> requests, int maxBlock) {
        CustomLinkedList<Integer> result = new CustomLinkedList<>();
        CustomLinkedList<Integer> sorted = sort(requests);
        
        // Hacia arriba
        for (Integer r : sorted) {
            if (r >= currentHead) result.add(r);
        }
        // Cambio de dirección
        for (int i = sorted.size() - 1; i >= 0; i--) {
            Integer r = sorted.get(i);
            if (r < currentHead) result.add(r);
        }
        return result;
    }

    private CustomLinkedList<Integer> sort(CustomLinkedList<Integer> list) {
        // Implementación simple de Bubble Sort para no usar colecciones de Java
        Integer[] arr = new Integer[list.size()];
        int i = 0;
        for (Integer val : list) arr[i++] = val;
        
        for (int n = 0; n < arr.length; n++) {
            for (int m = 0; m < arr.length - n - 1; m++) {
                if (arr[m] > arr[m+1]) {
                    int temp = arr[m];
                    arr[m] = arr[m+1];
                    arr[m+1] = temp;
                }
            }
        }
        CustomLinkedList<Integer> sorted = new CustomLinkedList<>();
        for (Integer val : arr) sorted.add(val);
        return sorted;
    }
}
