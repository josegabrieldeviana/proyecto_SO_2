package com.vfs.scheduling;

import com.vfs.structures.CustomQueue;
import com.vfs.structures.CustomLinkedList;

public class IOQueueManager {
    private CustomQueue<IORequest> requestQueue = new CustomQueue<>();
    private DiskScheduler scheduler;
    private int currentHeadPosition = 0;

    public IOQueueManager(DiskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public synchronized void enqueueRequest(IORequest request) {
        requestQueue.enqueue(request);
    }

    public synchronized CustomLinkedList<IORequest> processBatch(int maxBlocks) {
        if (requestQueue.isEmpty()) return new CustomLinkedList<>();

        CustomLinkedList<Integer> blockRequests = new CustomLinkedList<>();
        CustomLinkedList<IORequest> batch = new CustomLinkedList<>();

        while (!requestQueue.isEmpty()) {
            IORequest req = requestQueue.dequeue();
            blockRequests.add(req.getBlockId());
            batch.add(req);
        }

        // Aplicar Algoritmo de Planificación
        CustomLinkedList<Integer> scheduledBlocks = scheduler.schedule(currentHeadPosition, blockRequests, maxBlocks);
        
        // Actualizar la cabeza del disco al último bloque procesado
        if (scheduledBlocks.size() > 0) {
            currentHeadPosition = scheduledBlocks.get(scheduledBlocks.size() - 1);
        }

        // Devolver las solicitudes en el orden planificado
        CustomLinkedList<IORequest> sortedBatch = new CustomLinkedList<>();
        for (Integer blockId : scheduledBlocks) {
            for (IORequest req : batch) {
                if (req.getBlockId() == blockId) {
                    sortedBatch.add(req);
                    break;
                }
            }
        }
        return sortedBatch;
    }
}
