package packVentanas;

import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import packCodigo.Ranking;

import javax.swing.JTextArea;
import javax.swing.JLabel;

public class VRanking extends JFrame {

	private JPanel contentPane;
	JTextArea textArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VRanking frame = new VRanking();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}

	/**
	 * Create the frame.
	 */
	public VRanking() {
		Image icon = new ImageIcon(getClass().getResource("/icono.png")).getImage();
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[78.00][249.00][81.00]", "[][grow]"));
		
		JLabel lblRanking = new JLabel("Ranking");
		contentPane.add(lblRanking, "cell 1 0,alignx center,aligny center");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		contentPane.add(textArea, "cell 1 1,grow");
		addPers();
	}

	private void addPers(){
		
		textArea.setText("");
		ArrayList<String> l = Ranking.getRanking().obtenerRanking();
		Iterator<String> it = l.iterator();
		String nombreyPuntJ;
		while(it.hasNext()){
			nombreyPuntJ=it.next();
			textArea.append(nombreyPuntJ+"\n");
		}

	}
}

