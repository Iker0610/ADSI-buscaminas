package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class GestorLogrosTest {

    @Test
    void getGestorLogros() {
        //Caso de prueba único: mediante el método estático conseguimos tener el puntero a un objeto de tipo GestorLogros
        GestorLogros gl=null;
        assertNull(gl);
        gl=GestorLogros.getGestorLogros();
        assertNotNull(gl);
    }

    @Test
    void actualizarLogros() {
        try {
            ResultadoSQL res;
            Usuario.create("jon@ejemplo.com", "123", 1,"Mario",false);

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento y pierde
            GestorDB.getGestorDB().execSQL("DELETE FROM LOGRO WHERE nombre LIKE 'Test logro:%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM USUARIO WHERE EMAIL LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM LOGROSUSUARIO WHERE EMAIL LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO(nombre,descripcion,tipo,objetivo) VALUES('Test logro: 3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3)");
            GestorDB.getGestorDB().execSQL("INSERT INTO USUARIO(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res= GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            assertNull(res.getString("fechaObtencion"));
            GestorLogros.getGestorLogros().actualizarLogros(false,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            assertEquals(0,res.getInt("avance"));
            assertNull(res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento, gana y no lo consigue
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            //TODO Encontrar el error aquí
            assertEquals(1,res.getInt("avance"));
            assertNull(res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento, gana y lo consigue
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas");
            res.next();
            assertEquals(3,res.getInt("avance"));
            assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento y gana pero en un nivel diferente
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO(nombre,descripcion,tipo,objetivo,nivel) VALUES('Test logro: 3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,1)");
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET emavance=0 WHERE ail='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas') VALUES('"+Usuario.getUsuario().getEmail()+"','Test logro: 3 victorias Nvl 1','',1)");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(true,2);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
            res.next();
            assertEquals(1,res.getInt("avance"));
            assertNull(res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con varios elementos y gana pero no consigue ninguno
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"'");
            while(res.next()){
                assertNull(res.getString("fechaObtencion"));
            }

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con varios elementos y gana y condsigue un logro
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FROM FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias'");
            res.next();
            assertEquals(3,res.getInt("avance"));
            assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),res.getString("fechaObtencion"));
        }
        catch(SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLogros() {
        try {
            Usuario.create("jon@ejemplo", "123", 1,"Mario",false);
            String res=null;
            JsonObject datos=null;
            JsonArray logrosObtenidos,logrosRestantes;
            GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");
            GestorDB.getGestorDB().execSQL("INSERT INTO Usuario(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
            GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,tipo,objetivo) VALUES('Test logro: 3 seguidas','VictoriaConsecutiva',3)");
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");

            //Logros sin cargar
            logrosObtenidos=new JsonArray();
            logrosRestantes=new JsonArray();
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            assertEquals(0,logrosObtenidos.size());
            assertEquals(0,logrosRestantes.size());

            //Con los logros cargados
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            JsonObject datoLogro=Jsoner.deserialize((String) logrosRestantes.get(21),new JsonObject()); //Porque el logro añadido en esta prueba se coloca al final
            assertEquals("Test logro: 3 seguidas",(String) datoLogro.get("nombre"));
            assertEquals("0",datoLogro.get("avance").toString());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void cargarLogros() {
        try {
            Usuario.create("usuarioPassword2@ejemplo.com", "123", 1,"Mario",false);
            String res=null;
            JsonObject datos=null;
            JsonArray logrosObtenidos,logrosRestantes;
            GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");

            //Logros sin cargar
            logrosObtenidos=new JsonArray();
            logrosRestantes=new JsonArray();
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            assertEquals(0,logrosObtenidos.size());
            assertEquals(0,logrosRestantes.size());

            //Con los logros cargados
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            assertEquals(3,logrosObtenidos.size());
            assertEquals(15,logrosRestantes.size());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}