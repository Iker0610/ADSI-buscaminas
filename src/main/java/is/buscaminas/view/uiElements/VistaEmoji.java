package is.buscaminas.view.uiElements;


import is.buscaminas.controller.Partida;
import is.buscaminas.model.Contador;
import is.buscaminas.model.Usuario;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class VistaEmoji extends Button implements PropertyChangeListener {
    //Descripción:
    // - Elemento UI dedicado al emoji del buscaminas

    //Atributos
    private static final int size = 70;

    public VistaEmoji ()
    {
        super();

        //Se configura el tamaño
        setMinHeight(size);
        setMinWidth(size);
        setMaxHeight(size);
        setMaxWidth(size);

        /*Se añade este elemento como obsever al Contador y a la Partida
         *   - Se añade al contador para indicar cuando se ha superado los 999 segundos
         *   - Se añade a partida para poner el emoji de la derrota o victoria al finalizar
         *
         * *Nota: Se pasa por parametro esta instancia y también el método al que se le ha de llamar en vez de al propertyChange() (notify())
         *        De esta forma se separan desde el principio ambos 'notify()' que tendrán funcionalidades diferentes.
         */
        Contador.getContador().addObserver(this::actualizarEmojiTiempo);
        Partida.getPartida().addObserver(this::actualizarEmojiFinPartida);

        cambiarEmoji("estandar");
    }


    //Metodo del patrón observer
    @Override
    public void propertyChange (PropertyChangeEvent pNuevoEstado) {}

    private void actualizarEmojiFinPartida (PropertyChangeEvent propertyChangeEvent)
    {
        //* notify() que se llama cuando avisa la clase Partida

        //Pre: el nuevo valor ha de ser un objeto del tipo boolean
        //Post:Se ha actualizado el emoji al correspondiente según si el jugador ha ganado la partida o no

        if ((Boolean) propertyChangeEvent.getNewValue()) {
            cambiarEmoji("victoria");
        }
        else {
            cambiarEmoji("derrota");
        }
    }

    private void actualizarEmojiTiempo (PropertyChangeEvent propertyChangeEvent)
    {
        //* notify() que se llama cuando avisa la clase Contador

        //Pre: el nuevo valor ha de ser un objeto del tipo int
        //Post: Se cambia el aspecto del emoji si el valor del contador es 999

        if ((Integer) propertyChangeEvent.getNewValue() == 999) {
            cambiarEmoji("tiempo");
        }
    }

    //Metodo para cambiar el aspecto de la casilla:
    private void cambiarEmoji (String pString)
    {
        //Pre: Un string
        //Post: Se ha cambia el aspecto del emoji al establecido por el string

        //Se carga y se aplica
        Image imagenCasilla = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/emoji/" + pString + ".gif").toURI().toString());
        BackgroundSize backgroundSize = new BackgroundSize(size, size, false, false, false, false);
        setBackground(new Background(new BackgroundImage(imagenCasilla, null, null, null, backgroundSize)));
    }
}
