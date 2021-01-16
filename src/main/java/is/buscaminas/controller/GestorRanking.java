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
	
	public static GestorRanking getGestorRanging()
	{
		if (mGestorRanking == null){
			mGestorRanking = new GestorRanking();
		}
		return mGestorRanking;
	}
	
	public String obtenerRankingGlobal() throws SQLException
	{
		/*
		[while]
		Puntero -> null
		Objeto ->  new JsonObject(); #618576571x00
		Puntero -> 618576571x00

		Puntero.put(nickname) -> 618576571x00 -> nickname = nickname

		------------
		Garbage colector -> puntero solo existe en el while, hemos salido del while -> Es inutil -> borrar
		------------

		Puntero -> null
		Objeto ->  new JsonObject(); #618576571x00
		Puntero -> 618576571x00

		Puntero.put(nickname) -> 618576571x00 -> nickname = nickname

		 */
		JsonObject rankingJson = new JsonObject();
		JsonArray listadoRanking = new JsonArray();
		ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("Select nickname,nivel,puntuacion from Ranking order by puntuacion desc");
		while (resultado.next()){
			JsonObject elementoRankingJson = new JsonObject();
			String nickname = resultado.getString("nickname");
			int nivel = resultado.getInt("nivel");
			int puntuacion = resultado.getInt("puntuacion");
			elementoRankingJson.put("nickname", nickname);
			elementoRankingJson.put("nivel", nivel);
			elementoRankingJson.put("puntuacion", puntuacion);
			listadoRanking.add(elementoRankingJson);
		}
		resultado.close();
		rankingJson.put("ranking", listadoRanking);
		return rankingJson.toJson();
	}
	
	public String obtenerRankingPersonal() throws SQLException
	{
		JsonObject rankingJson = new JsonObject();
		JsonArray listadoRankingJson = new JsonArray();
		String email = Usuario.getUsuario().getEmail();
		ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("Select nickname,nivel,puntuacion from Ranking where email = '" + email + "' order by puntuacion desc");
		while (resultado.next()){
			JsonObject elementoRankinJson = new JsonObject();
			String nickname = resultado.getString("nickname");
			int nivel = resultado.getInt("nivel");
			int puntuacion = resultado.getInt("puntuacion");
			elementoRankinJson.put("nickname", nickname);
			elementoRankinJson.put("nivel", nivel);
			elementoRankinJson.put("puntuacion", puntuacion);
			listadoRankingJson.add(elementoRankinJson);
		}
		resultado.close();
		rankingJson.put("ranking", listadoRankingJson);
		return rankingJson.toJson();
	}
	
	public String clasificarPorNivelGlobal() throws SQLException
	{
		// Se hacen las consultas en la BD
		ResultadoSQL resultadoNiveles = GestorDB.getGestorDB().execSELECT("SELECT nivel FROM Nivel");
		
		JsonObject rankingClasificadoPorNiveles = new JsonObject();
		while (resultadoNiveles.next()){
			String nivel = resultadoNiveles.getString("nivel");
			rankingClasificadoPorNiveles.put(nivel, new JsonArray());
		}
		resultadoNiveles.close();
		
		ResultadoSQL resultadoRankingPorNivel = GestorDB.getGestorDB().execSELECT("SELECT nivel,nickname,puntuacion FROM Ranking ORDER BY nivel ASC, puntuacion DESC");
		
		while (resultadoRankingPorNivel.next()){
			JsonObject nuevoRegistroRanking = new JsonObject();
			String nivelElementoRanking = Integer.toString(resultadoRankingPorNivel.getInt("nivel"));
			nuevoRegistroRanking.put("nickname", resultadoRankingPorNivel.getString("nickname"));
			nuevoRegistroRanking.put("nivel", nivelElementoRanking);
			nuevoRegistroRanking.put("puntuacion", resultadoRankingPorNivel.getString("puntuacion"));
			
			((JsonArray) rankingClasificadoPorNiveles.get(nivelElementoRanking)).add(nuevoRegistroRanking);
		}
		resultadoRankingPorNivel.close();
		
		return rankingClasificadoPorNiveles.toJson();
	}
	
	
	public String clasificarPorNivelPersonal() throws SQLException
	{
		
		// Se hacen las consultas en la BD
		ResultadoSQL resultadoNiveles = GestorDB.getGestorDB().execSELECT("SELECT nivel FROM Nivel");
		
		JsonObject rankingClasificadoPorNiveles = new JsonObject();
		while (resultadoNiveles.next()){
			String nivel = resultadoNiveles.getString("nivel");
			rankingClasificadoPorNiveles.put(nivel, new JsonArray());
		}
		resultadoNiveles.close();
		
		String email = Usuario.getUsuario().getEmail();
		ResultadoSQL resultadoRankingPorNivel = GestorDB.getGestorDB().execSELECT("SELECT nivel,nickname,puntuacion FROM Ranking where email = '" + email + "' ORDER BY nivel ASC, puntuacion DESC");
		
		while (resultadoRankingPorNivel.next()){
			JsonObject nuevoRegistroRanking = new JsonObject();
			String nivelElementoRanking = Integer.toString(resultadoRankingPorNivel.getInt("nivel"));
			nuevoRegistroRanking.put("nickname", resultadoRankingPorNivel.getString("nickname"));
			nuevoRegistroRanking.put("nivel", nivelElementoRanking);
			nuevoRegistroRanking.put("puntuacion", resultadoRankingPorNivel.getString("puntuacion"));
			
			((JsonArray) rankingClasificadoPorNiveles.get(nivelElementoRanking)).add(nuevoRegistroRanking);
		}
		resultadoRankingPorNivel.close();
		
		return rankingClasificadoPorNiveles.toJson();
	}
	
	public void registrarPuntuacion(int pPuntuacion, int pNivel)
	{
		try{
			String nickname = Usuario.getUsuario().getNickname();
			String email = Usuario.getUsuario().getEmail();
			GestorDB.getGestorDB().execSQL("INSERT INTO Ranking (nivel, email, puntuacion, nickname) VALUES (" + pNivel + ",'" + email + "'," + pPuntuacion + ",'" + nickname + "')");
		}
		catch (SQLException ignored){
		}
	}
}

