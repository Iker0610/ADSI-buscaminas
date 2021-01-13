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
        GestorDB.getGestorDB().execSQL("UPDATE Usuario SET temaActual = %pNombreTema% WHERE email = %mailAct%");
    }

    public String cargarTemas() throws SQLException {
        //Obtenemos todos los temas
        ResultadoSQL rdo1 = GestorDB.getGestorDB().execSELECT("SELECT * FROM Tematica");

        //Obtenemos todos los desbloqueados por el usuario actual
        String mailAct = Usuario.getUsuario().getEmail();
        ResultadoSQL rdo2 = GestorDB.getGestorDB().execSELECT("SELECT nombreTema FROM Logro WHERE nombre IN (SELECT nombreLogro FROM LogrosUsuario WHERE email = %mailAct% AND fechaObtencion IS NOT NULL");

        //Creamos el JSON y la lista
        JsonObject temasJSON = new JsonObject();
        ArrayList temasDesbloq = new ArrayList();

        //Introducimos los temas desbloqueados por el usuario en la lista
        while (rdo2.next()){
            temasDesbloq.add(rdo2.getString("nombreTema"));
        }
        //Introducimos los datos en el JSON.
        //Estructura del JSON: { "1":[nombreTema,descripcion,desbloqueado?],"2": ... }
        int i = 1;
        while(rdo1.next()){
            JsonArray list = new JsonArray();
            list.add(rdo1.getString("nombre"));
            list.add(rdo1.getString("descripcion"));
            if(temasDesbloq.contains(rdo1.getString("nombre"))) { list.add(true); }
            else{ list.add(false); }
            temasJSON.put(Integer.toString(i), list);
            i++;
        }
        return temasJSON.toString();
    }
}
