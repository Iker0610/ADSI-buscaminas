package packCodigo;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriteRanking {
	
	private static XMLWriteRanking mXMLWriter;;
	
	private XMLWriteRanking(){
	}
	
	public static XMLWriteRanking getXMLWriteRanking(){
		if (mXMLWriter == null){
			mXMLWriter = new XMLWriteRanking();
		}
		return mXMLWriter;
	}
	
	public void cargar() {
		 
		  try {
	 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
	 
			
			for (int i=0;i<Ranking.getRanking().longLista();i++){
				// Puntuacion
				Element staff = doc.createElement("Nombre");
				System.out.println("El usuario: "+Ranking.getRanking().conseguirJugadorPos(i).obtenerNombre());
				staff.appendChild(doc.createTextNode(Ranking.getRanking().conseguirJugadorPos(i).obtenerNombre()));
				rootElement.appendChild(staff);

				// firstname elements
				Element firstname = doc.createElement("Puntuacion");
				System.out.println("Tiene la puntuacion: "+Ranking.getRanking().conseguirJugadorPos(i).obtenerPunt());
				firstname.appendChild(doc.createTextNode(Ranking.getRanking().conseguirJugadorPos(i).obtenerPunt()+""));
				rootElement.appendChild(firstname);
		 
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("ficheros/Ranking.xml")); 
			transformer.transform(source, result);
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		}
	}

