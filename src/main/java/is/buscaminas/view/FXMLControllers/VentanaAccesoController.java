package is.buscaminas.view.FXMLControllers;

import is.buscaminas.Main;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;


public class VentanaAccesoController {

    //Atributos normales
    private ToggleGroup dificultadGroup;

    //Atributos FXML
    @FXML
    private TextField nombreTextField;
    @FXML
    private RadioButton dificultad1;
    @FXML
    private RadioButton dificultad2;
    @FXML
    private RadioButton dificultad3;


    //Constructora
    @FXML
    public void initialize ()
    {
        dificultadGroup = new ToggleGroup();
        dificultad1.setToggleGroup(dificultadGroup);
        dificultad2.setToggleGroup(dificultadGroup);
        dificultad3.setToggleGroup(dificultadGroup);
    }

    @FXML
    public void pulsarAceptar ()
    {
        // Si se introduce un nombre y se selcciona una dificultad
        if (!nombreTextField.getText().equals("") && dificultadGroup.getSelectedToggle() != null) {

            // Guardamos el nombre de jugador
            String nombreUser = nombreTextField.getText();

            // Introducimos la dificultad seleccionada
            int numDificultad;
            String dificultad = ((Node) dificultadGroup.getSelectedToggle()).getId();

            try {
                numDificultad = Integer.parseInt(dificultad);
            }
            catch (Exception e) {
                numDificultad = 1;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "ERROR (introducir dificultad)", ButtonType.YES, ButtonType.NO);
            }

            // Se inicia la partida
            Main.login(nombreUser, numDificultad);

        }
        else {
            // Se le indica al usuario que ha de seleccionar un nombre y una dificultad
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Datos incorrectos");
            alerta.setHeaderText(null);
            alerta.setContentText("Introduce un nombre y una dificultad");
            alerta.show();
        }
    }
}
