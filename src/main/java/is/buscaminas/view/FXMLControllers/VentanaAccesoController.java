package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class VentanaAccesoController {

    @FXML private TextField nombreTextField;
    @FXML private TextField mailTextField;
    @FXML private TextField contrasenaTextField;
    @FXML private TextField mailRecuperacionTextField;
    @FXML private ImageView background;
    @FXML private ImageView title;
    @FXML private Button botonEntrar;


    //Constructora
    @FXML
    public void initialize (){
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/acceso/fondoAcceso.png").toURI().toString());
        background.setImage(backgroundImage);
        Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/acceso/buscaminas.png").toURI().toString());
        title.setImage(titleImage);
        botonEntrar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/acceso/botonEntrar.png); -fx-background-color: transparent;");
    }

    @FXML
    public void pulsarAceptar ()
    {
        // Si se introduce un nombre y TODO login correcto
        if (!nombreTextField.getText().equals("")) {

            // Guardamos el nombre de jugador
            String nombreUser = nombreTextField.getText();

            //TODO Login

            // Se abre el menú principal
            Main.login();

        }
        else {
            // Se le indica al usuario que ha de seleccionar un nombre y una dificultad
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Datos incorrectos");
            alerta.setHeaderText(null);
            alerta.setContentText("Error al iniciar sesión");
            alerta.show();
        }
    }

    @FXML
    public void recuperarContrasena(){
        //TODO
    }

    @FXML
    public void loginRedSocial(){
        //TODO
    }
}
