package is.buscaminas.view.uiElements;


import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class VistaTematica extends HBox
{
    public VistaTematica(String pNombre, String pDescripcion, boolean pBloqueada)
    {
        super();
        
        this.getChildren().add(new Label(pNombre));
        this.getChildren().add(new Label(pDescripcion));
        
        //Tamaño del botón TODO: METER ESTO EN EL CSS
        setMinHeight(60);
        setMinWidth(270);
        setMaxHeight(60);
        setMaxWidth(270);
        
        // TODO Ponerle CSS, y cambiarlo según el boolean
        if (!pBloqueada) this.setStyle("-fx-background-color: white;");
        
        else this.setStyle("-fx-background-color: grey;");
        
    }
}
