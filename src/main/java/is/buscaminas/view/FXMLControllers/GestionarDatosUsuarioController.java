package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class GestionarDatosUsuarioController {

    @FXML private TextField emailTextField;
    @FXML private TextField contrasenaTextField;
    @FXML private SplitMenuButton seleccionNivelInicial;
    @FXML private Button botonVolver;
    @FXML private Button botonGuardar;
    @FXML private ImageView background;

    @FXML public void initialize(){
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
        background.setImage(backgroundImage);
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
        botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonGuardar.png); -fx-background-color: transparent;");

        //TODO cargar datos (SplitMenu, mail, etc)

    }

    @FXML public void pulsarGuardar(){

        //TODO Guardar datos introducidos

        //Volver a la interfaz anterior
        Main.administrarUsuarios();
    }

    @FXML public void pulsarVolver(){ Main.administrarUsuarios(); }

}