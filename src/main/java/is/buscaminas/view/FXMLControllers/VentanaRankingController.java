package is.buscaminas.view.FXMLControllers;

import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.buscaminas.Contador;
import is.buscaminas.model.ranking.Ranking;
import is.buscaminas.view.uiElements.VistaRanking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VentanaRankingController {
    // Atributos
    VistaRanking[] rankingPorNivel;

    @FXML
    private Button aceptar;
    @FXML
    private StackPane zonaRanking;

    public void initialize ()
    {
        rankingPorNivel = new VistaRanking[3];
        for (int i = 0; i < 3; i++) rankingPorNivel[i] = new VistaRanking();

        mostrarRanking(Partida.getPartida().getDificultad());
    }

    @FXML
    public void cambiarVistaRanking (ActionEvent event)
    {
        int dificultad = Integer.parseInt(((Button) event.getSource()).getText().split(" ")[1]);
        mostrarRanking(dificultad);
    }

    private void mostrarRanking (int pDificultad)
    {
        clean();
        zonaRanking.getChildren().add(rankingPorNivel[pDificultad - 1]);
        Ranking.getRanking().obtenerRanking(pDificultad);
    }

    private void clean ()
    {
        zonaRanking.getChildren().clear();
        for (int i = 0; i < 3; i++) rankingPorNivel[i].getChildren().clear();
        for (int i = 0; i < 3; i++) rankingPorNivel[i] = new VistaRanking();
    }

    @FXML
    public void pulsarAceptar ()
    {
        Stage stage = (Stage) aceptar.getScene().getWindow();
        Contador.getContador().continuar(); //Reanudar contador
        stage.close();
        SFXPlayer.getSFXPlayer().setBackgroundTheme("marioTheme");
    }
}
