package is.buscaminas.model.ranking;

import is.buscaminas.model.Contador;
import is.buscaminas.model.ranking.estructurasDatos.OrderedDoubleLinkedList;
import is.buscaminas.view.uiElements.VistaRanking;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Ranking {

    // Atributos
    private static Ranking mRanking;
    private final OrderedDoubleLinkedList<JugadorRanking>[] lJugadoresPorDificultad;
    private final PropertyChangeSupport lObservers; //lista de observers
    private VistaRanking gridPane;

    // Constructora
    private Ranking ()
    {
        lJugadoresPorDificultad = new OrderedDoubleLinkedList[3];
        for (int i = 0; i < lJugadoresPorDificultad.length; i++) lJugadoresPorDificultad[i] = new OrderedDoubleLinkedList<>();
        lObservers = new PropertyChangeSupport(this);
        cargarRanking();
    }

    // Singleton
    public static Ranking getRanking ()
    {
        if (mRanking == null) mRanking = new Ranking();
        return mRanking;
    }

    // Métodos
    public void addObserver (PropertyChangeListener pObserver)
    {
        //Pre: Un observer
        //Post: Se ha añadido el observer a la lista de observers
        lObservers.addPropertyChangeListener(pObserver);
    }

    private void cargarRanking ()
    {
        String linea;
        int dificultadAct = 1;   //Leeremos 3 ficheros (0, 1 y 2)
        while (dificultadAct <= 3) {
            try {

                File archivoRanking = new File("src/main/resources/is/buscaminas/rankings/ranking" + dificultadAct + ".tsv");

                FileReader fr = new FileReader(archivoRanking);
                BufferedReader br = new BufferedReader(fr);

                // Mientras queden usuarios por leer en el fichero
                while ((linea = br.readLine()) != null) {
                    //Obtenemos la información de cada jugador y la añadimos a la lista correspondiente
                    String[] datosJugador = linea.split("\t");
                    JugadorRanking jugador = new JugadorRanking(datosJugador[0], Integer.parseInt(datosJugador[1]));
                    lJugadoresPorDificultad[dificultadAct - 1].add(jugador);
                }
                //Cerramos el fichero
                fr.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            dificultadAct++;
        }
    }

    private void actualizarRanking (int pDificultad)
    {
        Object[] top10 = lJugadoresPorDificultad[pDificultad - 1].getTop10();
        //Escribir en el fichero
        try {
            File archivoRanking = new File("src/main/resources/is/buscaminas/rankings/ranking" + pDificultad + ".tsv");
            FileWriter fileWritter = new FileWriter(archivoRanking, false);

            for (Object elem : top10) {
                if (elem instanceof JugadorRanking) {
                    JugadorRanking jugador = (JugadorRanking) elem;
                    fileWritter.write(jugador.getNombre() + "\t" + jugador.getPuntuacion() + "\n");
                }
            }

            fileWritter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // notificamos a los observers de que hay que mostrar el ranking de otro nivel
    }

    public void addJugadorRanking (int pDificultad, String pNombre)
    {
        int puntuacionJugador = calcularPuntuacion(pDificultad);
        System.out.println("PUNTUACION " + puntuacionJugador);
        JugadorRanking jugador = new JugadorRanking(pNombre, puntuacionJugador);
        lJugadoresPorDificultad[pDificultad - 1].add(jugador);
        actualizarRanking(pDificultad);
    }

    public void obtenerRanking (int pDificultad)
    {
        // Devuelve los **10 primeros jugadores (si los hay)**

        JugadorRanking[] lista = new JugadorRanking[10];
        int i = 0;
        for (Object elem : lJugadoresPorDificultad[pDificultad - 1].getTop10()) {
            if (elem instanceof JugadorRanking) {
                lista[i++] = (JugadorRanking) elem;
            }
        }
        lObservers.firePropertyChange("lRanking", null, lista);
    }

    private int calcularPuntuacion (int pDificultad)
    {
        return ((pDificultad * 2000) / Contador.getContador().getSeconds());
    }
}