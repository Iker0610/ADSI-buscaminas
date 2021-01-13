package is.buscaminas.controller;


import java.sql.SQLException;

public class GestorCuentaUsuario {

    private static GestorCuentaUsuario mGestorCuentaUsuario;


    private GestorCuentaUsuario(){}


    public static GestorCuentaUsuario getGestorCuentaUsuario(){
        if(mGestorCuentaUsuario == null){
            mGestorCuentaUsuario = new GestorCuentaUsuario();
        }
        return mGestorCuentaUsuario;
    }

    public String obtenerTemas() throws SQLException {
        return GestorTematica.getGestorTematica().obtenerTemas();
    }

    public void cambiarTematica(String pNombreTema) throws SQLException {
        GestorTematica.getGestorTematica().cambiarTematica(pNombreTema);
    }

    public void cambiarContrasena(String pNuevaContra){
        //TODO
    }
}
