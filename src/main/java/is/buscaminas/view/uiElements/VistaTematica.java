package is.buscaminas.view.uiElements;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class VistaTematica extends VBox
{
    public VistaTematica(String pNombre, String pDescripcion, boolean pBloqueada)
    {
        super();

        // Cargamos el título y ajustamos el estilo (blanco)
        Label nombreLabel = new Label(pNombre);
        nombreLabel.setStyle("-fx-text-fill: white;");
        this.getChildren().add(nombreLabel);

        // Cargamos la descripción y ajustamos el estilo (blanco)
        Label descripcionLabel = new Label(pDescripcion);
        descripcionLabel.setStyle("-fx-text-fill: white;");
        this.getChildren().add(descripcionLabel);
        
        // Ajustamos el tamaño del botón (VBox)
        setMinHeight(45);
        setMinWidth(400);
        setMaxHeight(45);
        setMaxWidth(400);

        // Ajustamos la posición y estilo del botón
        this.setPadding(new Insets(3,0 ,0 ,10));;
        this.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: white; -fx-border-width: 3;");

        // Si la temática está bloqueada no se podrá sleccionar
        if(pBloqueada) this.setDisable(true);
        
    }
}
