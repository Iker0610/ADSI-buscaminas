package is.buscaminas.model.buscaminas.casillas;

import java.beans.PropertyChangeListener;

public class CasillaTemp extends Casilla {

    public CasillaTemp (PropertyChangeListener pVistaCasilla)
    {
        super(pVistaCasilla);
    }

    @Override
    public int despejar ()
    {
        //Pre:
        //Post: Devuelve 0 indicando que no hay que hacer nada. Esta opción nunca debería *debería* presentarse. Pero por si las moscas.
        return 0;
    }
}
