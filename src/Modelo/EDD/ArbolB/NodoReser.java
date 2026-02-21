/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.hotel;

/**
 *
 * @author alexp
 */
public class NodoReser {
    private NodoReser hijoIzq;
    private NodoReser hijoDer;
    private Cliente data;

    public NodoReser(Cliente data) {
        this.hijoIzq = null;
        this.hijoDer = null;
        this.data = data;
    }

    public NodoReser getHijoIzq() {
        return hijoIzq;
    }

    public void setHijoIzq(NodoReser hijoIzq) {
        this.hijoIzq = hijoIzq;
    }

    public NodoReser getHijoDer() {
        return hijoDer;
    }

    public void setHijoDer(NodoReser hijoDer) {
        this.hijoDer = hijoDer;
    }

    public Cliente getData() {
        return data;
    }

    public void setData(Cliente data) {
        this.data = data;
    }
    
    public boolean hasDer(){
        return getHijoDer()!=null;
    }
    public boolean hasIzq(){
        return getHijoIzq()!=null;
    }
    public boolean esHoja(){
        return(getHijoIzq()==null && getHijoDer()==null);
    }
}
