package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class MenuPrincipalController
{
	// Atributos
	
	@FXML
	private ImageView background;
	@FXML
	private ImageView title;
	@FXML
	private Button botonRanking;
	@FXML
	private Button botonLogros;
	@FXML
	private Button botonGestionarCuenta;
	@FXML
	private Button botonAyuda;
	@FXML
	private Button botonVolver;
	@FXML
	private Button botonJugar;
	@FXML
	private Button botonAdmin;
	
	
	//Constructora
	
	@FXML
	private void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/fondoPrincipal.png").toURI().toString());
		background.setImage(backgroundImage);
		Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/buscaminas.png").toURI().toString());
		title.setImage(titleImage);
		botonRanking.setStyle(
				  "-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonRanking.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		botonLogros.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonLogros.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		botonGestionarCuenta.setStyle(
				  "-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonGestionarCuenta.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		botonAyuda.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonAyuda.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonVolver.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		botonJugar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonJugar.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		botonAdmin.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/principal/botonAdmin.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		if (!Usuario.getUsuario().esAdmin()){
			botonAdmin.setDisable(true);
			botonAdmin.setOpacity(0.0);
			
		}
		
		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setBackgroundTheme("mainMenuTheme");
	}
	
	// Métodos
	
	@FXML
	public void pulsarJugar()
	{
		GestorVentanas.getGestorVentanas().abrirSeleccionNivel();
	}
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
	
	@FXML
	public void pulsarVerRanking()
	{
		GestorVentanas.getGestorVentanas().mostrarRankingEmergente();
	}
	
	@FXML
	public void pulsarVerLogros()
	{
		GestorVentanas.getGestorVentanas().abrirLogros();
	}
	
	@FXML
	public void pulsarGestionarCuenta()
	{
		GestorVentanas.getGestorVentanas().abrirGestionarCuenta();
	}
	
	@FXML
	public void pulsarAyuda()
	{
		GestorVentanas.getGestorVentanas().mostrarAyudaEmergente();
	}
	
	@FXML
	public void pulsarAdmin()
	{
		GestorVentanas.getGestorVentanas().abrirAdministrar();
	}
}
