/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.EDD;
import javax.swing.JOptionPane;

/**
 *
 * @author PC
 */
public class Pila<T> {

    private Nodo<T> top;
    private Nodo<T> bottom;
    private int size;

    public Pila() {
        this.top = null;
        this.bottom = null;
        this.size = 0;
    }
    
    public Nodo getTop() {
        return top;
    }

    public void setTop(Nodo top) {
        this.top = top;
    }

    public Nodo getBottom() {
        return bottom;
    }

    public void setBottom(Nodo bottom) {
        this.bottom = bottom;
    }
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public boolean isEmpty(){
        return top == null;
    }
    
    public T peek(){
        if(isEmpty() == true){
            return null;
        }
        else{
            return (T) top.getData();
        }
    }
    

    public void push(T data){
        Nodo newNode = new Nodo(data);
        if(isEmpty() == true){
            top = newNode;
            bottom = newNode;
            size++;
        }
        else{
            top.setNext(newNode);
            top = newNode;
            size++;
        }
    }
    

    public Nodo pop(){
        Nodo pAux = null;
        if(isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Error: pila vacia");
        }
        else{
            Nodo pointer = bottom;
            while(pointer.getNext().getNext() != null){
                pointer = pointer.getNext();
            }
            pAux = top;
            pointer.setNext(null);
            top = pointer;
            size--;
        }
        return pAux;
    }
    
    
    
}

/*
    public Lista relaciones(Persona person){
        Lista relaciones = new Lista();
        String Nombre1 = person.getName();
        for(int x = 0; x < Usuarios.len(); x++){
            Persona persona = Usuarios.get(x);
            String Nombre2 = persona.getName();
            if(Nombre1 != Nombre2){
                relaciones.append(persona);
            }
        }
        return relaciones;      
    }
    
    public void dfs(Persona persona){
        pila pila = new pila();
        Lista conocidos = relaciones(persona);
        
        pila.push(persona);
        persona.setVisited(true);
        
        for(int x = 0; x < Conocidos.len(); x++){
            Persona p = (Persona) conocidos.get(x);
            if(!p.isVisited()){
                dfs(p);
            }
        }
    }*/

