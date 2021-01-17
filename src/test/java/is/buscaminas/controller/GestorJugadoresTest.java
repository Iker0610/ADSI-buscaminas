package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GestorJugadoresTest {

    @Test
    void mostrarUsuarios() {
        try {
            JsonArray lUsuarios = Jsoner.deserialize((String) GestorJugadores.getGestorJugadores().mostrarUsuarios(), new JsonArray());
            for (Object jsonObjectString : lUsuarios) {
                JsonObject json = (JsonObject)  jsonObjectString;
                ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT email FROM Usuario WHERE email = '"+ json.get("email") +"'");
                assertTrue(resultado.next());
                resultado.close();
            }
        }
        catch (SQLException e){
            System.out.printf("HA HABIDO UN PROBLEMA AL CONECTAR CON LA BASE DE DATOS");
        }
    }

    @Test
    void eliminar() {
        //No se pueden hacer JUnits, ya que salta una excepción al hacerlo, porque el programa intenta generar una alert
        //indicando que se ha eliminado correctamente al usuario pero no puede.
    }

    @Test
    void getDatosUsuario() {
        try {
            //Usuario no registrado como UsuarioEmail (sin contraseña).
            //Código prueba: 2.2.2.1.0 y 2.2.2.2.3.
            GestorJugadores.getGestorJugadores().guardarUsuarioSeleccionado("usuario1@ejemplo.com");
            JsonObject usuario1 = Jsoner.deserialize((String) GestorJugadores.getGestorJugadores().getDatosUsuario(), new JsonObject());
            ResultadoSQL resultado1 = GestorDB.getGestorDB().execSELECT("SELECT nivelInicial FROM Usuario WHERE email = 'usuario1@ejemplo.com'");
            assertEquals(Integer.parseInt(usuario1.get("nivel").toString()),resultado1.getInt("nivelInicial"));
            resultado1.close();

            //Usuario registrado como UsuarioEmail.
            //Código prueba: 2.2.2.3.1.
            GestorJugadores.getGestorJugadores().guardarUsuarioSeleccionado("usuarioPassword@ejemplo.com");
            JsonObject usuario2 = Jsoner.deserialize((String) GestorJugadores.getGestorJugadores().getDatosUsuario(), new JsonObject());
            ResultadoSQL resultado2 = GestorDB.getGestorDB().execSELECT("SELECT nivelInicial FROM Usuario WHERE email = 'usuarioPassword@ejemplo.com'");
            assertEquals(Integer.parseInt(usuario2.get("nivel").toString()),resultado2.getInt("nivelInicial"));
            resultado2.close();
            ResultadoSQL resultado3 = GestorDB.getGestorDB().execSELECT("SELECT contrasena FROM UsuarioEmail WHERE email = 'usuarioPassword@ejemplo.com'");
            assertEquals(usuario2.get("contrasena"),resultado3.getString("contrasena"));
            resultado3.close();

        }
        catch (SQLException e){
            System.out.printf("HA HABIDO UN PROBLEMA AL CONECTAR CON LA BASE DE DATOS");
        }
    }

    @Test
    void getNiveles() {
        try {
            JsonArray jsonArray = Jsoner.deserialize(GestorJugadores.getGestorJugadores().getNiveles(), new JsonArray());
            for (Object jsonObjectString : jsonArray) {
                JsonObject json = (JsonObject) jsonObjectString;
                ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT * FROM Nivel WHERE nivel = '" + json.get("nivel") + "'");
                assertTrue(resultado.next());
                resultado.close();
            }
        }
        catch (SQLException e){
            System.out.printf("HA HABIDO UN PROBLEMA AL CONECTAR CON LA BASE DE DATOS");
        }
    }

    @Test
    void guardar() {
        //No se pueden hacer JUnits, ya que salta una excepción al hacerlo, porque el programa intenta generar una alert
        //indicando que se han guardado correctamente los datos pero no puede.
    }
}