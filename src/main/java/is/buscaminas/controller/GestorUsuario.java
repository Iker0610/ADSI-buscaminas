package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/*------------------------------------------------------------------------*/


public class GestorUsuario
{
	// GESTOR LOGin
	
	public static final String API_KEY = "537000180979-0c61c2jl57r17a1gvh4mba5o0e9eulmm.apps.googleusercontent.com";
	/**
	 Value of the "API Secret".
	 */
	public static final String API_SECRET = "VbwCfkiTduUd2I5upiovZtnL";
	/**
	 Global instance of the JSON factory.
	 */
	static final JsonFactory JSON_FACTORY = new GsonFactory();
	/**
	 OAuth 2 scope.
	 */
	private static final String SCOPE = "email";
	
	/**
	 Global instance of the HTTP transport.
	 */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String TOKEN_SERVER_URL = "https://oauth2.googleapis.com/token";
	private static final String AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/auth";
	// Atributos
	private static GestorUsuario miGestorUsuario;
	/*------------------------------------------------------------------------*/
	private static DataStoreFactory DATA_STORE_FACTORY;
	
	/*------------------------------------------------------------------------*/
	
	// Constructora
	private GestorUsuario()
	{
	
	}
	
	// Patrón Singleton
	public static GestorUsuario getGestorUsuario()
	{
		if (miGestorUsuario == null){
			miGestorUsuario = new GestorUsuario();
		}
		return miGestorUsuario;
	}
	
	/**
	 Authorizes the installed application to access user's protected data.
	 */
	private static String obtenerAuthToken() throws Exception
	{
		// set up authorization code flow
		AuthorizationCodeFlow flow
				  = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(), HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL), new ClientParametersAuthentication(API_KEY, API_SECRET), API_KEY, AUTHORIZATION_SERVER_URL).setScopes(Arrays.asList(SCOPE)).enablePKCE().setDataStoreFactory(DATA_STORE_FACTORY).build();
		// authorize
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user").getAccessToken();
	}
	
	private static String obtenerEmailRedSocial(String bearerToken)
	{
		String email = "";
		try{
			URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
			connection.setDoOutput(true);
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String line;
			StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
			while ((line = reader.readLine()) != null){
				out.append(line);
			}
			JsonObject datosJson = Jsoner.deserialize(out.toString(), new JsonObject());
			email = (String) datosJson.get("email");
		}
		catch (Exception ignored){}
		return email;
	}
	
	// Métodos
	public void checkEmailContrasena(String pEmail, String pContra, String pNickname) throws Exception
	{
		System.out.println(pEmail);
		System.out.println(pContra);
		System.out.println(pNickname);
		try{
			ResultadoSQL res1 = GestorDB.getGestorDB().execSELECT("SELECT * FROM Usuario WHERE Email = '" + pEmail + "'");
			if (res1.next()){
				res1.close();
				ResultadoSQL res2 = GestorDB.getGestorDB().execSELECT("SELECT * FROM UsuarioEmail WHERE Email = '" + pEmail + "'");
				if (!res2.next()){
					res2.close();
					Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
					alerta.setTitle("Registro");
					alerta.setHeaderText(null);
					alerta.setContentText("La última vez iniciaste sesión con Google. ¿Quieres añadir una contraseña?");
					Optional<ButtonType> result = alerta.showAndWait();

					if (result.get() == ButtonType.OK){ //El usuario quiere añadir una contraseña
						GestorDB.getGestorDB().execSQL("INSERT INTO UsuarioEmail (Email, Contrasena) VALUES ('" + pEmail + "', '"+ pContra +"')");
						Usuario.create(pEmail, pNickname, 1, "Basico", false);
						GestorLogros.getGestorLogros().cargarLogros(pEmail);
					}
					else{
						throw new Exception();
					}
				}
				else {
					ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM Usuario NATURAL JOIN UsuarioEmail WHERE Email = '" + pEmail + "'");
					System.out.println("Se mete en el if");
					String tema = res.getString("temaActual");
					System.out.println("el tema es: " + tema);
					String contra = res.getString("contrasena");
					System.out.println("la contrasena es: " + contra);
					int niv = res.getInt("nivelInicial");
					boolean admin = res.getBoolean("esAdmin");
					res.close();
					System.out.println("Se cierra la conexion");
					if (contra.equals(pContra)) { //Si existe y la contrasena es correcta
						Usuario.create(pEmail, pNickname, niv, tema, admin);
						System.out.println("Se crea el usuario");
						GestorLogros.getGestorLogros().cargarLogros(pEmail);
						System.out.println("Se cargan los logros");
					} else {//Si existe pero la contrasena es incorrecta
						Alert alerta = new Alert(Alert.AlertType.ERROR);
						alerta.setTitle("Contraseña Incorrecta");
						alerta.setHeaderText(null);
						alerta.setContentText("La contraseña es incorrecta, prueba de nuevo");
						alerta.show();
						throw new Exception();
					}
				}
			}
			else{ //El usuario no existe
				Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
				alerta.setTitle("Registro");
				alerta.setHeaderText(null);
				alerta.setContentText("Este email no está registrado. ¿Quiere registrar una cuenta nueva?");
				Optional<ButtonType> result = alerta.showAndWait();
				
				if (result.get() == ButtonType.OK){ //El usuario no existe pero se quiere registrar
					Usuario.create(pEmail, pNickname, 1, "Basico", false);
					GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES ('" + pEmail + "')");
					GestorDB.getGestorDB().execSQL("INSERT INTO UsuarioEmail (email, contrasena) VALUES ('" + pEmail + "', '" + pContra + "')");
					GestorLogros.getGestorLogros().cargarLogros(pEmail);
				}
				else{
					throw new Exception();
				}
			}
			Usuario.create(pEmail, pNickname, 1, "Basico", true);
		}
		catch (IllegalAccessException | SQLException e){
			e.printStackTrace();
		}
	}
	
	/*------------------------------------------------------------------------------------------------------*/

	private void mandarEmail(String pDestinatario, String pAsunto, String pTexto) {
		//Configuracion del proveedor
		String remitente="alwayslate.noreply@gmail.com";
		String destinatario =pDestinatario;
		String asunto=pAsunto;
		String cuerpo=pTexto;
		String clave="ADSI1234";
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
		props.put("mail.smtp.user", remitente);
		props.put("mail.smtp.clave", clave);    //La clave de la cuenta
		props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
		props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
		props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

		//Configuracion de sesion
		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {

			message.setFrom(new InternetAddress(remitente));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
			message.setSubject(asunto);
			message.setText(cuerpo);
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", remitente, clave);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();


		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void recuperarContra(String pEmail) {
		try {
			ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM UsuarioEmail WHERE Email = '" + pEmail + "'");
			res.next();
			String contra = res.getString("contrasena");
			res.close();
			this.mandarEmail(pEmail, "Recuperacion Contraseña Buscaminas", "Su contraseña es: " + contra);
			Alert alerta = new Alert(Alert.AlertType.INFORMATION);
			alerta.setTitle("Contraseña Recuperada");
			alerta.setHeaderText(null);
			alerta.setContentText("Se le ha enviado un email con su contraseña");
			alerta.show();
		} catch (SQLException throwables) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Email Incorrecta");
			alerta.setHeaderText(null);
			alerta.setContentText("Este email no está registrado");
			alerta.show();
		}
	}
	
	public void checkEmail(String pNickname)
	{
		DATA_STORE_FACTORY = new MemoryDataStoreFactory();
		try{
			final String accessToken = obtenerAuthToken();
			String email = obtenerEmailRedSocial(accessToken);
			/*-------------------------------------------------------------*/
			
			ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM Usuario NATURAL JOIN UsuarioEmail WHERE Email = '" + email + "'");
			if (res.next()){
				String tema = res.getString("temaActual");
				int niv = res.getInt("nivelInicial");
				boolean admin = res.getBoolean("esAdmin");
				Usuario.create(email, pNickname, niv, tema, admin);
				GestorLogros.getGestorLogros().cargarLogros(email);
			}
			else{ //El usuario no existe
				Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
				alerta.setTitle("Registro");
				alerta.setHeaderText(null);
				alerta.setContentText("Esta cuenta no está registrada. ¿Quiere registrar una cuenta nueva?");
				Optional<ButtonType> result = alerta.showAndWait();
				
				if (result.get() == ButtonType.OK){ //El usuario no existe pero se quiere registrar
					GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES ('" + email + "')");
					Usuario.create(email, pNickname, 1, "Basico", false);
					GestorLogros.getGestorLogros().cargarLogros(email);
				}
				else{
					throw new Exception();
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void cambiarContrasena(String pNuevaContra){
		String email = Usuario.getUsuario().getEmail();
		ResultadoSQL res = null;
		try {
			res = GestorDB.getGestorDB().execSELECT("SELECT * FROM UsuarioEmail WHERE Email = '"+ email +"'");

			if (res.next()){
				res.close();
				GestorDB.getGestorDB().execSQL("UPDATE UsuarioEmail SET contrasena = '"+ pNuevaContra +"' WHERE Email = '"+ email +"'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
