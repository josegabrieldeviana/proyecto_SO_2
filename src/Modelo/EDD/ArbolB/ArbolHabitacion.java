/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.hotel;
import java.util.*;
/**
 *
 * @author alexp
 */
public class ArbolHabitacion {
    private NodoHabitacion Root;

    public ArbolHabitacion() {
        this.Root = null;
    }

    public NodoHabitacion getRoot() {
        return Root;
    }

    public void setRoot(NodoHabitacion Root) {
        this.Root = Root;
    }
    
    public boolean esVacio(){
        return  getRoot()==null;
        
    }
    
    
    public void insertar(Habitacion data){
        NodoHabitacion newNodo=new NodoHabitacion(data);
        if(esVacio()){
            this.Root=newNodo;
        }else{
            insertar(Root, data);
        }
    }
    public void insertar(NodoHabitacion Root, Habitacion data){
        NodoHabitacion newNodo = new NodoHabitacion(data);
        
        if(esVacio()){
            this.Root=newNodo;
        }
        else{
            if(Root.getNroHab().compareTo(newNodo.getNroHab())>0){
                if(Root.getHijoIzq()==null){
                    Root.setHijoIzq(newNodo);
                }else{
                    insertar(Root.getHijoIzq(),data);
                }
                
            }else if(Root.getNroHab().compareTo(newNodo.getNroHab())<0){
                if(Root.getHijoDer()==null){
                    Root.setHijoDer(newNodo);
                }else{
                    insertar(Root.getHijoDer(),data);
                }
            }else{
                 Root.getLista().InsertarFinal(data);   
            }
        }
    }
    
    
    
//    
//    public int altura(NodoHabitacion node) {
//        if (node == null) {
//            return 0;
//        } else {
//            return Math.max(altura(node.getHijoIzq()), altura(node.getHijoDer())) + 1;
//        }
//    }
//  
//    public NodoHabitacion rotarDerecha(NodoHabitacion y) {
//        NodoHabitacion x = y.getHijoIzq();
//        NodoHabitacion T2 = x.getHijoDer();
//        x.setHijoDer(y);
//        y.setHijoIzq(T2);
//
//        y.setAltura(Math.max(altura(y.getHijoIzq()), altura(y.getHijoDer())) + 1);
//        x.setAltura(Math.max(altura(x.getHijoIzq()), altura(x.getHijoDer())) + 1);
//        return x;
//    }
//     public NodoHabitacion rotarIzquierda(NodoHabitacion x) {
//        NodoHabitacion y = x.getHijoIzq();
//        NodoHabitacion T2 = y.getHijoDer();
//        y.setHijoIzq(x);
//        x.setHijoDer(T2);
//        x.setAltura(Math.max(altura(x.getHijoIzq()), altura(x.getHijoDer())) + 1);
//        y.setAltura(Math.max(altura(x.getHijoIzq()), altura(x.getHijoDer())) + 1);
//        return y;
//    }
//    
//    public boolean isBalanced(NodoHabitacion node) {
//        if (node == null) {
//            return true;
//        }
//
//        int leftHeight = altura(node.getHijoIzq());
//        int rightHeight = altura(node.getHijoDer());
//
//        return Math.abs(leftHeight - rightHeight) <= 1 
//            && isBalanced(node.getHijoIzq())
//            && isBalanced(node.getHijoDer());
//    }
//    
    
    
    public void imprimir(NodoHabitacion nodo){
        if(nodo!=null){
            imprimir(nodo.getHijoIzq()); // Visita el hijo izquierdo
            nodo.getLista().imprimir(); // Imprime el valor del nodo
            imprimir(nodo.getHijoDer()); // Visita el hijo derecho
        }
    }
    
    
    public NodoHabitacion buscar(String data){
        NodoHabitacion aux = this.Root;

        if (aux==null){ 
            return null;
        }else{
            NodoHabitacion info = buscador(aux, data);
            return info;
        }
        
    }
    
    public NodoHabitacion buscador(NodoHabitacion aux, String data){
        
        if(aux==null) return null;
        
        if(aux.getNroHab().equals(data)){
            return aux;
        }else if(aux.getNroHab().compareTo(data) > 0){
            return buscador(aux.getHijoIzq(), data);
        }else{
            return buscador(aux.getHijoDer(),data);
        }
    }
    
    
}
