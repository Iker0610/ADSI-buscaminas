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
        //Pre:
        //Post: Se obtiene un Json con los datos de todos los niveles.

        return GestorNiveles.getGestorNiveles().obtenerDatosNiveles();
    }

    public String getMensajeAyuda(){
        //Pre:
        //Post: Se obtiene un Json con el mensaje de ayuda.
        MenuAyuda menu = new MenuAyuda();
        menu.cargarMensaje();
        return menu.mostrarDatosAyuda();
    }
    public void guardarDatos(String pNivel,String pDificultad, String pColum, String nFilas, String pMensaje) throws SQLException {
        //Pre:
        //Post: Se han guardado los datos tanto de los niveles como del mensaje de ayuda.
        try {
            Boolean resultadoN = false;
            if (esNumerico(pDificultad) && esNumerico(pColum) && esNumerico(nFilas)) {
                if(dificultadAdecuada(Integer.parseInt(pDificultad), Integer.parseInt(nFilas), Integer.parseInt(pColum))) {
                    int nivel = Integer.parseInt(pNivel);
                    int dificultad = Integer.parseInt(pDificultad);
                    int colum = Integer.parseInt(pColum);
                    int filas = Integer.parseInt(nFilas);
                    resultadoN = GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, colum, filas);
                }
                else {
                    mostrarDificultadNoAdecuada();
                }
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
        //Pre: un String.
        //Post: Devuelve true si el String introducido es un int, false en caso contrario.
        try {
            Integer.parseInt(pCadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    private void mostrarMensajeError(){
        //Se muestra una alert indicando que ha ocurrido un error.
        Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
        errorDeActualizacion.setTitle("Error actualización datos");
        errorDeActualizacion.setHeaderText("Ha ocurrido un error al actualizar los datos");
        errorDeActualizacion.show();
    }
    private void mostrarMensajeGuardar(){
        //Se muestra una alert indicando que se han actualizado los datos correctamente.
        Alert datosGuardados = new Alert(Alert.AlertType.CONFIRMATION);
        datosGuardados.setTitle("Datos atualizados");
        datosGuardados.setHeaderText("Los datos se han actualizado correctamente");
        datosGuardados.show();
    }
    private void mostrarNoNumerico(){
        //Se muestra una alert indicando que se no han introducido integers en las casillas de dificultas, columnas o filas..
        Alert noNumerico= new Alert(Alert.AlertType.ERROR);
        noNumerico.setTitle("No numerico");
        noNumerico.setHeaderText("Por favor introduce los datos en el formato correcto.");
        noNumerico.show();
    }
    private boolean dificultadAdecuada( int pDificultad, int pFilas, int pColumnas){
        //Pre: tres integers.
        //Post: devuelve true si la dificultad es menor que el número de casillas, mayor que 1 y menor que 51.
        int resultado = pFilas * pColumnas;
        if(pDificultad < resultado && (pDificultad > 1) && (pDificultad < 51)){
            return true;
        }
        else{
            return false;
        }
    }
    private void mostrarDificultadNoAdecuada(){
        //Se muestra una alert indicando que la dificultad introducida no es adecuada.
        Alert noAdecuada= new Alert(Alert.AlertType.ERROR);
        noAdecuada.setTitle("No adecuada");
        noAdecuada.setHeaderText("La dificultad debe ser menor que el número de casillas del tablero");
        noAdecuada.show();
    }

}
