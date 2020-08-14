package packCodigo;

public class TableroBuilderN2 extends TableroBuilder{

	private Tablero elTablero ;
	private static TableroBuilderN2 miTablero = new TableroBuilderN2();
	
	public static TableroBuilderN2 getTableroBuilderN2(){
		return miTablero;
	}
	
	
	public Tablero asignarTablero(){
		elTablero = new Tablero(2,10,15);
		elTablero.generarMatriz();
		return elTablero;
	}

}

