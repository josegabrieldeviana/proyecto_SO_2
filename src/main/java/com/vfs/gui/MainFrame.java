package com.vfs.gui;

import com.vfs.core.*;
import com.vfs.scheduling.*;
import com.vfs.structures.CustomLinkedList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

public class MainFrame extends JFrame {
    private FileSystemManager fsm;
    private JTree fileTree;
    private DiskVisualizer diskVisualizer;
    private JTextArea logArea;
    private AllocationTableModel tableModel;
    private JButton btnCreate, btnDelete, btnVerify;
    private JCheckBox chkAdmin;
    private VDirectory currentDirectory;

    public MainFrame() {
        this.fsm = FileSystemManager.getInstance();
        this.currentDirectory = fsm.getRoot();
        initUI();
    }

    private void initUI() {
        setTitle("Simulador VFS Interactivo - Sistemas Operativos");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR: Roles ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chkAdmin = new JCheckBox("Modo Administrador", true);
        chkAdmin.addActionListener(e -> switchMode(chkAdmin.isSelected()));
        topPanel.add(chkAdmin);
        add(topPanel, BorderLayout.NORTH);

        // --- PANEL IZQUIERDO: Árbol con Listener ---
        fileTree = new JTree();
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (node == null) return;
                
                Object userObj = node.getUserObject();
                if (userObj instanceof VDirectory) {
                    currentDirectory = (VDirectory) userObj;
                    updateTable(currentDirectory);
                } else if (userObj instanceof VFile) {
                    // Si se selecciona un archivo, mostrar solo ese archivo
                    CustomLinkedList<VFile> singleFileList = new CustomLinkedList<>();
                    singleFileList.add((VFile) userObj);
                    tableModel.setFiles(singleFileList);
                }
            }
        });
        add(new JScrollPane(fileTree), BorderLayout.WEST);

        // --- PANEL CENTRAL: Disco y Tabla ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        diskVisualizer = new DiskVisualizer(fsm.getDisk());
        centerPanel.add(new JScrollPane(diskVisualizer));
        
        tableModel = new AllocationTableModel();
        centerPanel.add(new JScrollPane(new JTable(tableModel)));
        add(centerPanel, BorderLayout.CENTER);

        // --- PANEL DERECHO: Consola ---
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(new Color(0, 255, 0));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(logArea), BorderLayout.EAST);

        // --- PANEL INFERIOR: Acciones ---
        JPanel actionPanel = new JPanel();
        btnCreate = new JButton("Crear Archivo");
        btnCreate.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nombre del archivo:");
            String blocksStr = JOptionPane.showInputDialog("Cantidad de Bloques:");
            if (name != null && blocksStr != null) {
                try {
                    int blocks = Integer.parseInt(blocksStr);
                    if (fsm.createFile(name, blocks, currentDirectory)) {
                        logArea.append("[FS] Creado '" + name + "' en " + currentDirectory.getName() + "\n");
                        refreshUI();
                    } else {
                        logArea.append("[ERR] No hay espacio suficiente o permiso denegado.\n");
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
                
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar " + item.getName() + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    // Encontrar el padre en el sistema de archivos
                    VDirectory parent = findParent(fsm.getRoot(), item);
                    if (parent != null) {
                        fsm.deleteResource(item, parent);
                        logArea.append("[DEL] Eliminado: " + item.getName() + "\n");
                        refreshUI();
                    }
                }
            }
        });

        btnVerify = new JButton("Verificar Planificación");
        btnVerify.addActionListener(e -> verifySchedulingWithAnimation());

        JButton btnCrash = new JButton("Simular Crash");
        btnCrash.setBackground(new Color(200, 50, 50));
        btnCrash.setForeground(Color.WHITE);
        btnCrash.addActionListener(e -> {
            logArea.append("\n[SISTEMA] !!! CRASH DETECTADO !!!\n");
            fsm.getJournal().simulateCrash();
            logArea.append("[SISTEMA] Recuperación completada. Revisa la consola.\n");
            refreshUI();
        });

        actionPanel.add(btnCreate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnVerify);
        actionPanel.add(btnCrash);
        add(actionPanel, BorderLayout.SOUTH);

        refreshUI();
    }

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

    private void verifySchedulingWithAnimation() {
        int[] requestsArr = {95, 180, 34, 119, 11, 123, 62, 64};
        CustomLinkedList<Integer> reqs = new CustomLinkedList<>();
        for (int r : requestsArr) reqs.add(r);
        int head = 50;

        logArea.append("\n=== SIMULACIÓN DE CABEZAL ===\n");
        logArea.append("Peticiones: [95, 180, 34, 119, 11, 123, 62, 64], Inicio: " + head + "\n");

        DiskScheduler[] schedulers = {new SSTFScheduler(), new SCANScheduler(), new CSCANScheduler()};
        String[] names = {"SSTF", "SCAN", "C-SCAN"};

        for (int i = 0; i < schedulers.length; i++) {
            logArea.append("\nAlgoritmo: " + names[i] + "\n");
            CustomLinkedList<Integer> result = schedulers[i].schedule(head, reqs, 200);
            int current = head;
            int totalDistance = 0;
            
            for (Integer next : result) {
                int dist = Math.abs(next - current);
                logArea.append("  Cabezal: " + current + " -> " + next + " (Mov: " + dist + ")\n");
                totalDistance += dist;
                current = next;
            }
            logArea.append("TOTAL RECORRIDO: " + totalDistance + " cilindros.\n");
        }
        JOptionPane.showMessageDialog(this, "Verificación completada. Revisa la consola de eventos.");
    }

    private void refreshUI() {
        // Actualizar JTree
        DefaultMutableTreeNode rootNode = createTreeNodes(fsm.getRoot());
        fileTree.setModel(new DefaultTreeModel(rootNode));
        
        // Expandir todo el árbol
        for (int i = 0; i < fileTree.getRowCount(); i++) fileTree.expandRow(i);

        // Actualizar Disco
        diskVisualizer.updateDisk();
        
        // Actualizar Tabla con el directorio actual
        updateTable(currentDirectory);
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
