package is.buscaminas.model.logros;


public class LogroVictoriaNivel extends Logro
{
	//Atributos
	
	private int nivel;
	
	//Constructora
	
	public LogroVictoriaNivel(String pNombre, String pDescripcion, int pAvance, int pObjetivo, String pFechaObtencion, String pNombreTema, int pNivel)
	{
		super(pNombre, pDescripcion, pAvance, pObjetivo, pFechaObtencion, pNombreTema);
		this.nivel = pNivel;
	}
	
	//El método que comprueba el logro (SIN COMPLETAR)
	public boolean comprobarLogro(boolean victoria, int pNivel, String email)
	{
		boolean conseguido=false;
		if (victoria && nivel == pNivel){      //Si gana y está jugando en el mismo nivel que el logro
			conseguido=actualizarAvance();
		}
		//Si gana o pierde en otro nivel da igual y no contabiliza en este logro
		//Y si pierde en el mismo nivel da igual porque no cuenta las victorias consecutivas
		return conseguido;
	}
}