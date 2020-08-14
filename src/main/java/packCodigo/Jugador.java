package packCodigo;

public class Jugador {
	private String nombre;
	private int puntuacion;
	
	public Jugador(String pNombre){
		nombre = pNombre;
	}
	
	public void establecerPuntuacion(int pPuntuacion){
		puntuacion=pPuntuacion;
	}

	public int compareTo(Jugador pivote) {
		if(pivote.obtenerPunt()>this.obtenerPunt()){
			return 1;
		}else{
			if(pivote.obtenerPunt()<this.obtenerPunt()){
				return -1;
			}else{return 0;}
		}
	}
	
	public String obtenerNombre(){
		return nombre;
	}
	
	public int obtenerPunt(){
		return puntuacion;
	}
	
	private void ponerPunt(){
		this.puntuacion=Buscaminas.getBuscaminas().obtenerPuntuacion();
	}
	
	//public para las JUnit
	private boolean mismoJugador(){
		boolean mismo = false;
		if(this.obtenerNombre().equals(Buscaminas.getBuscaminas().obtenerNombreJugador())){
			mismo = true;
		}
		return mismo;
	}
	//public para las JUnit
	private void asignarPuntuacionR(){
		mayorPunt();
	}
	
	private void mayorPunt(){
		if(Buscaminas.getBuscaminas().obtenerPuntuacion()>=this.obtenerPunt()){
			this.ponerPunt();
		}
	}
}

