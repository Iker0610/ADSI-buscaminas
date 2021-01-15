package is.buscaminas.controller;


import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
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
    public void checkEmailContrasena(String pEmail, String pContra, String pNickname) throws Exception{
        try {
            ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM UsuarioEmail NATURAL JOIN Usuario WHERE Email = '"+ pEmail +"'");
            try {
                res.next();
                String tema=res.getString("temaActual");
                String contra=res.getString("contrasena");
                int niv=res.getInt("nivelInicial");
                boolean admin=res.getBoolean("esAdmin");
                if (contra==pContra){ //Si existe y la contrasena es correcta
                    Usuario.create(pEmail, pNickname, niv, tema, admin);
                    //GestorLogros.getGestorLogros().cargarLogros(pEmail);
                }
                else{//Si existe pero la contrasena es incorrecta
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Contraseña Incorrecta");
                    alerta.setHeaderText(null);
                    alerta.setContentText("La contraseña es incorrecta, prueba de nuevo");
                    alerta.show();
                }

            } catch (SQLException e) { //El usuario no existe
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Registro");
                alerta.setHeaderText(null);
                alerta.setContentText("Este email no está registrado. ¿Quiere registrar una cuenta nueva?");
                Optional<ButtonType> result = alerta.showAndWait();

                if (result.get() == ButtonType.OK){ //El usuario no existe pero se quiere registrar
                    Usuario.create(pEmail, pNickname, 1, "mario", false);
                    GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES ('"+ pEmail +"')");
                    GestorDB.getGestorDB().execSQL("INSERT INTO UsuarioEmail (email, contrasena) VALUES ('"+ pEmail +"', '"+ pContra +"')");
                    GestorLogros.getGestorLogros().cargarLogros(pEmail);
                }
                else{
                    throw new Exception();
                }
            }


            Usuario.create(pEmail, pNickname, 1, "mario", true);
        } catch (IllegalAccessException | SQLException e) {
            System.out.println("Problemas con SQL");
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
            ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT Contrasena FROM UsuarioEmail WHERE Email = '"+ pEmail +"'");
            res.next();
            String contra = res.getString("Contrasena");
            this.mandarEmail("Recuperacion Contraseña Buscaminas", "pEmail", "Su contraseña es: " + contra);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	}

}
