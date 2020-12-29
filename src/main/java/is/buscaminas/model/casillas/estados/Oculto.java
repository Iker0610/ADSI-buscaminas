package is.buscaminas.model.casillas.estados;

import is.buscaminas.model.SFXPlayer;
import is.buscaminas.model.casillas.Casilla;
import is.buscaminas.model.casillas.CasillaMina;
import is.buscaminas.model.casillas.CasillaNum;
import javafx.util.Pair;

public class Oculto implements IEstadoCasilla {

    @Override
    public int despejar (Casilla pCasilla)
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
            else {
                return 1;                                              // Si tiene alguna mina adyacente
            }
        }
        else {                                                               // Si es mina
            SFXPlayer.getSFXPlayer().playAbsoluteSFX("gameover");
            return 2;
        }
    }

    @Override
    public Pair<Boolean, Boolean> marcar (Casilla pCasilla)
    {
        //Pre: Se recibe una casilla para marcar
        //Post: Se cambia el estado a marcado. Al marcar esta casilla, devolvemos el par (TRUE,TRUE)

        pCasilla.cambiarEstado(new Marcado());
        return new Pair<>(true, true);
    }

    @Override
    public void verMinas (Casilla pCasilla)
    {
        if (pCasilla instanceof CasillaMina) pCasilla.cambiarEstado(new MinaSinMarcar());
    }
}
