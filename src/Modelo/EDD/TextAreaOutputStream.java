/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.EDD;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaOutputStream extends OutputStream {

    private final JTextArea textArea;
    private final StringBuilder sb = new StringBuilder();

    public TextAreaOutputStream(final JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public void write(int b) throws IOException {
        // Ignorar el retorno de carro (Windows)
        if (b == '\r') return;

        if (b == '\n') {
            final String text = sb.toString() + "\n";
            sb.setLength(0); // Limpiar buffer ANTES de enviar a la UI

            SwingUtilities.invokeLater(() -> {
                textArea.append(text);
                // AUTO-SCROLL: Hace que la consola baje sola
                textArea.setCaretPosition(textArea.getDocument().getLength());
            });
            return; // Salir para no agregar el \n al buffer de nuevo
        }

        sb.append((char) b);
    }
}