package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorLogros;
import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.SQLException;


public class UI_Logros
{
	// Atributos
	
	@FXML
	private ImageView background;
	@FXML
	private Button botonVolver;
	@FXML
	private VBox zonaLogros;
	
	
	//Constructora
	
	@FXML
	public void initialize() throws SQLException {
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/logros/fondoLogros.png").toURI().toString());
		background.setImage(backgroundImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/gestionarCuenta/botonVolver.png); -fx-background-color: transparent;");
		
		//TODO cargar todos los logros (Temporal)
		GestorLogros gl= GestorLogros.getGestorLogros();
		gl.cargarLogros(Usuario.getUsuario().getEmail());

	}
	
	
	// Métodos
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
}

