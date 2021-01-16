package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.ArrayList;

public class GestorTematica {

    private static GestorTematica mGestorTematica;


    private GestorTematica(){}

    //Patrón Singleton
    public static GestorTematica getGestorTematica(){
        if(mGestorTematica == null){
            mGestorTematica = new GestorTematica();
        }
        return mGestorTematica;
    }

    public void cambiarTematica(String pNombreTema)
    {
        // Pre: Se obtiene la temática seleccionada por el usuario
        // Post: La temática actual se cambia a la seleccionada

        //En la clase Usuario
        Usuario.getUsuario().setTematicaActual(pNombreTema);

        //En la BD
        String mailAct = Usuario.getUsuario().getEmail();
        try {
            GestorDB.getGestorDB().execSQL("UPDATE Usuario SET temaActual = '" + pNombreTema + "' WHERE email = '" + mailAct + "'");
        } catch (SQLException e) {
            Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
            errorDeCarga.setTitle("Error al actualizar la Base de Datos");
            errorDeCarga.setHeaderText("Error al actualizar el nuevo tema seleccionado");
            errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
            errorDeCarga.setOnCloseRequest((handler)->System.exit(-1));
            errorDeCarga.show();
        }
    }

    public String obtenerTemas() throws SQLException {
        // Pre:
        // Post: Se obtienen todos los temas de la Base de Datos y se mandan almacenado en un Json

        //Obtenemos todas las temáticas desbloqueadas por el usuario actual
        String mailAct = Usuario.getUsuario().getEmail();
        ResultadoSQL temasUser = GestorDB.getGestorDB().execSELECT("SELECT nombreTema FROM Logro WHERE nombre IN (SELECT nombreLogro FROM LogrosUsuario WHERE email = '" + mailAct + "' AND fechaObtencion IS NOT NULL)");

        //Metemos las temáticas desbloqueadas en un ArrayList
        ArrayList<String> temasDesbloq = new ArrayList<>();
        while (temasUser.next()){
            temasDesbloq.add(temasUser.getString("nombreTema"));
        }

        //Cerramos la conexión con la BD
        temasUser.close();

        //Obtenemos todos los temas de la BD
        ResultadoSQL allTemas = GestorDB.getGestorDB().execSELECT("SELECT * FROM Tematica");

        //Creamos el JSON
        JsonArray listaTemasJson = new JsonArray();

        //Introducimos los datos en el JSON.
        //Estructura del JSON: [ { descripcion, desbloqueado?, nombreTema }, { ... }, ... ]
        while(allTemas.next()){
            JsonObject temaJson = new JsonObject();
            temaJson.put("nombre", allTemas.getString("nombre"));
            temaJson.put("descripcion", allTemas.getString("descripcion"));
            if(temasDesbloq.contains(allTemas.getString("nombre"))) { temaJson.put("bloqueada", false); }
            else{ temaJson.put("bloqueada", true); }
            listaTemasJson.add(temaJson);
        }

        //Cerramos la conexción con la BD
        allTemas.close();

        //Devolvemos el Json en forma de String
        return listaTemasJson.toJson();
    }
}
