package is.buscaminas.view.uiElements;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;


public class VistaRanking extends GridPane
{
	
	public VistaRanking(String pRanking, boolean pMostrarNivel)
	{
		super();
		
		// Se configura la vista
		this.setPadding(new Insets(25));
		this.setHgap(0);
		this.setVgap(15);
		
		// Se cargan todos los números
		for (int i = 1; i <= 10; i++){
			int size = 40;
			Pane numPosicion = new Pane();
			numPosicion.setMinHeight(40);
			numPosicion.setMinWidth(40);
			numPosicion.setMaxHeight(40);
			numPosicion.setMaxWidth(40);
			
			Image imagenPosicion = new Image(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/ranking/" + i + ".png").toURI().toString());
			BackgroundSize backgroundSize = new BackgroundSize(size, size, false, false, false, true);
			numPosicion.setBackground(new Background(new BackgroundImage(imagenPosicion, null, null, null, backgroundSize)));
			
			this.add(numPosicion, 0, (i - 1));
			
			cambiarRanking(pRanking,pMostrarNivel);
		}
		
	}
	
	private void cambiarRanking(String pLRankingJson, boolean pMostrarNivel)
	{
		JsonArray jsonArrayRanking = Jsoner.deserialize(pLRankingJson, new JsonArray());
		
		for (int i = 0; i < 10; i++){
			String nombre = "    -";
			String puntuacion = "-";
			String nivel = "-";
			
			try{
				JsonObject elementoRanking = (JsonObject) jsonArrayRanking.get(i);
				nombre = (String) elementoRanking.get("nickname");
				puntuacion = elementoRanking.get("puntuacion").toString();
				nivel = elementoRanking.get("nivel").toString();
			}
			catch (IndexOutOfBoundsException ignored){
			}
			
			//Creamos los label ajustando el tamaño
			Label nombreLabel = new Label(nombre);
			nombreLabel.setFont(Font.font("MarioFont", 15));
			nombreLabel.setTextFill(Color.color(1, 1, 1));
			Label puntuacionLabel = new Label(puntuacion);
			puntuacionLabel.setFont(Font.font("MarioFont", 15));
			puntuacionLabel.setTextFill(Color.color(1, 1, 1));
			Label nivelLabel = new Label(nivel);
			nivelLabel.setFont(Font.font("MarioFont", 15));
			nivelLabel.setTextFill(Color.color(1, 1, 1));
			
			// Por cada usuario, insertamos la imagen de su posición, su nombre y puntuación.
			this.add(new Label("                  "), 1, i);
			this.add(nombreLabel, 2, i);
			this.add(new Label("        "), 3, i);
			this.add(puntuacionLabel, 4, i);
			if (pMostrarNivel){
				this.add(new Label("        "), 5, i);
				this.add(nivelLabel, 6, i);
			}
		}
	}
}
