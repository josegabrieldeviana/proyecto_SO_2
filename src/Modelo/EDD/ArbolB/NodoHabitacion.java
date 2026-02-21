/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.hotel;

/**
 *
 * @author alexp
 */
public class NodoHabitacion {
    private NodoHabitacion hijoIzq;
    private NodoHabitacion hijoDer;
    private String NroHab;
    private ListaHabitacion Lista;
    private int altura;

    public NodoHabitacion(Habitacion data) {
        this.hijoIzq = null;
        this.hijoDer = null;
        this.Lista = new ListaHabitacion(new Nodo(data));
        this.NroHab = data.getNum_hab();
    }

    public NodoHabitacion getHijoIzq() {
        return hijoIzq;
    }

    public void setHijoIzq(NodoHabitacion hijoIzq) {
        this.hijoIzq = hijoIzq;
    }

    public NodoHabitacion getHijoDer() {
        return hijoDer;
    }

    public void setHijoDer(NodoHabitacion hijoDer) {
        this.hijoDer = hijoDer;
    }

    public ListaHabitacion getLista() {
        return this.Lista;
    }

    public void setLista(ListaHabitacion data) {
        this.Lista = data;
    }
    
    public boolean hasDer(){
        return getHijoDer()!=null;
    }
    
    public boolean hasIzq(){
        return getHijoIzq()!=null;
    }
     public boolean esHoja(){
         return (getHijoDer()==null && getHijoIzq()==null);
     }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public String getNroHab() {
        return NroHab;
    }

    public void setNroHab(String NroHab) {
        this.NroHab = NroHab;
    }

    
}
