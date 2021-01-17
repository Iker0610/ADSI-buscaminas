package is.buscaminas.model;


import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.db.GestorDB;

import java.sql.SQLException;


public class Nivel
{
	// Atributos
	
	private final int nivel;
	private int dificultad;
	private int nColumnas;
	private int nFilas;
	
	
	// Constructora
	
	public Nivel(int pNivel, int pDificultad, int pNColumnas, int pNFilas)
	{
		nivel      = pNivel;
		dificultad = pDificultad;
		nColumnas  = pNColumnas;
		nFilas     = pNFilas;
	}
	
	
	// Métodos
	
	public boolean actualizarDatos(int pDificultad, int pNColumnas, int pNFilas) throws SQLException
	{
		boolean datosCorrectos;
		
		datosCorrectos = pNColumnas > 0 & pNColumnas <= 50 & pNFilas > 0 & pNColumnas <= 50 & pDificultad > 0 & pDificultad < pNColumnas * pNFilas;
		
		if (datosCorrectos){
			// Se actualizan en la BD
			String sqlSentence = "UPDATE Nivel SET dificultad ='" + pDificultad + "', nFilas = '" + pNFilas + "', nColumnas = '" + pNColumnas
										+ "' WHERE nivel = '" + nivel+"'";
			GestorDB.getGestorDB().execSQL(sqlSentence);
			
			
			// Si ha ido bien se cambian los valores
			dificultad = pDificultad;
			nColumnas  = pNColumnas;
			nFilas     = pNFilas;
		}
		
		return datosCorrectos;
	}
	
	public String conseguirDatosNivel()
	{
		JsonObject json = new JsonObject();
		json.put("nivel", nivel);
		json.put("dificultad", dificultad);
		json.put("nColumnas", nColumnas);
		json.put("nFilas", nFilas);
		return json.toJson();
	}
	public String getNivel(){
		JsonObject json = new JsonObject();
		json.put("nivel",nivel);
		return json.toJson();
	}
}
