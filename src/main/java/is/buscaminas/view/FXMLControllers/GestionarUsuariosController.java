package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class GestionarUsuariosController {
    @FXML private Button botonModificar;
    @FXML private Button botonEliminar;
    @FXML private Button botonVolver;
    @FXML private SplitMenuButton seleccionUsuario;
    @FXML private ImageView background;

    @FXML private void initialize(){
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/inicio/fondoAdmin.png").toURI().toString());
        background.setImage(backgroundImage);
        botonModificar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonModificar.png); -fx-background-color: transparent;");
        botonEliminar.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonEliminar.png); -fx-background-color: transparent;");
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/inicio/botonVolver.png); -fx-background-color: transparent;");
    }

    @FXML private void pulsarModificar(){
        //TODO pasar usuario
        Main.administrarDatosUsuario();
    }
    @FXML private void pulsarEliminar(){
        //TODO
    }
    @FXML private void pulsarVolver(){
        Main.administrar();
    }
}
