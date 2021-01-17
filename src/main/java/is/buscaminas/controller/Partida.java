package is.buscaminas.controller;


import is.buscaminas.model.Contador;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Partida
{
	/**
	 Esta clase se encarga de administrar la partida:
	 - Guardará el nombre del Usuario
	 - Guardará el nivel de dificultad
	 - Guardará si hay partidas activas o no
	 */
	
	//Atibutos
	private static Partida mPartida;
	private final PropertyChangeSupport lObservers;
	private int nivel;
	private boolean partidaActiva;
	
	//Constructora
	private Partida()
	{
		//Se inicializa la partida
		partidaActiva = false;
		lObservers = new PropertyChangeSupport(this);
	}
	
	//Singleton
	public static Partida getPartida()
	{
		if (mPartida == null) mPartida = new Partida();
		return mPartida;
	}
	
	public void iniciarPartida(int pNivel)
	{
		// Pre:
		// Post: Se inicia una partida del nivel indicado. Se incializa un tablero.
		nivel = pNivel;
		partidaActiva = true;
		inicializarTablero();
	}
	
	public void inicializarTablero()
	{
		// Pre:
		// Post: Se inicializa un tablero y se abre la interfaz del tablero.
		
		Tablero.getTablero().iniciarTablero();
		GestorVentanas.getGestorVentanas().abrirPartida();
	}
	
	//Metodos publicos de la clase para administrar los atributos:
	public String getDatosNivel()
	{
		//Pre:
		//Post: Devuelve los datos del nievl en formato JSON
		return GestorNiveles.getGestorNiveles().getDatosNivel(nivel);
	}
	
	public int getNivel()
	{
		return nivel;
	}
	
	public boolean hayPartidaActiva()
	{
		//Pre:
		//Post: Devuelve un boolean indicando si hay una partida activa o no
		return partidaActiva;
	}
	
	public void addObserver(PropertyChangeListener pObserver)
	{
		//Pre: Un observer
		//Post: Se ha añadido el observer a la lista de observers
		lObservers.addPropertyChangeListener(pObserver);
	}
	
	public void reiniciarPartida()
	{
		// Pre: Ha existido una partida previamente y el nivel está setteado
		// Post: En caso de que la partida no haya concluido, se da por perdida.
		//			Una vez la partida se ha finalziado se inicia una partida totalmente nueva del mismo nivel
		if (partidaActiva){
			finalizarPartida(false);
		}
		partidaActiva = true;
		inicializarTablero();
	}
	
	public void finalizarPartida(boolean pVictoria)
	{
		//Pre: Un boolean indicando si el jugador a ganado o no
		//Post: Se ha finalizado la partida y se han mostrado todas las minas ocultas
		
		//Se indica que no hay partidas activas
		partidaActiva = false;
		
		//Se avisa a los observers que ha finalizado la partida y cual ha sido el resultado
		lObservers.firePropertyChange("estadoPartida", null, pVictoria);
		
		Contador.getContador().parar();
		
		//Si el usuario ha ganado la partida se envían los datos para actualizar el ranking
		if (pVictoria){
			// Se calcula la puntuación
			int puntuacion = Contador.getContador().getSeconds() == 0 ? (nivel * 2000) + 100 : (nivel * 2000) / Contador.getContador().getSeconds();
			GestorRanking.getGestorRanging().registrarPuntuacion(puntuacion, nivel);
			
			//Se abre la ventana del ranking
			GestorVentanas.getGestorVentanas().mostrarRankingEmergente();
			SFXPlayer.getSFXPlayer().playFinalSFX("victory");
		}
		
		//Se para y resetea el contador
		Contador.getContador().reset();
	}
}
