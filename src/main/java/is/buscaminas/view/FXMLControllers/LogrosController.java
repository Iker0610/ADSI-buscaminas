package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class LogrosController {

    @FXML private ImageView background;
    @FXML private Button botonVolver;

    //Constructora
    @FXML
    public void initialize(){
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/logros/fondoLogros.png").toURI().toString());
        background.setImage(backgroundImage);
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/gestionarCuenta/botonVolver.png); -fx-background-color: transparent;");

        //TODO cargar todos los logros
    }

    @FXML public void pulsarVolver(){
        Main.login(Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s",""));
    }
}

