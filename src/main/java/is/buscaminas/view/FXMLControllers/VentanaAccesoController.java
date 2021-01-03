package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class VentanaAccesoController {

    //Atributos normales
    @FXML private TextField nombreTextField;
    @FXML private TextField mailTextField;
    @FXML private TextField contrasenaTextField;
    @FXML private TextField mailRecuperacionTextField;


    //Constructora
    @FXML
    public void initialize (){
        //TODO
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
