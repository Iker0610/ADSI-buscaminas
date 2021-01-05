package is.buscaminas.controller;


import is.buscaminas.model.buscaminas.Contador;
import is.buscaminas.model.ranking.Ranking;

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
    private int dificultad;
    private boolean partidaActiva;
    
    //Constructora
    private Partida()
    {
        //Se inicializa la partida
        partidaActiva = false;
        lObservers    = new PropertyChangeSupport(this);
    }
    
    //Singleton
    public static Partida getPartida()
    {
        if (mPartida == null) mPartida = new Partida();
        return mPartida;
    }
    
    // Login en la aplicacion
    public void iniciarPartida(int pDificultad)
    {
        dificultad    = pDificultad;
        partidaActiva = true;
        GestorVentanas.getGestorVentanas().abrirPartida();
    }
    
    //Metodos publicos de la clase para administrar los atributos:
    public int getDificultad()
    {
        //Pre:
        //Post: Devuelve el nivel de dificultad
        return dificultad;
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
        if (partidaActiva){
            finalizarPartida(false);
        }
        partidaActiva = true;
        GestorVentanas.getGestorVentanas().abrirPartida();
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
            
            // TODO Arreglar ranking
            Ranking.getRanking().addJugadorRanking(dificultad, "TODO");
            
            //Se abre la ventana del ranking
            GestorVentanas.getGestorVentanas().mostrarRankingEmergente();
            SFXPlayer.getSFXPlayer().playFinalSFX("victory");
        }
        
        //Se para y resetea el contador
        Contador.getContador().reset();
    }
}
