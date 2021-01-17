package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorNiveles;
import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.File;
import java.sql.SQLException;


public class UI_Acceso
{
	//Atributos normales
	
	private ToggleGroup dificultadGroup;
	
	//Atributos FXML
	
	@FXML
	private HBox zonaDificultades;
	@FXML
	private Button botonJugar;
	@FXML
	private Button botonVolver;
	@FXML
	private ImageView background;
	@FXML
	private ImageView title;
	
	
	//Constructora
	
	@FXML
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/fondoAcceso.png").toURI().toString());
		background.setImage(backgroundImage);
		Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/buscaminas.png").toURI().toString());
		title.setImage(titleImage);
		botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/botonVolver.png); -fx-background-color: transparent;");
		botonJugar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/acceso/botonEntrar.png); -fx-background-color: transparent;");
		
		// Se pone el tema de fondo
		SFXPlayer.getSFXPlayer().setBackgroundTheme("mainTheme");
		
		//Se cargan los radio buttons
		dificultadGroup = new ToggleGroup();
		cargarNiveles();
	}
	
	// Método para cargar los niveles
	private void cargarNiveles()
	{
		String dificultadPredeternminada = Integer.toString(Usuario.getUsuario().getNivelInicial());
		
		JsonArray json = Jsoner.deserialize(GestorNiveles.getGestorNiveles().obtenerDatosNiveles(), new JsonArray());
		for (Object child : json){
			JsonObject jsonNivel = Jsoner.deserialize((String) child, new JsonObject());
			String nivel = jsonNivel.get("nivel").toString();
			RadioButton seleccionNivel = new RadioButton();
			seleccionNivel.setId(nivel);
			seleccionNivel.setText(nivel);
			seleccionNivel.setMnemonicParsing(false);
			seleccionNivel.setFont(new Font("System Bold", 13));
			seleccionNivel.setToggleGroup(dificultadGroup);
			zonaDificultades.getChildren().add(seleccionNivel);
			if (nivel.equals(dificultadPredeternminada)){
				seleccionNivel.setSelected(true);
			}
		}
	}
	
	// Métodos
	
	@FXML
	public void pulsarAceptar()
	{
		// Si se selcciona una dificultad
		if (dificultadGroup.getSelectedToggle() != null){
			
			// Introducimos la dificultad seleccionada
			int numDificultad;
			String dificultad = ((Node) dificultadGroup.getSelectedToggle()).getId();
			
			try{
				numDificultad = Integer.parseInt(dificultad);
			}
			catch (Exception e){
				numDificultad = 1;
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "ERROR (introducir dificultad)", ButtonType.YES, ButtonType.NO);
				alert.show();
			}
			
			// Se inicia la partida
			SFXPlayer.getSFXPlayer().playSFX("gameIntro");
			Partida.getPartida().iniciarPartida(numDificultad);
			
		}
		else{
			// Se le indica al usuario que ha de seleccionar un nombre y una dificultad
			Alert alerta = new Alert(Alert.AlertType.WARNING);
			alerta.setTitle("Datos incorrectos");
			alerta.setHeaderText(null);
			alerta.setContentText("Introduce una dificultad");
			alerta.show();
		}
	}
	
	@FXML
	public void pulsarVolver()
	{
		GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
	}
}
