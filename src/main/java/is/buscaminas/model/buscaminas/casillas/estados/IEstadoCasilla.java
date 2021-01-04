package is.buscaminas.model.buscaminas.casillas.estados;


import is.buscaminas.model.buscaminas.casillas.Casilla;
import javafx.util.Pair;


public interface IEstadoCasilla
{
	int despejar(Casilla pCasilla);
	
	Pair<Boolean, Boolean> marcar(Casilla pCasilla);
	
	default void verMina(Casilla pCasilla){}
}
