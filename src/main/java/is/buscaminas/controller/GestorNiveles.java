package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
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
	
	private void cargarNiveles() {
		//Pre:
		//Post:Se han cargado los datos de todos los niveles de la base de datos.
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
	
	public boolean guardarDatos(int pNivel, int pDificultad, int pNColumnas, int pNFilas) throws SQLException {
		//Pre: cuatro integers, con el nivel, la dificultad, las columnas y las filas.
		//Post: devuelve true si se han actualizado los datos correctamente, false en caso contrario.
		Nivel nivel = lNiveles.get(pNivel - 1);
		return nivel.actualizarDatos(pDificultad, pNColumnas, pNFilas);
	}
	
	public String obtenerDatosNiveles() {
		//Pre:
		//Post: devuelve un JsonArray con los datos de los niveles.
		JsonArray lNivelesJSON = new JsonArray();
		for (Nivel nivel : lNiveles) lNivelesJSON.add(nivel.conseguirDatosNivel());
		return lNivelesJSON.toJson();
	}
	
	public String getDatosNivel(int pNivel) {
		//Pre: in nivel.
		//Post: devuelve un JsonArray con los datos de ese nivel.

		return lNiveles.get(--pNivel).conseguirDatosNivel();
	}

	public String getNiveles(){
		//Pre:
		//Post: devuelve un JsonArray con los niveles existentes.
		JsonArray lNivelesJSON = new JsonArray();
		for (Nivel nivel : lNiveles){
			JsonObject json = Jsoner.deserialize(nivel.getNivel(), new JsonObject());
			lNivelesJSON.add(json);
		}
		return lNivelesJSON.toJson();
	}
}
