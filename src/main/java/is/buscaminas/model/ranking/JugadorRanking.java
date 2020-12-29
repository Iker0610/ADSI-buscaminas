package is.buscaminas.model.ranking;

public class JugadorRanking implements Comparable<JugadorRanking> {

    // Atributos
    private final String nombre;
    private final int puntuacion;

    // Constructora
    public JugadorRanking (String pNombre, int pPuntuacion)
    {
        nombre = pNombre;
        puntuacion = pPuntuacion;
    }

    // Getters
    public String getNombre ()
    {
        return nombre;
    }

    public int getPuntuacion ()
    {
        return puntuacion;
    }

    // Métodos
    @Override
    public int compareTo (JugadorRanking pJugador)
    {
        Integer puntuacionJugador = pJugador.puntuacion;
        return ((Integer) puntuacion).compareTo(puntuacionJugador);
    }
}
