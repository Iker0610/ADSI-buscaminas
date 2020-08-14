package packCodigo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import packVentanas.VBuscaminas;

public class Tablero extends Observable{
	
	private int nivel;
	private int columnas;
	private int filas;
	private ArrayList<String> lMinas = new ArrayList<String>();
	private ArrayList<String> lCasillasVacias = new ArrayList<String>();
	private Stack<String> casillasPorVisitar = new Stack<String>();
	private ArrayList<String> lCasillasVisitadas = new ArrayList<String>();
	private Casilla[][] matriz;
	private ArrayList<String> lCasillasBandera = new ArrayList<String>();
	private int contadorCasillasDescubrir;
	
	public Tablero (int pNivel,int pFila, int pColumna){
		nivel = pNivel;
		filas = pFila-1;
		columnas = pColumna-1;
		contadorCasillasDescubrir = pFila*pColumna;
		matriz = new Casilla[pFila][pColumna];	
		}
	
	public void generarMatriz(){
		int minasAColocar = this.calcularMinas();
		int x = this.filas;
		int y = this.columnas;
		int i,j = 0;
		while(minasAColocar != 0){
			i = this.randInt(x);
			j = this.randInt(y);
			if(!((matriz[i][j]) instanceof CasillaMina)){
				matriz[i][j] = CasillaFactory.getMiFactoria().generarCasilla("Mina");
				matriz[i][j].inicializar(i+","+j);
				generarCasillasNumero(i,j);
				minasAColocar--;
			}
		}
		
		for(int k=0; k<=filas; k++){
			for(int l=0; l<=columnas;l++){
				if(matriz[k][l] == null){
					matriz[k][l] = CasillaFactory.getMiFactoria().generarCasilla("Vacia");
					matriz[k][l].inicializar(""+k+","+l);
				}
				if(!(matriz[k][l] instanceof CasillaMina)){
					anadirVecinos(k,l);
				}
			}
		}
		lMinas = minas();
		lCasillasVacias = vacias();
	}
	
	private int calcularMinas(){
		int sol = nivel*(columnas+1);
		return sol;
	}
	
	private static int randInt(int max) {
		int min = 0;
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + min);
	    return randomNum;
	}
	
	private void generarCasillasNumero(int pFila, int pColumna){
		generarH(pFila,pColumna);
		generarV(pFila,pColumna);
		generarDD(pFila,pColumna);
		generarDI(pFila,pColumna);
	}
	
	private void generarH(int pFila, int pColumna){
		if(pColumna==0){
			if (matriz[pFila][pColumna+1] == null){
				generarHP(pFila, pColumna);
			}else if(matriz[pFila][pColumna+1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila][pColumna+1])).sumarNumero();
			}
		}else if (pColumna==columnas){ 
			if (matriz[pFila][pColumna-1] == null){
				generarHN(pFila, pColumna);
			} else if(matriz[pFila][pColumna-1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila][pColumna-1])).sumarNumero();
			}
		}else {
			if (matriz[pFila][pColumna+1] == null){
				generarHP(pFila, pColumna);
			}else if(matriz[pFila][pColumna+1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila][pColumna+1])).sumarNumero();
			}
			if (matriz[pFila][pColumna-1] == null){
				generarHN(pFila, pColumna);
				}else if(matriz[pFila][pColumna-1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila][pColumna-1])).sumarNumero();
				}
		}
	}
	
	private void generarV(int pFila, int pColumna){
		if(pFila==0){
			if (matriz[pFila+1][pColumna] == null){
				generarVP(pFila, pColumna);
			}	else if(matriz[pFila+1][pColumna] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila+1][pColumna])).sumarNumero();	
			}			
		} else if(pFila==filas){
				if (matriz[pFila-1][pColumna] == null){
					generarVN(pFila, pColumna);
				}	else if(matriz[pFila-1][pColumna] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila-1][pColumna])).sumarNumero();			
				}			
		} else {
				if (matriz[pFila+1][pColumna] == null){
					generarVP(pFila, pColumna);
				}	else if(matriz[pFila+1][pColumna] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila+1][pColumna])).sumarNumero();
				}		
				if (matriz[pFila-1][pColumna] == null){
					generarVN(pFila, pColumna);
				}	else if(matriz[pFila-1][pColumna] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila-1][pColumna])).sumarNumero();	
				}	
		}
	}
	
	private void generarDD(int pFila, int pColumna){
		if(pFila==filas && pColumna != columnas){
			if(matriz[pFila-1][pColumna+1] == null){
				generarDDP(pFila, pColumna);
			} else if(matriz[pFila-1][pColumna+1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila-1][pColumna+1])).sumarNumero();
			}
		}else if (pFila==0 && pColumna != 0){
			if(matriz[pFila+1][pColumna-1] == null){
				generarDDN(pFila, pColumna);
			} else if(matriz[pFila+1][pColumna-1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila+1][pColumna-1])).sumarNumero();
				}
			}else if(pColumna == 0 && pFila !=0){
				if(matriz[pFila-1][pColumna+1] == null){
					generarDDP(pFila, pColumna);
				} else if(matriz[pFila-1][pColumna+1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila-1][pColumna+1])).sumarNumero();
				}
			} else if(pColumna == columnas && pFila!=0){
				if(matriz[pFila+1][pColumna-1] == null){
					generarDDN(pFila, pColumna);
				} else if(matriz[pFila+1][pColumna-1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila+1][pColumna-1])).sumarNumero();
					}
			}
			else if((pFila==0 && pColumna==0) || (pFila==filas && pColumna == 0) 
					|| (pFila == 0 && pColumna == columnas) || (pFila == filas && pColumna == columnas)){
				
			} else{
				if(matriz[pFila-1][pColumna+1] == null){
					generarDDP(pFila, pColumna);
				} else if(matriz[pFila-1][pColumna+1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila-1][pColumna+1])).sumarNumero();
				}
				if(matriz[pFila+1][pColumna-1] == null){
					generarDDN(pFila, pColumna);
				} else if(matriz[pFila+1][pColumna-1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila+1][pColumna-1])).sumarNumero();
					}
			}
		}
	
	
	private void generarDI(int pFila, int pColumna){
		if(pFila==0 && pColumna != columnas){
			if(matriz[pFila+1][pColumna+1] == null){
				generarDIP(pFila, pColumna);
			} else if(matriz[pFila+1][pColumna+1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila+1][pColumna+1])).sumarNumero();
			}
		}else if (pFila==filas && pColumna != 0){
			if(matriz[pFila-1][pColumna-1] == null){
				generarDIN(pFila, pColumna);
			} else if(matriz[pFila-1][pColumna-1] instanceof CasillaNumero){
				((CasillaNumero)(matriz[pFila-1][pColumna-1])).sumarNumero();
				}
			} else if(pColumna ==0 && pFila!=filas){
				if(matriz[pFila+1][pColumna+1] == null){
					generarDIP(pFila, pColumna);
				} else if(matriz[pFila+1][pColumna+1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila+1][pColumna+1])).sumarNumero();
				}
				}else if(pColumna == columnas && pFila !=0){
					if(matriz[pFila-1][pColumna-1] == null){
						generarDIN(pFila, pColumna);
					} else if(matriz[pFila-1][pColumna-1] instanceof CasillaNumero){
						((CasillaNumero)(matriz[pFila-1][pColumna-1])).sumarNumero();
						}
				}else if((pFila==0 && pColumna==0) || (pFila==filas && pColumna == 0) 
						|| (pFila == 0 && pColumna == columnas) || (pFila == filas && pColumna == columnas)){
				
				
			} else {
				if(matriz[pFila+1][pColumna+1] == null){
					generarDIP(pFila, pColumna);
				} else if(matriz[pFila+1][pColumna+1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila+1][pColumna+1])).sumarNumero();
				}
				if(matriz[pFila-1][pColumna-1] == null){
					generarDIN(pFila, pColumna);
				} else if(matriz[pFila-1][pColumna-1] instanceof CasillaNumero){
					((CasillaNumero)(matriz[pFila-1][pColumna-1])).sumarNumero();
					}
			}
	}
	
	private void generarHP(int pFila, int pColumna){
		matriz[pFila][pColumna+1] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila][pColumna+1].inicializar(""+pFila+","+(pColumna+1));
		((CasillaNumero)(matriz[pFila][pColumna+1])).sumarNumero();
	}
	
	private void generarHN(int pFila, int pColumna){
		matriz[pFila][pColumna-1] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila][pColumna-1].inicializar(""+pFila+","+(pColumna-1));
		((CasillaNumero)(matriz[pFila][pColumna-1])).sumarNumero();
	}
	
	private void generarVP(int pFila, int pColumna){
		matriz[pFila+1][pColumna] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila+1][pColumna].inicializar(""+(pFila+1)+","+pColumna);
		((CasillaNumero)(matriz[pFila+1][pColumna])).sumarNumero();
	}
	
	private void generarVN(int pFila, int pColumna){
		matriz[pFila-1][pColumna] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila-1][pColumna].inicializar(""+(pFila-1)+","+pColumna);
		((CasillaNumero)(matriz[pFila-1][pColumna])).sumarNumero();
	}
	
	private void generarDDP(int pFila, int pColumna){
		matriz[pFila-1][pColumna+1] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila-1][pColumna+1].inicializar(""+(pFila-1)+","+(pColumna+1));
		((CasillaNumero)(matriz[pFila-1][pColumna+1])).sumarNumero();
	}
	
	private void generarDDN(int pFila, int pColumna){
		matriz[pFila+1][pColumna-1] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila+1][pColumna-1].inicializar(""+(pFila+1)+","+(pColumna-1));
		((CasillaNumero)(matriz[pFila+1][pColumna-1])).sumarNumero();
	}
	
	private void generarDIP(int pFila, int pColumna){
		matriz[pFila+1][pColumna+1] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila+1][pColumna+1].inicializar(""+(pFila+1)+","+(pColumna+1));
		((CasillaNumero)(matriz[pFila+1][pColumna+1])).sumarNumero();
	}
	
	private void generarDIN(int pFila, int pColumna){
		matriz[pFila-1][pColumna-1] = CasillaFactory.getMiFactoria().generarCasilla("Numero");
		matriz[pFila-1][pColumna-1].inicializar(""+(pFila-1)+","+(pColumna-1));
		((CasillaNumero)(matriz[pFila-1][pColumna-1])).sumarNumero();
	}
	private Casilla buscarCasilla(int pFila, int pCol) {
		Casilla sol = matriz[pFila][pCol];
		return sol;
	}

	
	private void anadirVecinos(int pFila, int pCol){	
		anadirVecinosH(pFila,pCol);
		anadirVecinosV(pFila,pCol);
		anadirVecinosDD(pFila,pCol);
		anadirVecinosDI(pFila,pCol);
	}
	
	
	private void anadirVecinosH(int pFila, int pCol) {
		if(pCol != columnas && pCol != 0){
			( (matriz[pFila][pCol])).anadirVecino(pFila+","+(pCol-1));
			( (matriz[pFila][pCol])).anadirVecino(pFila+","+(pCol+1));
			} else if(pCol == 0){
				( (matriz[pFila][pCol])).anadirVecino(pFila+","+(pCol+1));
		} else if(pCol == columnas){
			( (matriz[pFila][pCol])).anadirVecino(pFila+","+(pCol-1));
		}
	}
	
	private void anadirVecinosV(int pFila, int pCol) {
		if(pFila != filas && pFila != 0){
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+pCol);
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+pCol);
			} else if(pFila == 0){
				( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+pCol);		
		} else if(pFila == filas){
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+pCol);
		}
	}
	
	private void anadirVecinosDD(int pFila, int pCol) {
		if(pFila == 0 && pCol != 0){
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+(pCol-1));
		}else if(pFila == filas && pCol != columnas){
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+(pCol+1));
		} else if(pFila != 0 && pCol == 0){
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+(pCol+1));
		} else if(pFila != filas && pCol == columnas){
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+(pCol-1));
		} else if((pFila==0 && pCol==0) || (pFila==filas && pCol == 0) || (pFila == 0 && pCol == columnas) || (pFila == filas && pCol == columnas)){
			
		} else{
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+(pCol-1));
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+(pCol+1));
		}
					
	}
	
	private void anadirVecinosDI(int pFila, int pCol) {
		if(pFila == 0 && pCol != columnas){
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+(pCol+1));
		}else if(pFila == filas && pCol != 0){
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+(pCol-1));
		} else if(pFila != filas && pCol == 0){
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+(pCol+1));
		} else if(pFila != 0 && pCol == columnas){
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+(pCol-1));
		} else if((pFila==0 && pCol==0) || (pFila==filas && pCol == 0) || (pFila == 0 && pCol == columnas) || (pFila == filas && pCol == columnas)){
			
		} else{
			( (matriz[pFila][pCol])).anadirVecino((pFila+1)+","+(pCol+1));
			( (matriz[pFila][pCol])).anadirVecino((pFila-1)+","+(pCol-1));
		}
					
	}

	public ArrayList<String> minas(){
		
		ArrayList<String> ls = new ArrayList<String>();
		
		for(int i=0; i<=filas; i++){
			for (int j=0; j<=columnas; j++){
				if(matriz[i][j] instanceof CasillaMina){
					ls.add(((CasillaMina)matriz[i][j]).obtenerCoordenadas());
				}
			}
		}
		return ls;
	}

	private ArrayList<String> vacias() {
		ArrayList<String> ls = new ArrayList<String>();
		
		for(int i=0; i<=filas; i++){
			for (int j=0; j<=columnas; j++){
				if(matriz[i][j] instanceof CasillaVacia){
					ls.add(matriz[i][j].obtenerCoordenadas());
				}
			}
		}
		return ls;
	}

	public int obtenerNumFilas() {
		return this.filas;
	}
	
	public int obtenerNumColumnas() {
		return this.columnas;
	}

	public void ponerBandera(int fila, int col) {
		boolean aux = matriz[fila][col].tieneBandera();
		matriz[fila][col].cambioBandera();
		if(aux != matriz[fila][col].tieneBandera()){
			if(matriz[fila][col].tieneBandera()){
				lCasillasBandera.add(fila+","+col);
			} else {
				if(lCasillasBandera.contains(fila+","+col)){
					lCasillasBandera.remove(fila+","+col);
				}
			}
			
			setChanged();
			notifyObservers(matriz[fila][col].tieneBandera()+",BANDERA");
		}
	}
	
	/****************************************
	 * @return lMinas.iterator();			*
	 ****************************************/
	private Iterator<String> getIteradorMinas(){
		return lMinas.iterator();
	}
	
	/****************************************
	 * @return lCasillasVacias.iterator();	*
	 ****************************************/
	private Iterator<String> getIteradorVacias(){
		return lCasillasVacias.iterator();
	}
	
	/********************************************
	 * @return lCasillasVisitadas.iterator();	*
	 ********************************************/
	private Iterator<String> getIteradorVisitadas(){
		return lCasillasVisitadas.iterator();
	}
	
	/**
	 * Muestra las minas cuando se acaba.
	 */
	public void mostrarTablero(){
		Iterator<String> itr = getIteradorMinas();
		String mina = null;
		int col;
		int fila;
		int conta=1;
		Casilla casilla;
		if (lMinas.size()>0){
			while(itr.hasNext()){
				conta++;
				mina=itr.next(); 
				col=this.separarCoordenadasCol(this.separarCoordenadasString(mina));
				fila=this.separarCoordenadasFil(this.separarCoordenadasString(mina));
				casilla=buscarCasilla(fila, col);
				if(!casilla.estaDesvelada()&&!casilla.tieneBandera()){
					casilla.descubrir();
					setChanged();
					notifyObservers(fila+","+col+","+10);
				}
			}
		}
		comprobarBanderas();
		
	}
	
	private void comprobarBanderas() {
		Iterator<String> itr = lCasillasBandera.iterator();
		String bandera = null;
		int col;
		int fila;
		Casilla casilla;
		if (lCasillasBandera.size()>0){
			while(itr.hasNext()){
				bandera=itr.next(); 
				col=this.separarCoordenadasCol(this.separarCoordenadasString(bandera));
				fila=this.separarCoordenadasFil(this.separarCoordenadasString(bandera));
				casilla=buscarCasilla(fila, col);
				if(! (casilla instanceof CasillaMina) && casilla.tieneBandera()){
					//casilla.cambioBandera();
					setChanged();
					notifyObservers(fila+","+col+","+11);	
				}
			}
		}
	}

	/************************************************************
	 * Devuelve la casilla vacia en caso de que se encuentre	*
	 * en la lista lCasillasVacias								*
	 * @param pFila												*
	 * @param pCol												*
	 * @return Casilla o null									*
	 ************************************************************/
	private String buscarCasillaVacia(int pFila, int pCol){
		Iterator<String> itr = getIteradorVacias();
		String casilla = null;
		boolean esta = false;
		
		while (itr.hasNext() && !esta){
			casilla = itr.next();
			if (estaCasilla(pFila, pCol, casilla)){
				esta = true;
			}
		}
		if(esta){
			return casilla;
		}
		else{
			return null;
		}
	}
	
	/************************************************************
	 * Si la fila y columna coinciden devuelve true si no false	*
	 * @param pFila												*
	 * @param pCol												*
	 * @param pCasilla											*
	 * @return esta												*
	 ************************************************************/
	private boolean estaCasilla(int pFila, int pCol, String pCasilla){
		String[] coord;
		int fil;
		int col;
		boolean esta = false;
		
		coord = this.separarCoordenadas(pCasilla);
		fil = this.separarCoordenadasFil(coord);
		col = this.separarCoordenadasCol(coord);
		
		if(fil == pFila && col == pCol){
			esta = true;
		}
		return esta;
	}
	
	/**
	 * @param pFila
	 * @param pCol
	 * @return
	 */
	private String buscarCasillaVisitada(int pFila, int pCol){
		Iterator<String> itr = getIteradorVisitadas();
		String casilla = null;
		boolean esta = false;
		
		while (itr.hasNext() && !esta){
			casilla = itr.next();
			if (estaCasilla(pFila, pCol, casilla)){
				esta = true;
			}
		}
		if(esta){
			return casilla;
		}
		else{
			return null;
		}
	}
	
	/**
	 * @param pFila
	 * @param pCol
	 */
	public void descubrirCasilla(int pFila, int pCol){
		Casilla casilla = this.buscarCasillaTablero(pFila, pCol);
		if(casilla instanceof CasillaMina&&!casilla.estaDesvelada()&&!casilla.tieneBandera()){
			casilla.descubrir();
			setChanged();
			notifyObservers(pFila+","+pCol+","+10);
			if(Buscaminas.getBuscaminas().getJuego()){
				Buscaminas.getBuscaminas().gameOver();
			}
		}else if(casilla instanceof CasillaNumero&&!casilla.estaDesvelada()&&!casilla.tieneBandera()){
			int num=((CasillaNumero)casilla).obtenerNumero();
			casilla.descubrir();
			contadorCasillasDescubrir--;
			setChanged();
			notifyObservers(pFila+","+pCol+","+num);
		
		}
		else{
			if(!casilla.estaDesvelada()&&!casilla.tieneBandera()){
				descubrirCasillaVacia(pFila,pCol);
			}
		}
	}
	
	private void descubrirCasillaVacia(int pFila, int pCol){
		String cadena=pFila+","+pCol;
		String actual;
		Iterator<String> itr = getIteradorVacias();
		ArrayList<String> aux = new ArrayList<String>();
		Casilla casilla;
		String[] coord;
		int f=0;
		int c=0;
		boolean finalizar=false;
		casilla = buscarCasillaTablero(pFila, pCol);
		while(itr.hasNext()&&!finalizar){
			actual=itr.next();
			if(actual.equals(cadena)&&!estaVisitada(cadena)){
				lCasillasVisitadas.add(actual);
				casilla.descubrir();
				contadorCasillasDescubrir--;
				setChanged();
				notifyObservers(pFila+","+pCol+","+0);
				aux=((CasillaVacia)casilla).devolverVecinos();
				anadirVecinosPorVisitar(aux);
				while(!casillasPorVisitar.isEmpty()){
					actual=cogeryEliminarPorVisitar();
					coord=separarCoordenadas(actual);
					f=separarCoordenadasFil(coord);
					c=separarCoordenadasCol(coord);
					descubrirCasilla(f, c);
					finalizar=true;
				}
			}
		}
	}
	
	private Casilla buscarCasillaTablero(int pFila, int pCol) {
		Casilla cas = matriz[pFila][pCol];
		return cas;
	}

	//public para las JUnit
	private boolean estaVisitada(String cadena) {
		if(lCasillasVisitadas.contains(cadena)){
			return true;
		}
		else{
			return false;
		}
	}

	private void anadirVecinosPorVisitar(ArrayList<String> pAux){
		Iterator<String> itr = pAux.iterator();
		while(itr.hasNext()){
			anadirPorVisitar(itr.next());
		}
	}
	
	
	
	/**
	 * @param pCasilla
	 */
	private void anadirVisitadas(String pCasilla){
		lCasillasVisitadas.add(pCasilla);
	}
	
	/**
	 * @param pCasilla
	 */
	private void anadirPorVisitar(String pCasilla){
		casillasPorVisitar.push(pCasilla);
	}
	
	/**
	 * @param pCasilla
	 */
	private void anadirVacia(String pCasilla){
		lCasillasVacias.add(pCasilla);
	}
	
	
	/**
	 * @param pCasilla
	 */
	private String cogeryEliminarPorVisitar(){
		return casillasPorVisitar.pop();
	}
	
	/****************************************************
	 * Coge las coordenadas de la casilla y lo separa	*
	 * metiendolo en un array de Strings -> String[]	*
	 * @param pCasilla									*
	 * @return pCasilla.obtenerCoordenadas().split(" ")	*
	 ****************************************************/
	private String[] separarCoordenadas(String pCasilla){
		return pCasilla.split(",");
	}
	
	/************************************************
	 * coge la coordenada de la col y lo pasa a int	*
	 * @param pCasilla								*
	 * @return Integer.parseInt(pCasilla[1])		*
	 ************************************************/
	private int separarCoordenadasCol(String[] pCasilla){
		return Integer.parseInt(pCasilla[1]);
	}
	
	/****************************************************
	 * coge la coordenada de la fila y lo pasa a int 	*
	 * @param pCasilla									*
	 * @return Integer.parseInt(pCasilla[0])			*
	 ****************************************************/
	private int separarCoordenadasFil(String[] pCasilla){
		return Integer.parseInt(pCasilla[0]);
	}
	
	/****************************************************
	 * separa un string									*
	 * @param pCasilla									*
	 * @return pCasilla.obtenerCoordenadas().split(" ")	*
	 ****************************************************/
	private String[] separarCoordenadasString(String pCoord){
		return pCoord.split(",");
	}
	
	//Metodos utilizados para la realizacion de las JUnit
	
	public Stack<String> getCasillasPorVisitar(){
		return casillasPorVisitar;
	}
	
	public ArrayList<String> getCasillasVacias(){
		return lCasillasVacias;
	}
	
	public ArrayList<String> getCasillasVisitadas(){
		return lCasillasVisitadas;
	}

	public boolean comprobarJuego() {
		Iterator<String> it = lCasillasBandera.iterator();
		boolean finalizado = true;
		if(getContadorCasillasDescubrir() != lMinas.size()){
			while(it.hasNext() && finalizado){
				String aux = it.next();
				if(!lMinas.contains(aux)){
					finalizado = false;
				}	
			}
		}
		
		return finalizado;
	}
	
	public int getContadorCasillasDescubrir(){
		int aux = contadorCasillasDescubrir;
		return aux;
	}

	public void descubrirTodosLosVecinos(int a, int b) {
		if(matriz[a][b] instanceof CasillaNumero && matriz[a][b].estaDesvelada()){
			int num = ((CasillaNumero)matriz[a][b]).obtenerNumero();
			ArrayList<String> lAux = matriz[a][b].obtenerVecinos();
			int contador = 0;
			Iterator<String> it = lAux.iterator();
			contador = cuantosTienenBandera(lAux);
			boolean mina = false;
			int vuelta = 1;
			if(num == contador){
				while(it.hasNext() && !mina){
					vuelta++;
					String aux = it.next();
					String[] p = separarCoordenadas(aux);
					int col = separarCoordenadasCol(p);
					int fil = separarCoordenadasFil(p);
					descubrirCasilla(fil, col);
				}
			}
		}
		
	}

	private int cuantosTienenBandera(ArrayList<String> lAux) {
		int cont = 0;
		Iterator<String> it = lAux.iterator();
		while(it.hasNext()){
			String aux = it.next();
			String[] p = separarCoordenadas(aux);
			int col = separarCoordenadasCol(p);
			int fil = separarCoordenadasFil(p);
			if(matriz[fil][col].tieneBandera()){
				cont++;
			}
		}
		return cont;
	}
	
}



