package is.buscaminas.controller;


import is.buscaminas.model.Usuario;

public class GestorUsuario
{
    // GESTOR LOGin

	// Atributos
	
	// Constructora
	
	// Patrón Singleton
	
	// Métodos
    public static void checkEmailContrasena(String pEmail, String pContra, String pNickname){
        try {
            Usuario.create(pEmail, pNickname,1,"amongus",true);
        } catch (IllegalAccessException e) {
            System.out.println("IKER LA HA LIADO");
        }
    }

}
