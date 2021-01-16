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
        try {
            FileWriter ayuda = null;
            PrintWriter pw = null;
            ayuda = new FileWriter("src/main/resources/is/buscaminas/MenuAyuda/Ayuda.txt", true);
            BufferedWriter bw = new BufferedWriter(ayuda);
            bw.write("");
            bw.write(pMensaje);
            bw.close();
            cargarMensaje();
        }
        catch (IOException e){
            Alert errorDeActualizacion = new Alert(Alert.AlertType.ERROR);
            errorDeActualizacion.setTitle("Error archivo no encontrado");
            errorDeActualizacion.setHeaderText("No se ha encontrado el archivo con el mensaje de ayuda");
            errorDeActualizacion.show();
        }
    }
    public boolean guardarMensaje(String pMensaje){
        setMensaje(pMensaje);
        if(mensaje == pMensaje){
            return true;
        }
        else{
            return false;
        }
    }
    public String mostrarDatosAyuda(){
        JsonObject json = new JsonObject();
        json.put("mensaje",mensaje);
        return json.toJson();
    }
    public void cargarMensaje(){
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
