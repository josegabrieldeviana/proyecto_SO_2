/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hashtablesPrim;

/**
 *
 * @author joseg
 */
public class registroHash {
   
    Lista[] clientes;
    int tamaño;

    public registroHash(int tamaño) {
        this.tamaño = tamaño;
        this.clientes = new Lista[tamaño];
        for (int i = 0; i < tamaño; i++) {
            this.clientes[i] = new Lista();
            
        }
    }
    
    public int hash(String nombre, String apellido){
        char nombrex;
        char apellidox;
        int numAscii=0;
        int apeAscii=0;
        for (int i = 0; i < nombre.length(); i++) {
            nombrex=nombre.charAt(i);
            numAscii+=(int)nombrex;
        }
        for (int i = 0; i < apellido.length(); i++) {
            apellidox=apellido.charAt(i);
            apeAscii+=(int)apellidox;
            
        }
        return (numAscii+apeAscii)%tamaño;
    }
    
    public void insertarHash(String numhab, String nombre, String apellido){
        int hashAlmacenado = hash(nombre, apellido);
        clientes[hashAlmacenado].insertar(numhab, nombre, apellido);
    }
    
    public void eliminarHash(String nombre, String apellido){
        int hashAlmacenado=hash(nombre, apellido);
        clientes[hashAlmacenado].eliminar(nombre, apellido);
    }
    
    public clientes buscarHash(String nombre, String apellido){
        int hashAlmacenado=hash(nombre, apellido);
        clientes clienteBuscado=clientes[hashAlmacenado].buscar(nombre, apellido);
        return clienteBuscado;
    }
    
    public void imprimirHash(){
        for (int i = 0; i < tamaño; i++) {
            System.out.println(this.clientes[i].imprimir()); //no confundir clase CLIENTES con lista CLIENTES.
        }
    }
}
