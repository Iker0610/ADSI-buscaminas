package is.buscaminas.controller;

import is.buscaminas.Partida;
import is.buscaminas.model.SFXPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class PanelMenuBuscaminasController {

    @FXML
    private void reiniciar (ActionEvent pEvento) { Partida.getPartida().reiniciarPartida(); }

    @FXML
    private void mostrarAyuda (ActionEvent pEvento) { Partida.getPartida().mostrarAyuda(); }

    @FXML
    private void mostrarRanking (ActionEvent pEvento)
    {
        Partida.getPartida().mostrarRanking();
        SFXPlayer.getSFXPlayer().setBackgroundTheme("challengeTheme");
    }
}
