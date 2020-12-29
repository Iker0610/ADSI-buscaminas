package is.buscaminas.model.casillas.estados;

import is.buscaminas.model.casillas.Casilla;
import javafx.util.Pair;

public class MinaSinMarcar implements IEstadoCasilla {

    @Override
    public int despejar (Casilla pCasilla)
    {
        return 0;
    }

    @Override
    public Pair<Boolean, Boolean> marcar (Casilla pCasilla)
    {
        //Pre: Una casilla
        //Post: Devuelve 0 ya que no se ha marcado ni desmarcado la casilla
        return new Pair<>(false, false);
    }

    @Override
    public void verMinas (Casilla pCasilla) {}
}
