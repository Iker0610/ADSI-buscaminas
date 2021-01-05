package is.buscaminas.model.casillas.estados;


import is.buscaminas.model.casillas.Casilla;
import javafx.util.Pair;


public class Interrogacion extends EstadoDespejable
{
	@Override
	public Pair<Boolean, Boolean> marcar(Casilla pCasilla)
	{
		//Pre: Se recibe una casilla para marcar
		//Post: Se cambia el estado a oculto. Como la casilla interrogación no cuenta como marcada, se devuelve (false,false)
		
		pCasilla.cambiarEstado(new Oculto());
		return new Pair<>(false, false);
	}
}
