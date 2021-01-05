package is.buscaminas.model.casillas;


import java.beans.PropertyChangeListener;


public class CasillaMina50 extends CasillaMina
{
	public CasillaMina50(PropertyChangeListener pVistaCasilla)
	{
		super(pVistaCasilla);
	}
	
	public CasillaMina50(CasillaTemp pCasilla)
	{
		//  Transforma la casilla temporal en una casilla mina reset
		super(pCasilla);
	}
}
