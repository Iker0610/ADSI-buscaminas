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
        // TODO ARREGLAR Y TERMINAR DE IMPLEMENTAR
        try {
            Usuario.create(pEmail, pNickname, 1, "mario", true);
        } catch (IllegalAccessException e) {
            System.out.println("IKER LA HA LIADO");
        }
    }

}
