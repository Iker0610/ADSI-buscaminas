package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorJugadores;
import is.buscaminas.controller.GestorVentanas;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;


public class GestionarDatosUsuarioController
{
	// Atributos
	
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField contrasenaTextField;
	@FXML
	private Label contrasenaLabel;
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
	public void initialize() throws SQLException {
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
		background.setImage(backgroundImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
		botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonGuardar.png); -fx-background-color: transparent;");

		//Cargar datos
		try{
			JsonObject datos = Jsoner.deserialize(GestorJugadores.getGestorJugadores().getDatosUsuario(), new JsonObject());
			emailTextField.setText((String) datos.get("email"));
			if (datos.get("contrasena") == null){
				contrasenaTextField.setVisible(false);
				contrasenaTextField.setDisable(true);
				contrasenaLabel.setVisible(false);
			}
			else{
				contrasenaTextField.setText((String) datos.get("contrasena"));
			}
			seleccionNivelInicial.setText(datos.get("nivel").toString());
			JsonArray jsonArray = Jsoner.deserialize(GestorJugadores.getGestorJugadores().getNiveles(), new JsonArray());
			for (Object jsonObjectString: jsonArray){
				JsonObject json = (JsonObject)  jsonObjectString;
				MenuItem nivel = new MenuItem((String) json.get("nivel").toString());
				seleccionNivelInicial.getItems().add(nivel);
				nivel.setOnAction((e)->{
					seleccionNivelInicial.setText(nivel.getText().toString());
				});
			}
		}
		catch (SQLException e){
			// Si existe algún error al cargar los datos se indica y se cierra la aplicación
			Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
			errorDeCarga.setTitle("Error carga datos");
			errorDeCarga.setHeaderText("Error al cargar los datos.");
			errorDeCarga.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
			errorDeCarga.setOnCloseRequest((handler)->System.exit(-1));
			errorDeCarga.show();
		}

	}
	
	
	// Métodos
	
	@FXML
	public void pulsarGuardar() throws SQLException {
		try{
			GestorJugadores.getGestorJugadores().guardar(emailTextField.getText(),contrasenaTextField.getText(),Integer.parseInt(seleccionNivelInicial.getText()));
		}
		catch (SQLException e){
			// Si existe algún error al guardar los datos se indica y se cierra la aplicación
			Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
			errorDeCarga.setTitle("Error actualización datos");
			errorDeCarga.setHeaderText("Error al actualizar los datos.");
			errorDeCarga.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
			errorDeCarga.setOnCloseRequest((handler)->System.exit(-1));
			errorDeCarga.show();

		}
	}
	
	@FXML
	public void pulsarVolver(){ GestorVentanas.getGestorVentanas().abrirAdministrar(); }

}