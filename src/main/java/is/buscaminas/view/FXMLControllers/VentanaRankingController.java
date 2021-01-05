package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.ranking.Ranking;
import is.buscaminas.view.uiElements.VistaRanking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;


public class VentanaRankingController
{
	// Atributos
	
	VistaRanking[] rankingPorNivel;
	
	@FXML
	private Button aceptar;
	@FXML
	private StackPane zonaRanking;
	@FXML
	private ImageView background;
	@FXML
	private ImageView title;
	@FXML
	private Button porNivel;
	@FXML
	private Button personalGlobal;
	
	
	//Constructora
	
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/fondoRanking.png").toURI().toString());
		background.setImage(backgroundImage);
		Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/titulo.png").toURI().toString());
		title.setImage(titleImage);
		aceptar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/aceptar.gif); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		//TODO ALTERNAR POR NIVEL-ABSOLUTO
		porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/clasificarPorNivel.png); -fx-background-color: transparent;");
		//TODO ALTERNAR GLOBAL-PERSONAL
		personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingPersonal.png); -fx-background-color: transparent;");
		
		//
		rankingPorNivel = new VistaRanking[3];
		for (int i = 0; i < 3; i++) rankingPorNivel[i] = new VistaRanking();
		
		mostrarRanking(Partida.getPartida().getDificultad());
		
		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setFloatWindowBackgroundTheme("challengeTheme");
	}
	
	
	// Métodos
	
	@FXML
	public void cambiarVistaRanking(ActionEvent event)
	{
		int dificultad = Integer.parseInt(((Button) event.getSource()).getText().split(" ")[1]);
		mostrarRanking(dificultad);
	}
	
	private void mostrarRanking(int pDificultad)
	{
		clean();
		zonaRanking.getChildren().add(rankingPorNivel[pDificultad - 1]);
		Ranking.getRanking().obtenerRanking(pDificultad);
	}
	
	private void clean()
	{
		zonaRanking.getChildren().clear();
		for (int i = 0; i < 3; i++) rankingPorNivel[i].getChildren().clear();
		for (int i = 0; i < 3; i++) rankingPorNivel[i] = new VistaRanking();
	}
	
	@FXML
	public void pulsarAceptar()
	{
		Stage stage = (Stage) aceptar.getScene().getWindow();
		stage.close();
	}
}
