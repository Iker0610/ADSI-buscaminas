package is.buscaminas.controller;


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
import com.github.cliftonlabs.json_simple.JsonObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/*------------------------------------------------------------------------*/

public class GestorUsuario {
    // GESTOR LOGin

    // Atributos
    private static GestorUsuario miGestorUsuario;
    private boolean allowConstructor;
    /*------------------------------------------------------------------------*/
    private static DataStoreFactory DATA_STORE_FACTORY;

    /**
     * OAuth 2 scope.
     */
    private static final String SCOPE = "email";

    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    static final JsonFactory JSON_FACTORY = new GsonFactory();

    public static final String API_KEY = "537000180979-0c61c2jl57r17a1gvh4mba5o0e9eulmm.apps.googleusercontent.com";

    /**
     * Value of the "API Secret".
     */
    public static final String API_SECRET = "VbwCfkiTduUd2I5upiovZtnL";

    private static final String TOKEN_SERVER_URL = "https://oauth2.googleapis.com/token";
    private static final String AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/auth";

    /*------------------------------------------------------------------------*/

    // Constructora
    private GestorUsuario() {

    }

    // Patrón Singleton
    public static GestorUsuario getGestorUsuario() {
        if (miGestorUsuario == null) {
            miGestorUsuario = new GestorUsuario();
        }
        return miGestorUsuario;
    }

    // Métodos
    public void checkEmailContrasena(String pEmail, String pContra, String pNickname) throws Exception {
        try {
            ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM UsuarioEmail NATURAL JOIN Usuario WHERE Email = '" + pEmail + "'");
            try {
                res.next();
                String tema = res.getString("temaActual");
                String contra = res.getString("contrasena");
                int niv = res.getInt("nivelInicial");
                boolean admin = res.getBoolean("esAdmin");
                if (contra.equals(pContra)) { //Si existe y la contrasena es correcta
                    Usuario.create(pEmail, pNickname, niv, tema, admin);
                    //GestorLogros.getGestorLogros().cargarLogros(pEmail);
                } else {//Si existe pero la contrasena es incorrecta
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Contraseña Incorrecta");
                    alerta.setHeaderText(null);
                    alerta.setContentText("La contraseña es incorrecta, prueba de nuevo");
                    alerta.show();
                    throw new Exception();
                }

            } catch (SQLException e) { //El usuario no existe
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Registro");
                alerta.setHeaderText(null);
                alerta.setContentText("Este email no está registrado. ¿Quiere registrar una cuenta nueva?");
                Optional<ButtonType> result = alerta.showAndWait();

                if (result.get() == ButtonType.OK) { //El usuario no existe pero se quiere registrar
                    Usuario.create(pEmail, pNickname, 1, "mario", false);
                    GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES ('" + pEmail + "')");
                    GestorDB.getGestorDB().execSQL("INSERT INTO UsuarioEmail (email, contrasena) VALUES ('" + pEmail + "', '" + pContra + "')");
                    GestorLogros.getGestorLogros().cargarLogros(pEmail);
                } else {
                    throw new Exception();
                }
            }


            Usuario.create(pEmail, pNickname, 1, "mario", true);
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void mandarEmail(String pDestinatario, String pAsunto, String pTexto) {
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

    public void recuperarContra(String pEmail) {
        try {
            ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT Contrasena FROM UsuarioEmail WHERE Email = '" + pEmail + "'");
            res.next();
            String contra = res.getString("Contrasena");
            this.mandarEmail("Recuperacion Contraseña Buscaminas", "pEmail", "Su contraseña es: " + contra);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*------------------------------------------------------------------------------------------------------*/

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static String obtenerAuthToken() throws Exception {
        // set up authorization code flow
        AuthorizationCodeFlow flow =
                new AuthorizationCodeFlow.Builder(
                        BearerToken.authorizationHeaderAccessMethod(),
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        new GenericUrl(TOKEN_SERVER_URL),
                        new ClientParametersAuthentication(API_KEY, API_SECRET),
                        API_KEY,
                        AUTHORIZATION_SERVER_URL)
                        .setScopes(Arrays.asList(SCOPE))
                        .enablePKCE()
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .build();
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

            String line = null;
            StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
            while ((line = reader.readLine()) != null){
                out.append(line);
            }
            JsonObject datosJson = Jsoner.deserialize(out.toString(), new JsonObject());
            email = (String)datosJson.get("email");
        }
        catch (Exception ignored){}
        return email;
    }

    public void checkEmail(String pNickname) throws Exception{
        DATA_STORE_FACTORY = new MemoryDataStoreFactory();
        try {
            final String accessToken = this.obtenerAuthToken();
            String email = obtenerEmailRedSocial(accessToken);
            /*-------------------------------------------------------------*/

            try {
                ResultadoSQL res = GestorDB.getGestorDB().execSELECT("SELECT * FROM Usuario WHERE Email = '"+ email +"'");
                try {
                    res.next();
                    String tema=res.getString("temaActual");
                    int niv=res.getInt("nivelInicial");
                    boolean admin=res.getBoolean("esAdmin");
                    Usuario.create(email, pNickname, niv, tema, admin);
                    GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES ('"+ email +"')");
                    GestorLogros.getGestorLogros().cargarLogros(email);


                } catch (SQLException e) { //El usuario no existe
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Registro");
                    alerta.setHeaderText(null);
                    alerta.setContentText("Esta cuenta no está registrada. ¿Quiere registrar una cuenta nueva?");
                    Optional<ButtonType> result = alerta.showAndWait();

                    if (result.get() == ButtonType.OK){ //El usuario no existe pero se quiere registrar
                        Usuario.create(email, pNickname, 1, "mario", false);
                        GestorDB.getGestorDB().execSQL("INSERT INTO Usuario (Email) VALUES ('"+ email +"')");
                        GestorLogros.getGestorLogros().cargarLogros(email);
                    }
                    else{
                        throw new Exception();
                    }
                }



            } catch (IllegalAccessException | SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
