package is.buscaminas.controller;


import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;

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

    public void cambiarTematica(String pNombreTema){
        GestorTematica.getGestorTematica().cambiarTematica(pNombreTema);
    }

    public void cambiarContrasena(String pNuevaContra){
        String email=Usuario.getUsuario().getEmail();
        try {
            GestorDB.getGestorDB().execSQL("UPDATE UsuarioEmail SET Contrasena = '"+ pNuevaContra +"' WHERE Email = '"+ email +"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
