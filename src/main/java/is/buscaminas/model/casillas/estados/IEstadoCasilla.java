package is.buscaminas.model.casillas.estados;

import is.buscaminas.model.casillas.Casilla;
import javafx.util.Pair;

public interface IEstadoCasilla {
    int despejar (Casilla pCasilla);

    Pair<Boolean, Boolean> marcar (Casilla pCasilla);

    void verMinas (Casilla pCasilla);
}
