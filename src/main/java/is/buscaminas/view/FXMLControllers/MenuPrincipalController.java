package is.buscaminas.view.FXMLControllers;


import is.buscaminas.controller.GestorVentanas;
import is.buscaminas.controller.Partida;
import is.buscaminas.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public class MenuPrincipalController
{

    @FXML private ImageView background;
    @FXML private ImageView title;
    @FXML private Button botonRanking;
    @FXML private Button botonLogros;
    @FXML private Button botonGestionarCuenta;
    @FXML private Button botonAyuda;
    @FXML private Button botonVolver;
    @FXML private Button botonJugar;

    //Constructora
    @FXML private void initialize(){
        //Cargar temática
        Image backgroundImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/fondoPrincipal.png").toURI().toString());
        background.setImage(backgroundImage);
        Image titleImage = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/buscaminas.png").toURI().toString());
        title.setImage(titleImage);
        botonRanking.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/botonRanking.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
        botonLogros.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/botonLogros.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
        botonGestionarCuenta.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/botonGestionarCuenta.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
        botonAyuda.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/botonAyuda.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
        botonVolver.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/botonVolver.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
        botonJugar.setStyle("-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/principal/botonJugar.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-color: transparent;");
    }

    @FXML public void pulsarJugar (){
        Partida.getPartida().iniciarPartida(1);
    }
    @FXML public void pulsarVolver (){
        GestorVentanas.getGestorVentanas().abrirMenuPrincipal();
    }
    @FXML public void pulsarVerRanking (){
        GestorVentanas.getGestorVentanas().mostrarRankingEmergente();
    }
    @FXML public void pulsarVerLogros (){
        GestorVentanas.getGestorVentanas().abrirLogros();
    }
    @FXML public void pulsarGestionarCuenta (){
        GestorVentanas.getGestorVentanas().abrirGestionarCuenta();
    }
    @FXML public void pulsarAyuda(){
        GestorVentanas.getGestorVentanas().mostrarAyudaEmergente();
    }
}
