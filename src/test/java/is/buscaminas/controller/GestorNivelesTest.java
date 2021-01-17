package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GestorNivelesTest {

    @Test
    void guardarDatos(){
        try {
            int nivel = 1;
            int dificultad = 5;
            int filas = 5;
            int columnas = 5;
            int dificultadNoValida;
            int filasNoValidas;
            int columnasNoValidas;
            //codigo prueba: 2.1.2.1.1
            dificultad = 4;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnas, filas);
            ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado.getInt("dificultad"),dificultad);
            resultado.close();

            //codigo prueba: 2.1.2.1.2
            dificultad = 1;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnas, filas);
            ResultadoSQL resultado1 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado1.getInt("dificultad"),dificultad);
            resultado1.close();

            //codigo prueba: 2.1.2.1.3
            dificultad = filas * columnas - 1;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnas, filas);
            ResultadoSQL resultado2 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado2.getInt("dificultad"),dificultad);
            resultado2.close();

            //codigo prueba: 2.1.2.1.4
            dificultadNoValida = 0;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultadNoValida, columnas, filas);
            ResultadoSQL resultado3 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado3.getInt("dificultad"),dificultad);
            resultado3.close();

            //codigo prueba: 2.1.2.1.5
            dificultadNoValida = -1;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultadNoValida, columnas, filas);
            ResultadoSQL resultado4 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado4.getInt("dificultad"),dificultad);
            resultado4.close();

            //codigo prueba: 2.1.2.1.7
            dificultadNoValida = 25;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultadNoValida, columnas, filas);
            ResultadoSQL resultado5 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado5.getInt("dificultad"),dificultad);
            resultado5.close();

            //codigo prueba: 2.1.2.1.8
            dificultadNoValida = 28;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultadNoValida, columnas, filas);
            ResultadoSQL resultado21 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado21.getInt("dificultad"),dificultad);
            resultado21.close();

            //codigo prueba: 2.1.2.2.1
            dificultad = 5;
            filas = 6;
            columnas =8;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnas, filas);
            ResultadoSQL resultado6 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado6.getInt("dificultad"),dificultad);
            assertEquals(resultado6.getInt("nFilas"),filas);
            assertEquals(resultado6.getInt("nColumnas"),columnas);
            resultado6.close();

            //codigo prueba: 2.1.2.2.2
            dificultad = 5;
            filas = 2;
            columnas =8;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnas, filas);
            ResultadoSQL resultado7 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado7.getInt("dificultad"),dificultad);
            assertEquals(resultado7.getInt("nFilas"),filas);
            assertEquals(resultado7.getInt("nColumnas"),columnas);
            resultado7.close();

            //codigo prueba: 2.1.2.2.3
            dificultad = 5;
            filas = 2;
            columnas =50;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnas, filas);
            ResultadoSQL resultado8 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado8.getInt("dificultad"),dificultad);
            assertEquals(resultado8.getInt("nFilas"),filas);
            assertEquals(resultado8.getInt("nColumnas"),columnas);
            resultado8.close();

            //codigo prueba: 2.1.2.2.4
            dificultad = 5;
            filasNoValidas = 1;
            columnasNoValidas =1;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnasNoValidas, filasNoValidas);
            ResultadoSQL resultado9 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado9.getInt("dificultad"),dificultad);
            assertEquals(resultado9.getInt("nFilas"),filas);
            assertEquals(resultado9.getInt("nColumnas"),columnas);
            resultado9.close();

            //codigo prueba: 2.1.2.2.5
            dificultad = 5;
            filasNoValidas = -1;
            columnasNoValidas =5;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnasNoValidas, filasNoValidas);
            ResultadoSQL resultado10 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado10.getInt("dificultad"),dificultad);
            assertEquals(resultado10.getInt("nFilas"),filas);
            assertEquals(resultado10.getInt("nColumnas"),columnas);
            resultado10.close();

            //codigo prueba: 2.1.2.2.6 y 2.1.2.2.7
            dificultad = 5;
            filasNoValidas = 5;
            columnasNoValidas =55;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultad, columnasNoValidas, filasNoValidas);
            ResultadoSQL resultado11 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado11.getInt("dificultad"),dificultad);
            assertEquals(resultado11.getInt("nFilas"),filas);
            assertEquals(resultado11.getInt("nColumnas"),columnas);
            resultado11.close();

            //codigo prueba: 2.1.2.2.8
            dificultadNoValida = 20;
            filasNoValidas = 4;
            columnasNoValidas = 5;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultadNoValida, columnasNoValidas, filasNoValidas);
            ResultadoSQL resultado12 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado12.getInt("dificultad"),dificultad);
            assertEquals(resultado12.getInt("nFilas"),filas);
            assertEquals(resultado12.getInt("nColumnas"),columnas);
            resultado12.close();

            //codigo prueba: 2.1.2.2.9
            dificultadNoValida = 20;
            filasNoValidas = 3;
            columnasNoValidas = 3;
            GestorNiveles.getGestorNiveles().guardarDatos(nivel, dificultadNoValida, columnasNoValidas, filasNoValidas);
            ResultadoSQL resultado13 = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado13.getInt("dificultad"),dificultad);
            assertEquals(resultado13.getInt("nFilas"),filas);
            assertEquals(resultado13.getInt("nColumnas"),columnas);
            resultado13.close();

        }
        catch (SQLException e){
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS");
        }

    }

    @Test
    void obtenerDatosNiveles(){
        try {
            JsonArray lNiveles = Jsoner.deserialize((String) GestorNiveles.getGestorNiveles().obtenerDatosNiveles(), new JsonArray());
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
    void getDatosNivel() {
        try {
            int nivel = 1;
            int dificultad = 1;
            int filas = 5;
            int columnas = 5;
            JsonObject NivelJson = Jsoner.deserialize((String) GestorNiveles.getGestorNiveles().getDatosNivel(nivel), new JsonObject());
            ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT dificultad, nFilas, nColumnas FROM Nivel Where nivel = '" + nivel + "'");
            assertEquals(resultado.getInt("dificultad"), Integer.parseInt(NivelJson.get("dificultad").toString()));
            assertEquals(resultado.getInt("nFilas"), Integer.parseInt(NivelJson.get("nFilas").toString()));
            assertEquals(resultado.getInt("nColumnas"), Integer.parseInt(NivelJson.get("nColumnas").toString()));
            resultado.close();
        }
        catch (SQLException e){
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS");
        }
    }

    @Test
    void getNiveles(){
        try {
            JsonArray lNiveles = Jsoner.deserialize((String) GestorNiveles.getGestorNiveles().getNiveles(), new JsonArray());
            for (Object jsonObjectString : lNiveles) {
                JsonObject NivelJson = (JsonObject) jsonObjectString;
                ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT * FROM Nivel Where nivel = '" + NivelJson.get("nivel") + "'");
                assertTrue(resultado.next());
                resultado.close();
            }

        }
        catch (SQLException e){
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS");
        }

    }
}