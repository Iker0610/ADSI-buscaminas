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
	
	private GestorNiveles()
	{
		lNiveles = new ArrayList<>();
		cargarNiveles();
	}
	
	public static GestorNiveles getGestorNiveles()
	{
		if (mGestorNiveles == null) mGestorNiveles = new GestorNiveles();
		return mGestorNiveles;
	}
	
	
	// Patrón Singleton
	
	private void cargarNiveles()
	{
		// Pre:
		// Post: Se cargan todos los datos de los niveles desde la base de datos. Se crea un objeto nivel por cada tupla en la tabla Nivel.
		try{
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
		catch (SQLException throwables){
			cargarNiveles();
		}
	}
	
	
	// Métodos
	
	public boolean guardarDatos(int pNivel, int pDificultad, int pNColumnas, int pNFilas) throws SQLException
	{
		// Pre: que el Nivel exista
		// Post: Si los datos cumplen las RI, el nivel se actualiza en memoria y en la BD
		Nivel nivel = lNiveles.get(pNivel - 1);
		return nivel.actualizarDatos(pDificultad, pNColumnas, pNFilas);
	}
	
	public String obtenerDatosNiveles()
	{
		// Post: Se obtiene un array con objetos JSON, cada objeto JSON corresponde a un nivel.
		JsonArray lNivelesJSON = new JsonArray();
		for (Nivel nivel : lNiveles) lNivelesJSON.add(nivel.conseguirDatosNivel());
		return lNivelesJSON.toJson();
	}
	
	public String getDatosNivel(int pNivel)
	{
		// Pre: El nivel existe.
		// Post: Se devuelve un JSON con los datos del nivel solicitado.
		return lNiveles.get(--pNivel).conseguirDatosNivel();
	}
}
