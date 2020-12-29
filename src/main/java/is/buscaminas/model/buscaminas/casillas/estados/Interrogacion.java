package is.buscaminas.model.buscaminas.casillas.estados;

import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.buscaminas.casillas.Casilla;
import is.buscaminas.model.buscaminas.casillas.CasillaMina;
import is.buscaminas.model.buscaminas.casillas.CasillaNum;
import javafx.util.Pair;

public class Interrogacion implements IEstadoCasilla {

    @Override
    public int despejar (Casilla pCasilla)
    {
        //Pre: Recibe una casilla para despejar
        //Post: Dependiendo del tipo de casilla devuelve número diferente (ver tabla valores en el método despejar de Tabla).

        pCasilla.cambiarEstado(new Despejado());
        if (pCasilla instanceof CasillaNum)                                     // Si no es mina
        {
            if (((CasillaNum) pCasilla).tieneCeroMinasAdyacentes())             // si tiene 0 minas adyacentes
            {
                SFXPlayer.getSFXPlayer().playSFX("waha");
                return 3;
            }

            else {
                return 1;                                                      // Si tiene alguna mina adyacente
            }
        }
        else {                                                               // Si es mina

            SFXPlayer.getSFXPlayer().playAbsoluteSFX("gameover");
            return 2;
        }                                                          // Si es mina
    }

    @Override
    public Pair<Boolean, Boolean> marcar (Casilla pCasilla)
    {
        //Pre: Se recibe una casilla para marcar
        //Post: Se cambia el estado a oculto. Como la casilla interrogación no cuenta como marcada, se devuelve (false,false)

        pCasilla.cambiarEstado(new Oculto());
        return new Pair<>(false, false);
    }

    @Override
    public void verMinas (Casilla pCasilla)
    {
        if (pCasilla instanceof CasillaMina) pCasilla.cambiarEstado(new MinaSinMarcar());
    }
}
