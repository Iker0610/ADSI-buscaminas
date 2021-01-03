package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import javafx.fxml.FXML;

public class MenuPrincipalController {

    @FXML public void pulsarJugar (){
        Main.jugar();
    }
    @FXML public void pulsarVolver (){
        Main.iniciarLogin();
    }
    @FXML public void pulsarVerRanking (){
        //TODO
    }
    @FXML public void pulsarVerLogros (){
        Main.verLogros();
    }
    @FXML public void pulsarGestionarCuenta (){
        Main.gestionarCuenta();
    }
    @FXML public void pulsarAyuda(){
        //TODO
    }
}
