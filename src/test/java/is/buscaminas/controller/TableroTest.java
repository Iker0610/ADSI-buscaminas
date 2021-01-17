package is.buscaminas.controller;


import is.buscaminas.model.Contador;
import is.buscaminas.model.casillas.Casilla;
import is.buscaminas.model.casillas.CasillaMina;
import is.buscaminas.model.casillas.CasillaMina50;
import is.buscaminas.model.casillas.CasillaMinaReset;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class TableroTest
{
	// IMPORTANTE ANTES DE EJECUTAR:
	// - COMMENTAR LA LINEA 51 DE LA CLASE PARTIDA ANTES DE HACER LOS DISTINTOS TEST PARA EVITAR LA ACTIVACIÓN DE LA INTERFAZ
	// - Poner el atributo matrizCasillas de casilla a publico para hacer el test. De lo contrario es IMPOSIBLE testear de forma automática,
	// 	debido a la aleatoriedad intrinseca de la generación del tablero, lo que hace imposible saber que minas se han generado y la posición de cada una.
	
	// Atributos Aux
	private static final int nivel = 2;
	private static int numCasillasMinasPorMarcar;
	private static int numFilas;
	private static int numColumnas;
	private static int numMinasTotal;
	private static Listener[][] matrizListener;
	
	@BeforeEach
	void setUp() throws SQLException
	{
		// Se obtienen los datos del nivek
		ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("SELECT * FROM nivel WHERE nivel =" + nivel);
		if (resultado.next()){
			numFilas = resultado.getInt("nFilas");
			numColumnas = resultado.getInt("nColumnas");
			numMinasTotal = resultado.getInt("dificultad");
		}
		else throw new SQLException();
		
		// Se crea la matriz de listeners
		matrizListener = new Listener[numFilas][numColumnas];
		for (int f = 0; f < numFilas; f++)
			for (int c = 0; c < numColumnas; c++)
				matrizListener[f][c] = new Listener();
		
		// Se inicia una Partida
		Partida.getPartida().iniciarPartida(nivel);
		
		// Se crean las casillas en el tablero
		Tablero.getTablero().addObserver(propertyChangeEvent->numCasillasMinasPorMarcar = (int) propertyChangeEvent.getNewValue());
		assertEquals(numMinasTotal, numCasillasMinasPorMarcar);
		
		// Se crea una lista de listeners para el tablero
		Tablero.getTablero().generarCasillasTablero(0, 0, matrizListener);
		Contador.getContador().iniciar();
	}
	
	@Test
	void generarCasillasTablero()
	{
		// El tablero se ha generado ya en el setUp
		//	Siendo la primera casilla maracada la (0,0)
		
		// Se comprueba el número de minas
		int numMinasGeneradas = 0;
		int numMinas50Generadas = 0;
		int numMinasResetGeneradas = 0;
		
		for (Casilla[] columnaCasilla : Tablero.getTablero().matrizCasillas){
			for (Casilla casilla : columnaCasilla){
				if (casilla instanceof CasillaMina50) numMinas50Generadas++;
				if (casilla instanceof CasillaMinaReset) numMinasResetGeneradas++;
				if (casilla instanceof CasillaMina) numMinasGeneradas++;
			}
		}
		
		
		assertEquals(numMinasTotal, numMinasGeneradas);
		assertEquals(1, numMinas50Generadas);
		assertEquals(1, numMinasResetGeneradas);
	}
	
	@Test
	void despejarCasillaMina50()
	{
		CasillaMina50 mina50 = null;
		int columnaMina50 = -1, filaMina50 = -1;
		int f = 0, c = 0;
		
		while (f < numFilas && mina50 == null){
			while (c < numColumnas && mina50 == null){
				if (Tablero.getTablero().matrizCasillas[f][c] instanceof CasillaMina50){
					mina50 = (CasillaMina50) Tablero.getTablero().matrizCasillas[f][c];
					columnaMina50 = c;
					filaMina50 = f;
				}
				else if (++c == numColumnas){
					c = 0;
					f++;
				}
			}
		}
		assertNotNull(mina50);
		assertNotEquals(-1, filaMina50);
		assertNotEquals(-1, columnaMina50);
		
		// Se despeja la mina:
		Tablero.getTablero().despejarCasilla(filaMina50, columnaMina50);
		
		// Se comprueba que se han mostrado el número correcto de minas
		int numMinasMostradasCorrecto = (numMinasTotal - 1) / 2;
		int numMinasMostradas = 0;
		for (Listener[] columnalistener : matrizListener)
			for (Listener listener : columnalistener)
				if (listener.estadoCasilla.equals("MinaMostrada")) numMinasMostradas++;
		
		assertEquals(numMinasMostradasCorrecto, numMinasMostradas);
	}
	
	
	@Test
	void despejarCasillaMinaReset() throws InterruptedException
	{
		CasillaMinaReset minaReset = null;
		int columnaMinaReset = -1, filaMinaReset = -1;
		int f = 0, c = 0;
		
		while (f < numFilas && minaReset == null){
			while (c < numColumnas && minaReset == null){
				if (Tablero.getTablero().matrizCasillas[f][c] instanceof CasillaMinaReset){
					minaReset = (CasillaMinaReset) Tablero.getTablero().matrizCasillas[f][c];
					columnaMinaReset = c;
					filaMinaReset = f;
				}
				else if (++c == numColumnas){
					c = 0;
					f++;
				}
			}
		}
		assertNotNull(minaReset);
		assertNotEquals(-1, filaMinaReset);
		assertNotEquals(-1, columnaMinaReset);
		
		// Añadimos un observer para comprobar que el estado de la partida nunca se resetea.
		// Si partida se resetea y avisa a los observers, la prueba fracasa.
		ChivatoFinPartida chivatoFinPartida = new ChivatoFinPartida();
		Partida.getPartida().addObserver(chivatoFinPartida);
		
		// Se obtiene el tiempo del contador antes de despejar para comprobar el contador
		Thread.sleep(5000); // Nos aseguramos de que no sea 0 para mejorar el test.
		int tiempoInicialContador = Contador.getContador().getSeconds();
		
		// Se despeja la mina:
		Tablero.getTablero().despejarCasilla(filaMinaReset, columnaMinaReset);
		
		// Se comprueba que todas las minas se han reseteado = tablero a null
		for (Casilla[] columnaCasilla : Tablero.getTablero().matrizCasillas)
			for (Casilla casilla : columnaCasilla) assertNull(casilla);
		
		//Se espera a que el tiempo del contador avance
		Thread.sleep(1500);
		
		// Se comprueba que el contador no se haya reseteado
		int tiempoFinalContador = Contador.getContador().getSeconds();
		assertNotEquals(0, tiempoFinalContador);
		assertNotEquals(tiempoInicialContador, tiempoFinalContador);
		assertTrue(tiempoInicialContador < tiempoFinalContador);
		
		// Se desactiva el chivato de la clase Partida para evitar que otros test lo activen.
		chivatoFinPartida.desactivarChivato();
		
		// Se comprueba que ahya partida activa
		assertTrue(Partida.getPartida().hayPartidaActiva());
	}
	
	
	// Clase listener de ayuda para generar el tablero
	private static class Listener implements PropertyChangeListener
	{
		public String estadoCasilla;
		
		@Override
		public void propertyChange(PropertyChangeEvent propertyChangeEvent)
		{
			estadoCasilla = (String) propertyChangeEvent.getNewValue();
		}
	}
	
	// Clase Listener para ver si la partida se resetea o no.
	private static class ChivatoFinPartida implements PropertyChangeListener
	{
		// Atributos
		private boolean activo = true;
		
		public void desactivarChivato()
		{
			this.activo = false;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent propertyChangeEvent)
		{
			if (activo)fail();
		}
	}
}