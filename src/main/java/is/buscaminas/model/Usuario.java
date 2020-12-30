package is.buscaminas.model;


import is.buscaminas.controller.GestorUsuario;

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
	private Usuario(String pEmail, String pNickname, int pNivelInicial, String pTematicaActual, boolean pEsAdmin){
		email = pEmail;
		nickname = pNickname;
		nivelInicial = pNivelInicial;
		tematicaActual = pTematicaActual;
		esAdmin = pEsAdmin;
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
	
	public String getTematicaActual(){
		return tematicaActual;
	}
	
	public void setTematicaActual(String tematicaActual){
		this.tematicaActual = tematicaActual;
	}
	
	// Setters
	
	public boolean esAdmin(){
		return esAdmin;
	}
}
