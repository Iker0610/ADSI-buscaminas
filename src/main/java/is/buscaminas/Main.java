package is.buscaminas;


import is.buscaminas.controller.GestorVentanas;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application
{
    
    ////////////////////////////////////////////////////////////////////////////////////////
    //NO TOCAR! A NO SER QUE SEPAS MUY MUY MUY BIEN QUE ESTÁS HACIENDO
    
    //MAIN
    public static void main(String[] args)
    {
        launch(args);
    }
    
    @Override
    public void start(Stage pStage)
    {
        //Pre:
        //Post: Se inicia la aplicación
        
        //Se carga el Gestor de Ventanas
        GestorVentanas.crearGestorVentanas(pStage);
        
        // Se muestra la pantalla de Login
        GestorVentanas.getGestorVentanas().abrirLogin();
    }
}
