package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.MenuAyuda;
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

    public String getMensajeAyuda(){
        MenuAyuda menu = new MenuAyuda();
        menu.cargarMensaje();
        return menu.mostrarDatosAyuda();
    }
    public void guardarDatos(String pNivel,String pDificultad, String pColum, String nFilas, String pMensaje) throws SQLException {
        try {
            Boolean resultadoN = true;
            if (esNumerico(pDificultad) && esNumerico(pColum) && esNumerico(nFilas)) {
                int nivel = Integer.parseInt(pNivel);
                int dificultad = Integer.parseInt(pDificultad);
                int colum = Integer.parseInt(pColum);
                int filas = Integer.parseInt(nFilas);
                    resultadoN = GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, colum, filas);
            }
            else{
                mostrarNoNumerico();
            }
            MenuAyuda menuAyuda = new MenuAyuda();
            boolean resultadoM = menuAyuda.guardarMensaje(pMensaje);
            if (resultadoN && resultadoM) {
                mostrarMensajeGuardar();
            } else {
                mostrarMensajeError();
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
            return false;
        }
    }
    private void mostrarMensajeError(){
        Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
        errorDeActualizacion.setTitle("Error actualización datos");
        errorDeActualizacion.setHeaderText("Ha ocurrido un error al actualizar los datos");
        errorDeActualizacion.show();
    }
    private void mostrarMensajeGuardar(){
        Alert datosGuardados = new Alert(Alert.AlertType.CONFIRMATION);
        datosGuardados.setTitle("Datos atualizados");
        datosGuardados.setHeaderText("Los datos se han actualizado correctamente");
        datosGuardados.show();
    }
    private void mostrarNoNumerico(){
        Alert noNumerico= new Alert(Alert.AlertType.ERROR);
        noNumerico.setTitle("No numerico");
        noNumerico.setHeaderText("Por favor introduce los datos en el formato correcto.");
        noNumerico.show();
    }

}
