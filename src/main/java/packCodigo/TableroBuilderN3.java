package packCodigo;


public class TableroBuilderN3 extends TableroBuilder{

	private Tablero elTablero ;
	private static TableroBuilderN3 miTablero = new TableroBuilderN3();
	
	public static TableroBuilderN3 getTableroBuilderN3(){
		return miTablero;
	}
		
	public Tablero asignarTablero(){
		elTablero = new Tablero(3,12,25);
		elTablero.generarMatriz();
		return elTablero;
	}

}

