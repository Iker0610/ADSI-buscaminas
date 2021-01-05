package is.buscaminas.controller;


import is.buscaminas.Main;
import is.buscaminas.model.buscaminas.Contador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class GestorVentanas
{
	// Atributos
	private static GestorVentanas gestorVentanas;
	private final Stage ventanaPrincipal;
	
	// Constructora
	private GestorVentanas(Stage pVentanaPrincipal)
	{
		ventanaPrincipal = pVentanaPrincipal;
		
		// Se co
		ventanaPrincipal.setTitle("Buscaminas");
		ventanaPrincipal.getIcons().add(new Image(new File("src/main/resources/is/buscaminas/inicio/logo.png").toURI().toString()));
		ventanaPrincipal.setResizable(false);
		ventanaPrincipal.centerOnScreen();
	}
	
	// Singleton
	
	public static void crearGestorVentanas(Stage pVentanaPrincipal)
	{
		// Pre: Es llamaddo por la clase Main
		// Post: Se crea el Gestor de Ventanas.
		Class<?> caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
		if (caller.equals(Main.class) && gestorVentanas == null) gestorVentanas = new GestorVentanas(pVentanaPrincipal);
		else throw new IllegalStateException();
	}
	
	// Se obtiene el gestor de ventanas. Se asume que se ha llamado siempre a
	public static GestorVentanas getGestorVentanas()
	{
		// Pre: haber llamado previamente al inicio del programa a crearGestorVentanas
		// Post: Se obtiene el GestorVentanas
		if (gestorVentanas != null) return gestorVentanas;
		else throw new IllegalStateException();
	}
	
	// Métodos para generar ventanas:
	private void abrirVentanaPrincipal(String pNombreVentana)
	{
		// Pre:
		// Post:
		try{
			//Se carga la pantalla y se introduce en el Stage
			Parent root = FXMLLoader.load(Main.class.getResource("fxml/" + pNombreVentana + ".fxml"));
			ventanaPrincipal.setScene(new Scene(root));
			
			//Se muestra el stage una vez cargado
			ventanaPrincipal.show();
		}
		catch (Exception e){
			// Si existe algún error al cargar el fxml se indica y se cierra la aplicación
			Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
			errorDeCarga.setTitle("Error carga FXML");
			errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/" + pNombreVentana + ".fxml");
			errorDeCarga.setContentText(e.toString() + "\n\nLa aplicación se cerrará");
			errorDeCarga.setOnCloseRequest((handler)->System.exit(-1));
			errorDeCarga.show();
		}
	}
	
	private void abrirVentanaEmergente(String pNombreVentana, String pTituloVentana)
	{
		// Pre:
		// Post: se carga y muestra una ventana emergente.
		// Se regula el contador, este activo o no.
		try{
			// Se carga el FXML
			Stage ventanaEmergente = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("fxml/" + pNombreVentana + ".fxml"));
			ventanaEmergente.setScene(new Scene(root));
			
			//Se configura el Stage
			ventanaEmergente.setTitle(pTituloVentana);
			ventanaEmergente.getIcons().add(new Image(new File("src/main/resources/is/buscaminas/temas/mario/assets/logo/" + pTituloVentana.toLowerCase() + ".png").toURI().toString()));
			ventanaEmergente.setResizable(false);
			ventanaEmergente.centerOnScreen();
			ventanaEmergente.initModality(Modality.WINDOW_MODAL);   // Hace que se carge el stage como ventana emergente (ventana hija)
			ventanaEmergente.initOwner(ventanaPrincipal);                 // Se indica cual es la ventana padre y la bloquea
			
			// Se configuran las acciones al cerrar la ventana -> Se reanuda el contador
			ventanaEmergente.setOnCloseRequest((pHandler)->{
				SFXPlayer.getSFXPlayer().stopFloatWindowBackground();
				if (Partida.getPartida().hayPartidaActiva()) Contador.getContador().continuar();
			});
			
			// Finalmente antes de mostrar la ventana se para el contador por si está activo.
			Contador.getContador().parar();
			
			// Se muestra la ventana
			ventanaEmergente.show();
			
		}
		catch (IOException e){
			// Si existe algún error al cargar el fxml se indica y se cierra la aplicación
			Alert errorDeCarga = new Alert(Alert.AlertType.ERROR);
			errorDeCarga.setTitle("Error carga FXML");
			errorDeCarga.setHeaderText("Error al cargar el archivo FXML: fxml/" + pNombreVentana + ".fxml");
			errorDeCarga.setContentText(e.toString() + Arrays.toString(e.getStackTrace()) + "\n\nLa aplicación se cerrará");
			errorDeCarga.setOnCloseRequest((handler)->System.exit(-1));
			errorDeCarga.show();
		}
	}
	
	// Métodos publicos para controlar el flujo de ventanas:
	
	public void abrirLogin()
	{
		abrirVentanaPrincipal("UI_Inicio");
	}
	
	public void abrirMenuPrincipal()
	{
		abrirVentanaPrincipal("UI_MenuPrincipal");
	}
	
	public void abrirGestionarCuenta()
	{
		abrirVentanaPrincipal("UI_GestionarCuenta");
	}
	
	public void abrirLogros()
	{
		abrirVentanaPrincipal("UI_Logros");
	}
	
	public void abrirPartida()
	{
		abrirVentanaPrincipal("ventanaPartidaBase");
	}
	
	public void abrirAdministrar()
	{
		abrirVentanaPrincipal("UI_Administrar");
	}
	
	public void abrirGestionarJuego()
	{
		abrirVentanaPrincipal("UI_GestionarDatosJuego");
	}
	
	public void abrirGestionarUsuarios()
	{
		abrirVentanaPrincipal("UI_GestionarUsuarios");
	}
	
	public void abrirGestionarDatosUsuario()
	{
		abrirVentanaPrincipal("UI_GestionarDatosUsuario");
	}
	
	public void abrirSeleccionNivel()
	{
		abrirVentanaPrincipal("ventanaAcceso");
	}
	
	public void mostrarRankingEmergente()
	{
		abrirVentanaEmergente("UI_VerRanking", "Ranking");
	}
	
	public void mostrarAyudaEmergente()
	{
		abrirVentanaEmergente("ventanaMenuAyuda", "Ayuda");
	}
	
	
}
