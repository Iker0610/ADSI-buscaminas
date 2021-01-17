package is.buscaminas.view.uiElements;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class VistaLogro extends HBox
{
	
	public VistaLogro(String pNombreLogro, String pDescripcion, String pAvance, String pObjetivo, String pObtencion, String pNombreTema)
	{
		super();
		
		// Cargamos el título y ajustamos el estilo (blanco)
		Label nombreLabel = new Label(pNombreLogro);
		nombreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold");
		this.getChildren().add(nombreLabel);
		
		//Cargamos la descripcion
		if (pDescripcion != null){
			Label descripcionLabel = new Label(pDescripcion);
			descripcionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold");
			this.getChildren().add(descripcionLabel);
		}
		
		Label fechaObtencion = new Label("    -    ");
		fechaObtencion.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold");
		if (pObtencion != null){
			fechaObtencion.setText(pObtencion + " ");
		}
		this.getChildren().add(fechaObtencion);
		
		if (pNombreTema != null){
			Label temaLabel = new Label(pNombreTema);
			temaLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold");
			this.getChildren().add(temaLabel);
		}
		
		Label avanceLabel = new Label("\t" + pAvance + "/" + pObjetivo);
		avanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold");
		this.getChildren().add(avanceLabel);
		
		
		// Ajustamos el tamaño de la vista
		setMinHeight(50);
		setMaxHeight(50);
		
		// Ajustamos el espaciado y la orientación entre elementos
		setSpacing(20);
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(15));
		
		// Ajustamos la posición y estilo del botón
		if (Integer.parseInt(pAvance) >= Integer.parseInt(pObjetivo)){ this.setStyle("-fx-background-color: rgba(31,31,31,0.5); -fx-border-color: green; -fx-border-width: 2;"); }
		else{ this.setStyle("-fx-background-color: rgba(31,31,31,0.5); -fx-border-color: red; -fx-border-width: 2;  -fx-opacity: 0.5"); }
	}
}
