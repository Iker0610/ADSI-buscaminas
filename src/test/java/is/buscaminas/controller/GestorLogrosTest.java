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
            Usuario.create("usuario1@ejemplo.com", "123", 1,"Mario",false);

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento y pierde
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('"+Usuario.getUsuario().getEmail()+"','3 seguidas','',2");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res= GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELGORO='3 seguidas'");
            res.next();
            assertEquals("",res.getString("fechaObtencion"));
            GestorLogros.getGestorLogros().actualizarLogros(false,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas'");
            res.next();
            assertEquals(0,res.getInt("avance"));
            assertEquals("",res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento, gana y no lo consigue
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas");
            res.next();
            assertEquals(1,res.getInt("avance"));
            assertEquals("",res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento, gana y lo consigue
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas");
            res.next();
            assertEquals(3,res.getInt("avance"));
            assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con 1 elemento y gana pero en un nivel diferente
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,'Minecraft',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('"+Usuario.getUsuario().getEmail()+"','3 victorias','',1)");
            GestorDB.getGestorDB().execSQL("DELETE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias'");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(true,2);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias'");
            res.next();
            assertEquals(1,res.getInt("avance"));
            assertEquals("",res.getString("fechaObtencion"));

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con varios elementos y gana pero no consigue ninguno
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('"+Usuario.getUsuario().getEmail()+"','3 seguidas','',0");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"'");
            while(res.next()){
                assertEquals("",res.getString("fechaObtencion"));
            }

            //Caso de prueba: lista de logrosObtenidos vacía y logrosRestantes con varios elementos y gana y condsigue un logro
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FROM FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias'");
            res.next();
            assertEquals(3,res.getInt("avance"));
            assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),res.getString("fechaObtencion"));

            //Eliminar todos los registros de las bases de datos
            GestorDB.getGestorDB().execSQL("DELETE * FROM LOGROSUSUARIOS");
            GestorDB.getGestorDB().execSQL("DELETE * FROM LOGROS");
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
            Usuario.create("usuario1@ejemplo.com", "123", 1,"Mario",false);
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROS VALUES('4 seguidas','Ganar 4 partidas seguidas','VictoriaConsecutiva',4,'Minecraft',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROS VALUES('3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('" + Usuario.getUsuario().getEmail() + "','4 seguidas','',0");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('" + Usuario.getUsuario().getEmail() + "','3 seguidas','" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "',3");
            String res=null;
            JsonObject datos=null;
            JsonArray logrosObtenidos,logrosRestantes;

            //Logros sin cargar
            logrosObtenidos=new JsonArray();
            logrosRestantes=new JsonArray();
            res=GestorLogros.getGestorLogros().getLogros();
            Jsoner.deserialize(res,datos);
            Jsoner.deserialize((String) datos.get("logrosObtenidos"),logrosObtenidos);
            Jsoner.deserialize((String) datos.get("logrosRestantes"),logrosRestantes);
            assertEquals(new JsonArray(),logrosObtenidos);
            assertEquals(new JsonArray(),logrosRestantes);

            //Con los logros cargados
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res=GestorLogros.getGestorLogros().getLogros();
            Jsoner.deserialize(res,datos);
            Jsoner.deserialize((String) datos.get("logrosObtenidos"),logrosObtenidos);
            Jsoner.deserialize((String) datos.get("logrosRestantes"),logrosRestantes);
            //PRIMERO LOGROS OBTENIDOS
                String logro=(String) logrosObtenidos.get(0);
                JsonObject datosLogro=null;
                Jsoner.deserialize(logro,datosLogro);
                assertEquals("3 seguidas",datosLogro.get("nombre"));
                assertEquals("Ganar 3 partidas seguidas",datosLogro.get("descripcion"));
                assertEquals(3,datosLogro.get("objetivo"));
                assertEquals(3,datosLogro.get("avance"));
                assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),datosLogro.get("fechaObtencion"));
                assertEquals("Among us",datosLogro.get("nombreTema"));
            //SEGUNDO LOGROS RESTANTES
                logro=(String) logrosRestantes.get(0);
                Jsoner.deserialize(logro,datosLogro);
                assertEquals("4 seguidas",datosLogro.get("nombre"));
                assertEquals("Ganar 4 partidas seguidas",datosLogro.get("descripcion"));
                assertEquals(4,datosLogro.get("objetivo"));
                assertEquals(0,datosLogro.get("avance"));
                assertEquals("",datosLogro.get("fechaObtencion"));
                assertEquals("Minecraft",datosLogro.get("nombreTema"));

            //Eliminar todos los registros de las bases de datos
            GestorDB.getGestorDB().execSQL("DELETE * FROM LOGROSUSUARIOS");
            GestorDB.getGestorDB().execSQL("DELETE * FROM LOGROS");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void cargarLogros() {
        try {
            Usuario.create("usuario1@ejemplo.com", "123", 1,"Mario",false);
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('" + Usuario.getUsuario().getEmail() + "','3 seguidas','',0");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('" + Usuario.getUsuario().getEmail() + "','3 seguidas','" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "',3");
            String res;
            JsonObject datos;
            JsonArray logrosObtenidos, logrosRestantes;

            //Los métodos no están cargados
            res = GestorLogros.getGestorLogros().getLogros();
            datos = null;
            Jsoner.deserialize(res, datos);
            logrosObtenidos = null;
            Jsoner.deserialize((String) datos.get("logrosObtenidos"), logrosObtenidos);
            logrosRestantes = null;
            Jsoner.deserialize((String) datos.get("logrosRestantes"), logrosRestantes);
            assertEquals(0, logrosObtenidos.size());
            assertEquals(0, logrosRestantes.size());

            //Hay logros cargados
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res = GestorLogros.getGestorLogros().getLogros();
            Jsoner.deserialize(res, datos);
            Jsoner.deserialize((String) datos.get("logrosObtenidos"), logrosObtenidos);
            Jsoner.deserialize((String) datos.get("logrosRestantes"), logrosRestantes);
            assertEquals(1, logrosObtenidos.size());
            assertEquals(1, logrosObtenidos.size());

            //Eliminar todos los registros de las bases de datos
            GestorDB.getGestorDB().execSQL("DELETE * FROM LOGROSUSUARIOS");
            GestorDB.getGestorDB().execSQL("DELETE * FROM LOGROS");
        }
        catch(SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}