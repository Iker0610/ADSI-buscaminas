package is.buscaminas.view.uiElements;


import javafx.scene.control.Label;
import javafx.scene.layout.*;


public class VistaAyuda extends VBox {

    // La Clase que muestra las páginas de ayuda
    public VistaAyuda (String pTexto)
    {
        super();

        //Cargamos el texto en un Label
        this.getChildren().add(new Label(pTexto));

        // Ajustamos el tamaño del texto (VBox)
        setMinHeight(250);
        setMinWidth(400);
        setMaxHeight(250);
        setMaxWidth(400);

        //TODO setStyle????
    }
}
