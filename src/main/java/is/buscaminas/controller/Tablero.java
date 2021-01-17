package is.buscaminas.controller;


import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import is.buscaminas.model.casillas.*;
import javafx.util.Pair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;


public class Tablero
{
	
	//Atributos
	private static Tablero mTablero;
	private final PropertyChangeSupport lObservers;
	private Casilla[][] matrizCasillas;
	private int casillasPorDespejar; //Las que no son minas y aún ocultas
	private int numMinas;
	private int numMinasPorMarcar; //numero de minas que te quedan por marcar (si marcas demasiadas casillas, será un num negativo)
	
	
	//Constructora
	private Tablero()
	{
		lObservers = new PropertyChangeSupport(this);
		generarMatrizTablero();
	}
	
	public static Tablero getTablero()
	{
		if (mTablero == null) mTablero = new Tablero();
		return mTablero;
	}
	
	public void iniciarTablero()
	{
		// Se crea o resetea (si ya existia) el tablero
		mTablero = new Tablero();
	}
	
	//Metodos--------------------------------------------
	
	//Metodo para añadir observer del número del marcador de minas
	
	public void addObserver(PropertyChangeListener pObserver)
	{
		//Pre: Un observer
		//Post: Se ha añadido el observer a la lista de observers
		
		lObservers.addPropertyChangeListener(pObserver);
		lObservers.firePropertyChange("numMinasPorMarcar", null, numMinasPorMarcar); //para que al empezar la partida tengamos el número en pantalla
	}
	
	//Metodo para generar la matriz
	private void generarMatrizTablero()
	{
		//Pre:
		//Post: - Se ha generado una matriz del tamaño indicado para el nivel de dificultad seleccionado
		//      - Se ha guardado el número de minas
		//      - Se ha guardado el número de casillas sin minas
		
		
		// Se obtienen los datoss del nivel mediante el JSON
		String datosNivelJSONString = Partida.getPartida().getDatosNivel();
		JsonObject datosNivelJSON = Jsoner.deserialize(datosNivelJSONString, new JsonObject());
		int numColumnas = ((BigDecimal) datosNivelJSON.get("nColumnas")).intValue();
		int numFilas = ((BigDecimal) datosNivelJSON.get("nFilas")).intValue();
		
		// Se genera la matriz:
		matrizCasillas = new Casilla[numFilas][numColumnas];
		
		// Se cargan los datos relacionados
		numMinas = ((BigDecimal) datosNivelJSON.get("dificultad")).intValue();
		casillasPorDespejar = (numFilas * numColumnas) - numMinas;
		numMinasPorMarcar = numMinas;
	}
	
	//----------------------------------------------
	//Metodos para generar las casillas del tablero
	//----------------------------------------------
	public void generarCasillasTablero(int pFila, int pColumna, PropertyChangeListener[][] pMatrizBotones)
	{
		//* Este método es llamado por el controller cuando el usuario hace el primer click
		
		//Pre: - Fila y columna de la primera casilla seleccionada por el jugador
		//     - Matriz con referencias a las casillas de la Vista
		//Post: Se ha generado el tablero de casillas
		
		//Primero se generan las minas
		generarMinas(pFila, pColumna, pMatrizBotones);
		
		//El resto de casillas se rellenan con casillas sin minas
		generarNoMinas(pMatrizBotones);
	}
	
	public void marcarPrevio(int pFila, int pColumna, PropertyChangeListener pCasillaMarcada)
	{
		// Si no está generado el tablero y se hace click derecho, se genera una CasillaTemp temporal
		
		//      Pre:    - Fila y columna de la primera casilla seleccionada por el jugador
		//              - Referencias a la casilla de la Vista que debe ser marcada
		//      Post: Se ha generado una casilla marcada temporal
		
		// Se comprueba que en esa casilla no haya nada
		if (matrizCasillas[pFila][pColumna] == null){
			//Se crea la CasillaNum temporal para que pueda ser marcada
			matrizCasillas[pFila][pColumna] = new CasillaTemp(pCasillaMarcada);
		}
		
	}
	
	private void generarMinas(int pFila, int pColumna, PropertyChangeListener[][] pMatrizBotones)
	{
		//Pre: - Fila y columna de la primera casilla seleccionada por el jugador
		//     - Matriz con referencias a las casillas de la Vista
		//Post: Se generan las minas con los siguientes criterios:
		//      - El número es el adecuado a la dificultad
		//      - Las minas se colocan aleatoriamente
		//      - No habrá ninguna mina ni en la primera casilla seleccionada por el jugador ni en sus adyacentes.
		//      - Habrá una MinaReset si hay más de 1 mina.
		//      - Habrá una Mina 50% si hay más de 2 minas.
		
		int numMinas = this.numMinas;
		int fila, columna;
		Random random = new Random();
		
		while (numMinas > 0){
			//Se obtienen 2 valores random que correspondan a posiciones dentro del tablero:
			fila    = random.nextInt(matrizCasillas.length);
			columna = random.nextInt(matrizCasillas[0].length);
			
			// Se comprueba que no sea ni la primera ni ninguna de las adyacentes
			// y que en esa casilla no haya nada o haya una CasillaTemp (significaría que la casilla es temporal, por lo que será cambiada a CasillaMina)
			
			// Si cumplen las condiciones se añade, de lo contrario se obtiene otra coordenada aleatoria
			if ((Math.abs(pFila - fila) > 1 || Math.abs(pColumna - columna) > 1)) // Si no es adyacente a la casilla del click
			{
				switch (numMinas){
					// Casilla RESET
					case 2:
						// Si la casilla esta vacía, se crea mina y se disminuyen las minas restantes
						if (matrizCasillas[fila][columna] == null){
							matrizCasillas[fila][columna] = new CasillaMinaReset(pMatrizBotones[fila][columna]);
							numMinas--;
						}
						// Si habia una casillaTemp:
						// Se manda crear una casilla mina pasandole la casillaTemp .
						// También disminuyen las minas restantes
						else if (matrizCasillas[fila][columna] instanceof CasillaTemp){
							//mando a la mina temp crear una casilla mina igual a ella
							matrizCasillas[fila][columna] = new CasillaMinaReset((CasillaTemp) matrizCasillas[fila][columna]);
							numMinas--;
						}
						break;
					
					// Casilla 50%
					case 3:
						// Si la casilla esta vacía, se crea mina y se disminuyen las minas restantes
						if (matrizCasillas[fila][columna] == null){
							matrizCasillas[fila][columna] = new CasillaMina50(pMatrizBotones[fila][columna]);
							numMinas--;
						}
						// Si habia una casillaTemp:
						// Se manda crear una casilla mina pasandole la casillaTemp .
						// También disminuyen las minas restantes
						else if (matrizCasillas[fila][columna] instanceof CasillaTemp){
							//mando a la mina temp crear una casilla mina igual a ella
							matrizCasillas[fila][columna] = new CasillaMina50((CasillaTemp) matrizCasillas[fila][columna]);
							numMinas--;
						}
						break;
					
					
					// Casillas Normales
					default:
						// Si la casilla esta vacía, se crea mina y se disminuyen las minas restantes
						if (matrizCasillas[fila][columna] == null){
							matrizCasillas[fila][columna] = new CasillaMina(pMatrizBotones[fila][columna]);
							numMinas--;
						}
						// Si habia una casillaTemp:
						// Se manda crear una casilla mina pasandole la casillaTemp .
						// También disminuyen las minas restantes
						else if (matrizCasillas[fila][columna] instanceof CasillaTemp){
							//mando a la mina temp crear una casilla mina igual a ella
							matrizCasillas[fila][columna] = new CasillaMina((CasillaTemp) matrizCasillas[fila][columna]);
							numMinas--;
						}
						break;
				}
			}
		}
	}
	
	private void generarNoMinas(PropertyChangeListener[][] pMatrizBotones)
	{
		//Pre: Matriz con referencias a las casillas de la Vista
		//Post: Se han generado las casillas que no contienen minas
		
		for (int fila = 0; fila < matrizCasillas.length; fila++){
			for (int columna = 0; columna < matrizCasillas[fila].length; columna++){
				
				// Si la casilla no ha sido creada, se crea
				if (matrizCasillas[fila][columna] == null){
					int minasAdyacentes = calcularMinasAdyacentes(fila, columna);
					matrizCasillas[fila][columna] = new CasillaNum(minasAdyacentes, pMatrizBotones[fila][columna]);
				}
				
				// Si la casilla es de tipo casillaTemp
				// Se le manda crear una casillaNum dandole un valor de numMinasAdyacentes y la casillaTemp.
				else if (matrizCasillas[fila][columna] instanceof CasillaTemp){
					int minasAdyacentes = calcularMinasAdyacentes(fila, columna);
					matrizCasillas[fila][columna] = new CasillaNum(minasAdyacentes, (CasillaTemp) matrizCasillas[fila][columna]);
				}
			}
		}
	}
	
	
	private int calcularMinasAdyacentes(int pFila, int pColumna)
	{
		//Pre:  La fila y la columna pertenecen a valores de la matriz
		//Post: Devuelve el número de minas adyacentes de una casilla
		
		int minasAdyacentes = 0;
		
		for (int fila = pFila - 1; fila <= pFila + 1; fila++){
			for (int columna = pColumna - 1; columna <= pColumna + 1; columna++){
				//Se comprueba que se está dentro del tablero
				if (0 <= fila && fila < matrizCasillas.length && 0 <= columna && columna < matrizCasillas[0].length){
					//Si es mina se incrementa el contador
					if (matrizCasillas[fila][columna] instanceof CasillaMina) minasAdyacentes++;
				}
			}
		}
		
		return minasAdyacentes;
	}
	//----------------------------------------------
	
	//---------------------------------------------------------------------------
	//Metodos accesibles por el controlador a la hora de seleccionar casillas
	//---------------------------------------------------------------------------
	public void despejarCasilla(int pFila, int pColumna)
	{
		//Pre:  La fila y la columna pertenecen a valores de la matriz
		//Post: - Se ha despejado la casilla en caso de poderse
		
		if (Partida.getPartida().hayPartidaActiva()){
			int result = matrizCasillas[pFila][pColumna].despejar();
			
			// - Segun lo devuelto por el intento de despejar la casilla se ejecutan las siguientes acciones:
			
			/* variable "result", int:
			 *  0 = no hacer absolutamente nada
			 *  1 = Únicamente disminuir el contador 'casillasPorDespejar'
			 *  2 = Se ha hecho click en mina, fin de la partida
			 *  3 = casilla sin minas adyacentes -> Despejar las de alrededor y decrementar el contador 'casillasPorDespejar'
			 *  4 = Despejar las casillas que estén inmediatamente adyacentes a la casilla despejada en caso de que el numero de minas adyacentes que tenga
			 *          sea exactamente el número de casillas marcadas en las 8 celdas inmediatamente adyacentes (no necesariamente tienen que estar bien marcadas)
			 *  5 = Se desvela la posición la mitad de las minas que aún no estén marcadas.
			 *  6 = Se resetea el tablero (no la partida, por tanto tampoco el contador)
			 */
			
			switch (result){
				case 1: // Se ha despejado una casilla no-mina
					casillasPorDespejar--;
					if (casillasPorDespejar == 0) Partida.getPartida().finalizarPartida(true);
					break;
				
				case 2: // Se ha despejado una mina
					Partida.getPartida().finalizarPartida(false);
					mostrarMinas();
					break;
				
				case 3: // Despejar alrededor normal
					despejarAlrededor(pFila, pColumna);
					casillasPorDespejar--;
					if (casillasPorDespejar == 0){
						Partida.getPartida().finalizarPartida(true); // Debería saltar un logro si ganas por esta llamada
					}
					break;
				
				case 4: // Despejar inmediatas adyacentes
					int relacionMinasBanderas = 0;  // Contador de las casillas marcadas
					for (int fila = pFila - 1; fila <= pFila + 1; fila++){
						for (int columna = pColumna - 1; columna <= pColumna + 1; columna++){
							if (0 <= fila && fila < matrizCasillas.length && 0 <= columna && columna < matrizCasillas[0].length){
								if (matrizCasillas[fila][columna].estaMarcada()){
									relacionMinasBanderas++;          // Si es casilla marcada se aumenta el numero de banderas adyacentes
								}
								if (matrizCasillas[fila][columna] instanceof CasillaMina){
									relacionMinasBanderas--;    // Si la casilla es una mina incrementa el contador de minas
								}
							}
						}
					}
					if (relacionMinasBanderas == 0){
						despejarAdyacentes(pFila, pColumna);     // Si el número de minas adyacentes es igual al número de casillas
					}
					break;                                                                              // con banderas, se procede a despejar las casillas
				
				case 5: // Despeja el 50% de las minas restantes sin marcar
					marcarMitadMinasRestantes();
					break;
				
				case 6: // Resetea el tablero
					Partida.getPartida().inicializarTablero();
					break;
			}   //Se ignoran el resto de casos
		}
	}
	
	
	public void despejarAlrededor(int pFila, int pColumna)
	{
		// Pre:     Unas coordenadas de una casilla
		// Post:    Se despejan las casillas de alrededor
		for (int fila = pFila - 1; fila <= pFila + 1; fila++){
			for (int columna = pColumna - 1; columna <= pColumna + 1; columna++){
				if (0 <= fila && fila < matrizCasillas.length && 0 <= columna && columna < matrizCasillas[0].length){                       // La casilla está en el tablero.
					if (!matrizCasillas[fila][columna].estaDespejada()){
						despejarCasilla(fila, columna);             // La casilla no está despejada. Aumenta la eficiencia del programa,
					}
					// además evita loops innecesarios al mandar despejar casillas
				}
			}
		}
	}
	
	public void despejarAdyacentes(int pFila, int pColumna)
	{
		//Pre:  Recibe el número de fila y columna de la casilla seleccionada.
         /* Requisitos para despejar casillas adyacentes:
         *  1- La casilla seleccionada debe estar despejada
         *  2- El número de casillas marcadas (bandera) o minas despejadas/mostradas alrededor de esa casilla debe ser igual al número de esa casilla
         * (interrogaciones cuentan como casilla oculta)


        //Post: Se despejan únicamente las casillas adyacentes a esa casilla EN CASO DE QUE SE CUMPLAN LOS REQUISITOS:
         *      Si una casilla cumple los requisitos, manda despejar las casillas adyacentes como si se tratase de una casilla con el número 0.
         *      Si se mandan despejar banderas no las despeja, y puede despejar minas perdiendo así la partida.
         */
		
		for (int fila = pFila - 1; fila <= pFila + 1; fila++){
			for (int columna = pColumna - 1; columna <= pColumna + 1; columna++){
				if (0 <= fila && fila < matrizCasillas.length && 0 <= columna && columna < matrizCasillas[0].length && (fila != pFila || columna != pColumna)){
					// La casilla está en el tablero. Además, no hay que despejar la casilla inicial
					int result = matrizCasillas[fila][columna].despejar();
					
					switch (result){
						case 1:
							// Si se ha despejado una casilla num, se decrementa el contador y se comprueba
							casillasPorDespejar--;                                                      // si se ha ganado
							if (casillasPorDespejar == 0) Partida.getPartida().finalizarPartida(true);
							break;
						
						case 2:
							Partida.getPartida().finalizarPartida(false);    // Si se manda despejar una mina, se acaba la partida
							mostrarMinas();
							break;
						
						case 3:
							// Si al despejar la casilla se pide despejar las de alrededor, se hace
							despejarAlrededor(fila, columna);
							casillasPorDespejar--;
							if (casillasPorDespejar == 0){
								Partida.getPartida().finalizarPartida(true); // Comprobamos si se ha ganado (este sería un raro caso muy puntual)
							}
							break;
						
						case 5: // Despeja el 50% de las minas restantes sin marcar
							marcarMitadMinasRestantes();
							break;
						
						case 6: // Resetea el tablero
							Partida.getPartida().inicializarTablero();
							break;
					}
				}
			}
		}
	}
	
	private void marcarMitadMinasRestantes()
	{
		// Pre:
		// Post: Se ha mostrado la posición del 50% de las minas que el usuario aún no había marcado correctamente
		
		// Se obtiene el número de casillas a marcar
		int mitadNumMinasSinMarcar = (numMinas - obtenerMinasCorrectamenteMarcadas()) / 2;
		
		// Se actualiza el valor de numMinasPorMarcar
		lObservers.firePropertyChange("numMinasPorMarcar", numMinasPorMarcar, numMinasPorMarcar - mitadNumMinasSinMarcar);
		
		// Se crea una lista con todas las minas que no estén ni marcadas ni despejadas
		ArrayList<CasillaMina> lMinasSinMarcar = new ArrayList<>();
		for (Casilla[] columna : matrizCasillas){
			for (Casilla casilla : columna){
				if (casilla instanceof CasillaMina && !casilla.estaMarcada()) lMinasSinMarcar.add((CasillaMina) casilla);
			}
		}
		
		// Se muestra la posición de una mina random de la lista obtenida.
		while (mitadNumMinasSinMarcar > 0){
			Random rand = new Random();
			int posRandom = rand.nextInt(lMinasSinMarcar.size());
			CasillaMina minaRandom = lMinasSinMarcar.remove(posRandom);
			minaRandom.verMina();
			mitadNumMinasSinMarcar--;
		}
	}
	
	private int obtenerMinasCorrectamenteMarcadas()
	{
		// Pre:
		// Post: Devuelve el número de minas que están correctamente marcadas.
		
		int minasCorrectamenteMarcadas = 0;
		for (Casilla[] columna : matrizCasillas){
			for (Casilla casilla : columna){
				if (casilla instanceof CasillaMina && casilla.estaMarcada()) minasCorrectamenteMarcadas++;
			}
		}
		return minasCorrectamenteMarcadas;
	}
	
	//-----------------------------------------------------------------------------------
	
	public void marcarCasilla(int pFila, int pColumna)
	{
		//Pre:  Recibe el número de fila y columna de la casilla seleccionada a ser marcada
		//Post: Se marca, desmarca o "interroga" la casilla en cuestión situada en esa fila y columna
		
		//      El resultado devuelto por la función marcar tiene la forma [key,value] (un par de (boolean,boolean))
		//      El primer boolean señala si se ha modificado el estado de la casilla (es decir, si se a marcado o desmarcado toma el valor TRUE)
		//      El segundo boolean (solo válido si el primer boolean es TRUE) indica con un TRUE si se ha marcado una casilla y FALSE si se ha desmarcado
		
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		
		
		if (Partida.getPartida().hayPartidaActiva()) //Si hay una partida activa
		{
			Pair<Boolean, Boolean> resultado = matrizCasillas[pFila][pColumna].marcar();
			
			if (resultado.getKey()) //Si se ha hecho algo (Primer boolean = TRUE)
			{
				if (resultado.getValue())           //Si se ha marcado una casilla (Segundo boolean = TRUE) se disminuye el numero de banderas "numMinasPorMarcar"
				{
					lObservers.firePropertyChange("numMinasPorMarcar", numMinasPorMarcar, --numMinasPorMarcar);
				}
				else{                              //Si se ha desmarcado una casilla (Segundo boolean = FALSE) se aumenta el numero de banderas "numMinasPorMarcar"
					lObservers.firePropertyChange("numMinasPorMarcar", numMinasPorMarcar, ++numMinasPorMarcar);
				}
			}
		}
	}
	
	private void mostrarMinas()
	{
		for (int fila = 0; fila <= matrizCasillas.length - 1; fila++){
			for (int columna = 0; columna <= matrizCasillas[0].length - 1; columna++){
				matrizCasillas[fila][columna].verMina();
			}
		}
	}
}
