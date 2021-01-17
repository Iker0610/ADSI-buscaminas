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
        JsonObject mensajeAyuda = Jsoner.deserialize((String) menu.mostrarDatosAyuda(), new JsonObject());
        JsonObject mensaje = Jsoner.deserialize((String) GestorDatosJuego.getGestorDatosJuego().getMensajeAyuda(), new JsonObject());
        assertEquals(mensajeAyuda.get("mensaje"), mensaje.get("mensaje"));
    }

    @Test
    void guardarDatos() {
        try {
            String mensaje = "";
            String nivel = "1";
            String dificultad;
            String filas = "5";
            String columnas = "5";
            String dificultadNoValida;
            String filasNoValidas;
            String columnasNoValidas;
            //codigo prueba: 2.1.2.1.2
            dificultad = "1";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel,dificultad,columnas,filas,mensaje);
            ResultadoSQL resultado1 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + Integer.parseInt(nivel) + "'");
            assertEquals(resultado1.getInt("dificultad"),Integer.parseInt(dificultad));
            resultado1.close();

            //codigo prueba: 2.1.2.1.3
            dificultad = "24";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel, dificultad, columnas, filas,mensaje);
            ResultadoSQL resultado2 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + Integer.parseInt(nivel) + "'");
            assertEquals(resultado2.getInt("dificultad"),Integer.parseInt(dificultad));
            resultado2.close();

            //codigo prueba: 2.1.2.2.1
            dificultad = "5";
            filas = "6";
            columnas ="8";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel, dificultad, columnas, filas,mensaje);
            ResultadoSQL resultado6 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + Integer.parseInt(nivel) + "'");
            assertEquals(resultado6.getInt("dificultad"),Integer.parseInt(dificultad));
            assertEquals(resultado6.getInt("nFilas"),Integer.parseInt(filas));
            assertEquals(resultado6.getInt("nColumnas"),Integer.parseInt(columnas));
            resultado6.close();

            //codigo prueba: 2.1.2.2.2
            dificultad = "5";
            filas = "2";
            columnas = "8";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel, dificultad, columnas, filas,mensaje);
            ResultadoSQL resultado7 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + Integer.parseInt(nivel) + "'");
            assertEquals(resultado7.getInt("dificultad"),Integer.parseInt(dificultad));
            assertEquals(resultado7.getInt("nFilas"),Integer.parseInt(filas));
            assertEquals(resultado7.getInt("nColumnas"),Integer.parseInt(columnas));
            resultado7.close();

            //codigo prueba: 2.1.2.2.3
            dificultad = "5";
            filas = "2";
            columnas = "50";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel, dificultad, columnas, filas,mensaje);
            ResultadoSQL resultado8 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + Integer.parseInt(nivel) + "'");
            assertEquals(resultado8.getInt("dificultad"),Integer.parseInt(dificultad));
            assertEquals(resultado8.getInt("nFilas"),Integer.parseInt(filas));
            assertEquals(resultado8.getInt("nColumnas"),Integer.parseInt(columnas));
            resultado8.close();

            //codigo prueba: 2.1.2.3.0
            mensaje = "test";
            dificultad = "5";
            filas = "5";
            columnas = "5";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel, dificultad, columnas, filas,mensaje);
            MenuAyuda menu = new MenuAyuda();
            JsonObject mensajeAyuda1 = Jsoner.deserialize((String) menu.mostrarDatosAyuda(), new JsonObject());
            assertEquals(mensaje, mensajeAyuda1.get("mensaje"));

            //codigo prueba: 2.1.2.3.1
            mensaje = "";
            GestorDatosJuego.getGestorDatosJuego().guardarDatos(nivel, dificultad, columnas, filas,mensaje);
            JsonObject mensajeAyuda2 = Jsoner.deserialize((String) menu.mostrarDatosAyuda(), new JsonObject());
            assertEquals(mensaje, mensajeAyuda2.get("mensaje"));


        }
        catch (SQLException e){
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS");
        }
    }
}