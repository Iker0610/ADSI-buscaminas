package is.buscaminas.controller;


import is.buscaminas.model.Usuario;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;


public class SFXPlayer
{
	// Atributos
	
	private static SFXPlayer mSFXPlayer;
	
	// Temática del usuario
	private String temaActual;
	
	// Tema de la ventana principal
	private MediaPlayer backgroundThemePlayer;
	private String backgroundThemeName;
	
	// Tema de la ventana flotante
	private MediaPlayer floatWindowBackgroundThemePlayer;
	private String floatWindowBackgroundThemeName;
	
	// Efecto de sonido
	private MediaPlayer efectPlayer;
	
	
	// Constructora
	
	private SFXPlayer()
	{
		this.backgroundThemePlayer = null;
		this.floatWindowBackgroundThemePlayer = null;
		this.efectPlayer = null;
	}
	
	
	// Singleton
	
	public static SFXPlayer getSFXPlayer()
	{
		if (mSFXPlayer == null) mSFXPlayer = new SFXPlayer();
		return mSFXPlayer;
	}
	
	
	// Métodos
	
	// BACKGROUND PARA VENTANAS PRINCIPALES
	public void setBackgroundTheme(String pTheme)
	{
		// Pre: Existe un Usuario con un tema valido
		// Post: Se ha seteado el tema de fondo y eliminado el antiguo en caso de haberlo
		
		// Se obtiene el tema actual del Usuario
		String temaDelUsuario = Usuario.getUsuario().getTematicaActual();
		
		// Se comprueba que el fondo sea distinto al que está puesto.
		if (pTheme != null && (!pTheme.equals(backgroundThemeName) || !temaDelUsuario.equals(temaActual))){
			if (backgroundThemePlayer != null){
				// Si se esta ejecutando un tema, se para y se libera la memoria
				backgroundThemePlayer.stop();
				backgroundThemePlayer.dispose();
				backgroundThemePlayer = null;
				backgroundThemeName = null;
			}
			
			// Se ejecuta el nuevo tema
			temaActual = temaDelUsuario;
			backgroundThemeName = pTheme;
			Media backgroundTheme = new Media(new File("src/main/resources/is/buscaminas/temas/" + temaDelUsuario.toLowerCase().replaceAll("\\s", "") + "/sfx/themes/" + pTheme + ".mp3").toURI().toString());
			backgroundThemePlayer = new MediaPlayer(backgroundTheme);
			backgroundThemePlayer.seek(Duration.ZERO);
			backgroundThemePlayer.setCycleCount(MediaPlayer.INDEFINITE);
			backgroundThemePlayer.play();
		}
	}
	
	
	// Metodos para parar y reanudar el tema de fondo
	
	private void playBackground()
	{
		if (backgroundThemePlayer != null) backgroundThemePlayer.play();
	}
	
	private void stopBackground()
	{
		if (backgroundThemePlayer != null) backgroundThemePlayer.stop();
	}
	
	
	//BACKGROUND PARA VENTANAS EMERGENTES
	
	public void setFloatWindowBackgroundTheme(String pTheme)
	{
		// Pre: Existe un Usuario con un tema valido
		// Post: Se ha seteado el tema de fondo de la ventana flotante y eliminado el antiguo en caso de haberlo.
		// 		En caso de haber un tema de fondo en la ventana principal se para.
		
		// Se obtiene el tema actual del Usuario
		String temaDelUsuario = Usuario.getUsuario().getTematicaActual();
		
		// Se comprueba que el fondo sea distinto al que está puesto.
		if (pTheme != null && (!pTheme.equals(floatWindowBackgroundThemeName) || !temaDelUsuario.equals(temaActual))){
			
			if (floatWindowBackgroundThemePlayer != null){
				// Si se esta ejecutando un tema, se para y se libera la memoria
				floatWindowBackgroundThemePlayer.stop();
				floatWindowBackgroundThemePlayer.dispose();
				floatWindowBackgroundThemePlayer = null;
				floatWindowBackgroundThemeName = null;
			}
			
			// Se para el tema anterior
			stopBackground();
			
			// Se arranca el nuevo tema
			temaActual = temaDelUsuario;
			floatWindowBackgroundThemeName = pTheme;
			Media backgroundTheme = new Media(new File("src/main/resources/is/buscaminas/temas/" + temaDelUsuario.toLowerCase().replaceAll("\\s", "") + "/sfx/themes/" + pTheme + ".mp3").toURI().toString());
			floatWindowBackgroundThemePlayer = new MediaPlayer(backgroundTheme);
			floatWindowBackgroundThemePlayer.seek(Duration.ZERO);
			floatWindowBackgroundThemePlayer.setCycleCount(MediaPlayer.INDEFINITE);
			floatWindowBackgroundThemePlayer.play();
		}
	}
	
	public void stopFloatWindowBackground()
	{
		// Pre:
		// Post:  Se para el tema de la ventana flotante y se libera memoria en caso de que exista tal tema
		
		if (floatWindowBackgroundThemePlayer != null){
			floatWindowBackgroundThemePlayer.stop();
			floatWindowBackgroundThemePlayer.dispose();
			floatWindowBackgroundThemePlayer = null;
			floatWindowBackgroundThemeName = null;
		}
		
		// Se reanuda el tema de fondo de la ventana principal
		playBackground();
	}
	
	
	// SFX Methods
	
	private void setSFX(String pTheme)
	{
		// Pre: Existe un Usuario con un tema valido
		// Post: Se ha ejecutado un efecto de sonido.
		//			En caso de existir uno previo, este se para, elimina y libera la memoria asociada.

		
		if (efectPlayer != null){
			// Si se esta ejecutando un efecto, se para y se libera la memoria
			efectPlayer.stop();
			if (efectPlayer.getOnEndOfMedia() != null) efectPlayer.getOnEndOfMedia().run();
			efectPlayer.dispose();
			efectPlayer = null;
		}
		
		// Se ejecuta el nuevo efecto
		Media effect = new Media(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/sfx/effects/" + pTheme + ".wav").toURI().toString());
		efectPlayer = new MediaPlayer(effect);
		efectPlayer.play();
	}
	
	public void playSFX(String pTheme)
	{
		// Post: Se ha ejecutado un efecto de sonido.
		// 		En caso de haber un tema de background su volumen se ha reducido al 30% mientras se ejecutaba el efecto
		//			Al finalizar el efecto el volumen del background se ha puesto al 100%
		
		// Se baja el volumen del tema principal
		if (backgroundThemePlayer != null) backgroundThemePlayer.setVolume(0.3);
		
		// Se configura el SFX y se ejectuta
		setSFX(pTheme);
		
		// Se resetea el volumen del tema de fondo
		if (backgroundThemePlayer != null) backgroundThemePlayer.setVolume(1);
	}
	
	public void playFinalSFX(String pTheme)
	{
		// Pre: Existe un Usuario con un tema valido
		// Post: Se ha ejecutado un efecto de sonido.
		//			En caso de haber un tema de background se ha eliminado y liberado la memoria.
		
		if (backgroundThemePlayer != null){
			// Si se esta ejecutando un tema, se para y se libera la memoria
			backgroundThemePlayer.stop();
			backgroundThemePlayer.dispose();
			backgroundThemePlayer = null;
			backgroundThemeName = null;
		}
		
		// Se configura el SFX y se ejectuta
		setSFX(pTheme);
	}
}
