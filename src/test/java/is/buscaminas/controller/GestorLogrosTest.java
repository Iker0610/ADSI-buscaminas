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
import java.util.Locale;

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

            /*
            6.5.1
            La lista de logrosObtenidos está vacía, logrosRestantes tiene un elemento y pierde.
            El sistema actualiza los logros según el resultado de la partida.
            El logro restante no ha sido obtenido y el avance se ha reseteado.
            El avance del logro ha sido reseteado.
             */
            GestorDB.getGestorDB().execSQL("DELETE FROM LOGRO WHERE nombre LIKE 'Test logro:%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM USUARIO WHERE EMAIL LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM LOGROSUSUARIO WHERE EMAIL LIKE 'jon%' OR nombreLogro LIKE 'Test logro%'");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO(nombre,descripcion,tipo,objetivo) VALUES('Test logro: 3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3)");
            GestorDB.getGestorDB().execSQL("INSERT INTO USUARIO(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res= GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            assertNull(res.getString("fechaObtencion"));
            res.close();
            GestorLogros.getGestorLogros().actualizarLogros(false,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            assertEquals(0,res.getInt("avance"));
            assertNull(res.getString("fechaObtencion"));
            res.close();

            /*
            6.5.2
            La lista de logrosObtenidos está vacía y logrosRestantes con un elemento, gana y no lo consigue.
            El sistema actualiza los logros según el resultado de la partida.
            El logro restante no ha sido obtenido y el avance ha sido incrementado.
            El avance del logro ha sido incrementado en una unidad.
             */
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            assertEquals(1,res.getInt("avance"));
            assertNull(res.getString("fechaObtencion"));
            res.close();

            /*
            6.5.3
            La lista de logrosObtenidos está vacía y logrosRestantes tiene un elemento, gana y lo consigue.
            El sistema actualiza los logros según el resultado de la partida.
            El logro restante ha sido obtenido y el avance es igual que el objetivo del logro.
            El logro está en logrosObtenidos, el avance es el mismo que el objetivo del logro y tiene fecha de obtención.
             */
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 seguidas'");
            res.next();
            assertEquals(3,res.getInt("avance"));
            assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),res.getString("fechaObtencion"));
            res.close();
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0,fechaObtencion=NULL WHERE email='"+ Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");

            /*
            6.5.4
            La lista de logrosObtenidos está vacía y logrosRestantes con un elemento gana pero en un nivel diferente.
            El sistema actualiza los logros según el resultado de la partida.
            El logro restante no ha sido obtenido y el avance no se ha actualizado ni reseteado.
            El logro restante no ha sido obtenido y el avance no se ha actualizado ni reseteado.
             */
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO(nombre,descripcion,tipo,objetivo,nivel) VALUES('Test logro: 3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,1)");
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 victorias Nvl 1'");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(true,2);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION,AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
            res.next();
            assertEquals(0,res.getInt("avance"));
            assertNull(res.getString("fechaObtencion"));
            res.close();

            /*
            6.5.5
            La lista de logrosObtenidos está vacía y logrosRestantes tiene varios elementos y gana pero no consigue ninguno.
            El sistema actualiza los logros según el resultado de la partida.
            Los logros restantes han sido actualizados según el tipo que sean.
            El avance de los logros ha sido incrementado pero no ha llegado al objetivo y no se ha conseguido.
             */
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
            res.next();
            assertNull(res.getString("fechaObtencion"));
            res.close();
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 victorias Nvl 1'");
            res.next();
            assertNull(res.getString("fechaObtencion"));
            res.close();

            /*
            6.5.6
            La lista de logrosObtenidos está vacía y logrosRestantes con varios elementos, gana y consigue un logro.
            El sistema actualiza los logros según el resultado de la partida.
            Los logros restantes han sido actualizados según el tipo que sean.
            El avance ha sido incrementado en cada logro y uno ha llegado al objetivo y se ha añadido a la lista de logrosObtenidos.
             */
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT * FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
            res.next();
            assertEquals(3,res.getInt("avance"));
            assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),res.getString("fechaObtencion"));
            res.close();
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
            GestorLogros.getGestorLogros().reset();
            String res=null;
            JsonObject datos=null;
            JsonArray logrosObtenidos,logrosRestantes;
            GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE ((email LIKE 'jon%') OR (nombreLogro LIKE 'Test logro%'))");
            GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");
            GestorDB.getGestorDB().execSQL("INSERT INTO Usuario(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
            GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,tipo,objetivo) VALUES('Test logro: 3 seguidas','VictoriaConsecutiva',3)");
            GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");

            /*
            6.6.1
            No se han cargado los logros en el sistema.
            El gestor de los logros intenta obtener la información de los logros en clases primitivas para la fácil lectura de la interfaz.
            Que las listas en memoria estén vacías.
            Las listas en memoria están vacías.
             */
            logrosObtenidos=new JsonArray();
            logrosRestantes=new JsonArray();
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            assertEquals(0,logrosObtenidos.size());
            assertEquals(0,logrosRestantes.size());

            /*
            6.6.2
            Se han cargado los logros en el sistema.
            El gestor de los logros intenta obtener la información de los logros en clases primitivas para la fácil lectura de la interfaz.
            Obtener la información de los logros según las obtenciones del usuario registrado.
            Obtenidas la información de los logros según las obtenciones del usuario registrado.
             */
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            JsonObject datoLogro=Jsoner.deserialize((String) logrosRestantes.get(logrosRestantes.size()-1),new JsonObject()); //Porque el logro añadido en esta prueba se coloca al final
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
            GestorLogros.getGestorLogros().reset();
            Usuario.create("usuarioPassword2@ejemplo.com", "123", 1,"Mario",false);
            String res=null;
            JsonObject datos=null;
            JsonArray logrosObtenidos,logrosRestantes;
            GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email LIKE 'jon%' OR nombreLogro LIKE 'Test logro%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
            GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");

            /*
            6.7.1
            Los logros están sin cargar
            El sistema carga los logros en memoria desde la base de datos.
            Las listas en memoria que almacenan los logros están vacías.
            Las listas en memoria que almacenan los logros están vacías.
             */
            logrosObtenidos=new JsonArray();
            logrosRestantes=new JsonArray();
            res=GestorLogros.getGestorLogros().getLogros();
            datos=Jsoner.deserialize(res,new JsonObject());
            logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"),new JsonArray());
            logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"),new JsonArray());
            assertEquals(0,logrosObtenidos.size());
            assertEquals(0,logrosRestantes.size());

            /*
            6.7.2
            Los logros están cargados.
            El sistema carga los logros en memoria desde la base de datos.
            Que las listas en memoria estén cargados según las obtenciones de logros del usuario registrado.
            Las listas en memoria están cargadas correspondientemente a lo conseguido por el usuario.
             */
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