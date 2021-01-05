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

public class GestionarDatosJuegoController {

    @FXML private TextField numMinasTextField;
    @FXML private TextField columnasTextField;
    @FXML private TextField filasTextField;
    @FXML private TextArea ayudaTextArea;
    @FXML private SplitMenuButton seleccionNivel;
    @FXML private Button botonVolver;
    @FXML private Button botonGuardar;
    @FXML private ImageView background;

    @FXML public void initialize(){
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
        background.setImage(backgroundImage);
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
        botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonGuardar.png); -fx-background-color: transparent;");

        //TODO cargar datos (SplitMenu, ayuda, etc)

    }

    @FXML public void pulsarGuardar(){

        //TODO Guardar datos introducidos

        //Volver a la interfaz anterior
        Main.administrar();
    }

    @FXML public void pulsarVolver(){
        Main.administrar();
    }

}
