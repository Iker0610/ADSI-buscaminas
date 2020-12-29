package is.buscaminas.view.uiElements;


import javafx.scene.layout.GridPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static java.lang.Math.abs;


public class VistaDisplay extends GridPane implements PropertyChangeListener {
    // Esta clase es un elemento de la vista
    // Extiende del GridPane lo que significa que se puede considerar un GridPane con funciones especiales
    // Se ha optado por un GridPane pues este nos permitía poner elementos en el ordenadamente
    // Este GridPane tiene 3 cifras, estos son elementos de Vista personalizados (como este).

    //Atributos
    VistaCifra cifra1;
    VistaCifra cifra2;
    VistaCifra cifra3;

    //Constructora
    protected VistaDisplay ()
    {
        super();

        //Se inicializan las 3 cifras
        cifra1 = new VistaCifra();
        cifra2 = new VistaCifra();
        cifra3 = new VistaCifra();

        //Se añade cada una en una columna del GridPane
        this.add(cifra3, 0, 0);
        this.add(cifra2, 1, 0);
        this.add(cifra1, 2, 0);
    }

    //Metodos

    @Override
    public void propertyChange (PropertyChangeEvent pEvento)
    {
        actualizarDisplay((Integer) pEvento.getNewValue());
    }

    protected void actualizarDisplay (int pValor)
    {
        //Pre: Un entero indicando el valor a mostrar
        //Post: Se ha actualizado el display de las de acuerdo al valor

        int cifraAct;
        if (pValor < 1000 && pValor > (-100)) {
            cifraAct = abs(pValor) % 10;
            cifra1.cambiarCifra(cifraAct);

            cifraAct = (abs(pValor) % 100) / 10;
            cifra2.cambiarCifra(cifraAct);

            if (pValor >= 0) {                     //si el número es positivo, la cifra3 marcará un número
                cifraAct = (abs(pValor) % 1000) / 100;
                cifra3.cambiarCifra(cifraAct);
            }
            else {                                  //si el número es negativo, la cifra3 marcará un guión "-"
                cifra3.cambiarCifra(-1);
            }
        }
    }
}
