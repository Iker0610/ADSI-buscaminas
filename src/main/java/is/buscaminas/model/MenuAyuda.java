package is.buscaminas.model;

import com.github.cliftonlabs.json_simple.JsonObject;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.ParameterMetaData;
import java.util.Arrays;

public class MenuAyuda {

    private String mensaje;

    public void MenuAyuda(){}

    private void setMensaje(String pMensaje) {
        //Pre: un String con el nuevo mensaje.
        //Post: se ha actualizado el mensaje en el txt.
        try {
            PrintWriter pw = null;
            FileWriter ayuda = new FileWriter("src/main/resources/is/buscaminas/MenuAyuda/Ayuda.txt");
            BufferedWriter bw = new BufferedWriter(ayuda);
            bw.write("");
            bw.write(pMensaje);
            bw.close();
        }
        catch (IOException e){
            Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
            errorDeActualizacion.setTitle("Error archivo no encontrado");
            errorDeActualizacion.setHeaderText("No se ha encontrado el archivo con el mensaje de ayuda");
            errorDeActualizacion.show();
        }
    }
    public boolean guardarMensaje(String pMensaje){
        //Pre: un String con el nuevo mensaje.
        //Post: se ha actualiado el mensaje del txt correctamente.
        setMensaje(pMensaje);
        cargarMensaje();
        if(mensaje.equals(pMensaje)){
            return true;
        }
        else{
            return false;
        }
    }
    public String mostrarDatosAyuda(){
        //Pre:
        //Post: devuelve un Json con el mensaje.
        JsonObject json = new JsonObject();
        json.put("mensaje",mensaje);
        return json.toJson();
    }
    public void cargarMensaje(){
        //Pre:
        //Post: se carga el mensaje del txt en la variable mensaje.
        try {
            mensaje = "";
            String cadena = "";
            File archivoAyuda = new File("src/main/resources/is/buscaminas/MenuAyuda/Ayuda.txt");
            FileReader ayuda = new FileReader(archivoAyuda);
            BufferedReader b = new BufferedReader(ayuda);
            while((cadena = b.readLine())!=null) {
                mensaje += cadena;
            }
            b.close();
        }
        catch (IOException e){
            Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
            errorDeActualizacion.setTitle("Error archivo no encontrado");
            errorDeActualizacion.setHeaderText("No se ha encontrado el archivo con el mensaje de ayuda");
            errorDeActualizacion.show();
        }
    }

}
