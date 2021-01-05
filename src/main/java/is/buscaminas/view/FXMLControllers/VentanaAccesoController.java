package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class VentanaAccesoController
{
	//Atributos normales
	
	private ToggleGroup dificultadGroup;
	
	//Atributos FXML
	
	@FXML
	private TextField nombreTextField;
	@FXML
	private RadioButton dificultad1;
	@FXML
	private RadioButton dificultad2;
	@FXML
	private RadioButton dificultad3;
	@FXML
	private Button botonJugar;
	@FXML
	private Button botonVolver;
	@FXML
	private ImageView background;
	@FXML
	private ImageView title;
	
	
	//Constructora
	
	@FXML
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/fondoAcceso.png").toURI().toString());
		background.setImage(backgroundImage);
		Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/buscaminas.png").toURI().toString());
		title.setImage(titleImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/botonVolver.png); -fx-background-color: transparent;");
		botonJugar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/botonEntrar.png); -fx-background-color: transparent;");
		
		dificultadGroup = new ToggleGroup();
		dificultad1.setToggleGroup(dificultadGroup);
		dificultad2.setToggleGroup(dificultadGroup);
		dificultad3.setToggleGroup(dificultadGroup);
		
		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setBackgroundTheme("marioTheme");
	}
	
	
	// Métodos
	
	@FXML
	public void pulsarAceptar()
	{
		// Si se selcciona una dificultad
		if (dificultadGroup.getSelectedToggle() != null){
			
			// Introducimos la dificultad seleccionada
			int numDificultad;
			String dificultad = ((Node) dificultadGroup.getSelectedToggle()).getId();
			
			try{
				numDificultad = Integer.parseInt(dificultad);
			}
			catch (Exception e){
				numDificultad = 1;
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "ERROR (introducir dificultad)", ButtonType.YES, ButtonType.NO);
				alert.show();
			}
			
			// Se inicia la partida
			Partida.getPartida().iniciarPartida(numDificultad);
			
		}
		else{
			// Se le indica al usuario que ha de seleccionar un nombre y una dificultad
			Alert alerta = new Alert(Alert.AlertType.WARNING);
			alerta.setTitle("Datos incorrectos");
			alerta.setHeaderText(null);
			alerta.setContentText("Introduce una dificultad");
			alerta.show();
		}
	}
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
}
