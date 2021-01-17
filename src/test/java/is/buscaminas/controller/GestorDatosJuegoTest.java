package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.model.MenuAyuda;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GestorDatosJuegoTest {

    @Test
    void getNiveles() {
        try {
            JsonArray lNiveles = Jsoner.deserialize((String) GestorDatosJuego.getGestorDatosJuego().getNiveles(), new JsonArray());
            for (Object jsonObjectString : lNiveles) {
                JsonObject NivelJson = Jsoner.deserialize((String) jsonObjectString, new JsonObject());
                ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + NivelJson.get("nivel") + "'");
                assertEquals(resultado.getInt("dificultad"), Integer.parseInt(NivelJson.get("dificultad").toString()));
                assertEquals(resultado.getInt("nFilas"), Integer.parseInt(NivelJson.get("nFilas").toString()));
                assertEquals(resultado.getInt("nColumnas"), Integer.parseInt(NivelJson.get("nColumnas").toString()));
                resultado.close();
            }

        }
        catch (SQLException e){
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS");
        }
    }

    @Test
    void getMensajeAyuda() {
        MenuAyuda menu = new MenuAyuda();
        menu.cargarMensaje();
        JsonObject mensajeAyuda = Jsoner.deserialize((String) menu.mostrarDatosAyuda(), new JsonObject());
        JsonObject mensaje = Jsoner.deserialize((String) GestorDatosJuego.getGestorDatosJuego().getMensajeAyuda(), new JsonObject());
        assertEquals(mensajeAyuda.get("mensaje"), mensaje.get("mensaje"));
    }

    @Test
    void guardarDatos() {
        //No se pueden probar los métodos de guardarDatos aquí, ya que al guardar los datos se abre una alert
        //indicando que se han guardado los datos correctamente, o que algún dato introducido no es correcto.
        //Los métodos de guardar que se han podido probar están en GestorNivelesTest.
    }
}