package is.buscaminas.model.buscaminas.casillas.estados;

import is.buscaminas.model.buscaminas.casillas.Casilla;
import javafx.util.Pair;

public class Despejado implements IEstadoCasilla {

    @Override
    public int despejar (Casilla pCasilla)
    {
        //Pre: Una casilla
        //Post: Devuelve 4, indicando que la casilla ya esta despejada, así que se deben comprobar las inmediatas adyacentes

        return 4;
    }

    @Override
    public Pair<Boolean, Boolean> marcar (Casilla pCasilla)
    {
        //Pre: Una casilla
        //Post: Devuelve 0 ya que no se ha marcado ni desmarcado la casilla
        return new Pair<>(false, false); //No ocurre nada si se manda marcar una casilla despejada
    }

    @Override
    public void verMinas (Casilla pCasilla) {}
}
