package is.buscaminas.view.uiElements;

import is.buscaminas.model.Contador;

import java.beans.PropertyChangeListener;


public class VistaContador extends VistaDisplay implements PropertyChangeListener {
    // Esta clase extiende de VistaDisplay, y es la encargada de crear un contador

    //Atributos

    //Constructora
    public VistaContador ()
    {
        super();
        //Se añade esta instancia como observer del contador
        Contador.getContador().addObserver(this);
    }

    //Metodos

}