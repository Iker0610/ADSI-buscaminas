package is.buscaminas.model.logros;

import is.buscaminas.controller.GestorLogros;
import is.buscaminas.controller.GestorUsuario;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LogroTest {

    @org.junit.jupiter.api.Test
    void comprobarLogro() {
        try {
            GestorUsuario.checkEmailContrasena("Jon", "123", "jortuzar");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 seguidas','Ganar 3 partidas seguidas en el nivel 1','VictoriaConsecutiva',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('" + Usuario.getUsuario().getEmail() + "','3 seguidas','',1)");
            ResultadoSQL res;
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());

            //Casos de prueba para logros de victorias consecutivas

            //Caso de prueba: pierde, el nivel da igual
            GestorLogros.getGestorLogros().actualizarLogros(false, 1);
            res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='3 seguidas'");
            res.next();
            assertEquals(0, res.getInt("avance"));

            //Caso de prueba: pierde en un nivel diferente
            GestorDB.getGestorDB().execSQL("DELETE FROM LOGRO WHERE NOMBRE='3 seguidas'");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('" + Usuario.getUsuario().getEmail() + "','3 seguidas','',1)");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            GestorLogros.getGestorLogros().actualizarLogros(false,2);
            res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='3 seguidas'");
            res.next();
            assertEquals(0,res.getInt("avance"));

            //Caso de prueba: gana la partida y consigue el logro
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas'");
            res.next();
            assertEquals(fecha,res.getString("fechaObtencion"));

            //Casos de prueba para logros de victorias en nivel
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('"+Usuario.getUsuario().getEmail()+"','3 victorias Nvl 1','',0)");

            //Caso de prueba: gana 1 vez en el mismo nivel pero no logra el objetivo
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1");
            res.next();
            assertEquals(1,res.getInt("avance"));

            //Caso de prueba: pierde en el mismo nivel
            GestorLogros.getGestorLogros().actualizarLogros(false,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1");
            res.next();
            assertEquals(1,res.getInt("avance"));

            //Caso de prueba: gana en un nivel diferente
            GestorLogros.getGestorLogros().actualizarLogros(true,2);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1");
            res.next();
            assertEquals(1,res.getInt("avance"));

            //Caso de prueba: gana en el mismo nivel y consigue el logro
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1");
            fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            res.next();
            assertEquals(fecha,res.getString("fechaObtencion"));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void actualizarAvance() {
        try {
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('"+Usuario.getUsuario().getEmail()+"','3 victorias Nvl 1','',0)");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            ResultadoSQL res;

            //Caso de prueba: aumentar avance pero no llegar al objetivo
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1'");
            res.next();
            assertEquals(1,res.getInt("avance"));

            //Caso de prueba: aumentar avance y llegar al objetivo
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1'");
            res.next();
            assertEquals(3,res.getInt("avance"));

            //Caso de prueba: el avance ya ha llegado al objetivo por lo que cuando se actualice no deberia actualizarse más
            GestorLogros.getGestorLogros().actualizarLogros(true,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='"+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 victorias Nvl 1'");
            res.next();
            assertEquals(3,res.getInt("avance"));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void resetearAvance() {
        try{
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGRO VALUES('3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3,'Among us',1)");
            GestorDB.getGestorDB().execSQL("INSERT INTO LOGROSUSUARIO VALUES('"+Usuario.getUsuario().getEmail()+"','3 seguidas','',2)");
            GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
            ResultadoSQL res;

            //Caso de prueba único: un logro con cualquier avance acaba en 0 tras este método
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL="+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas'");
            res.next();
            assertEquals(2,res.getInt("avance"));
            GestorLogros.getGestorLogros().actualizarLogros(false,1);
            res=GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL="+Usuario.getUsuario().getEmail()+"' AND NOMBRELOGRO='3 seguidas'");
            res.next();
            assertEquals(0,res.getInt("avance"));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void getDatosLogro() {
    }
}