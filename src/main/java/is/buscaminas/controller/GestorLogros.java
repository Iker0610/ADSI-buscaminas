package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import is.buscaminas.model.logros.DatosLogro;
import is.buscaminas.model.logros.Logro;
import is.buscaminas.model.logros.LogroVictoriaConsecutiva;
import is.buscaminas.model.logros.LogroVictoriaNivel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;


public class GestorLogros
{
	//Atributos
	
	private static GestorLogros miGestorLogros;
	private ArrayList<Logro> logrosObtenidos;
	private ArrayList<Logro> logrosRestantes;
	
	
	//Constructora
	
	private GestorLogros()
	{
		logrosObtenidos = new ArrayList<Logro>();
		logrosRestantes = new ArrayList<Logro>();
	}
	
	//Singleton
	
	public static GestorLogros getGestorLogros()
	{
		//Precondición: no tiene ninguna precondición.
		//Postcondicián: debe devolver un objeto de tipo GestorLogros que debe ser la única instancia de dicho objeto.
		if (miGestorLogros == null){
			miGestorLogros = new GestorLogros();
		}
		return miGestorLogros;
	}
	
	
	//Método para añadir un logro en la lista logrosObtenidos
	public void anadirLogroObtenido(Logro l)
	{
		//Precondición: el método recibe un objeto de tipo Logro.
		//Postcondicián: el método añade el logro recibido en la lista logrosObtenidos. No devuelve nada.
		if (!this.logrosObtenidos.contains(l)){
			this.logrosObtenidos.add(l);
		}
	}
	
	//Método para añadir un logro en la lista logrosRestantes
	public void anadirLogroRestante(Logro l)
	{
		//Precondición: el método recibe un objeto de tipo Logro.
		//Postcondicián: el método añade el logro recibido en la lista logrosRestantes. No devuelve nada.
		if (!this.logrosRestantes.contains(l)){
			this.logrosRestantes.add(l);
		}
	}
	
	//Método para eliminar un logro en la lista logrosObtenidos
	public void eliminarObtenido(Logro l)
	{
		//Precondición: recibe un logro.
		//Postcondicián: elimina de la lista el logro y, si no está, no hace nada.
		logrosObtenidos.remove(l);
	}
	
	//Método para eliminar un logro en la lista logrosRestantes
	public void eliminarRestante(Logro l)
	{
		//Precondición: recibe un logro.
		//Postcondicián: elimina de la lista el logro y, si no está, no hace nada.
		logrosRestantes.remove(l);
	}
	
	//Este método se ejecuta al finalizar la partida, poniendo al día los logros y comprobar si ha completado alguno.
	public void actualizarLogros(boolean victoria, int nivel)
	{
		//Precondición: el método debe recibir un booleano determinando el resultado de la partida jugada y un número que fije el nivel al que ha jugado.
		//Postcondicián: se han actualizado todos los logros en caso de que fuese necesario y, en caso de completarse, que se asigne a la lista de logros completados correctamente.
		try{
			Usuario usuario = Usuario.getUsuario();
			String email = usuario.getEmail();
			//Para hacer este método más modular, se ha optado por dividir en dos submétodos para hacer más fácil el mantenimiento de la aplicación.
			actualizarLogrosRestantes(victoria, nivel, email);
		}
		//En caso de que salte una excepción, ocurrira lo siguiente.
		catch (NoSuchElementException e){
			e.printStackTrace();
		}
	}
	
	//Método para actualizar los logros restantes
	private void actualizarLogrosRestantes(boolean victoria, int nivel, String email)
	{
		//Precondición: este método debe recibir las variables enviadas a su "método superior" y, además, el email del usuario que ha jugado en formato String.
		//Postcondicián: el logro es actualizado correctamente y, en caso de ser completado, pasarlo a la lista de logrosObtenidos.

		for (Logro logro : logrosRestantes) {
			if(logro.comprobarLogro(victoria, nivel, email)){
				eliminarRestante(logro);
				eliminarObtenido(logro);
			}
		}
	}
	
	//Método para generar los objetos para la interfaz gráfica (JSON).
	public String getLogros()
	{
		//Precondición: no recibe nada.
		//Postcondicián: devuelve un objeto de tipo JSON1(CAMBIAR POR NUEVO NOMBRE), definido en la documentación.
		//Como el el objeto JSON1 está formado por 2 atributos JSON2, los generamos con los métodos siguientes y, una vez obtenidos dichos objetos, llamamos a la constructora de JSON1 dandole los dos objetos que acabamos de sacar.
		
		// Se crea el objeto JSON
		JsonObject json1 = new JsonObject();
		
		// Se guardan los datos de ambas listas de logros
		json1.put("logrosObtenidos", getLogrosObtenidos());
		json1.put("logrosRestantes", getLogrosRestantes());
		
		// Se convierte a String y se devuelve
		return json1.toJson();
	}
	
	//Método para generar los objetos para la interfaz gráfica (JSON).
	private String getLogrosObtenidos()
	{
		//Precondición: no recibe nada.
		//Postcondicián: devuelve un objeto de tipo JSON2, que está formado por una lista con los atributos de los logros obtenidos.
		
		// Se crea el objeto JSON
		JsonArray json2 = new JsonArray();
		
		// Se guardan los datos de todos los logros de la lista en formato JSON
		for (Logro logro : logrosObtenidos) json2.add(logro.getDatosLogro());
		
		// Se convierte a String y se devuelve
		return json2.toJson();
	}
	
	//Método para generar los objetos para la interfaz gráfica (JSON).
	private String getLogrosRestantes()
	{
		//Precondición: no recibe nada.
		//Postcondicián: devuelve un objeto de tipo JSON2, que está formado por una lista con los atributos de los logros restantes.
		
		// Se crea el objeto JSON
		JsonArray json2 = new JsonArray();
		
		// Se guardan los datos de todos los logros de la lista en formato JSON
		for (Logro logro : logrosRestantes) json2.add(logro.getDatosLogro());
		
		// Se convierte a String y se devuelve
		return json2.toJson();
	}
	
	//Caso de uso Cargar Logros
	public void cargarLogros(String email)
	{
		//Precondición: recibe el email del jugador en formato String.
		//Postcondicián: se cargan todos los logros en memoria. El email será necesario para saber que logros ha obtenido dicho jugador. No devuelve nada.
		try {
			reset();
			GestorDB gestorDB = GestorDB.getGestorDB();
			String sql = "SELECT * FROM LogrosUsuario INNER JOIN LogroUsuario ON Logro.Nombre = LogroUsuario.NombreLogro";
			ResultadoSQL res = gestorDB.execSELECT(sql);
			while (res.next()) {
				DatosLogro datos = getDatosLogro(res);
				//Para que no afecte a la modularidad, se ha añadido un método privado para que el método cargarLogros no se ocupe también de crearlos, haciendo que este proceso sea más sostenible.
				generarLogro(datos);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//Método para obtener un objeto de tipo DatosLogro que contenga todos los atributos necesarios que requiera cada Logro cargado en memoria
	private DatosLogro getDatosLogro(ResultadoSQL res)
	{
		//Precondición: el método recibe el resultado de la encuesta realizada en lenguaje SQL.
		//Postcondicián: se devuelve un objeto de clase DatosLogros que se compone por los atributos primitivos de los logros.
		DatosLogro dl = null;
		try{
			String fecha = res.getString("fechaObtencion");
			String nombreLogro = res.getString("nombreLogro");
			String desc = res.getString("descripcion");
			int avance = res.getInt("avance");
			String tipo = res.getString("tipo");
			String nombreTema = res.getString("nombreTema");
			int objetivo = res.getInt("objetivo");
			int nivel = res.getInt("nivel");
			dl = new DatosLogro(nombreLogro, desc, fecha, avance, nombreTema, objetivo, tipo, nivel);
		}
		catch (SQLException e){
			System.out.println("Error al extraer de la base de datos");
		}
		return dl;
	}
	
	//Submétodo para crear los logros
	private void generarLogro(DatosLogro datos)
	{
		//Precondición: el método recibe el objeto de clase DatosLogro para poder crear los logros con los atributos que este contiene.
		//Postcondicián: el método genera cada logro según del tipo que sea correctamente.
		String tipo = datos.getTipo();
		if (tipo.equals("VictoriaConsecutiva")){
			LogroVictoriaConsecutiva lvc = new LogroVictoriaConsecutiva(datos.getNombreLogro(), datos.getDescripcion(), datos.getAvance(), datos.getObjetivo(), datos.getFecha(), datos.getNombreTema());
			if (datos.getAvance() >= datos.getObjetivo()){
				this.logrosObtenidos.add(lvc);
			}
			else{
				this.logrosRestantes.add(lvc);
			}
		}
		else if (tipo.equals("VictoriaNivel")){
			LogroVictoriaNivel lvn = new LogroVictoriaNivel(datos.getNombreLogro(), datos.getDescripcion(), datos.getAvance(), datos.getObjetivo(), datos.getFecha(), datos.getNombreTema(), datos.getNivel());
			if (datos.getAvance() >= datos.getObjetivo()){
				this.logrosObtenidos.add(lvn);
			}
			else{
				this.logrosRestantes.add(lvn);
			}
		}
	}
	
	//Resetear
	public void reset()
	{
		//Precondición: no hay precondición.
		//Postcondicián: se borran las listas.
		logrosObtenidos.clear();
		logrosRestantes.clear();
	}
}