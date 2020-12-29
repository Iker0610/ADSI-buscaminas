package is.buscaminas.model.buscaminas.casillas.estados;

import is.buscaminas.model.buscaminas.casillas.Casilla;
import javafx.util.Pair;

public class MarcadoIncorrecto implements IEstadoCasilla {

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
