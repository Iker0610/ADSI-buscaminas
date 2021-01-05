package is.buscaminas.view.FXMLControllers;

import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


public class PanelMenuBuscaminasController {

    @FXML private GridPane background;
    @FXML private Button botonRanking;
    @FXML private Button botonAyuda;

    @FXML
    public void initialize(){
        //Cargar Temática
        background.setStyle(" -fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/fondo/fondo2.png);");
        botonRanking.setStyle(" -fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/ranking/ranking.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent");
        botonAyuda.setStyle(" -fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/ayuda/btnAyuda.gif); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent");
    }

    @FXML
    private void reiniciar (ActionEvent pEvento) { Partida.getPartida().reiniciarPartida(); }
    
    @FXML
    private void mostrarAyuda(ActionEvent pEvento){ GestorVentanas.getGestorVentanas().mostrarAyudaEmergente(); }

    @FXML
    private void mostrarRanking (ActionEvent pEvento)
    {
        GestorVentanas.getGestorVentanas().mostrarRankingEmergente();
        SFXPlayer.getSFXPlayer().setBackgroundTheme("challengeTheme");
    }
}
