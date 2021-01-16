package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.model.Contador;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;


public class UI_MenuAyuda
{
	// Elementos FXML
	@FXML private Button botonSalir;
	@FXML private ImageView background;
	@FXML private VBox zonaTexto;

	
	// Construtora
	@FXML
	private void initialize()
	{
		// Cargar temática
		Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ayuda/fondoAyuda.png").toURI().toString());
		background.setImage(backgroundImage);
		botonSalir.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ayuda/botonSalir.png); -fx-background-color: transparent;");

		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setFloatWindowBackgroundTheme("helpTheme");

		// TODO Cargar texto de ayuda

	}
	
	
	// Metodos
	
	@FXML public void pulsarSalir(){
		Stage stage = (Stage) botonSalir.getScene().getWindow();
		SFXPlayer.getSFXPlayer().stopFloatWindowBackground();
		if (Partida.getPartida().hayPartidaActiva()) Contador.getContador().continuar();
		stage.close();
	}
}
