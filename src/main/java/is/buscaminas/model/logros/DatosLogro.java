package is.buscaminas.model.logros;


public class DatosLogro
{
	private String nombreLogro;
	private String descripcion;
	private String fecha;
	private int avance;
	private String nombreTema;
	private int objetivo;
	private String tipo;
	private int nivel;
	
	//Constructora
	public DatosLogro(String pNombreL, String pDesc, String pFecha, int pAvance, String pNombreT, int pObjetivo, String pTipo, int pNivel)
	{
		this.nombreLogro = pNombreL;
		this.descripcion = pDesc;
		this.fecha = pFecha;
		this.avance = pAvance;
		this.nombreTema = pNombreT;
		this.objetivo = pObjetivo;
		this.tipo = pTipo;
		this.nivel = pNivel;
	}
	
	//Los geters de los atributos
	
	public String getNombreLogro()
	{
		return nombreLogro;
	}
	
	public String getDescripcion()
	{
		return descripcion;
	}
	
	public String getFecha()
	{
		return fecha;
	}
	
	public int getAvance()
	{
		return avance;
	}
	
	public String getNombreTema()
	{
		return nombreTema;
	}
	
	public int getObjetivo()
	{
		return objetivo;
	}
	
	public String getTipo()
	{
		return tipo;
	}
	
	public int getNivel()
	{
		return nivel;
	}
}
