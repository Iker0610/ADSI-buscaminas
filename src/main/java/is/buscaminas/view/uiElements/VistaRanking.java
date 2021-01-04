package is.buscaminas.view.uiElements;

import is.buscaminas.model.Usuario;
import is.buscaminas.model.ranking.JugadorRanking;
import is.buscaminas.model.ranking.Ranking;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class VistaRanking extends GridPane implements PropertyChangeListener {

    public VistaRanking ()
    {
        super();
        //Se añade esta instancia como observer del contador
        Ranking.getRanking().addObserver(this);

        this.setPadding(new Insets(50));
        this.setHgap(30);
        this.setVgap(0);

        for (int i = 1; i <= 10; i++) {
            int size = 61;
            Pane numPosicion = new Pane();
            numPosicion.setMinHeight(size);
            numPosicion.setMinWidth(size);
            numPosicion.setMaxHeight(size);
            numPosicion.setMaxWidth(size);

            Image imagenPosicion = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s","") + "/assets/ranking/" + i + ".png").toURI().toString());
            BackgroundSize backgroundSize = new BackgroundSize(size, size, false, false, false, false);
            numPosicion.setBackground(new Background(new BackgroundImage(imagenPosicion, null, null, null, backgroundSize)));

            this.add(numPosicion, 0, (i - 1));
        }

    }

    @Override
    public void propertyChange (PropertyChangeEvent pEvento)
    {
        cambiarRanking((JugadorRanking[]) pEvento.getNewValue());
    }

    private void cambiarRanking (JugadorRanking[] pLJugadores)
    {
        int pos = 0;
        for (JugadorRanking jugador : pLJugadores) {
            String nombre = "    -";
            String puntuacion = "-";
            if (jugador != null) {
                nombre = jugador.getNombre();
                puntuacion = Integer.toString(jugador.getPuntuacion());
            }
            //Creamos los label ajustando el tamaño
            Label nombreLabel = new Label(nombre);
            nombreLabel.setFont(Font.font("MarioFont", 30));
            nombreLabel.setTextFill(Color.color(1, 1, 1));
            Label puntuacionLabel = new Label(puntuacion);
            puntuacionLabel.setFont(Font.font("MarioFont", 30));
            puntuacionLabel.setTextFill(Color.color(1, 1, 1));

            // Por cada usuario, insertamos la imagen de su posición, su nombre y puntuación.
            this.add(new Label("                  "), 1, pos);
            this.add(nombreLabel, 2, pos);
            this.add(new Label("        "), 3, pos);
            this.add(puntuacionLabel, 4, pos++);
        }
    }
}
