package packCodigo;

import java.util.ArrayList;
import java.util.Iterator;


public class Ranking {
	private static Ranking mRanking = new Ranking();
	private ArrayList<Jugador> lRanking;
	
	private Ranking(){
		lRanking = new ArrayList<Jugador>();
	}
	
	public static Ranking getRanking(){
		return mRanking;
	}
	
	public void cargarLista(){
		try {
			XMLParserRanking.getPDF2XMLParser().parseXmlFile("ficheros/Ranking.xml");
		} catch (XmlParsingException e) {
			e.printStackTrace();
		}
	}
	
	public void guardarLista(){
		XMLWriteRanking.getXMLWriteRanking().cargar();
	}
	
	public void anadirLista(Jugador pJ){
		
		lRanking.add(pJ);
	}
	
	private void ordenarLista(){
		QuickSort q = new QuickSort(this.lRanking);
		this.lRanking = q.getOrdenada();
	}
	
	public boolean estaEnRanking(String nJ){
		boolean esta = false;
		Iterator<Jugador> it = lRanking.iterator();
		while (it.hasNext()&&!esta){
			Jugador aux = it.next();
			if(aux.obtenerNombre().equals(nJ)){
				esta = true;
			}
		}
		return esta;
	}
	
	private Iterator<Jugador> getIteradorJugador(){
		return lRanking.iterator();
	}
	
	public ArrayList<String> obtenerRanking(){		
		ordenarLista();
		ArrayList<String> l = new ArrayList<String>();
		int cont = 0;
		Iterator<Jugador> itr = getIteradorJugador();
		Jugador j;
		String n;
		while(cont<10&&cont<lRanking.size()&&itr.hasNext()){
			j=itr.next();
			n=" "+j.obtenerNombre()+"			"+j.obtenerPunt()+" ";
			l.add(n);
			cont++;
		}
		return l;
	}
	
	public void buscarJugador(String pNombre){
		Iterator<Jugador> itr = getIteradorJugador();
		Jugador j;
		while (itr.hasNext()){
			j=itr.next();
			if(pNombre.equals(j.obtenerNombre())){
				j.establecerPuntuacion(Buscaminas.getBuscaminas().obtenerPuntuacion());
			}
				
		}
	}
	
	public Jugador obtJugador(String pNombre){
		Iterator<Jugador> itr = getIteradorJugador();
		Jugador j=null;
		boolean enc = false;
		while (itr.hasNext()&&!enc){
			j=itr.next();
			if(pNombre.equals(j.obtenerNombre())){
				enc=true;
			}	
		}
			return j;
	}
	
	public Jugador conseguirJugadorPos(int i){
		return lRanking.get(i);
	}
	
	public int longLista(){
		return lRanking.size();
	}
	//METODOS USADOS PARA LAS JUNITS
		public void reset(){
			lRanking = new ArrayList<Jugador>();
		}
}

