package is.buscaminas.view.FXMLControllers;


import is.buscaminas.view.uiElements.VistaAyuda;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.File;

public class MenuAyudaController {

    // Atributos
    private int pagTotal;
    private int pagAct;

    // Elementos FXML
    @FXML
    private VistaAyuda panelAyuda;

    @FXML
    private Text paginacion;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;

    // Construtora
    @FXML
    private void initialize ()
    {
        // Pre
        // Post:    Se establece el número de páginas de ayuda
        //          Se carga la primera página si existe.
        //          Se bloquea el botón para ir a la página anterior.
        //          Si no hay páginas se establece como página actual la 0 y se bloquea el botón de avanzar
        pagAct = 0;
        pagTotal = getNumPagTotal();
        btnPrev.setDisable(true);
        if (pagTotal <= 1) btnNext.setDisable(true);
        if (pagTotal > 0) {
            pagAct = 1;
            paginacion.setText(pagAct + "/" + pagTotal);
            panelAyuda.cambiarPaginaAyuda(pagAct);
        }
    }

    // Metodos
    private int getNumPagTotal ()
    {
        //Pre:
        //Post: Devuelve el número de páginas de ayuda que hay disponibles

        File[] paginas = new File("src/main/resources/is/buscaminas/temas/mario/ayuda/").listFiles();
        return paginas != null ? paginas.length : 0;
    }

    // Metodos de eventos
    @FXML
    private void nextAyuda ()
    {
        // Se actualiza el indexado
        pagAct++;
        paginacion.setText(pagAct + "/" + pagTotal);

        // Se actualiza la página del menú
        panelAyuda.cambiarPaginaAyuda(pagAct);

        // Si se ha llegado al final se bloquea el botón de avanzar
        if (pagAct >= pagTotal) btnNext.setDisable(true);

        // Se comprueba si es posible habilitar el botón de retroceder
        if (pagAct > 1) btnPrev.setDisable(false);
    }

    @FXML
    private void prevAyuda ()
    {
        // Se actualiza el indexado
        pagAct--;
        paginacion.setText(pagAct + "/" + pagTotal);

        // Se actualiza la página del menú
        panelAyuda.cambiarPaginaAyuda(pagAct);

        // Si se ha llegado al principio se bloquea el botón de retroceder
        if (pagAct <= 1) btnPrev.setDisable(true);

        // Se comprueba si es posible habilitar el botón de avanzar
        if (pagAct < pagTotal) btnNext.setDisable(false);
    }
}
