package is.buscaminas.controller;

import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GestorRankingTest {

    @BeforeEach
    void setUp() {

        try {
            // Eliminamos los datos del Ranking de la Base de Datos
            GestorDB.getGestorDB().execSQL("DELETE FROM Ranking");
            // Insertamos datos de ejemplo en la Base de Datos
            GestorDB.getGestorDB().execSQL("INSERT INTO Ranking (nivel,email,puntuacion,nickname) VALUES " +
                    "(3,'usuario3@ejemplo.com',333,'usuario3')," +
                    "(2,'usuario2@ejemplo.com',222,'usuario2')," +
                    "(1,'usuario1@ejemplo.com',256,'usuario1')," +
                    "(3,'usuario1@ejemplo.com',109,'usuario1')," +
                    "(4,'usuario1@ejemplo.com',521,'usuario1')," +
                    "(2,'usuario1@ejemplo.com',964,'usuario1')");

        } catch (SQLException e){
            System.out.println("Error al conectar con la base de datos (beforeEach)");
        }

    }

    @AfterEach
    void tearDown() throws SQLException {

        // Eliminamos los datos del Ranking de la Base de Datos
        GestorDB.getGestorDB().execSQL("DELETE FROM Ranking");
    }

    @Test
    void obtenerRankingGlobalTest() throws SQLException {

        // Obtenemos el ranking mediante el metodo a testear
        String ranking = GestorRanking.getGestorRanging().obtenerRankingGlobal();
        // La estructura del json del ranking esperada es la siguiente
        String datos = "{\"ranking\":[{\"puntuacion\":964,\"nickname\":\"usuario1\",\"nivel\":2}," +
                                     "{\"puntuacion\":521,\"nickname\":\"usuario1\",\"nivel\":4}," +
                                     "{\"puntuacion\":333,\"nickname\":\"usuario3\",\"nivel\":3}," +
                                     "{\"puntuacion\":256,\"nickname\":\"usuario1\",\"nivel\":1}," +
                                     "{\"puntuacion\":222,\"nickname\":\"usuario2\",\"nivel\":2}," +
                                     "{\"puntuacion\":109,\"nickname\":\"usuario1\",\"nivel\":3}]}";
        assertEquals(datos,ranking);
    }

    @Test
    void obtenerRankingPersonalTest() throws IllegalAccessException, SQLException {

        // Creamos el usuario
        Usuario.create("usuario1@ejemplo.com","usuario1",1,"Mario",false);
        // Obtenemos el ranking mediante el metodo a testear
        String ranking = GestorRanking.getGestorRanging().obtenerRankingPersonal();
        // La estructura del json del ranking esperada es la siguiente
        String datos = "{\"ranking\":[{\"puntuacion\":964,\"nickname\":\"usuario1\",\"nivel\":2}," +
                                     "{\"puntuacion\":521,\"nickname\":\"usuario1\",\"nivel\":4}," +
                                     "{\"puntuacion\":256,\"nickname\":\"usuario1\",\"nivel\":1}," +
                                     "{\"puntuacion\":109,\"nickname\":\"usuario1\",\"nivel\":3}]}";
        // Comprobamos si los datos coinciden
        assertEquals(datos,ranking);
    }

    @Test
    void clasificarPorNivelGllobalTest() throws SQLException {

        // Obtenemos el ranking mediante el metodo a testear
        String ranking = GestorRanking.getGestorRanging().clasificarPorNivelGlobal();
        // La estructura del json del ranking esperada es la siguiente
        String datos = "{\"1\":[{\"puntuacion\":\"256\",\"nickname\":\"usuario1\",\"nivel\":\"1\"}]," +
                        "\"2\":[{\"puntuacion\":\"964\",\"nickname\":\"usuario1\",\"nivel\":\"2\"},{\"puntuacion\":\"222\",\"nickname\":\"usuario2\",\"nivel\":\"2\"}]," +
                        "\"3\":[{\"puntuacion\":\"333\",\"nickname\":\"usuario3\",\"nivel\":\"3\"},{\"puntuacion\":\"109\",\"nickname\":\"usuario1\",\"nivel\":\"3\"}]," +
                        "\"4\":[{\"puntuacion\":\"521\",\"nickname\":\"usuario1\",\"nivel\":\"4\"}]}";
        // Comprobamos si los datos coinciden
        assertEquals(datos,ranking);
    }

    @Test
    void clasificarPorNivelPersonalTest() throws IllegalAccessException, SQLException {

        // Creamos el usuario
        Usuario.create("usuario1@ejemplo.com","usuario1",1,"Mario",false);
        // Obtenemos el ranking mediante el metodo a testear
        String ranking = GestorRanking.getGestorRanging().clasificarPorNivelPersonal();
        // La estructura del json del ranking esperada es la siguiente
        String datos = "{\"1\":[{\"puntuacion\":\"256\",\"nickname\":\"usuario1\",\"nivel\":\"1\"}]," +
                        "\"2\":[{\"puntuacion\":\"964\",\"nickname\":\"usuario1\",\"nivel\":\"2\"}]," +
                        "\"3\":[{\"puntuacion\":\"109\",\"nickname\":\"usuario1\",\"nivel\":\"3\"}]," +
                        "\"4\":[{\"puntuacion\":\"521\",\"nickname\":\"usuario1\",\"nivel\":\"4\"}]}";
        // Comprobamos si los datos coinciden
        assertEquals(datos,ranking);
    }

    @Test
    void registrarPuntuacionTest() throws IllegalAccessException, SQLException {

        // Variables para pasarle al metodo
        int puntuacion = 888;
        int nivel = 2;

        // Creamos el usuario
        Usuario.create("usuario@ejemplo.com","usuario",1,"Mario",false);
        // Introducimos la nueva tupla en la Basde de Datos
        GestorRanking.getGestorRanging().registrarPuntuacion(puntuacion,nivel);
        // Obtenemos el emial y el nickname del Usuario
        String nickname = Usuario.getUsuario().getNickname();
        String email = Usuario.getUsuario().getEmail();
        // Hacemos un Select que devuelva la tupla con los datos introducidos
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("Select email,nickname,nivel,puntuacion from Ranking where email = '" + email + "' and nickname = '" + nickname + "' and nivel = " + nivel + " and puntuacion = " + puntuacion + " order by puntuacion desc");
        // Comprobamos que la tupla se ha insertado en la Base de Datos
        assertTrue(resultado.next());
        resultado.close();
    }
}