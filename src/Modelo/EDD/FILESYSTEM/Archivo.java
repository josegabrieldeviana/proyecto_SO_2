/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.EDD.FILESYSTEM;
import java.awt.Color;

/**
 *
 * @author joseg
 */
public class Archivo {
    public long dirFirstBlock;
    public int ID;
    public String Nombre; //nombre archivo
    long numBlocks; //dimensión de los bloques que ocupa archivo
    public Color colorBlock; //color designado para los bloques que ocupa el archivo
    
}
