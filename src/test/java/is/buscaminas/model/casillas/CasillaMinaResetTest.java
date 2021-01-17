package is.buscaminas.model.casillas;


import is.buscaminas.model.casillas.estados.*;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CasillaMinaResetTest
{
	// Atributos para la casilla
	CasillaMinaReset casillaMinaResetTest;
	String estadoCasillaTestString;
	
	@BeforeEach
	void setUp()
	{
		casillaMinaResetTest = new CasillaMinaReset(propertyChangeEvent->estadoCasillaTestString = (String) propertyChangeEvent.getNewValue());
	}
	
	@Test
	public void testCambioDeEstado()
	{
		// Estos casos de prueba comprueban el funcionamiento de código antiguo.
		// Además están implicitos en los casos de prueba definidos para esta funcionalidad: 3.1.X.Y
		
		// La casilla está oculta
		casillaMinaResetTest.cambiarEstado(new Oculto());
		assertEquals("Oculto", estadoCasillaTestString);
		assertFalse(casillaMinaResetTest.estaDespejada());
		assertFalse(casillaMinaResetTest.estaMarcada());
		
		// La casilla está marcada
		casillaMinaResetTest.cambiarEstado(new Marcado());
		assertEquals("Marcado", estadoCasillaTestString);
		assertFalse(casillaMinaResetTest.estaDespejada());
		assertTrue(casillaMinaResetTest.estaMarcada());
		
		// La casilla está en Interrogación
		casillaMinaResetTest.cambiarEstado(new Interrogacion());
		assertEquals("Interrogacion", estadoCasillaTestString);
		assertFalse(casillaMinaResetTest.estaDespejada());
		assertFalse(casillaMinaResetTest.estaMarcada());
		
		// La casilla está despejada
		casillaMinaResetTest.cambiarEstado(new Despejado());
		assertEquals("Despejado", estadoCasillaTestString);
		assertTrue(casillaMinaResetTest.estaDespejada());
		assertTrue(casillaMinaResetTest.estaMarcada());
		
		// La casilla está Mostrada
		casillaMinaResetTest.cambiarEstado(new MinaMostrada());
		assertEquals("MinaMostrada", estadoCasillaTestString);
		assertFalse(casillaMinaResetTest.estaDespejada());
		assertTrue(casillaMinaResetTest.estaMarcada());
		
		// La casilla está en estado Marcado Incorrecto
		casillaMinaResetTest.cambiarEstado(new MarcadoIncorrecto());
		assertEquals("MarcadoIncorrecto", estadoCasillaTestString);
		
		// (en principio no se deberia dar este caso, por lo que el resuktado es irrelevante)
		assertFalse(casillaMinaResetTest.estaDespejada());
		assertFalse(casillaMinaResetTest.estaMarcada());
	}
	
	@Test
	public void testMarcarCasilla()
	{
		
		// Variables auxiliares
		Pair<Boolean, Boolean> resultadoCasilla;
		
		//////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.1.1.1
		
		// La casilla está oculta y se marca
		resultadoCasilla = casillaMinaResetTest.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Marcado", estadoCasillaTestString);
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertTrue(resultadoCasilla.getKey());
		assertTrue(resultadoCasilla.getValue());
		
		
		//////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.1.1.2
		
		// La casilla está marcada se pone en estado interrogación
		resultadoCasilla = casillaMinaResetTest.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Interrogacion", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertFalse(casillaMinaResetTest.estaMarcada());
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertTrue(resultadoCasilla.getKey());
		assertFalse(resultadoCasilla.getValue());
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.1.1.3
		
		// La casilla está en estado interrogación y se pone en estado Oculta
		resultadoCasilla = casillaMinaResetTest.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Oculto", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertFalse(casillaMinaResetTest.estaMarcada());
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertFalse(resultadoCasilla.getKey());
		assertFalse(resultadoCasilla.getValue());
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.1.1.4
		
		// Se pone la casilla en estado Despejado
		casillaMinaResetTest.cambiarEstado(new Despejado());
		
		// La casilla está en estado Despejado y se mantiene en estado Despejado
		resultadoCasilla = casillaMinaResetTest.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("Despejado", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertTrue(casillaMinaResetTest.estaMarcada());
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertFalse(resultadoCasilla.getKey());
		assertFalse(resultadoCasilla.getValue());
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.1.1.5
		
		// Se pone la casilla en estado Despejado
		casillaMinaResetTest.cambiarEstado(new MinaMostrada());
		
		// La casilla está en estado MinaMostrada y se mantiene en estado MinaMostrada
		resultadoCasilla = casillaMinaResetTest.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("MinaMostrada", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertTrue(casillaMinaResetTest.estaMarcada());
		
		// Se comprueba que el resultado devuelto es acorde a la documentación:
		// (0,X) -> No se ha modificado nada (por conveniencia, siempre devolveremos FALSE en el segundo bit del par)
		// (1,0) -> Se ha desmarcado una casilla
		// (1,1) -> Se ha marcado una casilla
		assertFalse(resultadoCasilla.getKey());
		assertFalse(resultadoCasilla.getValue());
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		// Caso de prueba 3.1.1.6
		
		// Se pone la casilla en estado MarcadoIncorrecto
		casillaMinaResetTest.cambiarEstado(new MarcadoIncorrecto());
		
		// La casilla está en estado MarcadoIncorrecto y se mantiene en estado MarcadoIncorrecto
		resultadoCasilla = casillaMinaResetTest.marcar();
		
		// Se comprueba que al observer se le indica el estado correcto.
		assertEquals("MarcadoIncorrecto", estadoCasillaTestString);
		
		// Se comprueba que el método estaMarcada localiza el estado correctamente
		assertFalse(casillaMinaResetTest.estaMarcada());
		
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
		
		// Caso de prueba 3.1.2.1: La casilla está oculta
		casillaMinaResetTest.cambiarEstado(new Oculto());
		assertEquals(6, casillaMinaResetTest.despejar());
		assertEquals("Despejado", estadoCasillaTestString);
		
		// Caso de prueba 3.1.2.2: La casilla está marcada
		casillaMinaResetTest.cambiarEstado(new Marcado());
		assertEquals(0, casillaMinaResetTest.despejar());
		assertEquals("Marcado", estadoCasillaTestString);
		
		// Caso de prueba 3.1.2.3: La casilla está en Interrogación
		casillaMinaResetTest.cambiarEstado(new Interrogacion());
		assertEquals(6, casillaMinaResetTest.despejar());
		assertEquals("Despejado", estadoCasillaTestString);
		
		// Caso de prueba 3.1.2.4: La casilla está despejada
		casillaMinaResetTest.cambiarEstado(new Despejado());
		assertEquals(0, casillaMinaResetTest.despejar());
		assertEquals("Despejado", estadoCasillaTestString);
		
		// Caso de prueba 3.1.2.5: La casilla está Mostrada
		casillaMinaResetTest.cambiarEstado(new MinaMostrada());
		assertEquals(0, casillaMinaResetTest.despejar());
		assertEquals("MinaMostrada", estadoCasillaTestString);
		
		// Caso de prueba 3.1.2.6: La casilla está en estado Marcado Incorrecto
		// (en principio no se deberia dar este caso, por lo que el resuktado es irrelevante)
		casillaMinaResetTest.cambiarEstado(new MarcadoIncorrecto());
		assertEquals(0, casillaMinaResetTest.despejar());
		assertEquals("MarcadoIncorrecto", estadoCasillaTestString);
	}
	
	@Test
	public void testConversionCasillaTempACasillaMina50()
	{
		// Este caso de prueba comprueba el funcionamiento de código antiguo.
		// No debería cambiar pues no ha sido afectado.
		
		// Se crea una casilla temp
		estadoCasillaTestString = null;
		CasillaTemp casillaTemp = new CasillaTemp(propertyChangeEvent->estadoCasillaTestString = (String) propertyChangeEvent.getNewValue());
		assertEquals("Oculto", estadoCasillaTestString);
		
		
		// Se transforma en una casilla MinaReset
		CasillaMinaReset casillaMinaResetTest2 = new CasillaMinaReset(casillaTemp);
		assertEquals("Oculto", estadoCasillaTestString);
		
		// Se comprueba que el observer se haya traspasado correctamente:
		casillaMinaResetTest2.cambiarEstado(new Interrogacion());
		assertEquals("Interrogacion", estadoCasillaTestString);
		
		// Se comprueba que despejar da un valor correcto:
		assertEquals(6, casillaMinaResetTest.despejar());
	}
}