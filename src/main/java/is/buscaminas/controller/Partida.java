package is.buscaminas.controller;

import is.buscaminas.Main;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.buscaminas.Contador;
import is.buscaminas.model.ranking.Ranking;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Partida {
    /**
     * Esta clase se encarga de administrar la partida:
     * - Guardará el nombre del Usuario
     * - Guardará el nivel de dificultad
     * - Guardará si hay partidas activas o no
     */

    //Atibutos
    private static Partida mPartida;
    private final PropertyChangeSupport lObservers;
    private final Stage ventanaAct;
    private String nombreUsuario;
    private int dificultad;
    private boolean partidaActiva;

    //Constructora
    private Partida ()
    {
        //Se inicializa la partida
        partidaActiva = false;
        lObservers = new PropertyChangeSupport(this);
        ventanaAct = Main.getStage();
    }

    //Singleton
    public static Partida getPartida ()
    {
        if (mPartida == null) mPartida = new Partida();
        return mPartida;
    }

    // Login en la aplicacion
    public void iniciarPartida(String pUsuario, int pDificultad)
    {
        nombreUsuario = pUsuario;
        dificultad    = pDificultad;
    
        inicializarTablero();
    }

    //Metodos publicos de la clase para administrar los atributos:
    public int getDificultad ()
    {
        //Pre:
        //Post: Devuelve el nivel de dificultad
        return dificultad;
    }

    public boolean hayPartidaActiva ()
    {
        //Pre:
        //Post: Devuelve un boolean indicando si hay una partida activa o no
        return partidaActiva;
    }

    public void addObserver (PropertyChangeListener pObserver)
    {
        //Pre: Un observer
        //Post: Se ha añadido el observer a la lista de observers
        lObservers.addPropertyChangeListener(pObserver);
    }

    public void reiniciarPartida ()
    {
        if (partidaActiva) {
            finalizarPartida(false);
        }
        partidaActiva = true;
        inicializarTablero();
    }
    
    //Metodos relacionados a la partida
    public void inicializarTablero()
    {
        //Pre:
        //Post: Se inicia una ventana con una partida de buscaminas
        
        try{
            //Se inicia el tablero
            Tablero.getTablero().iniciarTablero();
            
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/ventanaPartidaBase.fxml"));
            ventanaAct.setScene(new Scene(root));
    
            //Se activa el boolean que indica que existe una partida activa
            partidaActiva = true;

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/ventanaPartidaBase.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void finalizarPartida (boolean pVictoria)
    {
        //Pre: Un boolean indicando si el jugador a ganado o no
        //Post: Se ha finalizado la partida y se han mostrado todas las minas ocultas

        //Se indica que no hay partidas activas
        partidaActiva = false;

        //Se avisa a los observers que ha finalizado la partida y cual ha sido el resultado
        lObservers.firePropertyChange("estadoPartida", null, pVictoria);

        Contador.getContador().parar();

        //Si el usuario ha ganado la partida se envían los datos para actualizar el ranking
        if (pVictoria) {
            Ranking.getRanking().addJugadorRanking(dificultad, nombreUsuario);

            //Se abre la ventana del ranking
            this.mostrarRanking();
            SFXPlayer.getSFXPlayer().stopBackground();
            SFXPlayer.getSFXPlayer().playSFX("victory");
        }

        //Se para y resetea el contador
        Contador.getContador().reset();
    }


    //Métodos relacionados con las diversas pantallas:
    public void mostrarAyuda ()
    {
        // Pre:
        // Post: se carga y muestra una ventana emergente que muestra el menú de ayuda
        try {
            // Se carga el FXML
            Stage ventanaAyuda = new Stage();
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/ventanaMenuAyuda.fxml"));
            ventanaAyuda.setScene(new Scene(root));

            //Se configura el Stage
            ventanaAyuda.setTitle("Ayuda");
            ventanaAyuda.getIcons().add(new Image(new File("src/main/resources/is/buscaminas/temas/mario/assets/logo/ayuda.png").toURI().toString()));
            ventanaAyuda.setResizable(false);
            ventanaAyuda.centerOnScreen();
            ventanaAyuda.initModality(Modality.WINDOW_MODAL);   // Hace que se carge el stage como ventana emergente (ventana hija)
            ventanaAyuda.initOwner(ventanaAct);                 // Se indica cual es la ventana padre y la bloquea

            // Se configuran las acciones al cerrar la ventana -> Se reanuda el contador
            ventanaAyuda.setOnCloseRequest((pHandler) -> {
                SFXPlayer.getSFXPlayer().setBackgroundTheme("marioTheme");
                if (partidaActiva) Contador.getContador().continuar();
            });

            // Finalmente antes de mostrar la ventana se para el contador
            Contador.getContador().parar();

            // Se muestra la ventana
            ventanaAyuda.show();

            SFXPlayer.getSFXPlayer().setBackgroundTheme("storeTheme");
        }
        catch (IOException e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/ventanaMenuAyuda.fxml");
            errorDeCarga.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }

    }

    public void mostrarRanking ()
    {
        // Pre:
        // Post: se carga y muestra una ventana emergente que muestra el menú de ayuda
        try {
            // Se carga el FXML
            Stage ventanaRanking = new Stage();
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_VerRanking.fxml"));
            ventanaRanking.setScene(new Scene(root));

            //Se configura el Stage
            ventanaRanking.setTitle("Ranking");
            ventanaRanking.getIcons().add(new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/logo/ranking.png").toURI().toString()));
            ventanaRanking.setResizable(false);
            ventanaRanking.centerOnScreen();
            ventanaRanking.initModality(Modality.WINDOW_MODAL);   // Hace que se carge el stage como ventana emergente (ventana hija)
            ventanaRanking.initOwner(ventanaAct);                 // Se indica cual es la ventana padre y la bloquea

            // Se configuran las acciones al cerrar la ventana -> Se reanuda el contador
            ventanaRanking.setOnCloseRequest((pHandler) -> {
                SFXPlayer.getSFXPlayer().setBackgroundTheme("marioTheme");
                if (partidaActiva) Contador.getContador().continuar();
            });

            // Finalmente antes de mostrar la ventana se para el contador
            Contador.getContador().parar();

            // Se muestra la ventana
            ventanaRanking.show();
        }
        catch (IOException e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_VerRanking.fxml");
            errorDeCarga.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirMenuPrincipal() {
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_MenuPrincipal.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_MenuPrincipal.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirGestinarCuenta(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_GestionarCuenta.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_GestionarCuenta.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirLogros(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_Logros.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_Logros.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirAdministrar(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_Administrar.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_Administrar.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirAdministrarJuego(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_GestionarDatosJuego.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_GestionarDatosJuego.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirAdministrarUsuarios(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_GestionarUsuarios.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_GestionarUsuarios.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirAdministrarDatosUsuario(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/UI_GestionarDatosUsuario.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/UI_GestionarDatosUsuario.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }

    public void abrirVentanaAcceso(){
        try{
            //Se carga la pantalla y se introduce en el Stage
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/ventanaAcceso.fxml"));
            ventanaAct.setScene(new Scene(root));

            //Se muestra el stage una vez cargado
            ventanaAct.show();
        }
        catch (Exception e) {
            // Si existe algún error al cargar el fxml se indica y se cierra la aplicación
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error carga FXML");
            errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/ventanaAcceso.fxml");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler) -> System.exit(-1));
            errorDeCarga.show();
        }
    }
}
