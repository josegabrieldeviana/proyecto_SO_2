package com.vfs.gui;

import com.vfs.core.SimulatedDisk;
import javax.swing.*;
import java.awt.*;

public class DiskVisualizer extends JPanel {
    private SimulatedDisk disk;

    public DiskVisualizer(SimulatedDisk disk) {
        this.disk = disk;
        this.setPreferredSize(new Dimension(400, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (disk == null) return;

        boolean[] status = disk.getFreeBlocksStatus();
        int cols = 10;
        int blockSize = 30;
        int padding = 5;

        for (int i = 0; i < status.length; i++) {
            int x = (i % cols) * (blockSize + padding) + 10;
            int y = (i / cols) * (blockSize + padding) + 10;

            g.setColor(disk.getBlockColor(i));
            g.fillRect(x, y, blockSize, blockSize);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, blockSize, blockSize);
            g.drawString(String.valueOf(i), x + 2, y + 12);
        }
    }

    public void updateDisk() {
        repaint();
    }
}
