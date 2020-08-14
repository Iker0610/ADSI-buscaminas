package packCodigo;

import java.util.ArrayList;

public class QuickSort {
	 
    private Jugador[] lista;
     
    public QuickSort(ArrayList<Jugador> pL){
        lista = new Jugador[pL.size()];
        pasarAArray(pL);
        quickSort(lista);
    }
     
    public ArrayList<Jugador> getOrdenada(){
        ArrayList<Jugador> l = new ArrayList<Jugador>();
        for(int i =0; i<lista.length;i++){
            l.add(lista[i]);
        }
        return l;
    }
    private void pasarAArray(ArrayList<Jugador> pL){
        for (int i =0; i<pL.size();i++){
            lista[i] = pL.get(i);
        }
    }
     private void quickSort(Jugador[] laTabla){
         quickSort(laTabla, 0, laTabla.length-1);
         }
      
                  
     private void quickSort(Jugador[] tabla, int inicio, int fin){
         if ( fin - inicio > 0 ) { // hay m�s de un elemento en la tabla
             int indiceParticion = particion(tabla, inicio, fin);
             quickSort(tabla, inicio, indiceParticion - 1);
             quickSort(tabla, indiceParticion + 1, fin);
         }
     }   
    private int particion(Jugador[] tabla, int i, int f){
        Jugador pivote = tabla[i];
        int izq = i;
        int der = f;
        while ( izq < der ){
            while ( tabla[izq].compareTo(pivote) <= 0 && izq < der){
                izq++;
            }
            while ( tabla[der].compareTo(pivote) > 0 ) {
                der--;
            }
            if ( izq < der ){
                swap(tabla, izq, der);
            }
        }
        tabla[i] = tabla[der];
        tabla[der] = pivote;
        return der;
    }
 
    private void swap(Jugador[] tabla, int one, int two) {
        Jugador temp = tabla[one];
        tabla[one] = tabla[two];
        tabla[two] = temp;
    }
}

