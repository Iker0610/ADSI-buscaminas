package is.buscaminas.controller;

import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GestorUsuarioTest {

    //El resto de métodos se tienen que probar de manera gráfica, dado que requieren de la interacción del usuario mediante interfaces

    @Test
    public void cambiarContrasenaTest(){
        try {
            GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES 'ejemplo'");
            GestorDB.getGestorDB().execSQL("INSERT INTO UsuarioEmail (Email, contrasena) VALUES ('ejemplo', '1234')");
            Usuario.create("ejemplo", "nick", 1, "basico", false);
            GestorUsuario.getGestorUsuario().cambiarContrasena("ABCD");
            ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM UsuarioEmail WHERE Email = 'ejemplo'");
            res.next();
            String contrasena = res.getString("contrasena");
            assert(contrasena.equals("ABCD"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}