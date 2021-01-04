package is.buscaminas.model.buscaminas.casillas;


import java.beans.PropertyChangeListener;


public class CasillaMinaReset extends CasillaMina
{
	public CasillaMinaReset(PropertyChangeListener pVistaCasilla)
	{
		super(pVistaCasilla);
	}
	
	public CasillaMinaReset(CasillaTemp pCasilla)
	{
		//  Transforma la casilla temporal en una casilla mina reset
		super(pCasilla);
	}
}
