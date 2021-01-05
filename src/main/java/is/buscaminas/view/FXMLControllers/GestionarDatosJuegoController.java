package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorVentanas;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class GestionarDatosJuegoController
{
	// Atributos
	
	@FXML
	private TextField numMinasTextField;
	@FXML
	private TextField columnasTextField;
	@FXML
	private TextField filasTextField;
	@FXML
	private TextArea ayudaTextArea;
	@FXML
	private SplitMenuButton seleccionNivel;
	@FXML
	private Button botonVolver;
	@FXML
	private Button botonGuardar;
	@FXML
	private ImageView background;
	
	
	//Constructora
	
	@FXML
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
		background.setImage(backgroundImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
		botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonGuardar.png); -fx-background-color: transparent;");
		
		//TODO cargar datos (SplitMenu, ayuda, etc)
		
	}
	
	
	// Métodos
	
	@FXML
	public void pulsarGuardar()
	{
		//TODO Guardar datos introducidos
	}
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirAdministrar();
	}
	
}
