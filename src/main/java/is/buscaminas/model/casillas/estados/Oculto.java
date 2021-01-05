package is.buscaminas.model.casillas.estados;


import is.buscaminas.model.casillas.Casilla;
import javafx.util.Pair;


public class Oculto extends EstadoDespejable
{
	@Override
	public Pair<Boolean, Boolean> marcar(Casilla pCasilla)
	{
		//Pre: Se recibe una casilla para marcar
		//Post: Se cambia el estado a marcado. Al marcar esta casilla, devolvemos el par (TRUE,TRUE)
		
		pCasilla.cambiarEstado(new Marcado());
		return new Pair<>(true, true);
	}
}
