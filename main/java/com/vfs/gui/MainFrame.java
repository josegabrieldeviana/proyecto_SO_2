package com.vfs.gui;

import com.vfs.core.*;
import com.vfs.scheduling.*;
import com.vfs.process.LockManager;
import com.vfs.journaling.JournalEntry;
import com.vfs.structures.CustomLinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class MainFrame extends JFrame {
    private FileSystemManager fsm;
    private JTree fileTree;
    private DiskVisualizer diskVisualizer;
    private JTextArea logArea;
    private AllocationTableModel tableModel;
    private JButton btnCreate, btnDelete;
    private JCheckBox chkAdmin;
    private VDirectory currentDirectory;

    // Scheduler UI Components
    private JTextField txtInitialHead, txtReqNum, txtReqBlock;
    private DefaultTableModel ioTableModel;
    private HeadVisualizer headVisualizer;
    private CustomLinkedList<IOReq> ioQueue;
    private DiskScheduler currentPolicy;
    private Timer animationTimer;

    // System Status
    private JTextArea lockArea;

    public MainFrame() {
        this.fsm = FileSystemManager.getInstance();
        this.currentDirectory = fsm.getRoot();
        this.ioQueue = new CustomLinkedList<>();
        initUI();
    }

    private void initUI() {
        setTitle("Simulador VFS Interactivo - Sistemas Operativos");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Pestaña de Sistema de Archivos
        tabbedPane.addTab("Sistema de Archivos", createFileSystemPanel());

        // 2. Pestaña de Planificador de Disco
        tabbedPane.addTab("Planificador de Disco", createSchedulerPanel());

        // 3. Pestaña de Estado del Sistema
        tabbedPane.addTab("Estado del Sistema", createStatusPanel());

        add(tabbedPane);
        refreshUI();
        refreshStatus();
    }

    private JPanel createFileSystemPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // --- PANEL SUPERIOR: Roles ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chkAdmin = new JCheckBox("Modo Administrador", true);
        chkAdmin.addActionListener(e -> switchMode(chkAdmin.isSelected()));
        topPanel.add(chkAdmin);
        panel.add(topPanel, BorderLayout.NORTH);

        // --- PANEL IZQUIERDO: Árbol ---
        fileTree = new JTree();
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node == null) return;
            
            Object userObj = node.getUserObject();
            if (userObj instanceof VDirectory) {
                currentDirectory = (VDirectory) userObj;
                updateTable(currentDirectory);
            } else if (userObj instanceof VFile) {
                CustomLinkedList<VFile> singleFileList = new CustomLinkedList<>();
                singleFileList.add((VFile) userObj);
                tableModel.setFiles(singleFileList);
            }
        });
        panel.add(new JScrollPane(fileTree), BorderLayout.WEST);

        // --- PANEL CENTRAL: Disco y Tabla ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        diskVisualizer = new DiskVisualizer(fsm.getDisk());
        centerPanel.add(new JScrollPane(diskVisualizer));
        
        tableModel = new AllocationTableModel();
        centerPanel.add(new JScrollPane(new JTable(tableModel)));
        panel.add(centerPanel, BorderLayout.CENTER);

        // --- PANEL DERECHO: Consola ---
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(new Color(0, 255, 0));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(logArea), BorderLayout.EAST);

        // --- PANEL INFERIOR: Acciones ---
        JPanel actionPanel = new JPanel();
        btnCreate = new JButton("Crear Archivo");
        btnCreate.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nombre del archivo:");
            String blocksStr = JOptionPane.showInputDialog("Cantidad de Bloques:");
            if (name != null && blocksStr != null) {
                try {
                    int blocks = Integer.parseInt(blocksStr);
                    if (fsm.getDisk().getFreeBlockCount() < blocks) {
                        JOptionPane.showMessageDialog(this, "WARNING: No hay suficientes bloques disponibles. Solicitados: " + blocks + ", Libres: " + fsm.getDisk().getFreeBlockCount(), "Espacio Insuficiente", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    logArea.append("\n-> PENDIENTE: Crear archivo '" + name + "'\n");
                    if (fsm.createFile(name, blocks, currentDirectory)) {
                        logArea.append("-> CONFIRMADO: Creado '" + name + "' en " + currentDirectory.getName() + "\n");
                        refreshUI();
                        refreshStatus();
                    } else {
                        logArea.append("-> UNDO: Fallo en la creación (Permisos o espacio).\n");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad de bloques inválida.");
                }
            }
        });

        btnDelete = new JButton("Eliminar Seleccionado");
        btnDelete.addActionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof FileSystemItem) {
                FileSystemItem item = (FileSystemItem) node.getUserObject();
                if (item == fsm.getRoot()) {
                    JOptionPane.showMessageDialog(this, "No se puede eliminar la raíz.");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + item.getName() + "'?");
                if (confirm == JOptionPane.YES_OPTION) {
                    VDirectory parent = findParent(fsm.getRoot(), item);
                    if (parent != null) {
                        logArea.append("\n-> PENDIENTE: Eliminar '" + item.getName() + "'\n");
                        fsm.deleteResource(item, parent);
                        logArea.append("-> CONFIRMADO: Eliminado '" + item.getName() + "'\n");
                        refreshUI();
                        refreshStatus();
                    }
                }
            }
        });

        JButton btnCrash = new JButton("Simular Crash");
        btnCrash.setBackground(new Color(200, 50, 50));
        btnCrash.setForeground(Color.WHITE);
        btnCrash.addActionListener(e -> {
            logArea.append("\n[SISTEMA] !!! CRASH DETECTADO !!!\n");
            fsm.getJournal().simulateCrash();
            logArea.append("[SISTEMA] Recuperación completada. Revisa la consola.\n");
            refreshUI();
            refreshStatus();
        });

        actionPanel.add(btnCreate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnCrash);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSchedulerPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // --- Inputs Norte ---
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Cabezal Inicial (0-199):"));
        txtInitialHead = new JTextField("50", 4);
        inputPanel.add(txtInitialHead);

        inputPanel.add(new JLabel(" | Nº Solicitud:"));
        txtReqNum = new JTextField("1", 3);
        inputPanel.add(txtReqNum);

        inputPanel.add(new JLabel("Bloque Operación:"));
        txtReqBlock = new JTextField("", 4);
        inputPanel.add(txtReqBlock);

        JButton btnAddReq = new JButton("Añadir Solicitud");
        btnAddReq.addActionListener(e -> {
            try {
                int reqNum = Integer.parseInt(txtReqNum.getText());
                int block = Integer.parseInt(txtReqBlock.getText());
                if (block < 0 || block > 199) {
                    JOptionPane.showMessageDialog(this, "El bloque debe estar entre 0 y 199");
                    return;
                }
                ioQueue.add(new IOReq(reqNum, block, "Pendiente"));
                txtReqNum.setText(String.valueOf(reqNum + 1));
                txtReqBlock.setText("");
                refreshIOTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Valores inválidos");
            }
        });
        inputPanel.add(btnAddReq);

        JButton btnClearReqs = new JButton("Limpiar Cola");
        btnClearReqs.addActionListener(e -> {
            ioQueue = new CustomLinkedList<>();
            refreshIOTable();
            headVisualizer.resetPath();
        });
        inputPanel.add(btnClearReqs);

        panel.add(inputPanel, BorderLayout.NORTH);

        // --- Centro: Cola y Animación ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        
        // Tabla de Cola
        ioTableModel = new DefaultTableModel(new String[]{"Nº Solicitud", "Bloque Destino", "Estado"}, 0);
        JTable ioTable = new JTable(ioTableModel);
        centerPanel.add(new JScrollPane(ioTable));

        // Animador
        headVisualizer = new HeadVisualizer();
        centerPanel.add(headVisualizer);

        panel.add(centerPanel, BorderLayout.CENTER);

        // --- Sur: Políticas y Simulación ---
        JPanel southPanel = new JPanel();
        JButton btnFIFO = new JButton("FIFO");
        JButton btnSSTF = new JButton("SSTF");
        JButton btnSCAN = new JButton("SCAN");
        JButton btnCSCAN = new JButton("C-SCAN");

        btnFIFO.addActionListener(e -> { currentPolicy = new FIFOScheduler(); prepareSimulation("FIFO"); });
        btnSSTF.addActionListener(e -> { currentPolicy = new SSTFScheduler(); prepareSimulation("SSTF"); });
        btnSCAN.addActionListener(e -> { currentPolicy = new SCANScheduler(); prepareSimulation("SCAN"); });
        btnCSCAN.addActionListener(e -> { currentPolicy = new CSCANScheduler(); prepareSimulation("C-SCAN"); });

        JButton btnStart = new JButton("Iniciar Simulación");
        btnStart.setBackground(Color.GREEN);
        btnStart.addActionListener(e -> startSimulation());

        southPanel.add(btnFIFO);
        southPanel.add(btnSSTF);
        southPanel.add(btnSCAN);
        southPanel.add(btnCSCAN);
        southPanel.add(new JLabel("   ||   "));
        southPanel.add(btnStart);

        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        lockArea = new JTextArea();
        lockArea.setEditable(false);
        lockArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JButton btnRefresh = new JButton("Actualizar Estado");
        btnRefresh.addActionListener(e -> refreshStatus());

        panel.add(new JLabel("  Locks Activos en el Sistema:"), BorderLayout.NORTH);
        panel.add(new JScrollPane(lockArea), BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.SOUTH);

        return panel;
    }

    // --- Lógica del Planificador ---
    class IOReq {
        int id;
        int block;
        String status;
        public IOReq(int id, int block, String status) {
            this.id = id;
            this.block = block;
            this.status = status;
        }
    }

    private void refreshIOTable() {
        ioTableModel.setRowCount(0);
        for (IOReq req : ioQueue) {
            ioTableModel.addRow(new Object[]{req.id, req.block, req.status});
        }
    }

    private void prepareSimulation(String policyName) {
        if (ioQueue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cola de E/S está vacía.");
            return;
        }
        try {
            int head = Integer.parseInt(txtInitialHead.getText());
            CustomLinkedList<Integer> reqs = new CustomLinkedList<>();
            for (IOReq req : ioQueue) reqs.add(req.block);
            
            CustomLinkedList<Integer> path = currentPolicy.schedule(head, reqs, 200);
            headVisualizer.setupPath(head, path);
            
            for (IOReq req : ioQueue) req.status = "Pendiente";
            refreshIOTable();
            
            JOptionPane.showMessageDialog(this, "Política " + policyName + " aplicada. Listo para simular.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cabezal inicial inválido.");
        }
    }

    private void startSimulation() {
        if (headVisualizer.path.isEmpty() && headVisualizer.targetHead == headVisualizer.currentHead) {
            JOptionPane.showMessageDialog(this, "No hay ruta programada o ya finalizó.");
            return;
        }

        if (animationTimer != null && animationTimer.isRunning()) return;

        animationTimer = new Timer(30, e -> {
            boolean moved = false;
            if (headVisualizer.currentHead < headVisualizer.targetHead) {
                headVisualizer.currentHead++;
                moved = true;
            } else if (headVisualizer.currentHead > headVisualizer.targetHead) {
                headVisualizer.currentHead--;
                moved = true;
            }

            if (!moved) {
                // Llegamos al destino
                updateReqStatus(headVisualizer.targetHead, "Completado");
                
                if (headVisualizer.pathIterator != null && headVisualizer.pathIterator.hasNext()) {
                    headVisualizer.targetHead = headVisualizer.pathIterator.next();
                    updateReqStatus(headVisualizer.targetHead, "Procesando");
                } else {
                    animationTimer.stop();
                    JOptionPane.showMessageDialog(this, "Simulación finalizada.");
                }
            }
            headVisualizer.repaint();
        });
        
        updateReqStatus(headVisualizer.targetHead, "Procesando");
        animationTimer.start();
    }

    private void updateReqStatus(int blockDest, String newStatus) {
        for (IOReq req : ioQueue) {
            if (req.block == blockDest && req.status.equals(newStatus.equals("Completado") ? "Procesando" : "Pendiente")) {
                req.status = newStatus;
                break;
            }
        }
        refreshIOTable();
    }

    // --- Animación Customizada ---
    class HeadVisualizer extends JPanel {
        int currentHead = 50;
        int targetHead = 50;
        CustomLinkedList<Integer> path = new CustomLinkedList<>();
        Iterator<Integer> pathIterator;

        public void setupPath(int initial, CustomLinkedList<Integer> newPath) {
            this.currentHead = initial;
            this.path = newPath;
            this.pathIterator = path.iterator();
            if (pathIterator.hasNext()) {
                this.targetHead = pathIterator.next();
            } else {
                this.targetHead = initial;
            }
            repaint();
        }

        public void resetPath() {
            path = new CustomLinkedList<>();
            pathIterator = null;
            targetHead = currentHead;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();
            
            // Background
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, w, h);

            // Track line (0 to 199)
            g.setColor(Color.DARK_GRAY);
            int trackY = h / 2;
            int margin = 30;
            int trackWidth = w - (margin * 2);
            g.fillRect(margin, trackY - 5, trackWidth, 10);
            
            // Markers
            g.setColor(Color.BLACK);
            g.drawString("0", margin - 10, trackY + 25);
            g.drawString("199", w - margin - 15, trackY + 25);
            
            // Draw Target points
            g.setColor(Color.BLUE);
            for (Integer p : path) {
                int px = margin + (int)((p / 199.0) * trackWidth);
                g.fillOval(px - 3, trackY - 10, 6, 20);
            }

            // Draw Head
            int headX = margin + (int)((currentHead / 199.0) * trackWidth);
            g.setColor(Color.RED);
            g.fillOval(headX - 8, trackY - 15, 16, 30);
            
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Cabezal Actual: " + currentHead, headX - 30, trackY - 25);
        }
    }

    // --- Funciones auxiliares heredadas ---
    private VDirectory findParent(VDirectory current, FileSystemItem target) {
        for (FileSystemItem child : current.getChildren()) {
            if (child == target) return current;
            if (child.isDirectory()) {
                VDirectory found = findParent((VDirectory) child, target);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void switchMode(boolean admin) {
        fsm.switchMode(admin);
        btnCreate.setEnabled(admin);
        btnDelete.setEnabled(admin);
        logArea.append("[SISTEMA] Cambio a modo: " + (admin ? "ADMINISTRADOR" : "USUARIO") + "\n");
    }

    private void refreshUI() {
        DefaultMutableTreeNode rootNode = createTreeNodes(fsm.getRoot());
        fileTree.setModel(new DefaultTreeModel(rootNode));
        for (int i = 0; i < fileTree.getRowCount(); i++) fileTree.expandRow(i);
        diskVisualizer.updateDisk();
        updateTable(currentDirectory);
    }

    private void refreshStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LOCKS DE ARCHIVOS ===\n");
        CustomLinkedList<LockManager.FileLock> locks = LockManager.getLocks();
        if (locks.isEmpty()) {
            sb.append("No hay locks activos en este momento.\n");
        } else {
            for (LockManager.FileLock lock : locks) {
                sb.append("- ").append(lock.toString()).append("\n");
            }
        }
        sb.append("\n=== ÚLTIMAS ENTRADAS DEL JOURNAL ===\n");
        CustomLinkedList<JournalEntry> logs = fsm.getJournal().getLogs();
        int count = 0;
        // Mostramos las últimas 10 (recorriendo desde el final de manera simplificada)
        for (JournalEntry entry : logs) {
            sb.append("[").append(entry.getStatus()).append("] ").append(entry.getOperation()).append(" -> ").append(entry.getTargetPath()).append("\n");
        }
        
        lockArea.setText(sb.toString());
    }

    private void updateTable(VDirectory dir) {
        CustomLinkedList<VFile> files = new CustomLinkedList<>();
        if (dir == fsm.getRoot()) {
            collectAllFiles(fsm.getRoot(), files);
        } else {
            for (FileSystemItem item : dir.getChildren()) {
                if (!item.isDirectory()) files.add((VFile) item);
            }
        }
        tableModel.setFiles(files);
    }

    private void collectAllFiles(VDirectory dir, CustomLinkedList<VFile> list) {
        for (FileSystemItem item : dir.getChildren()) {
            if (item.isDirectory()) collectAllFiles((VDirectory) item, list);
            else list.add((VFile) item);
        }
    }

    private DefaultMutableTreeNode createTreeNodes(FileSystemItem item) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(item) {
            @Override
            public String toString() {
                FileSystemItem item = (FileSystemItem) getUserObject();
                return item.getName() + (item.isDirectory() ? "/" : "");
            }
        };

        if (item.isDirectory()) {
            for (FileSystemItem child : ((VDirectory) item).getChildren()) {
                node.add(createTreeNodes(child));
            }
        }
        return node;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}