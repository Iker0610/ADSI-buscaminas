package is.buscaminas.model;


import com.github.cliftonlabs.json_simple.JsonObject;


public class Nivel
{
	// Atributos
	
	private final int nivel;
	private int dificultad;
	private int nColumnas;
	private int nFilas;
	
	
	// Constructora
	
	public Nivel(int pNivel, int pDificultad, int pNColumnas, int pNFilas){
		nivel = pNivel;
		dificultad = pDificultad;
		nColumnas = pNColumnas;
		nFilas = pNFilas;
	}
	
	
	// Métodos
	
	public boolean esNivel(int pNivel){
		return nivel == pNivel;
	}
	
	public boolean actualizarDatos(int pDificultad, int pNColumnas, int pNFilas){
		// TODO COMPROBAR QUE LSO DATOS CUMPLEN ANTES DE CAMBIARLOS
		dificultad = pDificultad;
		nColumnas = pNColumnas;
		nFilas = pNFilas;
		
		// TODO CAMBIAR EL RETURN
		return true;
	}
	
	public String conseguirDatosNivel(){
		JsonObject json = new JsonObject();
		json.put("nivel", nivel);
		json.put("dificultad", dificultad);
		json.put("nColumnas", nColumnas);
		json.put("nFilas", nFilas);
		return json.toJson();
	}
}
