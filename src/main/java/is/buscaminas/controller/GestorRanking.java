package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;


public class GestorRanking
{
	
	private static GestorRanking mGestorRanking;
	
	private GestorRanking()
	{
	}

	// Patron Singleton
	public static GestorRanking getGestorRanging()
	{
		if (mGestorRanking == null){
			mGestorRanking = new GestorRanking();
		}
		return mGestorRanking;
	}

	// PRE:
	// POST: Obtiene y devuelve el ranking global sin clasificar de la base de datos
	public String obtenerRankingGlobal() throws SQLException
	{
		// Se crea un JsonObject que sera el ranking compuesto por un JsonArray en el que se añadira un json por cada tupla en el ranking
		JsonObject rankingJson = new JsonObject();
		JsonArray listadoRanking = new JsonArray();

		// Se hace un Select de los datos 'nickname','nivel' y 'puntuacion' en la Base de Datos
		ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT nickname,nivel,puntuacion FROM Ranking ORDER BY puntuacion DESC");

		// Para cada tupla
		while (resultado.next()){

			// Se crea un JsonObject y se extraen los datos de la tupla actual
			JsonObject elementoRankingJson = new JsonObject();
			String nickname = resultado.getString("nickname");
			int nivel = resultado.getInt("nivel");
			int puntuacion = resultado.getInt("puntuacion");

			// Se insertan los datos en el JsonObject
			elementoRankingJson.put("nickname", nickname);
			elementoRankingJson.put("nivel", nivel);
			elementoRankingJson.put("puntuacion", puntuacion);

			// Se añade el JsonObject al JsonArray
			listadoRanking.add(elementoRankingJson);
		}
		// Se cierra la consulta de la BD
		resultado.close();

		// Se inserta el JsonArray en el objeto ranking (JsonObject)
		rankingJson.put("ranking", listadoRanking);

		// Se devuelve el JsonObject final en formato String
		return rankingJson.toJson();
	}

	// PRE:
	// POST: Obtiene y devuelve el ranking personal sin clasificar de la BD
	public String obtenerRankingPersonal() throws SQLException
	{
		// Se crea un JsonObject que sera el ranking compuesto por un JsonArray en el que se añadira un json por cada tupla en el ranking
		JsonObject rankingJson = new JsonObject();
		JsonArray listadoRankingJson = new JsonArray();

		// Se obtiene el email del usuario actual
		String email = Usuario.getUsuario().getEmail();

		// Se hace un SELECT de las tuplas del ranking que contienen el email del usuario
		ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT nickname,nivel,puntuacion FROM Ranking WHERE email = '" + email + "' ORDER BY puntuacion DESC");

		while (resultado.next()){

			// Se crea un JsonObject y se extraen los datos de la tupla actual
			JsonObject elementoRankinJson = new JsonObject();
			String nickname = resultado.getString("nickname");
			int nivel = resultado.getInt("nivel");
			int puntuacion = resultado.getInt("puntuacion");

			// Se insertan los datos en el JsonObject
			elementoRankinJson.put("nickname", nickname);
			elementoRankinJson.put("nivel", nivel);
			elementoRankinJson.put("puntuacion", puntuacion);

			// Se añade el JsonObject al JsonArray
			listadoRankingJson.add(elementoRankinJson);
		}
		// Se cierra la consulta de la BD
		resultado.close();

		// Se inserta el JsonArray en el objeto ranking (JsonObject)
		rankingJson.put("ranking", listadoRankingJson);

		// Se devuelve el JsonObject final en formato String
		return rankingJson.toJson();
	}

	// PRE:
	// POST: Obtiene y devuelve el ranking global clasificado por niveles de la BD
	public String clasificarPorNivelGlobal() throws SQLException
	{
		// Se hace un SELECT de los niveles
		ResultadoSQL resultadoNiveles = GestorDB.getGestorDB().execSELECT("SELECT nivel FROM Nivel");

		// Se crea el objeto final que contendra el ranking
		JsonObject rankingClasificadoPorNiveles = new JsonObject();

		// Para cada nivel
		while (resultadoNiveles.next()){

			// Se añade el nivel y un JsonArray al objeto ranking final
			String nivel = resultadoNiveles.getString("nivel");
			rankingClasificadoPorNiveles.put(nivel, new JsonArray());
		}

		// Se cierra la consulta de la BD
		resultadoNiveles.close();

		// Se hace un SELECT de las tuplas del Ranking ordenadas por nivel
		ResultadoSQL resultadoRankingPorNivel = GestorDB.getGestorDB().execSELECT("SELECT nivel,nickname,puntuacion FROM Ranking ORDER BY nivel ASC, puntuacion DESC");

		// Para cada tupla
		while (resultadoRankingPorNivel.next()){

			// Se extraen los datos y se insertan en un JsonObject
			JsonObject nuevoRegistroRanking = new JsonObject();
			String nivelElementoRanking = Integer.toString(resultadoRankingPorNivel.getInt("nivel"));
			nuevoRegistroRanking.put("nickname", resultadoRankingPorNivel.getString("nickname"));
			nuevoRegistroRanking.put("nivel", nivelElementoRanking);
			nuevoRegistroRanking.put("puntuacion", resultadoRankingPorNivel.getString("puntuacion"));

			// Se añade el JsonObject que contiene la los datos de la tupla actual al JsonArray correspondiente a su nivel
			((JsonArray) rankingClasificadoPorNiveles.get(nivelElementoRanking)).add(nuevoRegistroRanking);
		}

		// Se cierra la consulta de la BD
		resultadoRankingPorNivel.close();

		// Se devuelve el JsonObject final en formato String
		return rankingClasificadoPorNiveles.toJson();
	}

	// PRE:
	// POST: Obtiene y devuelve el ranking personal clasificado por niveles de la BD
	public String clasificarPorNivelPersonal() throws SQLException
	{
		
		// Se hace un SELECT de los niveles
		ResultadoSQL resultadoNiveles = GestorDB.getGestorDB().execSELECT("SELECT nivel FROM Nivel");

		// Se crea el objeto final que contendra el ranking
		JsonObject rankingClasificadoPorNiveles = new JsonObject();

		// Para cada tupla
		while (resultadoNiveles.next()){

			// Se añade el nivel y un JsonArray al objeto ranking final
			String nivel = resultadoNiveles.getString("nivel");
			rankingClasificadoPorNiveles.put(nivel, new JsonArray());
		}
		// Se cierra la consulta de la BD
		resultadoNiveles.close();

		// Se obtiene el email del usuario actual
		String email = Usuario.getUsuario().getEmail();

		// Se hace un SELECT de las tuplas del ranking que contienen el email del usuario ordenadas por nivel
		ResultadoSQL resultadoRankingPorNivel = GestorDB.getGestorDB().execSELECT("SELECT nivel,nickname,puntuacion FROM Ranking where email = '" + email + "' ORDER BY nivel ASC, puntuacion DESC");

		// Para cada tupla
		while (resultadoRankingPorNivel.next()){

			// Se extraen los datos y se insertan en un JsonObject
			JsonObject nuevoRegistroRanking = new JsonObject();
			String nivelElementoRanking = Integer.toString(resultadoRankingPorNivel.getInt("nivel"));
			nuevoRegistroRanking.put("nickname", resultadoRankingPorNivel.getString("nickname"));
			nuevoRegistroRanking.put("nivel", nivelElementoRanking);
			nuevoRegistroRanking.put("puntuacion", resultadoRankingPorNivel.getString("puntuacion"));

			// Se añade el JsonObject que contiene la los datos de la tupla actual al JsonArray correspondiente a su nivel
			((JsonArray) rankingClasificadoPorNiveles.get(nivelElementoRanking)).add(nuevoRegistroRanking);
		}

		// Se cierra la consulta de la BD
		resultadoRankingPorNivel.close();

		// Se devuelve el JsonObject final en formato String
		return rankingClasificadoPorNiveles.toJson();
	}

	// PRE: Recibe la puntuacion y el nivel de la tupla a insertar en el ranking
	// POST: Inserta una nueva tupla en el ranking en la BD
	public void registrarPuntuacion(int pPuntuacion, int pNivel)
	{
		try{

			// Se obtiene el nickname y el email del Usuario
			String nickname = Usuario.getUsuario().getNickname();
			String email = Usuario.getUsuario().getEmail();

			// Se inserta la tupla en la BD
			GestorDB.getGestorDB().execSQL("INSERT INTO Ranking (nivel, email, puntuacion, nickname) VALUES (" + pNivel + ",'" + email + "'," + pPuntuacion + ",'" + nickname + "')");
		}
		catch (SQLException ignored){
		}
	}
}

