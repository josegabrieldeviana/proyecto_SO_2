package com.vfs.persistence;

import com.vfs.core.FileSystemItem;
import com.vfs.core.FileSystemManager;
import com.vfs.core.VDirectory;
import com.vfs.core.VFile;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class PersistenceManager {

    public static void saveState(String filePath) {
        FileSystemManager fsm = FileSystemManager.getInstance();
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"root\": ");
        serializeItem(fsm.getRoot(), json, 1);
        json.append("\n}");

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadState(String filePath) {
        System.out.println("Cargando estado desde " + filePath + "...");
        // Para una implementación real, se recomienda usar una librería como org.json o GSON
        // tal como permite el lineamiento técnico Nº 2.
        // Aquí se muestra un ejemplo de cómo se estructuraría la carga.
        /*
        try (FileReader reader = new FileReader(filePath)) {
            // Lógica de parseo JSON aquí
            // Reconstruir el árbol de directorios (root)
            // Reasignar los bloques en el SimulatedDisk
            System.out.println("Estado cargado con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    private static void serializeItem(FileSystemItem item, StringBuilder json, int indent) {
        String pad = "  ".repeat(indent);
        json.append("{\n");
        json.append(pad).append("  \"name\": \"").append(item.getName()).append("\",\n");
        json.append(pad).append("  \"owner\": \"").append(item.getOwner()).append("\",\n");
        json.append(pad).append("  \"isDirectory\": ").append(item.isDirectory()).append(",\n");
        
        if (item.isDirectory()) {
            VDirectory dir = (VDirectory) item;
            json.append(pad).append("  \"children\": [\n");
            int i = 0;
            for (FileSystemItem child : dir.getChildren()) {
                if (i > 0) json.append(",\n");
                json.append(pad).append("    ");
                serializeItem(child, json, indent + 2);
                i++;
            }
            json.append("\n").append(pad).append("  ]\n");
        } else {
            VFile file = (VFile) item;
            json.append(pad).append("  \"numBlocks\": ").append(file.getNumBlocks()).append(",\n");
            json.append(pad).append("  \"startBlock\": ").append(file.getStartBlockId()).append("\n");
        }
        json.append(pad).append("}");
    }

    // Nota: La carga (Load) requeriría un parser manual de JSON 
    // Debido a la restricción de no usar librerías ni colecciones de Java, 
    // se implementará un guardado robusto y una carga simplificada.
}
