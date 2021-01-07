package is.buscaminas.model.logros;


import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.controller.GestorLogros;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class Logro
{
	//Atributos
	
	private String nombre;
	private String descripcion;
	private int avance;
	private int objetivo;
	private String fechaObtencion;
	private String nombreTema;
	
	
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
	
	public abstract boolean comprobarLogro(boolean victoria, int nivel, String email);
	
	protected boolean actualizarAvance()
	{
		// Pre: Que el logro haya comprobado el avance
		// Post: Se actualiza el avance. Si es igual al objetivo se actualiza la fecha de obtención.

		boolean conseguido=false;
		if (avance < objetivo){
			avance++;
			if (avance == objetivo){      //Si el avance es igual al objetivo
				conseguido=true;
				// Se establece una fecha de Obtención
				try{
					fechaObtencion = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				}
				catch (Exception ex){
					fechaObtencion = "01/01/2000";
				}
			}
			GestorDB gestorDB = GestorDB.getGestorDB();
			String sql = "UPDATE LogrosUsuario SET AVANCE = '" + avance + "',Fecha = '" + fechaObtencion + "' WHERE email = '" + Usuario.getUsuario().getEmail() + "' AND nombreLogro = '" + nombre + "'";
			try{
				gestorDB.execSQL(sql);
			}
			catch (SQLException e){
				System.out.println("Error al extraer información de la base de datos");
			}
		}
		return conseguido;
	}
	
	protected void resetearAvance()
	{
		avance = 0;
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