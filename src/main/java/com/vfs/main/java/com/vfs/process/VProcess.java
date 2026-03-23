package com.vfs.process;

import com.vfs.core.FileSystemManager;
import com.vfs.core.VDirectory;
import com.vfs.structures.CustomLinkedList;
import java.util.concurrent.Semaphore;

public class VProcess implements Runnable {
    private int id;
    private String owner;
    private ProcessState state;
    private CustomLinkedList<String> tasks; 
    private int currentTaskIndex;
    private static Semaphore diskSemaphore = new Semaphore(1); // Un solo proceso a la vez en el disco

    public VProcess(int id, String owner) {
        this.id = id;
        this.owner = owner;
        this.state = ProcessState.NEW;
        this.tasks = new CustomLinkedList<>();
        this.currentTaskIndex = 0;
    }

    public void addTask(String task) { tasks.add(task); }

    @Override
    public void run() {
        try {
            this.state = ProcessState.RUNNING;
            System.out.println("Proceso " + id + " iniciando ejecución...");
            
            for (String task : tasks) {
                this.state = ProcessState.BLOCKED; // Bloqueado esperando E/S
                diskSemaphore.acquire();
                this.state = ProcessState.RUNNING;
                
                System.out.println("Proceso " + id + " ejecutando: " + task);
                executeTask(task);
                
                Thread.sleep(1000); // Simulando tiempo de procesamiento
                diskSemaphore.release();
            }
            
            this.state = ProcessState.TERMINATED;
            System.out.println("Proceso " + id + " terminado.");
        } catch (InterruptedException e) {
            this.state = ProcessState.TERMINATED;
            Thread.currentThread().interrupt();
        }
    }

    private void executeTask(String task) {
        FileSystemManager fsm = FileSystemManager.getInstance();
        String[] parts = task.split(" ");
        String op = parts[0].toUpperCase();
        
        if (op.equals("CREATE")) {
            String name = parts[1];
            int blocks = Integer.parseInt(parts[2]);
            fsm.createFile(name, blocks, fsm.getRoot());
        }
        // ... otras operaciones
    }

    public int getId() { return id; }
    public String getOwner() { return owner; }
    public ProcessState getState() { return state; }
    public void setState(ProcessState state) { this.state = state; }
    public CustomLinkedList<String> getTasks() { return tasks; }
}
