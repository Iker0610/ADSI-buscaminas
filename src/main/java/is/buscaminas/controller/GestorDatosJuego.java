package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.MenuAyuda;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;

public class GestorDatosJuego {
    //Atributos
    private static GestorDatosJuego mGestorDatosJuego;

    //Constructora
    private GestorDatosJuego(){}

    public static GestorDatosJuego getGestorDatosJuego(){
        if (mGestorDatosJuego == null) mGestorDatosJuego = new GestorDatosJuego();
        return mGestorDatosJuego;

    }

    public String mostrarDatosJuego() throws SQLException {
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT  FROM Usuario");
        JsonArray lEmailsJSON = new JsonArray();
        while(resultado.next()){
            JsonObject json = new JsonObject();
            json.put("email", resultado.getString("email"));
            lEmailsJSON.add(json);
        }
        resultado.close();
        return lEmailsJSON.toJson();
    }
    public String getNiveles() {

        return GestorNiveles.getGestorNiveles().obtenerDatosNiveles();
    }
    public void guardarDatos(String pNivel, String pColum, String nFilas, String pMensaje){

    }

}
