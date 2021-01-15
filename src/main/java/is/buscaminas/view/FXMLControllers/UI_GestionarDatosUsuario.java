package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorVentanas;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class UI_GestionarDatosUsuario
{
	// Atributos
	
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField contrasenaTextField;
	@FXML
	private SplitMenuButton seleccionNivelInicial;
	@FXML
	private Button botonVolver;
	@FXML
	private Button botonGuardar;
	@FXML
	private ImageView background;
	
	
	// Constructora
	
	@FXML
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
		background.setImage(backgroundImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
		botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonGuardar.png); -fx-background-color: transparent;");
		
		//TODO cargar datos (SplitMenu, mail, etc)
	}
	
	
	// Métodos
	
	@FXML
	public void pulsarGuardar()
	{
		//TODO Guardar datos introducidos
	}
	
	@FXML
	public void pulsarVolver(){ GestorVentanas.getGestorVentanas().abrirAdministrar(); }
}