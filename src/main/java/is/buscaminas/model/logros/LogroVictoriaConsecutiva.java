package is.buscaminas.model.logros;


public class LogroVictoriaConsecutiva extends Logro
{
	//Constructora
	
	public LogroVictoriaConsecutiva(String pNombre, String pDescripcion, int pAvance, int pObjetivo, String pFechaObtencion, String pNombreTema)
	{
		super(pNombre, pDescripcion, pAvance, pObjetivo, pFechaObtencion, pNombreTema);
	}
	
	
	//El método que comprueba el logro
	
	public boolean comprobarLogro(boolean victoria, int pNivel, String email)
	{
		//Precondición: recibe los parámetros de victoria, el nivel del mapa y el email del jugador.
		//Postcondición: se comprueba el logro y si se completa cambiar el logro a logros obtenidos.

		boolean conseguido=false;
		if (victoria){   //Si gana y aún no ha conseguido el logro
			conseguido=actualizarAvance();
		}
		else{      //Si no gana y aún no ha conseguido el objetivo
			resetearAvance();
		}
		//En caso de que gane o que pierda pero el avance sea mayor o igual que el objetivo, no hará nada porque ya ha conseguido el logro
		return conseguido;
	}
}