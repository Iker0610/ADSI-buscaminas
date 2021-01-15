package is.buscaminas.controller;


import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


import java.util.Properties;
import java.sql.SQLException;


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
    public void checkEmailContrasena(String pEmail, String pContra, String pNickname){
        // TODO ARREGLAR Y TERMINAR DE IMPLEMENTAR
        try {
            Usuario.create(pEmail, pNickname, 1, "mario", true);
        } catch (IllegalAccessException e) {
            System.out.println("IKER LA HA LIADO");
        }
    }

    private void mandarEmail(String pDestinatario, String pAsunto, String pTexto){
	    //Configuracion del proveedor
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        //Configuracion de sesion
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("alwayslate.noreply@gmail.com", "ADSI1234");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("alwayslate.noreply@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(pDestinatario)
            );
            message.setSubject(pAsunto);
            message.setText(pTexto);

            Transport.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
