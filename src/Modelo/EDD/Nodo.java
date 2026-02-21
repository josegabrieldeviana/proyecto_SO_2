/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.EDD;

/**
 *
 * @author JGDV
 * @param <T>
 */
public class Nodo<T> {
    public T data;
    public Nodo next;

    /*
    Se añaden atributos para que pueda funcionar esta clase con el método de lista simple.
    */
    
    Nodo first;
    Nodo last;
    boolean visited;
    
    /**
     * @param dataL
    */
    public Nodo(T dataL) {
        this.data = dataL;
        this.next = null;
        this.visited=false;
        this.first=first;
        this.last=last;
    }

    public T getData() {
        return data;
    } //hola

    public Nodo<T> getNext() {
        return next;
    }

    /**
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     *
     * @param next
     */
    public void setNext(Nodo next) {
        this.next = next;
    }

    
}