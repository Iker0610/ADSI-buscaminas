package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorRanking;
import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Contador;
import is.buscaminas.model.Usuario;
import is.buscaminas.view.uiElements.VistaRanking;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;


public class UI_VerRanking
{
	// Atributos
	
	VistaRanking[] rankingPorNivel;
	private boolean personal;
	private boolean porNiveles;
	
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
		porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/clasificarPorNivel.png); -fx-background-color: transparent;");
		personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingPersonal.png); -fx-background-color: transparent;");
		
		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setFloatWindowBackgroundTheme("rankingTheme");

		personal = false;
		porNiveles = false;
	}

	
	
	// Métodos

	@FXML
	public void pulsarPorNiveles() throws SQLException {
		if (porNiveles) {
			if (personal) {
				String jsonNiveles = GestorRanking.getGestorRanging().obtenerRankingPersonal();
			}
			else {
				String jsonNiveles = GestorRanking.getGestorRanging().obtenerRankingGlobal();
			}
			porNiveles = false;
			porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/clasificarPorNivel.png); -fx-background-color: transparent;");
		}
		else {
			if (personal) {
				String jsonNiveles = GestorRanking.getGestorRanging().clasificarPorNivelPersonal();
			}
			else {
				String jsonNiveles = GestorRanking.getGestorRanging().clasificarPorNivel();
			}
			porNiveles = true;
			porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingAbsoluto.png); -fx-background-color: transparent;");
		}
		//mostrarRanking(jsonNiveles);
	}

	@FXML
	public void pulsarPersonalGlobal() throws SQLException {
		if (personal) {
			if (porNiveles) {
				String jsonNiveles = GestorRanking.getGestorRanging().clasificarPorNivel();
			}
			else {
				String jsonNiveles = GestorRanking.getGestorRanging().obtenerRankingGlobal();
			}
			personal = false;
			personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingGlobal.png); -fx-background-color: transparent;");
		}
		else {
			if (porNiveles) {
				String jsonNiveles = GestorRanking.getGestorRanging().clasificarPorNivelPersonal();
			}
			else {
				String jsonNiveles = GestorRanking.getGestorRanging().obtenerRankingPersonal();
			}
			personal = true;
			personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/VerRankingPersonal.png); -fx-background-color: transparent;");
		}
		//mostrarRanking(jsonNiveles);
	}
	
	private void mostrarRanking(String ranking)
	{
		clean();

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
		SFXPlayer.getSFXPlayer().stopFloatWindowBackground();
		if (Partida.getPartida().hayPartidaActiva()) Contador.getContador().continuar();
		stage.close();
	}
}
