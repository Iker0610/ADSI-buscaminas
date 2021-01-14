package is.buscaminas.view.uiElements;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class VistaTematica extends VBox
{
    public VistaTematica(String pNombre, String pDescripcion, boolean pBloqueada)
    {
        super();

        Label nombreLabel = new Label(pNombre);
        nombreLabel.setStyle("-fx-text-fill: white; -fx-font-style: bold;");
        this.getChildren().add(nombreLabel);

        Label descripcionLabel = new Label(pDescripcion);
        descripcionLabel.setStyle("-fx-text-fill: white;");
        this.getChildren().add(descripcionLabel);
        
        //Tamaño del botón
        setMinHeight(45);
        setMinWidth(400);
        setMaxHeight(45);
        setMaxWidth(400);

        this.setPadding(new Insets(3,0 ,0 ,10));;
        this.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: white; -fx-border-width: 3;");
        if(pBloqueada) this.setDisable(true);
        
    }
}
