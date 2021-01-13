package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorCuentaUsuario;
import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.model.Usuario;
import is.buscaminas.view.uiElements.VistaTematica;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.SQLException;


public class GestionarCuentaController
{
	// Atributos
	private VistaTematica temaSeleccionado;

	@FXML
	private VBox columnaBotones;
	@FXML
	TextField nuevaContra;
	@FXML
	private ImageView background;
	@FXML
	private Button botonVolver;
	@FXML
	private Button botonGuardar;
	@FXML
	private Button botonCambiar;
	
	
	//Constructora
	
	@FXML
	void initialize() throws SQLException
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/gestionarCuenta/fondoGestionarCuenta.png").toURI().toString());
		background.setImage(backgroundImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/gestionarCuenta/botonVolver.png); -fx-background-color: transparent;");
		botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/gestionarCuenta/botonGuardar.png); -fx-background-color: transparent;");
		botonCambiar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/gestionarCuenta/botonCambiar.png); -fx-background-color: transparent;");
		
		//Cargar Temáticas
		JsonArray listaTemasJson = Jsoner.deserialize(GestorCuentaUsuario.getGestorCuentaUsuario().obtenerTemas(), new JsonArray());
		for (Object jsonObject : listaTemasJson){
			JsonObject temaJson = (JsonObject) jsonObject;
			columnaBotones.getChildren().add(generarBoton((String) temaJson.get("nombre"), (String) temaJson.get("descripcion"), (boolean) temaJson.get("bloqueada")));
		}
		
	}
	
	
	// Métodos
	
	private VistaTematica generarBoton(String pNombre, String pDescripcion, boolean pBloqueada)
	{
		VistaTematica nuevoTema = new VistaTematica(pNombre, pDescripcion, pBloqueada);
		nuevoTema.setId(pNombre);
		if (pBloqueada) nuevoTema.setMouseTransparent(true);
		if (pNombre.equals(Usuario.getUsuario().getTematicaActual())) temaSeleccionado = nuevoTema;
		nuevoTema.setOnMousePressed(this::seleccionarTematica);
		
		return nuevoTema;
	}
	
	private void seleccionarTematica(MouseEvent pEvento)
	{
		VistaTematica nuevoTemaSeleccionado = (VistaTematica) pEvento.getSource();
		if (nuevoTemaSeleccionado.equals(temaSeleccionado)){
			temaSeleccionado.setMouseTransparent(false);
			temaSeleccionado = nuevoTemaSeleccionado;
			temaSeleccionado.setMouseTransparent(true);
		}
	}
	
	@FXML
	void pulsarCambiarContrasena()
	{
		GestorCuentaUsuario.getGestorCuentaUsuario().cambiarContrasena(nuevaContra.getText());
	}
	
	@FXML
	public void pulsarVolver()
	{
		//Se abre el menú principal
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
	
	@FXML
	public void pulsarGuardar() throws SQLException {

		if (temaSeleccionado != null){
			GestorCuentaUsuario.getGestorCuentaUsuario().cambiarTematica(temaSeleccionado.getId());
		}
		//Se abre el menú principal
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
}
