package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;
import java.util.ArrayList;

public class GestorTematica {

    private static GestorTematica mGestorTematica;


    private GestorTematica(){}


    public static GestorTematica getGestorTematica(){
        if(mGestorTematica == null){
            mGestorTematica = new GestorTematica();
        }
        return mGestorTematica;
    }

    public void cambiarTematica(String pNombreTema) throws SQLException {
        Usuario.getUsuario().setTematicaActual(pNombreTema);
        String mailAct = Usuario.getUsuario().getEmail();
        GestorDB.getGestorDB().execSQL("UPDATE Usuario SET temaActual = " + pNombreTema + " WHERE email = " + mailAct);
    }

    public String obtenerTemas() throws SQLException {
    
        //Obtenemos todos los desbloqueados por el usuario actual y los metemos en un arraylist
        String mailAct = Usuario.getUsuario().getEmail();
        ArrayList<String> temasDesbloq = new ArrayList<>();
        ResultadoSQL temasUser = GestorDB.getGestorDB().execSELECT("SELECT nombreTema FROM Logro WHERE nombre IN (SELECT nombreLogro FROM LogrosUsuario WHERE email = '" + mailAct + "' AND fechaObtencion IS NOT NULL)");
    
        while (temasUser.next()){
            temasDesbloq.add(temasUser.getString("nombreTema"));
        }
        temasUser.close();
    
        //Obtenemos todos los temas
        ResultadoSQL allTemas = GestorDB.getGestorDB().execSELECT("SELECT * FROM Tematica");
    
        //Creamos el JSON
        JsonArray listaTemasJson = new JsonArray();

        //Introducimos los datos en el JSON.
        //Estructura del JSON: [ {nombreTema,descripcion,desbloqueado?},{...}, ... ]
        while(allTemas.next()){
            JsonObject temaJson = new JsonObject();
            temaJson.put("nombre", allTemas.getString("nombre"));
            temaJson.put("descripcion", allTemas.getString("descripcion"));
            if(temasDesbloq.contains(allTemas.getString("nombre"))) { temaJson.put("bloqueada", false); }
            else{ temaJson.put("bloqueada", true); }
            listaTemasJson.add(temaJson);
        }
        allTemas.close();
        return listaTemasJson.toJson();
    }
}
