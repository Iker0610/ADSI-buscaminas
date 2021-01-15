package is.buscaminas.controller;

import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GestorTematicaTest {

    @BeforeEach
    void setUp() {
        try{
            // Creamos un nuevo usuario de prueba en la Base de Datos
            GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (email, nivelInicial, esAdmin, temaActual) VALUES ('pruebaTemas',1,0,'Mario');");

            // Obtenemos el nombre del Logro que desbloquea la temática de Mario
            ResultadoSQL logro = GestorDB.getGestorDB().execSELECT("SELECT nombre FROM Logro WHERE nombreTema = 'Mario'");
            logro.next();
            String nombreLogro = logro.getString("nombre");
            logro.close();

            // El nuevo usuario tendrá desbloqueada unicamente la temática de Mario
            GestorDB.getGestorDB().execSQL("INSERT INTO LogrosUsuario (email, nombreLogro, fechaObtencion, avance) VALUES ('pruebaTemas','" + nombreLogro + "','01/01/2001',3);");

            // Cargamos el nuevo usuario en la clase Usuario
            Usuario.create("pruebaTemas","123",1,"Mario",false);
        }
        catch (SQLException e){
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS (beforeEach)");
        }
        catch (IllegalAccessException e){
            System.out.println("ERROR AL CREAR EL USUARIO (beforeEach)");
        }
    }

    @AfterEach
    void tearDown() {

        try {
            //Borramos los datos del usuario de prueba de la BD
            GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email = 'pruebaTemas'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email = 'pruebaTemas'");

        }
        catch (SQLException E) {
            System.out.println("ERROR A CONECTAR CON LA BASE DE DATOS (afterEach)");
        }
    }

    @Test
    void cambiarTematica()  {
        try {
            // Le cambiamos la temática a otra que no sea la actual (Mario -> Among Us)
            GestorTematica.getGestorTematica().cambiarTematica("Among Us");

            // Comprobamos si la temática actual en el objeto ha cambiado
            assertEquals(Usuario.getUsuario().getTematicaActual(), "Among Us");

            //Comprobamos si la temática actual en BD ha cambiado
            ResultadoSQL rdo = GestorDB.getGestorDB().execSELECT("SELECT temaActual FROM Usuario WHERE email = '" + Usuario.getUsuario().getEmail() + "';");
            rdo.next();
            assertEquals(rdo.getString("temaActual"), "Among Us");
            rdo.close();
        }
        catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS (cambiarTemaTest)");
        }
    }

    @Test
    void obtenerTemas() {
        try {
            // Obtenemos los temas de dicho Usuario
            String json = GestorTematica.getGestorTematica().obtenerTemas();

            //El nuevo Usuario solo tendrá desbloqueada la temática de Mario, por lo que la estructura esperada del Json es la siguiente:
            String datos =  "[{\"descripcion\":\"Tema basado en el nuevo juego multijugador de Among Us.\",\"bloqueada\":true,\"nombre\":\"Among Us\"}," +
                            "{\"descripcion\":\"Tema basado en la famosa franquicia de Nintendo Mario Bros.\",\"bloqueada\":false,\"nombre\":\"Mario\"}," +
                            "{\"descripcion\":\"Tema basico desbloqueado para todos los usuarios.\",\"bloqueada\":true,\"nombre\":\"Basico\"}]";

            //Comprobamos si los datos obtenidos y los esperados son iguales
            assertEquals(json,datos);
        }
        catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS (obtenerTemasTest)");
        }
    }
}