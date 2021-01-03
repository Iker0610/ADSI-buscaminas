package is.buscaminas.model.buscaminas.casillas.estados;


import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.buscaminas.casillas.*;


public abstract class EstadoDespejable implements IEstadoCasilla
{
	@Override
	public int despejar(Casilla pCasilla)
	{
		//Pre: Recibe una casilla para despejar
		//Post: Dependiendo del tipo de casilla devuelve un número diferente (ver tabla valores en el método despejar de Tabla).
		
		pCasilla.cambiarEstado(new Despejado());
		
		if (pCasilla instanceof CasillaNum)                             // Si no es mina
		{
			if (((CasillaNum) pCasilla).tieneCeroMinasAdyacentes())     // si tiene 0 minas adyacentes
			{
				SFXPlayer.getSFXPlayer().playSFX("waha");
				return 3;
			}
			else{
				return 1;                                              // Si tiene alguna mina adyacente
			}
		}
		else if (pCasilla instanceof CasillaMina50){
			return 5;
		}
		else if (pCasilla instanceof CasillaMinaReset){
			return 6;
		}
		else{                                                               // Si es mina
			SFXPlayer.getSFXPlayer().playAbsoluteSFX("gameover");
			return 2;
		}
	}
	
	@Override
	public void verMinas(Casilla pCasilla)
	{
		if (pCasilla instanceof CasillaMina) pCasilla.cambiarEstado(new MinaMostrada());
	}
}
