package is.buscaminas.controller;

import is.buscaminas.model.Usuario;

public class GestorTematica {

    private static GestorTematica mGestorTematica;


    private GestorTematica(){}


    public static GestorTematica getGestorTematica(){
        if(mGestorTematica == null){
            mGestorTematica = new GestorTematica();
        }
        return mGestorTematica;
    }

    public void cambiarTematica(String pNombreTema){
        Usuario.getUsuario().setTematicaActual(pNombreTema);
        String mailAct = Usuario.getUsuario().getEmail();
        //GestorDB.getGestorDB().execSQL("UPDATE Usuario SET temaActual = %pNombreTema% WHERE email = %mailAct%");
    }

    /*
    public JSONObject cargarTemas(){
        //Obtenemos todos los temas
        ResultadoSQL rdo1 = new ResultadoSQL;
        rdo1 = GestorDB.getGestorDB().execSQL("SELECT * FROM Tematica");


        String mailAct = Usuario.getUsuario().getEmail();

        //Obtenemos todos los desbloqueados por el usuario
        ResultadoSQL rdo2 = new ResultadoSQL;
        rdo2 = GestorDB.getGestorDB().execSQL("SELECT nombreTema FROM Logro WHERE nombre IN (SELECT nombreLogro FROM LogrosUsuario WHERE email = %mailAct% AND fechaObtencion IS NOT NULL");
        //Introducimos los datos obtenidos en un JSON
        JSONObject temasJSON = new JSONObject();
        String nombre, descripcion;
        while (rdo1.next() != null){
            nombre = rdo1.getString(nombre);
            descripcion = rdo1.getString(descripcion);
            //TODO introducir temas en JSON (todos con bloq = true)
        }
        while(rdo2.next() != null){
            nombre = rdo2.get(nombreTema);
            //TODO poner los temas de rdo2 con bloq = false
        }
        return temasJSON;
    }
    */

}
