package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestorJugadores {
    //Atributos
    private static GestorJugadores mGestorJugadores;
    private String usuarioSeleccionado;

    //Constructora
    private GestorJugadores() {
    }

    public static GestorJugadores getGestorJugadores() {
        if (mGestorJugadores == null) mGestorJugadores = new GestorJugadores();
        return mGestorJugadores;

    }

    public String mostrarUsuarios() throws SQLException {
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT email FROM Usuario");
        JsonArray lEmailsJSON = new JsonArray();
        while (resultado.next()) {
            JsonObject json = new JsonObject();
            json.put("email", resultado.getString("email"));
            lEmailsJSON.add(json);
        }
        resultado.close();
        return lEmailsJSON.toJson();
    }

    public void eliminar(String pUsuario) throws SQLException {
        GestorDB.getGestorDB().execSQL("DELETE FROM Usuario WHERE email = '" + pUsuario + "'");
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT contrasena FROM UsuarioEmail WHERE email = '" + pUsuario + "'");
        if (resultado.next()) {
            //Si es UsuarioEmail
            GestorDB.getGestorDB().execSQL("DELETE FROM UsuarioEmail WHERE email = '" + pUsuario + "'");
        }
        mostrarMensajeEliminar();
    }

    private void mostrarMensajeEliminar() {
        Alert usuarioEliminado = new Alert(Alert.AlertType.CONFIRMATION);
        usuarioEliminado.setTitle("Usuario eliminado");
        usuarioEliminado.setHeaderText("El usuario ha sido eliminado con éxito");
        usuarioEliminado.show();
    }

    public void guardarUsuarioSeleccionado(String pUsuario) {
        usuarioSeleccionado = pUsuario;
    }

    public String getDatosUsuario() throws SQLException {
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT contrasena FROM UsuarioEmail WHERE email = '" + usuarioSeleccionado + "'");
        JsonObject datos = new JsonObject();
        if (resultado.next()) {
            //Si es UsuarioEmail
            datos.put("contrasena", resultado.getString("contrasena"));
        }
        ResultadoSQL nivel = GestorDB.getGestorDB().execSELECT("SELECT nivelInicial FROM Usuario WHERE email = '" + usuarioSeleccionado + "'");
        if (nivel.next()) {
            datos.put("nivel", nivel.getInt("nivelInicial"));
        }
        datos.put("email", usuarioSeleccionado);
        nivel.close();
        resultado.close();
        return datos.toJson();
    }

    public String getNiveles() throws SQLException {
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT nivel FROM Nivel");
        JsonArray lnivelesJSON = new JsonArray();
        while (resultado.next()) {
            JsonObject json = new JsonObject();
            json.put("nivel", resultado.getString("nivel"));
            lnivelesJSON.add(json);
        }
        resultado.close();
        return lnivelesJSON.toJson();
    }

    public void guardar(String pEmail, String pContrasena, int pNivel) throws SQLException {
        //Comprobamos que no haya ya un usuario con ese email
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT * FROM Usuario WHERE email = '" + pEmail + "'");
        if (resultado.next()) {
            mostrarUsuarioExistente();
        } else {
            //comprobar que el email introducido es válido
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher mather = pattern.matcher(pEmail);
            if (mather.find() == true) {
                //Se actualizan los datos en Usuario
                GestorDB.getGestorDB().execSQL("UPDATE Usuario SET email = '" + pEmail + "', nivelInicial = '" + pNivel + "' WHERE email = '" + usuarioSeleccionado + "'");
                if (pContrasena != "") {
                    //Si es UsuarioEmail
                    GestorDB.getGestorDB().execSQL("UPDATE UsuarioEmail SET email = '" + pEmail + "', contrasena = '" + pContrasena + "' WHERE email = '" + usuarioSeleccionado + "'");
                }
                guardarUsuarioSeleccionado(pEmail);
                mostrarDatosActualizados();
            } else {
                Alert emailNoValido = new Alert(Alert.AlertType.INFORMATION);
                emailNoValido.setTitle("Email no válido");
                emailNoValido.setHeaderText("Por favor introduzca un email válido. Por ejemplo: ejemplo@email.com");
                emailNoValido.show();
            }
        }
        resultado.close();

    }

    private void mostrarUsuarioExistente() {
        Alert usuarioEliminado = new Alert(Alert.AlertType.ERROR);
        usuarioEliminado.setTitle("Ya existe ese usuario");
        usuarioEliminado.setHeaderText("Ya existe un usuario con ese email");
        usuarioEliminado.show();
    }

    private void mostrarDatosActualizados() {
        Alert datosActualizados = new Alert(Alert.AlertType.CONFIRMATION);
        datosActualizados.setTitle("Datos actualizados");
        datosActualizados.setHeaderText("Los datos se han actualizado con éxito");
        datosActualizados.show();
    }
}