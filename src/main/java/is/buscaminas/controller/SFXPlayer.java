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
	private MediaPlayer backgroundThemePlayer;
	private String backgroundThemeName;
	private MediaPlayer floatWindowBackgroundThemePlayer;
	private String floatWindowBackgroundThemeName;
	private MediaPlayer efectPlayer;
	
	private SFXPlayer()
	{
		this.backgroundThemePlayer            = null;
		this.floatWindowBackgroundThemePlayer = null;
		this.efectPlayer                      = null;
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
		if (pTheme != null && !pTheme.equals(backgroundThemeName)){
			if (backgroundThemePlayer != null){
				// Si se esta ejecutando un tema, se para y se libera la memoria
				backgroundThemePlayer.stop();
				backgroundThemePlayer.dispose();
				backgroundThemePlayer = null;
				backgroundThemeName = null;
			}
			
			// Se arranca el nuevo tema
			backgroundThemeName = pTheme;
			Media backgroundTheme = new Media(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/sfx/themes/" + pTheme + ".mp3").toURI().toString());
			backgroundThemePlayer = new MediaPlayer(backgroundTheme);
			backgroundThemePlayer.seek(Duration.ZERO);
			backgroundThemePlayer.setCycleCount(MediaPlayer.INDEFINITE);
			backgroundThemePlayer.play();
		}
	}
	
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
		if (pTheme != null && !pTheme.equals(floatWindowBackgroundThemeName)){
			floatWindowBackgroundThemeName = pTheme;
			stopBackground();
			if (floatWindowBackgroundThemePlayer != null){
				// Si se esta ejecutando un tema, se para y se libera la memoria
				floatWindowBackgroundThemePlayer.stop();
				floatWindowBackgroundThemePlayer.dispose();
				floatWindowBackgroundThemePlayer = null;
				floatWindowBackgroundThemeName   = null;
			}
			
			// Se arranca el nuevo tema
			Media backgroundTheme = new Media(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/sfx/themes/" + pTheme + ".mp3").toURI().toString());
			floatWindowBackgroundThemePlayer = new MediaPlayer(backgroundTheme);
			floatWindowBackgroundThemePlayer.seek(Duration.ZERO);
			floatWindowBackgroundThemePlayer.setCycleCount(MediaPlayer.INDEFINITE);
			floatWindowBackgroundThemePlayer.play();
		}
	}
	
	public void stopFloatWindowBackground()
	{
		if (floatWindowBackgroundThemePlayer != null){
			floatWindowBackgroundThemePlayer.stop();
			floatWindowBackgroundThemePlayer.dispose();
			floatWindowBackgroundThemePlayer = null;
			floatWindowBackgroundThemeName   = null;
		}
		playBackground();
	}
	
	
	// SFX Methods
	
	private void setSFX(String pTheme)
	{
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
	}
	
	public void playSFX(String pTheme)
	{
		// Se baja el volumen del tema principal
		if (backgroundThemePlayer != null) backgroundThemePlayer.setVolume(0.3);
		
		// Se configura el SFX
		setSFX(pTheme);
		
		// Se ejecuta
		efectPlayer.play();
		
		// Se resetea el volumen del tema de fondo
		if (backgroundThemePlayer != null) backgroundThemePlayer.setVolume(1);
	}
	
	public void playAbsoluteSFX(String pTheme)
	{
		// Se para el backgroun
		stopBackground();
		
		// Se configura el SFX
		setSFX(pTheme);
		
		// Se configura para que al acabar vuelva a poner el background
		efectPlayer.setOnEndOfMedia(this::playBackground);
		
		// Play
		efectPlayer.play();
	}
	
	public void playFinalSFX(String pTheme)
	{
		if (backgroundThemePlayer != null){
			// Si se esta ejecutando un tema, se para y se libera la memoria
			backgroundThemePlayer.stop();
			backgroundThemePlayer.dispose();
			backgroundThemePlayer = null;
			backgroundThemeName   = null;
		}
		setSFX(pTheme);
		efectPlayer.play();
	}
}
