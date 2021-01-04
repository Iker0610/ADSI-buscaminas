package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class GestionarCuentaController {

    //@FXML TextField nuevaContra;
    @FXML private ImageView background;
    @FXML private Button botonVolver;
    @FXML private Button botonGuardar;
    @FXML private Button botonCambiar;

    //Constructora
    @FXML void initialize () throws MalformedURLException {
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/gestionarCuenta/fondoGestionarCuenta.png").toURI().toString());
        background.setImage(backgroundImage);
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/gestionarCuenta/botonVolver.png); -fx-background-color: transparent;");
        botonGuardar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/gestionarCuenta/botonGuardar.png); -fx-background-color: transparent;");
        botonCambiar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/gestionarCuenta/botonCambiar.png); -fx-background-color: transparent;");

        //TODO cargar todas las temáticas

    }

    @FXML void pulsarCambiarContrasena() throws SQLException {
        //No funcionará hasta que el login esté implementado
        //GestorCuentaUsuario.getGestorCuentaUsuario().cambiarContrasena(nuevaContra.getText());
    }

    @FXML public void pulsarVolver(){
        //Se abre el menú principal
        Main.login();
    }

    @FXML public void pulsarGuardar(){
        //TODO se guarda la temática seleccionada

        //Se abre el menú principal
        Main.login();
    }


}
