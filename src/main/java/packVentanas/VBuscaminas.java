package packVentanas;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import packCodigo.Buscaminas;
import packCodigo.NoArchivoAudioException;
import packCodigo.Ranking;
import packCodigo.Tablero;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

@SuppressWarnings({ "serial", "deprecation" })
public class VBuscaminas extends JFrame implements ActionListener, Observer{

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu1, menu2;
	private JMenuItem item1, item2, item3;
	private JPanel panel_2;
	private JLabel lblNewLabel;
	private JLabel[] Banderas = new JLabel[3];
	private JLabel[] Tiempo = new JLabel[3];
	private JPanel panel;
	private int fil;
	private int col;
	private JLabel[] lcasillas;
	private VBuscaminas vBusca = this;
	private Boolean juego = true;
	private Boolean finalizado = false;
	private Clip clip;
	private AudioInputStream ais;
	private int bomba = 0;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VBuscaminas frame = new VBuscaminas(2);
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
	public VBuscaminas(int nivel) {
		Image icon = new ImageIcon(getClass().getResource("/icono.png")).getImage();
		setIconImage(icon);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(nivel == 1){
			setBounds(100, 100, 500, 450);
		}else if(nivel == 2){
			setBounds(100, 100, 730, 600);
		}else if(nivel == 3){
			setBounds(100, 100, 1150, 710);
		}
		setTitle("Buscaminas");
		setResizable(false); 
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu1 = new JMenu("Juego");
		menuBar.add(menu1);
		
		menu2 = new JMenu("Ayuda");
		menuBar.add(menu2);
		
		item1 = new JMenuItem("Nuevo");
		item1.addActionListener(this);
		menu1.add(item1);
		
		
		item2 = new JMenuItem("Ver");
		item2.addActionListener(this);
		menu2.add(item2);
		
		item3 = new JMenuItem("Ranking");
		item3.addActionListener(this);
		menu1.add(item3);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new MigLayout("", "[200.00]", "[40.00][204.00]"));
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.LIGHT_GRAY);
		contentPane.add(panel_2, "cell 0 0,grow");
	
		panel_2.setLayout(new MigLayout("", "[20.00][20.00][17.00][][20][][]", "[]"));
		
		for(int i=0; i<3; i++){
			JLabel j1 = new JLabel();
			Banderas[i] = j1;
			panel_2.add(j1, "cell "+i+" 0, grow");
			j1.setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		lblNewLabel = new JLabel();
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBackground(new Color(255, 255, 0));
		panel_2.add(lblNewLabel, "cell 3 0,growx");
		lblNewLabel.setIcon(new ImageIcon(VBuscaminas.class.getResource("/Reset.png")));
		
		
		lblNewLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				Buscaminas.getBuscaminas().reset(vBusca);
				lblNewLabel.setIcon(new ImageIcon(VBuscaminas.class.getResource("/Reset.png")));
			}
		});
		
		for(int i=4; i<7; i++){
			JLabel j1 = new JLabel();
			Tiempo[i-4] = j1;
			panel_2.add(j1, "cell "+i+" 0, grow");
			j1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		
		
		panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		contentPane.add(panel, "cell 0 1,grow");
		
		iniciarCasillas(nivel);
		Buscaminas.getBuscaminas().inicioJuego(nivel);
		Buscaminas.getBuscaminas().anadirObservador(this);
		fil=Buscaminas.getBuscaminas().obtenerNumFilas();
		col=Buscaminas.getBuscaminas().obtenerNumColumnas();
		mostrarTablero();
		anadirCasillas();
	}


	
	private void iniciarCasillas(int pNivel) {
		if(pNivel == 1){
			lcasillas = new JLabel[70];
		}else if(pNivel == 2){
			lcasillas = new JLabel[150];
		}else if(pNivel == 3){
			lcasillas = new JLabel[300];
		}
		
	}

	private void mostrarTablero(){
		
		String SFila = "";
		String SCol = "";
		for(int i=0;i<=fil;i++){
			SFila=SFila+"[]";
			for(int j=0;j<=col;j++){
				SCol=SCol+"[]";
			}
		}
		panel.setLayout(new MigLayout("", SCol, SFila));
	}
	
	public void anadirCasillas(){
		String f="";
		String c="";
		int cont=0;
		for(int i=0; i<=col; i++){
			f= Integer.toString(i);
			for(int j=0; j<=fil; j++){
				c= Integer.toString(j);
				JLabel l1 = new JLabel();
				lcasillas[cont]=l1;
				cont++;
				l1.setFont(new Font("Tahoma", Font.PLAIN, 11));
				l1.setHorizontalAlignment(SwingConstants.CENTER);
				l1.setBackground(new Color(255, 255, 255));
				panel.add(l1, "cell"+f+" "+c);
								
				l1.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e){
						 if (e.getButton() == MouseEvent.BUTTON3 && juego && !finalizado) {
							 int a;
							 int b;
							 a=getx(buscarPosCasilla((JLabel)e.getSource()));
							 b=gety(buscarPosCasilla((JLabel)e.getSource()));
		                     Buscaminas.getBuscaminas().ponerBandera(a,b);
		                     Buscaminas.getBuscaminas().comprobarJuego();
		                  }
						 else if(e.getButton() == MouseEvent.BUTTON1 && juego && !finalizado){
							 int a;
							 int b;
							 a=getx(buscarPosCasilla((JLabel)e.getSource()));
							 b=gety(buscarPosCasilla((JLabel)e.getSource()));
							 Buscaminas.getBuscaminas().descubrirCasilla(a,b);
		                     Buscaminas.getBuscaminas().comprobarJuego();
					} else
						if(e.getButton() == MouseEvent.BUTTON2 && juego && !finalizado){
							int a;
							int b;
							a=getx(buscarPosCasilla((JLabel)e.getSource()));
							b=gety(buscarPosCasilla((JLabel)e.getSource()));
							Buscaminas.getBuscaminas().descubrirTodosLosVecinos(a,b);
							Buscaminas.getBuscaminas().comprobarJuego();
					}
				}
					});
				l1.setIcon(new ImageIcon(VBuscaminas.class.getResource("/Casilla.png")));
			}
		}
	}
	
	private int gety(int pPos) {
		return (pPos/(fil+1));

	}

	private int getx(int pPos) {
		if(pPos>10){
				return pPos%(fil+1);
			}
			else{
				return (pPos%(fil+1));
			}
	}

	private int buscarPosCasilla(JLabel source) {
		int pos=0;
		while(lcasillas[pos]!=source){
			pos++;
		}
		return pos;
	}

	public void update(Observable o, Object arg) {
		String[]p = arg.toString().split(",");
		if(o instanceof Buscaminas){ 
			   if(p.length==2){
				   if(p[1]!=null){
					   int aux;
					   int num = Integer.parseInt(p[1]);
					   for(int i=2; i>=0; i--){
						   aux = num%10;
						   num = num/10;
							Banderas[i].setIcon(new ImageIcon(VBuscaminas.class.getResource("/Crono"+aux+".png")));			
						}
				   }
				   if(p[0]!=null){
					   int aux;
					   int num = Integer.parseInt(p[0]);
					   for(int i=2; i>=0; i--){
						   aux = num%10;
						   num = num/10;
							Tiempo[i].setIcon(new ImageIcon(VBuscaminas.class.getResource("/Crono"+aux+".png")));			
						}
				   }
			   }else if(arg instanceof Boolean){
				   if(arg.toString().equals("false")){
					   juego = false;
					   try {
						   play(juego);
					   } catch (NoArchivoAudioException e) {
						   e.printStackTrace();
					   }
					   lblNewLabel.setIcon(new ImageIcon(VBuscaminas.class.getResource("/Perder.png")));
					   JOptionPane.showMessageDialog(null, "OOOHHHHH QUE PENA, HAS ENCONTRADO UNA MINA!!!");
					   Ranking.getRanking().guardarLista();
				   }
				   else {
					   juego = true;
					   finalizado = false;
					   bomba = 0;
					   habilitarCasillas();
				   }
			   } else if(p.length ==3){
				   int pos = calcularPosicion(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
				   if(p[2].toString().equals("PonerBandera")){
					   lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/CasillaBandera.png")));
				   } else {
					   lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/Casilla.png"))); 
				   } 

			   } else if(arg.equals("FINALIZADO")){
				   finalizado = true;
				   try {
					   play(finalizado);
				   } catch (NoArchivoAudioException e) {
					   e.printStackTrace();
				   }
				   lblNewLabel.setIcon(new ImageIcon(VBuscaminas.class.getResource("/Victoria.png"))); 
				   mostrarRanking();
				   Ranking.getRanking().guardarLista();
				   JOptionPane.showMessageDialog(null, "HAS RESUELTO CORRECTAMENTE!!!");

			   }
			} else if(o instanceof Tablero){
				if (p.length == 3){
				int pos = calcularPosicion(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
				  if(1<=Integer.parseInt(p[2]) && Integer.parseInt(p[2])<=8){
					  lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/Casilla"+Integer.parseInt(p[2])+".png")));
				    }else if(Integer.parseInt(p[2])==0){
				    	   lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/CasillaVacia.png")));
				    }else if(Integer.parseInt(p[2])==10){
				    	if(bomba == 0){
				    		 lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/CasillaPrimeraMina.png")));
				    		 bomba++;
				    	} else {
				    		 lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/CasillaMina.png")));	  
				    	}
				    }else if(Integer.parseInt(p[2])==11){
				    	lcasillas[pos].setIcon(new ImageIcon(VBuscaminas.class.getResource("/CasillaNoMina.png")));
				    }
				}
			}
	}
	

	public void actionPerformed(ActionEvent e) {
        if (e.getSource()==item1) {
        	Buscaminas.getBuscaminas().reset(vBusca);
        } else if (e.getSource() == item2){
        	VAyuda vA = new VAyuda();
			vA.setVisible(true);
        }else if (e.getSource() == item3){
        	mostrarRanking();
        }
   }
	
	private void habilitarCasillas(){
		for(int i=0;i<lcasillas.length;i++){
			lcasillas[i].setEnabled(true);
			lcasillas[i].setIcon(new ImageIcon(VBuscaminas.class.getResource("/Casilla.png")));
		}
	}
	
	private int calcularPosicion(int pFila, int pCol){
		int pos = 0; 
		pos = (pCol*(fil+1))+pFila;
		return pos;	
	}
	
	private void mostrarRanking(){
		Buscaminas.getBuscaminas().calcularPuntos();
    	VRanking vR = new VRanking();
		vR.setVisible(true);
	}
		
	private void autoGuardadoRank(){
		Timer timer;
		TimerTask  timerTask = new TimerTask() {
			@Override
			public void run() {
				try{
		    		 Thread.sleep(10000); 
		    	  }catch (Exception e) {}
				
			}
		};
		timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, 0, 50);
	}
	
	private void play(boolean pB) throws NoArchivoAudioException{
		if (pB==false){
			if (new File("sources/lose.wav").getAbsoluteFile() != null){
				try {
					ais = AudioSystem.getAudioInputStream(new File("src/main/resources/lose.wav").getAbsoluteFile());
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					clip = AudioSystem.getClip();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				try {
					clip.open(ais);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				throw new NoArchivoAudioException();
			}
		}else{
			if (new File("sources/win.wav").getAbsoluteFile() != null){
				try {
					ais = AudioSystem.getAudioInputStream(new File("sources/win.wav").getAbsoluteFile());
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					clip = AudioSystem.getClip();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				try {
					clip.open(ais);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				throw new NoArchivoAudioException();
			}
		}
		clip.start();
	}
}

