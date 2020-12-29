package is.buscaminas.model.buscaminas.casillas;

import java.beans.PropertyChangeListener;

public class CasillaMina extends Casilla {

    //Constructora
    public CasillaMina (PropertyChangeListener pVistaCasilla)
    {
        super(pVistaCasilla);
    }

    public CasillaMina (CasillaTemp pCasilla)
    {
        //  Transforma la casilla temporal en una casilla mina
        super(pCasilla);
    }
}
