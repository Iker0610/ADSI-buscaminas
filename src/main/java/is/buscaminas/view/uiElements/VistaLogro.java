package is.buscaminas.view.uiElements;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


public class VistaLogro extends HBox {

    public VistaLogro(String pNombreLogro, String pDescripcion, String pAvance, String pObjetivo, String pObtencion, String pNombreTema)
    {
        super();

        // Cargamos el título y ajustamos el estilo (blanco)
        Label nombreLabel = new Label(pNombreLogro);
        nombreLabel.setStyle("-fx-text-fill: white;");
        this.getChildren().add(nombreLabel);

        //Cargamos la descripcion
        if(pDescripcion!=null) {
            Label descripcionLabel = new Label(pDescripcion);
            descripcionLabel.setStyle("-fx-text-fill: white;");
            this.getChildren().add(descripcionLabel);
        }

        //
        Label avanceLabel = new Label("Avance: " + pAvance + "/" + pObjetivo);

        //
        if(pNombreTema!=null) {
            Label temaLabel = new Label(pNombreTema);
        }

        if(pObtencion!=null){
            Label fechaObtencion= new Label(pObtencion);
        }

        // Ajustamos el tamaño de la vista
        setMinHeight(45);
        setMinWidth(400);
        setMaxHeight(45);
        setMaxWidth(400);

        // Ajustamos la posición y estilo del botón
        this.setPadding(new Insets(3,0 ,0 ,10));
        if (Integer.parseInt(pAvance) >= Integer.parseInt(pObjetivo)){ this.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: green; -fx-border-width: 3;"); }
        else { this.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: red; -fx-border-width: 3;"); }
    }
}
