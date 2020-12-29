package is.buscaminas;

import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {

    //Atibutos
    private static Stage ventanaAct;

    ////////////////////////////////////////////////////////////////////////////////////////
    //NO TOCAR! A NO SER QUE SEPAS MUY MUY MUY BIEN QUE ESTÁS HACIENDO

    //MAIN
    public static void main (String[] args)
    {
        launch(args);
    }

    //Obtener el Stage
    public static Stage getStage ()
    {
        return ventanaAct;
    }

    // Login en la aplicacion
    public static void login (String pUsuario, int pDificultad)
    {

        Partida.getPartida().iniciarPartida(pUsuario, pDificultad);
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void start (Stage pStage)
    {
        //Pre:
        //Post: Se inicia la aplicación

        //Se carga la fuente
        try {
            Font.loadFont(new FileInputStream(new File("src/main/resources/is/buscaminas/fuentes/MarioFont.ttf")), 20);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Se guarda el Stage
        ventanaAct = pStage;

        //Se configura el Stage
        ventanaAct.setTitle("Buscaminas");
        ventanaAct.getIcons().add(new Image(new File("src/main/resources/is/buscaminas/ui/assets/logo/logoBuscaminas.png").toURI().toString()));
        ventanaAct.setResizable(false);
        ventanaAct.centerOnScreen();

        // Se muestra la pantalla de Login
        iniciarLogin();
        SFXPlayer.getSFXPlayer().setBackgroundTheme("marioTheme");
    }

    private void iniciarLogin ()
    {
        //Pre:
        //Post: Se muestra la pantalla login

        try {
            //Se carga la pantalla y se introduce en el Stage
                Parent root = FXMLLoader.load(Main.class.getResource("ui/fxml/ventanaAcceso.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: ui/fxml/ventanaAcceso.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }
}
