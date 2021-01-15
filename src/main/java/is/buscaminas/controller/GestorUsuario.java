package is.buscaminas.controller;


import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.sql.SQLException;
import java.util.Properties;

public class GestorUsuario
{
    // GESTOR LOGin

	// Atributos
	private static GestorUsuario miGestorUsuario;
	private boolean allowConstructor;

	// Constructora
	private GestorUsuario(){

    }

	// Patrón Singleton
	public static GestorUsuario getGestorUsuario(){
	    if (miGestorUsuario==null){
	        miGestorUsuario=new GestorUsuario();
        }
	    return miGestorUsuario;
    }
	// Métodos
    public static void checkEmailContrasena(String pEmail, String pContra, String pNickname){
        // TODO ARREGLAR Y TERMINAR DE IMPLEMENTAR
        try {
            Usuario.create(pEmail, pNickname, 1, "mario", true);
        } catch (IllegalAccessException e) {
            System.out.println("IKER LA HA LIADO");
        }
    }

    private void mandarEmail(String pAsunto, String pDestinatario, String pTexto){
	    //Configuracion del proveedor
        //TODO
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        //Configuracion de sesion
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alwayslate.noreply@gmail.com", "ADSI1234");
            }
        });
    }

    public void recuperarContra(String pEmail){
        try {
            ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT Contrasena FROM UsuarioEmail WHERE Email = '"+ pEmail +"';");
            res.next();
            String contra = res.getString("Contrasena");
            this.mandarEmail("Recuperacion Contraseña Buscaminas", "pEmail", "Su contraseña es: " + contra);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	}

}
