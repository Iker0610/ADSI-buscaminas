package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorJugadores;
import is.buscaminas.controller.GestorVentanas;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;


public class GestionarUsuariosController
{
	// Atributos
	
	@FXML
	private Button botonModificar;
	@FXML
	private Button botonEliminar;
	@FXML
	private Button botonVolver;
	@FXML
	private SplitMenuButton seleccionUsuario;
	@FXML
	private ImageView background;
	
	
	//Constructora
	
	@FXML
	private void initialize() throws SQLException {
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
		background.setImage(backgroundImage);
		botonModificar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonModificar.png); -fx-background-color: transparent;");
		botonEliminar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonEliminar.png); -fx-background-color: transparent;");
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");

		//Carga de datos
		try{
			JsonArray jsonArray = Jsoner.deserialize(GestorJugadores.getGestorJugadores().mostrarUsuarios(), new JsonArray());
			for (Object jsonObjectString: jsonArray){
				JsonObject json = (JsonObject)  jsonObjectString;
				MenuItem email = new MenuItem((String) json.get("email"));
				seleccionUsuario.getItems().add(email);
				email.setOnAction((e)->{
					seleccionUsuario.setText(email.getText());
				});
				seleccionUsuario.setText(email.getText());
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
	private void pulsarModificar() {
		GestorJugadores.getGestorJugadores().guardarUsuarioSeleccionado(seleccionUsuario.getText());
		if (!seleccionUsuario.getText().equals("Usuario")){
			GestorVentanas.getGestorVentanas().abrirGestionarDatosUsuario();
		}
	}
	
	@FXML
	private void pulsarEliminar() throws SQLException {
		try{
			GestorJugadores.getGestorJugadores().eliminar(seleccionUsuario.getText());
		}
		catch (SQLException e){
			// Si existe algún error al eliminar un usuario y se cierra la aplicación
			Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
			errorDeCarga.setTitle("Error eliminar usuario");
			errorDeCarga.setHeaderText("Error al eliminar un usuario");
			errorDeCarga.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
			errorDeCarga.setOnCloseRequest((handler)->System.exit(-1));
			errorDeCarga.show();
		}

	}
	
	@FXML
	private void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirAdministrar();
	}
}
