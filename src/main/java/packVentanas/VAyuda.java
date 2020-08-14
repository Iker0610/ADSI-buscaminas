package packVentanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class VAyuda extends JFrame {

	private JPanel contentPane;

	/**	
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VAyuda frame = new VAyuda();
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
	public VAyuda() {
		Image icon = new ImageIcon(getClass().getResource("/icono.png")).getImage();
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new MigLayout("", "[368.00]", "[][][][]"));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("AYUDA");
		contentPane.add(lblNewLabel, "cell 0 0,alignx center");
		
		JTextArea textArea = new JTextArea();
		textArea.setText("El juego consiste en despejar todas las casillas de \n"
				+ "una pantalla que no oculten una mina.\n"+
		"Algunas casillas tienen un n�mero, el cual indica la\n"
		+ "cantidad de minas que hay en las casillas\n"
		+ "circundantes.\n"+
				"As�, si una casilla tiene el n�mero 3, significa que de\n"
				+ "las ocho casillas que hay alrededor (si no es en una\n"
				+ "esquina o borde) hay 3 con minas y 5 sin minas.\n"
				+ "Si se descubre una casilla sin n�mero indica que\n"
				+ "ninguna de las casillas vecinas tiene mina y\n"
				+ "�stas se descubren autom�ticamente.\n"
				+ "Si se descubre una casilla con una mina se pierde\n"
				+ "la partida.\n"
				+ "Se puede poner una marca en las casillas que el\n"
				+ "jugador piensa que hay minas para ayudar a\n"
				+ "descubrir las que est�n cerca.");
		textArea.setFont(new Font("Roboto Slab", Font.BOLD, 14));
		textArea.setEditable(false);
		contentPane.add(textArea, "cell 0 1");
		
		JLabel l1 = new JLabel("");
		contentPane.add(l1, "cell 0 3,alignx center");
		l1.setIcon(new ImageIcon(VBuscaminas.class.getResource("/Ayuda.png")));
	}

}

