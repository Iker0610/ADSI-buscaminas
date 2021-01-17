package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorRanking;
import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Contador;
import is.buscaminas.model.Usuario;
import is.buscaminas.view.uiElements.VistaRanking;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;


public class UI_VerRanking
{
	// Atributos
	
	
	// JSON con los datos del ranking
	JsonObject jsonRanking;
	
	
	// Booleans para controlar el modo
	private boolean personal;
	private boolean porNiveles;
	
	// Zona para los botones de seleccion de nivel
	private HBox seleccionNivel;
	
	// Nivel actualmente seleccionadop
	private Button botonNivelSeleccionado = null;
	
	
	@FXML
	private Button aceptar;
	@FXML
	private StackPane zonaRanking;
	@FXML
	private ImageView background;
	@FXML
	private ImageView title;
	@FXML
	private Button porNivel;
	@FXML
	private Button personalGlobal;
	@FXML
	private VBox vBoxCabecera;
	
	
	//Constructora
	
	public void initialize()
	{
		//Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/fondoRanking.png").toURI().toString());
		background.setImage(backgroundImage);
		Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/titulo.png").toURI().toString());
		title.setImage(titleImage);
		aceptar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/aceptar.gif); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
		porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/clasificarPorNivel.png); -fx-background-color: transparent;");
		personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingPersonal.png); -fx-background-color: transparent;");
		
		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setFloatWindowBackgroundTheme("rankingTheme");
		
		
		// Se settean los parámetros para comprobar el modo
		personal = false;
		porNiveles = false;
		
		// Se obtienen los datos:
		try{
			jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().obtenerRankingGlobal(), new JsonObject());
			mostrarRanking(((JsonArray) jsonRanking.get("ranking")).toJson(), true);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
	}
	
	
	// Métodos
	
	@FXML
	public void pulsarPorNiveles() throws SQLException
	{
		if (porNiveles){
			// Se settea el booleano de control a false
			porNiveles = false;
			
			// Se elimina la sección de la interfaz para seleccionar nivel
			vBoxCabecera.getChildren().remove(seleccionNivel);
			botonNivelSeleccionado = null;
			
			porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingAbsoluto.png); -fx-background-color: transparent;");
			
			// Se obtiene el json adecuado y se carga el ranking
			if (personal){
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().obtenerRankingPersonal(), new JsonObject());
				mostrarRanking(((JsonArray) jsonRanking.get("ranking")).toJson(), true);
			}
			else{
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().obtenerRankingGlobal(), new JsonObject());
				mostrarRanking(((JsonArray) jsonRanking.get("ranking")).toJson(), true);
			}
		}
		else{
			// Se settea el booleano de control a true
			porNiveles = true;
			
			// Se configura el boton de la interfaz
			porNivel.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/clasificarPorNivel.png); -fx-background-color: transparent;");
			
			// Se obtiene el json adecuado
			if (personal){
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().clasificarPorNivelPersonal(), new JsonObject());
			}
			else{
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().clasificarPorNivelGlobal(), new JsonObject());
			}
			
			// Se añade la sección de la interfaz para seleccionar nivel
			cargarZonaSeleccionNivel();
		}
	}
	
	@FXML
	public void pulsarPersonalGlobal() throws SQLException
	{
		if (personal){
			// Se settea el booleano de control a false
			personal = false;
			
			// Se configura el botón
			personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/VerRankingPersonal.png); -fx-background-color: transparent;");
			
			// Se obtiene el json adecuado y se carga el ranking
			if (porNiveles){
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().clasificarPorNivelGlobal(), new JsonObject());
				cargarZonaSeleccionNivel();
			}
			else{
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().obtenerRankingGlobal(), new JsonObject());
				mostrarRanking(((JsonArray) jsonRanking.get("ranking")).toJson(), true);
			}
		}
		else{
			// Se settea el booleano de control a true
			personal = true;
			
			// Se configura el botón
			personalGlobal.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/verRankingGlobal.png); -fx-background-color: transparent;");
			
			// Se obtiene el json adecuado y se carga el ranking
			if (porNiveles){
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().clasificarPorNivelPersonal(), new JsonObject());
				cargarZonaSeleccionNivel();
			}
			else{
				jsonRanking = Jsoner.deserialize(GestorRanking.getGestorRanging().obtenerRankingPersonal(), new JsonObject());
				mostrarRanking(((JsonArray) jsonRanking.get("ranking")).toJson(), true);
			}
		}
		//mostrarRanking(jsonNiveles);
	}
	
	private void mostrarRanking(String pRanking, boolean pMostrarNivel)
	{
		zonaRanking.getChildren().clear();
		zonaRanking.getChildren().add(new VistaRanking(pRanking, pMostrarNivel));
	}
	
	private void cargarZonaSeleccionNivel()
	{
		if (seleccionNivel == null){
			seleccionNivel = new HBox();
			seleccionNivel.setSpacing(40);
			seleccionNivel.setAlignment(Pos.CENTER);
		}
		seleccionNivel.getChildren().clear();
		for (Object nivel : jsonRanking.keySet()){
			Button botonNivel = new Button();
			botonNivel.setId(nivel.toString());
			botonNivel.setText(nivel.toString());
			botonNivel.setStyle("-fx-background-color: black; -fx-border-radius: 2; -fx-border-color: white; -fx-text-fill: white; -fx-font-size: 10");
			botonNivel.setPrefWidth(50);
			botonNivel.setOnMouseClicked(this::cambiarNivel);
			seleccionNivel.getChildren().add(botonNivel);
		}
		if (!vBoxCabecera.getChildren().contains(seleccionNivel)) vBoxCabecera.getChildren().add(seleccionNivel);
		cambiarNivel((Button) seleccionNivel.getChildren().get(0));
	}
	
	private void cambiarNivel(MouseEvent mouseEvent)
	{
		cambiarNivel(((Button) mouseEvent.getSource()));
	}
	
	private void cambiarNivel(Button pBotonNivel)
	{
		if (botonNivelSeleccionado != null){
			botonNivelSeleccionado.setDisable(false);
		}
		botonNivelSeleccionado = pBotonNivel;
		botonNivelSeleccionado.setDisable(true);
		mostrarRanking(((JsonArray) jsonRanking.get(botonNivelSeleccionado.getId())).toJson(), false);
	}
	
	@FXML
	public void pulsarAceptar()
	{
		Stage stage = (Stage) aceptar.getScene().getWindow();
		SFXPlayer.getSFXPlayer().stopFloatWindowBackground();
		if (Partida.getPartida().hayPartidaActiva()) Contador.getContador().continuar();
		stage.close();
	}
}
