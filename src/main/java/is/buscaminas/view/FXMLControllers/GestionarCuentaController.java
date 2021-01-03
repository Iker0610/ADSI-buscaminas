package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import javafx.fxml.FXML;

import java.net.MalformedURLException;
import java.sql.SQLException;

public class GestionarCuentaController {

    //@FXML TextField nuevaContra;

    //Constructora
    @FXML void initialize () throws MalformedURLException {
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
