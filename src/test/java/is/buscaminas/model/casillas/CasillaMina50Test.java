package is.buscaminas.model.casillas;


import is.buscaminas.model.casillas.estados.*;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CasillaMina50Test
{
	// Atributos para la casilla
	CasillaMina50 casillaMina50Test;
	String estadoCasillaTestString;
	
	@BeforeEach
	void setUp()
	{
		casillaMina50Test = new CasillaMina50(propertyChangeEvent->estadoCasillaTestString = (String) propertyChangeEvent.getNewValue());
	}
	
	@Test
	public void testCambioDeEstado()
	{
		// Caso de prueba
		
		// La casilla está oculta
		casillaMina50Test.cambiarEstado(new Oculto());
		assertEquals("Oculto", estadoCasillaTestString);
		assertFalse(casillaMina50Test.estaDespejada());
		assertFalse(casillaMina50Test.estaMarcada());
		
		// La casilla está marcada
		casillaMina50Test.cambiarEstado(new Marcado());
		assertEquals("Marcado", estadoCasillaTestString);
		assertFalse(casillaMina50Test.estaDespejada());
		assertTrue(casillaMina50Test.estaMarcada());
		
		// La casilla está en Interrogación
		casillaMina50Test.cambiarEstado(new Interrogacion());
		assertEquals("Interrogacion", estadoCasillaTestString);
		assertFalse(casillaMina50Test.estaDespejada());
		assertFalse(casillaMina50Test.estaMarcada());
		
		// La casilla está despejada
		casillaMina50Test.cambiarEstado(new Despejado());
		assertEquals("Despejado", estadoCasillaTestString);
		assertTrue(casillaMina50Test.estaDespejada());
		assertTrue(casillaMina50Test.estaMarcada());
		
		// La casilla está Mostrada
		casillaMina50Test.cambiarEstado(new MinaMostrada());
		assertEquals("MinaMostrada", estadoCasillaTestString);
		assertFalse(casillaMina50Test.estaDespejada());
		assertTrue(casillaMina50Test.estaMarcada());
		
		// La casilla está en estado Marcado Incorrecto
		casillaMina50Test.cambiarEstado(new MarcadoIncorrecto());
		assertEquals("MarcadoIncorrecto", estadoCasillaTestString);
		
		// (en principio no se deberia dar este caso, por lo que el resuktado es irrelevante)
		assertFalse(casillaMina50Test.estaDespejada());
		assertFalse(casillaMina50Test.estaMarcada());
	}
	
	@Test
	public void testMarcarCasilla()
	{
		
		// Variables auxiliares
		Pair<Boolean, Boolean> resultadoCasilla;
		
		//////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.2.1.1
		
		// La casilla está oculta y se marca
		resultadoCasilla = casillaMina50Test.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Marcado", estadoCasillaTestString);
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertTrue(resultadoCasilla.getKey());
		assertTrue(resultadoCasilla.getValue());
		
		//////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.2.2.1
		
		// La casilla está marcada se pone en modo interrogación
		resultadoCasilla = casillaMina50Test.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Interrogacion", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertFalse(casillaMina50Test.estaMarcada());
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertTrue(resultadoCasilla.getKey());
		assertFalse(resultadoCasilla.getValue());
		
		////////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.2.2.1
		
		// La casilla está en modo interrogación y se pone en modo Oculta
		resultadoCasilla = casillaMina50Test.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Oculto", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertFalse(casillaMina50Test.estaMarcada());
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertFalse(resultadoCasilla.getKey());
		assertFalse(resultadoCasilla.getValue());
	}
	
	@Test
	public void testDespejar()
	{
		// Caso de prueba
		
		// El resultado del método despejar se tiene que corresponder con estas especificaciones:
		/*  0 = no hacer absolutamente nada
		 *  1 = Únicamente disminuir el contador 'casillasPorDespejar'
		 *  2 = Se ha hecho click en mina, fin de la partida
		 *  3 = casilla sin minas adyacentes -> Despejar las de alrededor y decrementar el contador 'casillasPorDespejar'
		 *  4 = Despejar las casillas que estén inmediatamente adyacentes a la casilla despejada en caso de que el numero de minas adyacentes que tenga
		 *      sea exactamente el número de casillas marcadas en las 8 celdas inmediatamente adyacentes (no necesariamente tienen que estar bien marcadas)
		 *  5 = Se desvela la posición la mitad de las minas que aún no estén marcadas.
		 *  6 = Se resetea el tablero (no la partida, por tanto tampoco el contador)
		 */
		
		// La casilla está oculta
		casillaMina50Test.cambiarEstado(new Oculto());
		assertEquals(5, casillaMina50Test.despejar());
		assertEquals("Despejado", estadoCasillaTestString);
		
		// La casilla está marcada
		casillaMina50Test.cambiarEstado(new Marcado());
		assertEquals(0, casillaMina50Test.despejar());
		assertEquals("Marcado", estadoCasillaTestString);
		
		// La casilla está en Interrogación
		casillaMina50Test.cambiarEstado(new Interrogacion());
		assertEquals(5, casillaMina50Test.despejar());
		assertEquals("Despejado", estadoCasillaTestString);
		
		// La casilla está despejada
		casillaMina50Test.cambiarEstado(new Despejado());
		assertEquals(0, casillaMina50Test.despejar());
		assertEquals("Despejado", estadoCasillaTestString);
		
		// La casilla está Mostrada
		casillaMina50Test.cambiarEstado(new MinaMostrada());
		assertEquals(0, casillaMina50Test.despejar());
		assertEquals("MinaMostrada", estadoCasillaTestString);
		
		// La casilla está en estado Marcado Incorrecto
		// (en principio no se deberia dar este caso, por lo que el resuktado es irrelevante)
		casillaMina50Test.cambiarEstado(new MarcadoIncorrecto());
		assertEquals(0, casillaMina50Test.despejar());
		assertEquals("MarcadoIncorrecto", estadoCasillaTestString);
	}
	
	@Test
	public void testConversionCasillaTempACasillaMina50()
	{
		// Caso de prueba
		
		// Se crea una casilla temp
		estadoCasillaTestString = null;
		CasillaTemp casillaTemp = new CasillaTemp(propertyChangeEvent->estadoCasillaTestString = (String) propertyChangeEvent.getNewValue());
		assertEquals("Oculto", estadoCasillaTestString);
		
		
		// Se transforma en una casilla Mina50
		CasillaMina50 casillaMina50Test2 = new CasillaMina50(casillaTemp);
		assertEquals("Oculto", estadoCasillaTestString);
		
		// Se comprueba que el observer se haya traspasado correctamente:
		casillaMina50Test2.cambiarEstado(new Interrogacion());
		assertEquals("Interrogacion", estadoCasillaTestString);
		
		// Se comprueba que despejar da un valor correcto:
		assertEquals(5, casillaMina50Test.despejar());
	}
}