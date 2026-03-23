package com.vfs.process;

import com.vfs.structures.CustomLinkedList;

public class VProcess {
    private int id;
    private String owner;
    private ProcessState state;
    private CustomLinkedList<String> tasks; // Operaciones a realizar: "CREATE file", "READ file", etc.
    private int currentTaskIndex;

    public VProcess(int id, String owner) {
        this.id = id;
        this.owner = owner;
        this.state = ProcessState.NEW;
        this.tasks = new CustomLinkedList<>();
        this.currentTaskIndex = 0;
    }

    public void addTask(String task) { tasks.add(task); }
    public int getId() { return id; }
    public String getOwner() { return owner; }
    public ProcessState getState() { return state; }
    public void setState(ProcessState state) { this.state = state; }
    public CustomLinkedList<String> getTasks() { return tasks; }
}
