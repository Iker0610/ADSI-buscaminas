package is.buscaminas.model;


import is.buscaminas.controller.GestorUsuario;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;


public class Usuario
{
	// Atributos
	private static Usuario mUsuario;
	private final String email;
	private final String nickname;
	private final int nivelInicial;
	private final boolean esAdmin;
	private String tematicaActual;
	
	// Constructora y patrón singleton
	private Usuario(String pEmail, String pNickname, int pNivelInicial, String pTema, boolean pEsAdmin)
	{
		email        = pEmail;
		nickname     = pNickname;
		nivelInicial = pNivelInicial;
		esAdmin      = pEsAdmin;
		setTematicaActual(pTema);
	}
	
	public static void create(String pEmail, String pNickname, int pNivelInicial, String pTematicaActual, boolean pEsAdmin) throws IllegalAccessException{
		Class<?> caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
		if (caller.equals(GestorUsuario.class)) mUsuario = new Usuario(pEmail, pNickname, pNivelInicial, pTematicaActual, pEsAdmin);
		else throw new IllegalAccessException();
	}
	
	public static Usuario getUsuario() throws NoSuchElementException{
		if (mUsuario != null) return mUsuario;
		else throw new NoSuchElementException();
	}
	
	
	// Getters
	
	public String getEmail(){
		return email;
	}
	
	public String getNickname(){
		return nickname;
	}
	
	public int getNivelInicial(){
		return nivelInicial;
	}
	
	public String getTematicaActual()
	{
		return tematicaActual;
	}
	
	public boolean esAdmin()
	{
		return esAdmin;
	}
	
	// Setters
	
	public void setTematicaActual(String pTema)
	{
		this.tematicaActual = pTema;
		
		//Se carga la fuente
		try{
			Font.loadFont(new FileInputStream(new File("src/main/resources/is/buscaminas/temas/" + pTema + "/fuente/font.ttf")), 20);
		}
		catch (FileNotFoundException e){
			// Todo Crear emergente
			e.printStackTrace();
		}
	}
}
