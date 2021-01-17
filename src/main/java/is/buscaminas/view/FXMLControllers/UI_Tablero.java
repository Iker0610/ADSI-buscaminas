package is.buscaminas.view.FXMLControllers;


import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.controller.Partida;
import is.buscaminas.controller.SFXPlayer;
import is.buscaminas.controller.Tablero;
import is.buscaminas.model.Contador;
import is.buscaminas.model.Usuario;
import is.buscaminas.view.uiElements.VistaCasilla;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;


public class UI_Tablero
{
	//Atributos FXML
	
	@FXML
	private GridPane tableroCasillas;
	
	
	//Constructora
	
	@FXML
	public void initialize()
	{
		//Pre:
		//Post: El tablero se ha generado y configurado, y se han añadido las casillas
		
		//Cargar temática
		tableroCasillas.setStyle(
				  "-fx-background-image: url(is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/assets/fondo/fondo.png); -fx-background-repeat: no-repeat; -fx-background-size: cover, auto; -fx-background-insets:0 0 0 0, 4 0 0 4, 4;");
		
		// Se pone el tema de fondo:
		SFXPlayer.getSFXPlayer().setBackgroundTheme("mainTheme");
		
		//Se genera y configura el tablero (incluidos el listener del click inicial)
		inicializarTablero();
		
		//Se añaden las casillas
		crearCasillasTablero();
	}
	
	
	// Métodos
	
	private void inicializarTablero()
	{
		//Pre:
		//Post: Se ha generado y configurado el trablero
		
		//Se configura el espacio entre elementos del GridPane
		tableroCasillas.setVgap(1);
		tableroCasillas.setHgap(1);
		
		//Se crea el evento que se ejecutará al despejar por primera vez una casilla (antes de despejarla en el modelo)
		EventHandler<MouseEvent> primerClick = new EventHandler<>()
		{
			@Override
			public void handle(MouseEvent pEvento)
			{
				//Se mira si es un click izquierdo y se ha pulsado en una casilla
				if (pEvento.isPrimaryButtonDown() && pEvento.getTarget() instanceof VistaCasilla){
					//Obtenemos la fila y columna de la casilla que se ha clickado
					int fila = GridPane.getRowIndex((Node) pEvento.getTarget());
					int columna = GridPane.getColumnIndex((Node) pEvento.getTarget());
					
					//Obtenemos la matriz de referencias a las Casillas de la vista
					VistaCasilla[][] matrizCasillas = getMatrizCasillas();
					
					//Llamamos al tablero (modelo) y le mandamos generar las casillas
					Tablero.getTablero().generarCasillasTablero(fila, columna, matrizCasillas);
					
					//Se inicia el contador
					Contador.getContador().iniciar();
					
					//Se elimina este mismo evento pues solo se ha de ejecutar la primera vez
					tableroCasillas.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
				}
				// Se comprueba si es click derecho y se ha pulsado en una casilla
				else if (pEvento.isSecondaryButtonDown() && pEvento.getTarget() instanceof VistaCasilla){
					//Obtenemos la fila y columna de la casilla que se ha clickado
					int fila = GridPane.getRowIndex((Node) pEvento.getTarget());
					int columna = GridPane.getColumnIndex((Node) pEvento.getTarget());
					
					//Mandamos al tablero crear una casilla temporal en la posición del click. Se le pasa la VistaCasilla para que pueda actualizarse la vista
					VistaCasilla casilla = getMatrizCasillas()[fila][columna];
					Tablero.getTablero().marcarPrevio(fila, columna, casilla);
				}
				else{ //si se ha realizado cualquier otra accion se consume el evento
					pEvento.consume();
				}
			}
		};
		
		//Se le añade al GridPane (el tablero de la vista) el evento anterior como un 'Filtro de eventos'
		tableroCasillas.addEventFilter(MouseEvent.MOUSE_PRESSED, primerClick);
	}
	
	private void crearCasillasTablero()
	{
		//Pre:
		//Post: Se han añadido las casillas (vista) al tablero (vista)
		
		String datosNivelJSONString = Partida.getPartida().getDatosNivel();
		JsonObject datosNivelJSON = Jsoner.deserialize(datosNivelJSONString, new JsonObject());
		int numColumnas = ((BigDecimal) datosNivelJSON.get("nColumnas")).intValue();
		int numFilas = ((BigDecimal) datosNivelJSON.get("nFilas")).intValue();
		
		for (int colum = 0; numColumnas > colum; colum++){
			for (int row = 0; numFilas > row; row++) tableroCasillas.add(generarCasilla(), colum, row);
		}
	}
	
	private VistaCasilla generarCasilla()
	{
		//Pre:
		//Post: Devuelve un VistaCasilla configurada
		
		// Se crea y se le añade el evento adecuado cuando se selecciona
		VistaCasilla nuevaCasilla = new VistaCasilla();
		nuevaCasilla.setOnMousePressed(this::gestionarEventoCasilla);
		//*Mediante el operador :: se le indica objeto y método de ese objeto al que hay que llamar
		
		return nuevaCasilla;
	}
	
	private void gestionarEventoCasilla(MouseEvent pEvento)
	{
		//Pre: Un evento de ratón
		//Post: Se llama al tablero (modelo) y se le indica que casilla se desea despejar o marcar
		VistaCasilla casilla = (VistaCasilla) pEvento.getTarget();
		if (pEvento.isPrimaryButtonDown()){                            // Si se ha hecho click izquierdo
			
			int filaCasilla = GridPane.getRowIndex(casilla);
			int columnaCasilla = GridPane.getColumnIndex(casilla);
			Tablero.getTablero().despejarCasilla(filaCasilla, columnaCasilla);
		}
		else if (pEvento.isSecondaryButtonDown())                        // Si se ha hecho click derecho
		{
			int filaCasilla = GridPane.getRowIndex(casilla);
			int columnaCasilla = GridPane.getColumnIndex(casilla);
			Tablero.getTablero().marcarCasilla(filaCasilla, columnaCasilla);
		}
	}
	
	private VistaCasilla[][] getMatrizCasillas()
	{
		//Pre: El tableroCasilla se ha inicializado
		//Post: Se ha creado una matriz de VistaCasillas con las casillas del tablero (vista)
		
		if (tableroCasillas != null){
			
			//Se genera la matriz a devolver
			VistaCasilla[][] matrizCasillas = new VistaCasilla[tableroCasillas.getRowCount()][tableroCasillas.getColumnCount()];
			
			//Se obtiene la lista de nodos hijos del GridPane -> Lista de las casillas (vista)
			ObservableList<Node> casillas = tableroCasillas.getChildren();
			
			//Por cada casilla se obtiene su posicion (fila, columna) y se añade a la matriz resultado
			for (Node casilla : casillas){
				matrizCasillas[GridPane.getRowIndex(casilla)][GridPane.getColumnIndex(casilla)] = (VistaCasilla) casilla;
			}
			
			return matrizCasillas;
		}
		else{ return (new VistaCasilla[0][0]); }
	}
}