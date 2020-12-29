package is.buscaminas.view.uiElements;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

import java.io.File;

public class VistaAyuda extends Pane {

    // La Clase que muestra las páginas de ayuda
    public VistaAyuda ()
    {
        super();
        setPrefSize(820, 460);
        //Ponemos la primera imagen
        cambiarPaginaAyuda(1);
    }

    public void cambiarPaginaAyuda (int pPag)
    {
        //Pre: un numero
        //Post: Se carga la pagina correspondiente a ese numero

        //cargamos la imagen
        Image imagenCasilla = new Image(new File("src/main/resources/is/buscaminas/ui/ayuda/ayudaPag" + pPag + ".png").toURI().toString());
        BackgroundSize backgroundSize = new BackgroundSize(820, 460, false, false, false, false);
        setBackground(new Background(new BackgroundImage(imagenCasilla, null, null, null, backgroundSize)));
    }
}
