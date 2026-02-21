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
public class Lista<T> {
    public Nodo<T> Head;
    private Nodo<T> Tail;

    public Lista() {
        this.Head = this.Tail = null;

    }

    /*
    *
    */

    // public String nombreDebug(){
    // return this.
    // }

    /**
     *
     * @param n
     */
    public Lista(Nodo<T> n) {
        this.Head = this.Tail = n;

    }

    public boolean isEmpty() {
        return this.Head == null;
    }

    public int size() {
        int i = 0;

        if (isEmpty()) {
            return 0;
        }
        Nodo<T> aux = this.Head;
        while (aux != null) {
            aux = aux.getNext();
            i++;
        }
        return i;
    }

    /**
     *
     * @param dataL
     */
    public void addFirst(T dataL) {
        Nodo<T> n = new Nodo(dataL);
        if (isEmpty()) {
            this.Head = n;
            this.Tail = n;
        } else {
            n.setNext(this.Head);
            this.Head = n;
        }
    }

    /**
     *
     * @param dataL
     */
    public void addLast(T dataL) {
        Nodo<T> n = new Nodo(dataL);
        if (isEmpty()) {
            this.Head = n;
            this.Tail = n;

        } else {
            this.Tail.setNext(n);
            this.Tail = n;
        }
    }

    public void add(T dataL, int i) {
        if (isEmpty() || i == 0) {
            this.addFirst(dataL);
        } else if (i >= (size() - 1)) {
            this.addLast(dataL);
        } else if (i < 0) {
            this.add(dataL, size() + i);
        } else {
            Nodo<T> n = new Nodo(dataL);
            Nodo aux = this.Head;
            int count = 0;
            while (count < i - 1) {
                aux = aux.getNext();
                count++;
            }
            n.setNext(aux.getNext());
            aux.setNext(n);
        }
    }

    public T deleteFirst() {
        if (isEmpty()) {
            return null;
        }
        T data = this.Head.getData();
        if (this.Head == this.Tail) {
            this.Head = null;
            this.Tail = null;
        } else {
            Nodo<T> temp = this.Head;
            this.Head = this.Head.getNext();
            temp.setNext(null);
        }
        return data;
    }

    public T deleteLast() {
        if (isEmpty()) {
            return null;
        }
        T data = this.Tail.getData();
        if (this.Head == this.Tail) {
            this.Head = null;
            this.Tail = null;
        } else {
            Nodo<T> pre = this.Head;
            while (pre.getNext() != this.Tail) {
                pre = pre.getNext();
            }
            pre.setNext(null);
            this.Tail = pre;
        }
        return data;
    }

    public T delete(int i) {
        if (isEmpty()) {
            return null;
        } else if (i == 0) {
            return deleteFirst();
        } else if (i == size() - 1) {
            return deleteLast();
        } else if (i < 0) {
            return delete(size() + i);
        } else if (i > size() - 1) {
            System.out.println("\nNo Funciona");
            return null;
        } else {
            Nodo<T> aux = this.Head;
            int count = 0;
            while (count < i - 1) {
                aux = aux.getNext();
                count++;
            }
            Nodo<T> del = aux.getNext();
            aux.setNext(del.getNext());
            del.setNext(null);
            return del.getData();
        }
    }

    /**
     * 
     * @param i El índice del objeto que queremos conseguir.
     * @return el objeto que queremos obtener que corresponde al índice "i"
     *         insertado.
     */
    public T BuscarPosicion(int i) {
        if (isEmpty()) {
            return null;
        }
        Nodo<T> aux = this.Head;
        int count = 0;
        while (aux != null) {
            if (count == i) {
                return aux.getData();
            }
            aux = aux.getNext();
            count++;
        }
        return null;
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("Lista Vacia");
        } else {
            Nodo aux = this.Head;
            int i = 0;
            while (aux != null) {
                System.out.print(aux.getData());
                aux = aux.getNext();
                i++;
            }
            System.out.println("");
        }
    }

    /**
     * 
     * @param dato
     * @return
     */
    public boolean buscar(T dato) {
        Nodo<T> aux = this.Head;
        while (aux != null) {
            if (aux.getData().toString().equals(dato.toString())) {
                return true;
            }
            aux = aux.getNext();
        }
        return false;
    }

    public T buscarLast() {
        if (this.Head == null) {
            return null;
        }
        if (this.Head.getNext() == null) {
            return this.Head.getData();
        }

        Nodo<T> current = this.Head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current.getData();
    }

    /**
     * Imprime todas las strings de una Lista.
     * 
     * @return
     */
    public String printString() {
        String stringList = "";
        Nodo aux = this.Head;

        while (aux != null) {
            stringList += " " + aux.data;
            aux = aux.next;
        }
        return stringList;
    }

    public void vaciar() {
        this.Head = null;
        this.Tail = null;
    }

    /**
     * Busca en la lista el objeto que tenga el valor más bajo en un atributo
     * numérico específico (int).
     * 
     * @param nombreAtributo El nombre del atributo (field) a comparar.
     * @return El objeto con el valor más bajo, o null si la lista está vacía o hay
     *         un error.
     */
    public T buscarPMinAtributo(String nombreAtributo) {
        if (isEmpty()) {
            return null;
        }

        T minObjeto = null;
        int minValor = Integer.MAX_VALUE;

        try {
            Nodo<T> aux = this.Head;
            while (aux != null) {
                T obj = aux.getData();
                if (obj != null) {
                    java.lang.reflect.Field field = obj.getClass().getDeclaredField(nombreAtributo);
                    field.setAccessible(true);
                    int valor = field.getInt(obj);

                    if (valor < minValor) {
                        minValor = valor;
                        minObjeto = obj;
                    }
                }
                aux = aux.getNext();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("[ERROR] No se pudo acceder al atributo '" + nombreAtributo + "': " + e.getMessage());
            return null;
        }

        return minObjeto;
    }

    /**
     * Elimina la primera ocurrencia de un dato específico en la lista.
     * 
     * @param dato El dato a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public boolean remove(T dato) {
        if (isEmpty() || dato == null)
            return false;

        if (this.Head.getData().equals(dato)) {
            deleteFirst();
            return true;
        }

        Nodo<T> prev = this.Head;
        Nodo<T> curr = this.Head.getNext();

        while (curr != null) {
            if (curr.getData().equals(dato)) {
                if (curr == this.Tail) {
                    this.Tail = prev;
                    prev.setNext(null);
                } else {
                    prev.setNext(curr.getNext());
                    curr.setNext(null);
                }
                return true;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return false;
    }
}
