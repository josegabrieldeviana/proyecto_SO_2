/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.EDD;

/**
 *
 * @author joseg
 */ 
public class ListaSimple {
    Nodo first;
   //si quisiera hacer un ATRIBUTO si es CICLO, entonces es MEJOR inicializarla en lista SIMPLE.
 

    public ListaSimple() {
        this.first = null;
        
    }
    
    public boolean esCiclo(){ //este método verifica ei es una lista circular
        Nodo aux=this.first;
        while (aux.next!=null && aux.visited==false){
            aux=aux.next; 
            aux.visited=true;
    }
        if(aux.visited==true){
            return true;
    }else{
            return false;
        }
            
        //ultimo.siguiente=this.primero; //esto apunta del ultimo hacia el principio
    
    }
    
    public void buscarPosicion(int dato, int pos){ //
        int contador=1;
        Nodo nuevo = new Nodo(dato);
        Nodo aux=this.first;
        while (aux.next!=null){
            aux=aux.next;
        }
        aux.next = nuevo;
        
        if(pos > 0){
            aux = this.first;
            while (pos > contador){ //aqui lo ponemos para que la posición del recorrido este justo
                aux=aux.next;
                contador++;
                
            }
            nuevo.next=aux; //aqui ya decimos que el aux apunta hacia el nuevo.
        }
         //
    }
    
    public String buscarUltimoString(){
        Nodo aux=this.first;
        if(this.first!=null){
            while(aux.next!=null){
                aux=aux.next;
            }
        return aux.toString();    
        }
        return null;
    }
    
    public void iniciarYeliminar(){ //esto es para eliminar toda la lista 
        this.first=null;
    }
    
    public int tamano(){
        int contador=0;
        Nodo aux=this.first;
        while (aux.next!=null && aux.visited==false){
            aux=aux.next;
            aux.visited=true;
            contador++;
        }
        return contador;
    }
    
    public String imprimir(){
        String cadena = "";
        Nodo aux=this.first;
        while (aux.next!=null && aux.visited==false){
            cadena += aux.data; //hacer tests con esto, solo cambie de private a public T data
            aux=aux.next;
            aux.visited=true;
            
            
        }
        return cadena;      

    }
    /** 
     * hace una lista e inserta un objeto.
    */
    public void hacerLista1(int dato){
        Nodo last=new Nodo(dato);
        Nodo aux=this.first;
        if(this.first==null){
            this.first = last;
        }else{
         while (aux.next!=null){
            aux=aux.next;
        }
        aux.next=last; //aquí el apuntador del ultimo apunta hacia el ultimo
        }
    }
    
    
    
   public String sumarListas(ListaSimple lista2){
       String num1 = this.imprimirListas(); //primero hacemos las listas, las convertimos ambas en String con ImprimirListas
       String num2 = lista2.imprimirListas(); //y aqui tambien, que es la que pusimos en los parametros; ahora lo vemos con imprimirLista
       
       int suma = Integer.parseInt(num1) + Integer.parseInt(num2); //aqui los sumamos en forma de numeros enteros
       String resultado = String.valueOf(suma); //Aquí hacemos un string con String.valueof(suma)
       ListaSimple resultadoLista=new ListaSimple(); //Aquí hacemos una nueva lista con "lista resultadoLista=new lista"
       
       for (int i = 0; i < resultado.length(); i++) { //aquí recorremos toda la lista
           char c = resultado.charAt(i); //aquí con char c=resultado.charAt(i)
           String r = String.valueOf(c); //aqui con valueof  hacemos un string
           int dato = Integer.parseInt(r); //aquí con integer.parseint(r) convertimos el string en entero
           resultadoLista.hacerLista1(dato); //aquí hacemos la lista con el dato con la lista del principio
       }
       return resultadoLista.imprimirListas(); //aquí ahora tenemos que imprimir la lista hecha con resultado lista
       
   }
    /**esto es para insertar al final de una lsta a un objeto.
     */
   public void insertarfinal(int dato){
        Nodo ultimo=new Nodo(dato);
        this.first.next=ultimo;
   }
    /**
     *
     * @return
     */
    public String imprimirListas(){
        String listastring="";
        Nodo aux=this.first;
        
        while (aux!=null){
            listastring+=" "+aux.data;
            aux=aux.next;
        }
        return listastring;
        
    }
    
    
    
    public void hacerListaString(String dato){
        Nodo last=new Nodo(dato);
        Nodo aux=this.first;
        if(this.first==null){
            this.first = last;
        }else{
         while (aux.next!=null){
            aux=aux.next;
        }
        aux.next=last; //aquí el apuntador del ultimo apunta hacia el ultimo
        }
    }
    
//    
//    /**Método encargado de escoger un nombre random de una lista de 7 posibles procesos y devolverlo.
//     * Tiene capacidad MAXIMA de 7 nombres para procesos NORMALES síncronos, este método es usado
//     * en el método de generar "x" procesos, qué es util al principio del programa y al generar 20 procesos.
//    * 
//    * @return se escoge UNO de los 7 objetos de la lista simple.
//    * 
//    */
//        public void escogerRandomPNORMAL(int dato, int pos){ //
//            
//        int contador=1;
//        Nodo nuevo = new Nodo(dato);
//        Nodo aux=this.first;
//        while (aux.next!=null){
//            aux=aux.next;
//        }
//        aux.next = nuevo;
//        
//        if(pos > 0){
//            aux = this.first;
//            while (pos > contador){ //aqui lo ponemos para que la posición del recorrido este justo
//                aux=aux.next;
//                contador++;
//                
//            }
//            nuevo.next=aux; //aqui ya decimos que el aux apunta hacia el nuevo.
//        }
//         //
//    }
    
}
