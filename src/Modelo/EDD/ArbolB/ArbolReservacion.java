/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.hotel;

/**
 *
 * @author alexp
 */
public class ArbolReservacion {
    private NodoReser Root;
    
    public ArbolReservacion(){
        this.Root=null;
    }

    public NodoReser getRoot() {
        return Root;
    }

    public void setRoot(NodoReser Root) {
        this.Root = Root;
    }
    
    public boolean esVacio(){
        return getRoot()==null;
    }
    
    public void insertar(Cliente data){
        NodoReser newNodo=new NodoReser(data);
        if(this.Root==null){
            this.Root=newNodo;
        }else{
            insertador(this.Root, newNodo);
        }
    }
                
    public void insertador(NodoReser Root, NodoReser newNodo){
        if(esVacio()){
            this.Root=newNodo;
            
        }else{
            if(Root.getData().getCi().compareTo(newNodo.getData().getCi()) > 0){
                if(Root.getHijoIzq()==null){
                    Root.setHijoIzq(newNodo);
                }else{
                    insertador(Root.getHijoIzq(), newNodo);
                }
                
            }else if(Root.getData().getCi().compareTo(newNodo.getData().getCi()) < 0){
                if(Root.getHijoDer()==null){
                    Root.setHijoDer(newNodo);
                }else{
                    insertador(Root.getHijoDer(),newNodo);
                }
            }else{
                System.out.println("El elemento ya se encuentra en el arbol");
            }
        }
        
    }
    
    public NodoReser eliminador(NodoReser Root, String CI){
        if (Root == null){
            return Root;
        }
        
        if(Root.getData().getCi().compareTo(CI) > 0){
            Root.setHijoIzq(eliminador(Root.getHijoIzq(), CI));
            return Root;
        }else if(Root.getData().getCi().compareTo(CI) > 0){
            Root.setHijoDer(eliminador(Root.getHijoDer(), CI));
            return Root;
        }
        
        if(Root.getHijoIzq() == null){
            NodoReser aux = Root.getHijoDer();
            return aux;
        }else if(Root.getHijoDer() == null){
            NodoReser aux = Root.getHijoIzq();
            return aux;
        }else{
            NodoReser succesoral = Root;
            
            NodoReser succesorado = Root.getHijoDer();
            
            while(succesorado.getHijoIzq() != null){
                succesoral = succesorado;
                succesorado = succesorado.getHijoIzq();
            }
            
            if(succesoral != Root){
               succesoral.setHijoIzq(succesorado.getHijoDer());
            }else{
                succesoral.setHijoDer(succesorado.getHijoIzq());
            }
            
            Root.setData(succesorado.getData());
            return Root;         
        }
    }
    
    public NodoReser buscar(String data){
        NodoReser aux = this.Root;
        
        if (aux==null){ 
            return null;
        }else{
            NodoReser info = buscador(aux, data);
            return info;
        }
        
    }
    
    public NodoReser buscador(NodoReser aux, String data){
        
        if(aux==null) return null;
        
        if(aux.getData().getCi().equals(data)){
            return aux;
        }else if(aux.getData().getCi().compareTo(data) > 0){
            return buscador(aux.getHijoIzq(), data);
        }else{
            return buscador(aux.getHijoDer(),data);
        }
    }
    
    public void preorden(NodoReser Root){
        if(Root!=null){
            System.out.println(Root.getData() +",");
            preorden(Root.getHijoIzq());
            preorden(Root.getHijoDer());           
        }
    }
    
    public void postorden(NodoReser Root){
        if(Root!=null){
            postorden(Root.getHijoIzq());
            postorden(Root.getHijoDer());
            System.out.println(Root.getData() +",");
        }
    }
    
    public void inorden(NodoReser Root){
        if(Root!=null){
            inorden(Root.getHijoIzq());
            System.out.println(Root.getData() + ",");
            inorden(Root.getHijoDer());
        }
    }
    
    public void imprimir(NodoReser nodo){
        if(nodo!=null){
            imprimir(nodo.getHijoIzq()); // Visita el hijo izquierdo
            System.out.println(nodo.getData()); // Imprime el valor del nodo
            imprimir(nodo.getHijoDer()); // Visita el hijo derecho
        }
    }
}
