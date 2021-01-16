package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.Arrays;

public class GestorDatosJuego {
    //Atributos
    private static GestorDatosJuego mGestorDatosJuego;

    //Constructora
    private GestorDatosJuego(){}

    public static GestorDatosJuego getGestorDatosJuego(){
        if (mGestorDatosJuego == null) mGestorDatosJuego = new GestorDatosJuego();
        return mGestorDatosJuego;

    }

    public String getNiveles() {

        return GestorNiveles.getGestorNiveles().obtenerDatosNiveles();
    }
    public void guardarDatos(String pNivel,String pDificultad, String pColum, String nFilas, String pMensaje) throws SQLException {
        try {
            if (esNumerico(pNivel) && esNumerico(pDificultad) && esNumerico(pColum) && esNumerico(nFilas)) {
                int nivel = Integer.parseInt(pNivel);
                int dificultad = Integer.parseInt(pDificultad);
                int colum = Integer.parseInt(pColum);
                int filas = Integer.parseInt(nFilas);
                if (dificultadAdecuada(dificultad, filas, colum)) {
                    Boolean resultado = GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, colum, filas);
                    if(resultado){
                        //TODO actualizar ayuda
                    }
                    else{
                        mostrarMensajeError();
                    }
                }
            }
        }
        catch (SQLException e){
            Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
            errorDeActualizacion.setTitle("Error actualización datos");
            errorDeActualizacion.setHeaderText("Ha ocurrido un error al actualizar los datos");
            errorDeActualizacion.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
            errorDeActualizacion.setOnCloseRequest((handler)->System.exit(-1));
            errorDeActualizacion.show();
        }
    }
    private boolean esNumerico(String pCadena){
        try {
            Integer.parseInt(pCadena);
            return true;
        } catch (NumberFormatException nfe){
            // Si existe algún error al cargar los datos se indica y se cierra la aplicación
            Alert noNumerico= new Alert(Alert.AlertType.ERROR);
            noNumerico.setTitle("No numerico");
            noNumerico.setHeaderText("Por favor introduce los datos en el formato correcto.");
            noNumerico.show();
            return false;
        }
    }
    private boolean dificultadAdecuada(int pDificultad, int pFilas, int pColum){
        if (pDificultad<pFilas*pColum){
            return true;
        }
        else{
            return false;
        }

    }
    private void mostrarMensajeError(){
        Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
        errorDeActualizacion.setTitle("Error actualización datos");
        errorDeActualizacion.setHeaderText("Ha ocurrido un error al actualizar los datos");
        errorDeActualizacion.show();
    }

}
