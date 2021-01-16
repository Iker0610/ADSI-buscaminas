package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorDatosJuego;
import is.buscaminas.controller.GestorJugadores;
import is.buscaminas.controller.GestorVentanas;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;


public class UI_GestionarDatosJuego
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
	public void initialize(){
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
		background.setImage(backgroundImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
		botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonGuardar.png); -fx-background-color: transparent;");

		//Cargar datos
		JsonArray lNiveles = Jsoner.deserialize(GestorDatosJuego.getGestorDatosJuego().getNiveles(), new JsonArray());

		for (Object jsonObjectString: lNiveles) {
			JsonObject NivelJson = Jsoner.deserialize((String) jsonObjectString, new JsonObject());
			MenuItem nivel = new MenuItem((String) NivelJson.get("nivel").toString());
			seleccionNivel.getItems().add(nivel);
			nivel.setOnAction((e) -> {
				seleccionNivel.setText(nivel.getText().toString());
				columnasTextField.setText(NivelJson.get("nColumnas").toString());
				filasTextField.setText(NivelJson.get("nFilas").toString());
				numMinasTextField.setText(NivelJson.get("dificultad").toString());
			});
			seleccionNivel.setText(nivel.getText().toString());
			columnasTextField.setText(NivelJson.get("nColumnas").toString());
			filasTextField.setText(NivelJson.get("nFilas").toString());
			numMinasTextField.setText(NivelJson.get("dificultad").toString());
		}
		JsonObject menu = Jsoner.deserialize(GestorDatosJuego.getGestorDatosJuego().getMensajeAyuda(), new JsonObject());
		ayudaTextArea.setText(menu.get("mensaje").toString());
		
	}
	
	
	// Métodos
	
	@FXML
	public void pulsarGuardar() throws SQLException {
		try{
			GestorDatosJuego.getGestorDatosJuego().guardarDatos(seleccionNivel.getText(),numMinasTextField.getText(),columnasTextField.getText(),filasTextField.getText(),ayudaTextArea.getText());
		}
		catch (SQLException e){

		}
	}
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirAdministrar();
	}
	
}
