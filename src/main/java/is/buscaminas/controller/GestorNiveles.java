package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonArray;
import is.buscaminas.model.Nivel;

import java.util.ArrayList;


public class GestorNiveles
{
	// Atributos
	
	private static GestorNiveles mGestorNiveles;
	private final ArrayList<Nivel> lNiveles;
	
	
	// Constructora y carga de datos
	
	private GestorNiveles(){
		lNiveles = new ArrayList<>();
		cargarNiveles();
	}
	
	public static GestorNiveles getGestorNiveles(){
		if (mGestorNiveles == null) mGestorNiveles = new GestorNiveles();
		return mGestorNiveles;
	}
	
	
	// Patrón Singleton
	
	private void cargarNiveles(){
		// TODO
	}
	
	
	// Métodos
	
	public boolean guardarDatos(int pNivel, int pDificultad, int pNColumnas, int pNFilas){
		Nivel nivel = lNiveles.get(pNivel);
		return nivel.actualizarDatos(pDificultad, pNColumnas, pNFilas);
	}
	
	public String obtenerDatosNiveles(){
		JsonArray lNivelesJSON = new JsonArray();
		for (Nivel nivel : lNiveles) lNivelesJSON.add(nivel.conseguirDatosNivel());
		return lNivelesJSON.toJson();
	}
}
