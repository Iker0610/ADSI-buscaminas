package is.buscaminas.model.buscaminas.casillas.estados;

import is.buscaminas.model.buscaminas.casillas.Casilla;
import is.buscaminas.model.buscaminas.casillas.CasillaNum;
import javafx.util.Pair;

public class Marcado implements IEstadoCasilla {

    @Override
    public int despejar (Casilla pCasilla)
    {
        //Pre: Se recibe una casilla para despejar
        //Post: Puesto que la casilla está marcada, no se realiza ninguna acción, por lo que se devuelve 0

        return 0;
    }

    @Override
    public Pair<Boolean, Boolean> marcar (Casilla pCasilla)
    {
        //Pre: Se recibe una casilla para marcar
        //Post: Se cambia el estado a Interrogacion. Al desmarcar una casilla, devolvemos el par (TRUE,FALSE)

        pCasilla.cambiarEstado(new Interrogacion());
        return new Pair<>(true, false);
    }

    @Override
    public void verMinas (Casilla pCasilla)
    {
        if (pCasilla instanceof CasillaNum) pCasilla.cambiarEstado(new MarcadoIncorrecto());
    }
}
