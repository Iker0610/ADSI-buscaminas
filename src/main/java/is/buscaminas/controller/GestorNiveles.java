package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonArray;
import is.buscaminas.model.Nivel;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;
import java.util.ArrayList;


public class GestorNiveles
{
	// Atributos
	
	private static GestorNiveles mGestorNiveles;
	private final ArrayList<Nivel> lNiveles;
	
	
	// Constructora y carga de datos
	
	private GestorNiveles() throws SQLException
	{
		lNiveles = new ArrayList<>();
		cargarNiveles();
	}
	
	public static GestorNiveles getGestorNiveles() throws SQLException
	{
		if (mGestorNiveles == null) mGestorNiveles = new GestorNiveles();
		return mGestorNiveles;
	}
	
	
	// Patrón Singleton
	
	private void cargarNiveles() throws SQLException
	{
		ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT * FROM Nivel");
		while (resultado.next()){
			int nombreNivel = resultado.getInt("nivel");
			int dificultad = resultado.getInt("dificultad");
			int nColumnas = resultado.getInt("nColumnas");
			int nFilas = resultado.getInt("nFilas");
			
			Nivel nivel = new Nivel(nombreNivel, dificultad, nColumnas, nFilas);
			lNiveles.add(nombreNivel - 1, nivel);
		}
	}
	
	
	// Métodos
	
	public boolean guardarDatos(int pNivel, int pDificultad, int pNColumnas, int pNFilas) throws SQLException
	{
		Nivel nivel = lNiveles.get(pNivel - 1);
		return nivel.actualizarDatos(pDificultad, pNColumnas, pNFilas);
	}
	
	public String obtenerDatosNiveles()
	{
		JsonArray lNivelesJSON = new JsonArray();
		for (Nivel nivel : lNiveles) lNivelesJSON.add(nivel.conseguirDatosNivel());
		return lNivelesJSON.toJson();
	}
}
