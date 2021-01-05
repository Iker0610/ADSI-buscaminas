package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class AdministrarController {

    @FXML private ImageView background;
    @FXML private Button botonGestionarUsuarios;
    @FXML private Button botonGestionarJuego;
    @FXML private Button botonVolver;

    //Constructora
    @FXML public void initialize(){
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
        background.setImage(backgroundImage);
        botonGestionarJuego.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonJuego.png); -fx-background-color: transparent;");
        botonGestionarUsuarios.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonUsuarios.png); -fx-background-color: transparent;");
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
    }

    @FXML public void pulsarGestionarUsuarios(){
        Main.administrarUsuarios();
    }

    @FXML public void pulsarGestionarJuego(){
        Main.administrarJuego();
    }

    @FXML public void pulsarVolver(){
        Main.login(Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s",""));
    }
}
