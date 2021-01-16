package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorUsuario;
import is.buscaminas.controller.GestorVentanas;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class InicioController
{
	// Atributos
	
	@FXML
	private TextField nombreTextField;
	@FXML
	private TextField mailTextField;
	@FXML
	private TextField contrasenaTextField;
	@FXML
	private TextField mailRecuperacionTextField;
	@FXML
	private ImageView background;
	@FXML
	private ImageView title;
	@FXML
	private Button botonEntrar;
	@FXML
	private Button botonRedSocial;
	@FXML
	private Button botonRecuperar;
	
	
	//Constructora
	
	@FXML
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoInicio.png").toURI().toString());
		background.setImage(backgroundImage);
		Image titleImage = new Image(new File("src/main/resources/is/buscaminas/inicio/buscaminas.png").toURI().toString());
		title.setImage(titleImage);
		botonEntrar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonEntrar.png); -fx-background-color: transparent;");
		botonRedSocial.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonRedSocial.png); -fx-background-color: transparent;");
		botonRecuperar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonRecuperar.png); -fx-background-color: transparent;");
	}
	
	// Métodos
	
	@FXML
	public void pulsarAceptar()
	{
		// Si se introduce un nombre y
		if (!nombreTextField.getText().equals("")){
			
			// Guardamos el nombre de jugador
			String nombreUser = nombreTextField.getText();
			

			boolean correcto=false;

			try {
				GestorUsuario.getGestorUsuario().checkEmailContrasena(mailTextField.getText(), contrasenaTextField.getText(), nombreUser);
				// Se abre el menú principal
				GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
			} catch (Exception e) {

			}





			
		}
		else{
			// Se le indica al usuario que ha de seleccionar un nombre y una dificultad
			Alert alerta = new Alert(Alert.AlertType.WARNING);
			alerta.setTitle("Datos incorrectos");
			alerta.setHeaderText(null);
			alerta.setContentText("Error al iniciar sesión");
			alerta.show();
		}
	}
	
	@FXML
	public void recuperarContrasena(){
		GestorUsuario.getGestorUsuario().recuperarContra(mailRecuperacionTextField.getText());
	}
	
	@FXML
	public void loginRedSocial()
	{
		try {
			GestorUsuario.getGestorUsuario().checkEmail(nombreTextField.getText());
			// Se abre el menú principal
			GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
		} catch (Exception e) {

		}

	}
}