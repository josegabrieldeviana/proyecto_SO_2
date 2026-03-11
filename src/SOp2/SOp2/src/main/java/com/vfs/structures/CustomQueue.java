package com.vfs.structures;

public class CustomQueue<T> {
    private CustomLinkedList<T> list = new CustomLinkedList<>();

    public void enqueue(T data) {
        list.add(data);
    }

    public T dequeue() {
        if (isEmpty()) return null;
        T data = list.get(0);
        list.remove(data);
        return data;
    }

    public T peek() {
        return isEmpty() ? null : list.get(0);
    }

    public boolean isEmpty() { return list.isEmpty(); }
    public int size() { return list.size(); }
}
