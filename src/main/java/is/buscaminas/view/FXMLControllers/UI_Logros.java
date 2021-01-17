package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorCuentaUsuario;
import is.buscaminas.controller.GestorLogros;
import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.model.Usuario;
import is.buscaminas.view.uiElements.VistaLogro;
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

		//Hacer las vistas
		JsonObject jLogros=Jsoner.deserialize(GestorLogros.getGestorLogros().getLogros(), new JsonObject());
		JsonArray logrosObtenidos=Jsoner.deserialize((String) jLogros.get("logrosObtenidos"),new JsonArray());
		JsonArray logrosRestantes=Jsoner.deserialize((String) jLogros.get("logrosRestantes"),new JsonArray());

		for(Object jDatosLogro : logrosObtenidos){
			JsonObject jDatos=Jsoner.deserialize((String) jDatosLogro,new JsonObject());
			VistaLogro vista=new VistaLogro((String)jDatos.get("nombre"),(String)jDatos.get("descripcion"),jDatos.get("avance").toString(),jDatos.get("objetivo").toString(),(String)jDatos.get("fechaObtencion"),(String)jDatos.get("nombreTema"));
			zonaLogros.getChildren().add(vista);
		}

		for(Object jDatosLogro : logrosRestantes){
			JsonObject jDatos=Jsoner.deserialize((String) jDatosLogro,new JsonObject());
			VistaLogro vista=new VistaLogro((String)jDatos.get("nombre"),(String)jDatos.get("descripcion"),jDatos.get("avance").toString(),jDatos.get("objetivo").toString(),(String)jDatos.get("fechaObtencion"),(String)jDatos.get("nombreTema"));
			zonaLogros.getChildren().add(vista);
		}

	}
	
	
	// Métodos
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
}

