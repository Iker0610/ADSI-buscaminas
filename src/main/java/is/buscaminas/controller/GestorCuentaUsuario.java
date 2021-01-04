package is.buscaminas.controller;



public class GestorCuentaUsuario {

    private static GestorCuentaUsuario mGestorCuentaUsuario;


    private GestorCuentaUsuario(){}


    public static GestorCuentaUsuario getGestorCuentaUsuario(){
        if(mGestorCuentaUsuario == null){
            mGestorCuentaUsuario = new GestorCuentaUsuario();
        }
        return mGestorCuentaUsuario;
    }

    /*
    public JSONObject cargarTemas(){
        return GestorTematica.getGestorTematica().cargarTemas();
    }
    */

    public void cambiarTematica(String pNombreTema){
        GestorTematica.getGestorTematica().cambiarTematica(pNombreTema);
    }

    public void cambiarContrasena(String pNuevaContra){
        //TODO
    }
}
