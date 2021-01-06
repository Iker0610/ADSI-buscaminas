package is.buscaminas.model.logros;


import com.github.cliftonlabs.json_simple.JsonObject;


public class Logro
{
	//Atributos
	
	protected String nombre;
	protected String descripcion;
	protected int avance;
	protected int objetivo;
	protected String fechaObtencion;
	protected String nombreTema;
	
	
	//Constructora
	
	public Logro(String pNombre, String pDescripcion, int pAvance, int pObjetivo, String pFechaObtencion, String pNombreTema)
	{
		nombre = pNombre;
		descripcion = pDescripcion;
		avance = pAvance;
		objetivo = pObjetivo;
		fechaObtencion = pFechaObtencion;
		nombreTema = pNombreTema;
	}
	
	
	// Métodos
	
	public void comprobarLogro(boolean victoria, int nivel, String email)
	{
	}
	
	//Método que devuelve un elemento para la interfaz gráfica con los datos del logro en formato primitivo.
	public String getDatosLogro()
	{
		//Precondición: no tiene precondición.
		//Postcondición: el método devuelve el objeto que tiene que devolver.
		JsonObject json3 = new JsonObject();
		json3.put("nombre", nombre);
		json3.put("descripcion", descripcion);
		json3.put("objetivo", objetivo);
		json3.put("avance", avance);
		json3.put("fechaObtencion", fechaObtencion);
		json3.put("nombreTema", nombreTema);
		return json3.toJson();
	}
}