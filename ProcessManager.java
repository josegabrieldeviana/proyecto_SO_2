package com.vfs.gui;

import com.vfs.core.SimulatedDisk;
import javax.swing.*;
import java.awt.*;

public class DiskVisualizer extends JPanel {
    private SimulatedDisk disk;

    public DiskVisualizer(SimulatedDisk disk) {
        this.disk = disk;
        this.setPreferredSize(new Dimension(800, 400));
        this.setBackground(new Color(245, 247, 250)); // Fondo moderno y claro
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (disk == null) return;

        Graphics2D g2d = (Graphics2D) g;
        // Activar Anti-aliasing para bordes suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        boolean[] status = disk.getFreeBlocksStatus();
        
        int blockSize = 35;
        int padding = 8;
        int startX = 15;
        int startY = 15;
        
        // Calcular columnas dinámicamente basadas en el ancho del panel
        int panelWidth = getWidth();
        int cols = Math.max(1, (panelWidth - startX * 2) / (blockSize + padding));

        for (int i = 0; i < status.length; i++) {
            int x = startX + (i % cols) * (blockSize + padding);
            int y = startY + (i / cols) * (blockSize + padding);

            Color blockColor = disk.getBlockColor(i);
            
            // Si el bloque está libre (Blanco por defecto), darle un tono más sutil
            if (blockColor.equals(Color.WHITE)) {
                blockColor = new Color(230, 235, 240);
            }

            // Sombra del bloque
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillRoundRect(x + 2, y + 2, blockSize, blockSize, 10, 10);

            // Fondo del bloque
            g2d.setColor(blockColor);
            g2d.fillRoundRect(x, y, blockSize, blockSize, 10, 10);

            // Borde del bloque
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.setStroke(new BasicStroke(1.2f));
            g2d.drawRoundRect(x, y, blockSize, blockSize, 10, 10);

            // Texto (Número de bloque)
            g2d.setColor(status[i] ? new Color(100, 110, 120) : Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            String text = String.valueOf(i);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            
            // Centrar texto exactamente en el medio del bloque
            g2d.drawString(text, x + (blockSize - textWidth) / 2, y + (blockSize + textHeight) / 2 - 2);
        }
        
        // Ajustar altura preferida dinámicamente si hay muchos bloques
        int totalRows = (int) Math.ceil((double) status.length / cols);
        int neededHeight = startY * 2 + totalRows * (blockSize + padding);
        if (getPreferredSize().height != neededHeight) {
            setPreferredSize(new Dimension(getWidth(), neededHeight));
            revalidate();
        }
    }

    public void updateDisk() {
        repaint();
    }
}
