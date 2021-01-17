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
			
			//6.8 Casos de prueba para logros de victorias consecutivas
			
			/*
			6.8.1.1
			El usuario pierde.
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			Que el avance de los logros esté a 0.
			El avance de los logros está a 0.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(false, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(0, res.getInt("avance"));
			res.close();
			
			/*
			6.8.1.2
			El usuario pierde en un nivel diferente.
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			Que el avance de los logros esté a 0.
			El avance de los logros está a 0.
			 */
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=1 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 3 seguidas'");
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());
			GestorLogros.getGestorLogros().actualizarLogros(false, 2);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(0, res.getInt("avance"));
			res.close();

			/*
			6.8.1.3
			El usuario gana la partida y consigue el logro.
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			Que el avance del logro sea igual al objetivo, que el logro conseguido esté en la lista logrosObtenidos y que le logro esté conseguido (tenga fecha de obtención).
			El avance es igual al objetivo, el logro tiene fecha de Obtención y está en lista logrosObtenidos.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			res = GestorDB.getGestorDB().execSELECT("SELECT FECHAOBTENCION FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 seguidas'");
			res.next();
			assertEquals(fecha, res.getString("fechaObtencion"));
			res.close();
			
			//6.9 Casos de prueba para logros de victorias en nivel
			GestorDB.getGestorDB().execSQL("INSERT INTO Logro(nombre,descripcion,tipo,objetivo,nivel) VALUES('Test logro: 3 victorias Nvl 1','Ganar 3 partidas en el nivel 1','VictoriaNivel',3,1)");
			GestorDB.getGestorDB().execSQL("UPDATE LogrosUsuario SET avance=0 WHERE email='" + Usuario.getUsuario().getEmail() + "' AND nombreLogro='Test logro: 3 victorias Nvl 1'");
			GestorLogros.getGestorLogros().cargarLogros(Usuario.getUsuario().getEmail());

			/*
			6.8.2.1
			El usuario gana 1 vez en el mismo nivel pero no logra el objetivo.
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			Que el avance del logro sea igual al objetivo, que el logro obtenido esté en la lista de logrosObtenidos y que el logro esté conseguido (tenga fecha de obtención).
			El avance es igual al objetivo, el logro tiene fecha de obtención y está en lista logrosObtenidos.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			/*
			6.8.2.2
			Pierde en el mismo nivel
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			El logro no se actualiza y se queda como está.
			El logro no se actualiza y se queda como está.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(false, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			/*
			6.8.2.3
			Gana en un nivel diferente
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			El logro no se actualiza y se queda como está.
			El logro no se actualiza y se queda como está.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(true, 2);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();

			/*
			6.8.2.4
			Gana en el mismo nivel y consigue el logro.
			El método comprueba los logros y hace lo consecuente con el resultado de la partida.
			El avance del logro llega al objetivo, se le añade la fecha de Obtención y se mueve a logrosObtenidos.
			EL avance llega al objetivo, se le ha añadido la fecha de obtención y el logro ha sido movido a logrosObtenidos.
			 */
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
			
			/*
			6.9.1
			Aumentar avance pero no llega al objetivo.
			El método aumenta en una unidad el avance del logro correspondiente y se ocupa de actualizar en la base de datos.
			El avance ha sido incrementado pero no ha llegado al objetivo.
			El avance ha sido incrementado pero no ha llegado al objetivo.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(1, res.getInt("avance"));
			res.close();
			
			/*
			6.9.2
			Aumentar avance y llegar al objetivo
			El método aumenta en una unidad el avance del logro correspondiente y se ocupa de actualizar en la base de datos.
			El avance ha sido incrementado y ha llegado al objetivo.
			El avance ha sido incrementado y ha llegado al objetivo.
			 */
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			GestorLogros.getGestorLogros().actualizarLogros(true, 1);
			res = GestorDB.getGestorDB().execSELECT("SELECT AVANCE FROM LOGROSUSUARIO WHERE EMAIL='" + Usuario.getUsuario().getEmail() + "' AND NOMBRELOGRO='Test logro: 3 victorias Nvl 1'");
			res.next();
			assertEquals(3, res.getInt("avance"));
			res.close();
			
			/*
			6.9.3
			El avance ya ha llegado al objetivo,
			El método aumenta en una unidad el avance del logro correspondiente y se ocupa de actualizar en la base de datos.
			El avance no tiene que incrementarse porque ya ha llegado al objetivo.
			El avance no se incrementa.
			 */
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
			
			/*
			6.10
			El logro tiene un avance que no es 0.
			Un logro con cualquier avance acaba en 0.
			Que el avance del logro sea 0.
			El avance del logro es 0.
			 */
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
			
			/*
			6.11.1
			Los logros están sin cargar.
			El método devuelve de tipo primitivo los datos que componen un logro para que sea más fácil de leer por la interfaz.
			Que las listas de memoria que almacenan los logros estén vacías.
			Las listas de memoria que almacenan los logros están vacías.
			 */
			logrosObtenidos = new JsonArray();
			logrosRestantes = new JsonArray();
			res = GestorLogros.getGestorLogros().getLogros();
			datos=Jsoner.deserialize(res, new JsonObject());
			logrosObtenidos=Jsoner.deserialize((String) datos.get("logrosObtenidos"), new JsonArray());
			logrosRestantes=Jsoner.deserialize((String) datos.get("logrosRestantes"), new JsonArray());
			assertEquals(0, logrosObtenidos.size());
			assertEquals(0, logrosRestantes.size());
			
			/*
			6.11.2
			Los logros se han cargado.
			El método devuelve de tipo primitivo los datos que componen un logro para que sea más fácil de leer por la interfaz.
			Que devuelva la información de los logros en formato primitivo.
			Devuelve la información de los logros en formato primitivo.
			 */
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