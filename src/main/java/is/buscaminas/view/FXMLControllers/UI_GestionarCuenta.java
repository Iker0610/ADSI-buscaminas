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
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.SQLException;


public class UI_GestionarCuenta
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
		
		//Cargar botones para seleccionae temática nueva
		JsonArray listaTemasJson = Jsoner.deserialize(GestorCuentaUsuario.getGestorCuentaUsuario().obtenerTemas(), new JsonArray());
		for (Object jsonObject : listaTemasJson){
			JsonObject temaJson = (JsonObject) jsonObject;
			columnaBotones.setSpacing(10);
			columnaBotones.getChildren().add(generarBoton((String) temaJson.get("nombre"), (String) temaJson.get("descripcion"), (boolean) temaJson.get("bloqueada")));
		}
		
	}
	
	// Se genera un botón (VBox) con los datos de cada temática
	private VistaTematica generarBoton(String pNombre, String pDescripcion, boolean pBloqueada)
	{
		// Pre: Se obtienen los datos de una temática a cargar
		// Post: Se genera un botón para dicha temática
		VistaTematica nuevoTema = new VistaTematica(pNombre, pDescripcion, pBloqueada);
		nuevoTema.setId(pNombre);
		if (pNombre.equals(Usuario.getUsuario().getTematicaActual())) temaSeleccionado = nuevoTema;
		nuevoTema.setOnMouseClicked(this::seleccionarTematica);
		
		return nuevoTema;
	}

	private void seleccionarTematica(MouseEvent pEvento)
	{
		// Pre: Se ha pulsado sobre un botón
		// Post: El botón ha cambiado de estilo y se ha guardado el nombre seleccionado
		VistaTematica nuevoTemaSeleccionado = (VistaTematica) pEvento.getSource();
		if (temaSeleccionado != null){
			temaSeleccionado.setMouseTransparent(false);
			temaSeleccionado.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: white; -fx-border-width: 3;");
		}
		temaSeleccionado = nuevoTemaSeleccionado;
		temaSeleccionado.setMouseTransparent(true);
		temaSeleccionado.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: #f9a602; -fx-border-width: 3;");
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
			// Se actualiza el tema seleccionado
			GestorCuentaUsuario.getGestorCuentaUsuario().cambiarTematica(temaSeleccionado.getId());
		}
		//Se abre el menú principal
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
}
