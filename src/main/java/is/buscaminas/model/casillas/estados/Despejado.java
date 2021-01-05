package is.buscaminas.model.casillas.estados;


import is.buscaminas.model.casillas.Casilla;
import is.buscaminas.model.casillas.CasillaNum;
import javafx.util.Pair;


public class Despejado implements IEstadoCasilla
{
	@Override
	public int despejar(Casilla pCasilla)
	{
		//Pre: Una casilla
		//Post: Devuelve 4, indicando que la casilla ya esta despejada, así que si es numérica se deben comprobar las inmediatas adyacentes.
		//      Si no es una casilla numérica devuelve 0.
		return (pCasilla instanceof CasillaNum) ? 4 : 0;
	}
	
	@Override
	public Pair<Boolean, Boolean> marcar(Casilla pCasilla)
	{
		//Pre: Una casilla
		//Post: Devuelve 0 ya que no se ha marcado ni desmarcado la casilla
		return new Pair<>(false, false); //No ocurre nada si se manda marcar una casilla despejada
	}
}
