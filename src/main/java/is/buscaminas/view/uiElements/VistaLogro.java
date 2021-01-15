package is.buscaminas.view.uiElements;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


public class VistaLogro extends HBox {

    public VistaLogro(String pNombreLogro, boolean pDesbloqueado)
    {
        super();

        // Cargamos el título y ajustamos el estilo (blanco)
        Label nombreLabel = new Label(pNombreLogro);
        nombreLabel.setStyle("-fx-text-fill: white;");
        this.getChildren().add(nombreLabel);

        // Ajustamos el tamaño de la vista
        setMinHeight(45);
        setMinWidth(400);
        setMaxHeight(45);
        setMaxWidth(400);

        // Ajustamos la posición y estilo del botón
        this.setPadding(new Insets(3,0 ,0 ,10));
        if (pDesbloqueado){ this.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: green; -fx-border-width: 3;"); }
        else { this.setStyle("-fx-background-color: #1f1f1f; -fx-border-color: red; -fx-border-width: 3;"); }
    }
}
