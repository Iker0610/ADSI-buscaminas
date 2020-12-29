package is.buscaminas.view.uiElements;


import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class VistaCasilla extends Button implements PropertyChangeListener {
    //Descripción:
    // - Elemento UI dedicado a las casillas, extiende de Button

    //Atributos
    private static final int size = 40;

    public VistaCasilla ()
    {
        super();

        //Se configura el tamaño
        setMinHeight(size);
        setMinWidth(size);
        setMaxHeight(size);
        setMaxWidth(size);

        //Se establece la apariencia inicial
        cambiarApariencia("Oculto");
    }

    //Metodo del patrón observer
    @Override
    public void propertyChange (PropertyChangeEvent pNuevoEstado)
    {
        String nuevoEstado = (String) pNuevoEstado.getNewValue();
        cambiarApariencia(nuevoEstado);
    }

    //Metodo para cambiar el aspecto de la casilla:
    private void cambiarApariencia (String pString)
    {
        //Pre: Un String indicando el estado
        //Post: Se cambia la apariencia de la casilla

        //Se carga la imagen y se aplica 'como fondo'
        Image imagenCasilla = new Image(new File("src/main/resources/is/buscaminas/ui/assets/casilla/" + pString + ".gif").toURI().toString());
        BackgroundSize backgroundSize = new BackgroundSize(size, size, false, false, false, false);
        setBackground(new Background(new BackgroundImage(imagenCasilla, null, null, null, backgroundSize)));
    }
}
