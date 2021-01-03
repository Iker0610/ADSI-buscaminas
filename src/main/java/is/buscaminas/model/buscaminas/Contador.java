package is.buscaminas.model.buscaminas;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;


public class Contador
{
	//Atributos
	private static Contador mContador;
	private final PropertyChangeSupport lObservers; //lista de observers
	private int seconds;
	private Timer timer;
	
	//Constructora
	private Contador()
	{
		lObservers = new PropertyChangeSupport(this);
	}
	
	//Metodo get del Singleton
	public static Contador getContador()
	{
		if (mContador == null){
			mContador = new Contador();
		}
		return mContador;
	}
	
	//Métodos:
	
	public void addObserver(PropertyChangeListener pObserver)
	{
		//Pre: Un observer
		//Post: Se ha añadido el observer a la lista de observers
		lObservers.addPropertyChangeListener(pObserver);
		pObserver.propertyChange(new PropertyChangeEvent(this, "seconds", null, Math.max(seconds, 0)));
	}
	
	public int getSeconds()
	{
		return seconds;
	}
	
	public void iniciar()
	{
		//Pre:
		//Post: Se ha iniciado el conteo. Cada segundo se notificará a los observers
		
		/*
		 * Para implementar el timer se usa la clase Timer.
		 * Esta clase permite ejecutar tareas en momentos concretos. En este caso se ha empleado la opción de ejecutar tareas periódicamente.
		 * Para ello se pasa al método scheduleAtFixedRate() la tarea que se desea ejecutar (TimerTask)
		 * y el tiempo hasta la primera ejecución y el tiempo entre las ejecuciones posteriores (el periodo). (Ambos en milisegundos)
		 */
		
		reset(); //Nos aseguramos de que no exista ningún timer funcionando
		timer = new Timer(true); //Con el is Daemon se indica que el hilo que generará esa clase se puede finalizar sin problemas al cerrar la aplicació
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				lObservers.firePropertyChange("seconds", seconds, Math.max(++seconds, 0));
			}
		}, 0, 1000);
	}
	
	public void reset()
	{
		//Pre:
		//Post: Se ha parado el contador y se reinician los segundos
		parar();
		seconds = -1;
		timer   = null;
	}
	
	public void continuar()
	{
		//Pre:
		//Post: Continua la cuenta desde los segundos que marque la variable
		if (timer != null){
			timer
					  = new Timer(true); //Con el is Daemon se indica que el hilo que generará esa clase se puede finalizar sin problemas al cerrar la aplicació
			timer.scheduleAtFixedRate(new TimerTask()
			{
				@Override
				public void run()
				{
					lObservers.firePropertyChange("seconds", seconds, ++seconds);
					
				}
			}, 1000, 1000);
		}
	}
	
	public void parar()
	{
		//Pre:
		//Post: Se ha parado el contador
		if (timer != null) timer.cancel(); //Elimina la tarea que se está ejecutando en estos momentos y deshabilita permanentemente el timer
	}


    /*
    Codigo alternativo empleando los Thread:

    @Override
    public void run ()
    {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Thread.sleep(1000);
                lObservers.firePropertyChange("seconds", seconds, ++seconds);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     */
}
