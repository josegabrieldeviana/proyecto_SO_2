package com.vfs.journaling;

import com.vfs.structures.CustomLinkedList;

public class Journal {
    private CustomLinkedList<JournalEntry> entries = new CustomLinkedList<>();
    private int nextId = 1;

    public synchronized JournalEntry logOperation(String operation, String path) {
        JournalEntry entry = new JournalEntry(nextId++, operation, path);
        entries.add(entry);
        return entry;
    }

    public CustomLinkedList<JournalEntry> getEntries() { return entries; }
}
