package is.buscaminas.model.logros;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.GestorLogros;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class LogroTest
{
	
	@org.junit.jupiter.api.Test
	void comprobarLogro()
	{
		try{
			GestorLogros.getGestorLogros().reset();
			Usuario.create("jon@ejemplo.com", "123", 1, "Mario", false);
			GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email LIKE 'jon%' OR nombreLogro LIKE 'Test logro%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro:%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE EMAIL LIKE 'jon%'");
			GestorDB.getGestorDB().execSQL("INSERT INTO Usuario(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo) VALUES('Test logro: 3 seguidas','Ganar 3 partidas consecutivas','VictoriaConsecutiva',3)");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=1 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 3 seguidas'");
			ResultadoSQL res;
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
			
			//Casos de prueba para logros de victorias consecutivas
			
			//Caso de prueba: pierde, el nivel da igual
			GestorLogros.getGestorLogros().actualizarLogros(false, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(0, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: pierde en un nivel diferente
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=1 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 3 seguidas'");
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
			GestorLogros.getGestorLogros().actualizarLogros(false, 2);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(0, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: gana la partida y consigue el logro
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			res = GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(fecha, res.getString("fechaObtencion"));
			res.close();
			
			//Casos de prueba para logros de victorias en nivel
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo,nivel) VALUES('Test logro: 3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,1)");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 3 victorias Nvl 1'");
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());

			//Caso de prueba: gana 1 vez en el mismo nivel pero no logra el objetivo
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: pierde en el mismo nivel
			GestorLogros.getGestorLogros().actualizarLogros(false, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: gana en un nivel diferente
			GestorLogros.getGestorLogros().actualizarLogros(true, 2);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: gana en el mismo nivel y consigue el logro
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			res.next();
			assertEquals(fecha, res.getString("fechaObtencion"));
			res.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@org.junit.jupiter.api.Test
	void actualizarAvance()
	{
		try{
			GestorLogros.getGestorLogros().reset();
			Usuario.create("jon@ejemplo.com", "123", 1, "Mario", false);
			GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email='"+Usuario.getUsuario().getEmail()+"' OR nombreLogro LIKE 'Test logro%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");
			GestorDB.getGestorDB().execSQL("INSERT INTO Usuario(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo,nivel) VALUES('Test logro: 3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,1)");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance =0 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 3 victorias Nvl 1'");
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
			ResultadoSQL res;
			
			//Caso de prueba: aumentar avance pero no llegar al objetivo
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: aumentar avance y llegar al objetivo
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(3, res.getInt("avance"));
			res.close();
			
			//Caso de prueba: el avance ya ha llegado al objetivo por lo que cuando se actualice no deberia actualizarse más
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(3, res.getInt("avance"));
			res.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@org.junit.jupiter.api.Test
	void resetearAvance()
	{
		try{
			GestorLogros.getGestorLogros().reset();
			Usuario.create("jon@ejemplo.com", "123", 1, "Mario", false);
			GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email LIKE 'jon%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");
			GestorDB.getGestorDB().execSQL("INSERT INTO Usuario(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo) VALUES('Test logro: 3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3)");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=2 WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
			ResultadoSQL res;
			
			//Caso de prueba único: un logro con cualquier avance acaba en 0 tras este método
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(2, res.getInt("avance"));
			res.close();
			GestorLogros.getGestorLogros().actualizarLogros(false, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(0, res.getInt("avance"));
			res.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@org.junit.jupiter.api.Test
	void getDatosLogro()
	{
		try{
			GestorLogros.getGestorLogros().reset();
			Usuario.create("jon@ejemplo.com", "123", 1, "Mario", false);
			GestorDB.getGestorDB().execSQL("DELETE FROM LogrosUsuario WHERE email LIKE 'jon%' OR nombreLogro LIKE 'Test logro%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Logro WHERE nombre LIKE 'Test logro%'");
			GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email LIKE 'jon%'");
			GestorDB.getGestorDB().execSQL("INSERT INTO Usuario(email) VALUES('"+Usuario.getUsuario().getEmail()+"')");
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo) VALUES('Test logro: 4 seguidas','Ganar 4 partidas seguidas','VictoriaConsecutiva',4)");
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo) VALUES('Test logro: 3 seguidas','Ganar 3 partidas seguidas','VictoriaConsecutiva',3)");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 4 seguidas'");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=3,fechaObtencion='" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "' WHERE email='"+Usuario.getUsuario().getEmail()+"' AND nombreLogro='Test logro: 3 seguidas'");
			String res = null;
			JsonObject datos = null;
			JsonArray logrosObtenidos, logrosRestantes;
			
			//Logros sin cargar
			logrosObtenidos = new JsonArray();
			logrosRestantes = new JsonArray();
			res = GestorLogros.getGestorLogros().getLogros();
			datos=Jsoner.deserialize(res, new JsonObject());
			logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"), new JsonArray());
			logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"), new JsonArray());
			assertEquals(0, logrosObtenidos.size());
			assertEquals(0, logrosRestantes.size());
			
			//Con los logros cargados
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
			res = GestorLogros.getGestorLogros().getLogros();
			datos=Jsoner.deserialize(res, new JsonObject());
			logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"), new JsonArray());
			logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"), new JsonArray());
			//PRIMERO LOGROS OBTENIDOS
				String logro = (String) logrosObtenidos.get(1);	//Porque el logro conseguido está en la segunda posición
				JsonObject datosLogro = null;
				datosLogro=Jsoner.deserialize(logro, new JsonObject());
				assertEquals("Test logro: 3 seguidas", (String) datosLogro.get("nombre"));
				assertEquals("Ganar 3 partidas seguidas", (String) datosLogro.get("descripcion"));
				assertEquals("3", datosLogro.get("objetivo").toString());
				assertEquals("3", datosLogro.get("avance").toString());
				assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), datosLogro.get("fechaObtencion"));
			//SEGUNDO LOGROS RESTANTES
				logro = (String) logrosRestantes.get(logrosRestantes.size()-1);
				datosLogro=Jsoner.deserialize(logro, new JsonObject());
				assertEquals("Test logro: 4 seguidas", datosLogro.get("nombre"));
				assertEquals("Ganar 4 partidas seguidas", datosLogro.get("descripcion"));
				assertEquals("4", datosLogro.get("objetivo").toString());
				assertEquals("0", datosLogro.get("avance").toString());
				assertNull(datosLogro.get("fechaObtencion"));
		}
      catch (Exception e){
          e.printStackTrace();
      }
   }
}