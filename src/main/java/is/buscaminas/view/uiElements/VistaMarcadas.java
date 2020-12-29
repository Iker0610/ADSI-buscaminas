package is.buscaminas.view.uiElements;

import is.buscaminas.controller.Tablero;

import java.beans.PropertyChangeListener;


public class VistaMarcadas extends VistaDisplay implements PropertyChangeListener {
    // Esta clase extiende de VistaDisplay, y es la encargada de crear un contador

    //Atributos

    //Constructora
    public VistaMarcadas ()
    {
        super();
        //Se añade esta instancia como observer del contador
        Tablero.getTablero().addObserver(this);
    }

    //Metodos
}
