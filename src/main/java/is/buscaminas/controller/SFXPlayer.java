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
	
	// BACKGROUND
	private void setBackgroundTheme(String pTheme, MediaPlayer pBackground)
	{
		if (pBackground != null){
			// Si se esta ejecutando un tema, se para y se libera la memoria
			pBackground.stop();
			pBackground.dispose();
		}
		
		// Se arranca el nuevo tema
		Media backgroundTheme = new Media(new File("src/main/resources/is/buscaminas/temas/" + Usuario.getUsuario().getTematicaActual().toLowerCase().replaceAll("\\s", "") + "/sfx/themes/" + pTheme + ".mp3").toURI().toString());
		pBackground = new MediaPlayer(backgroundTheme);
		pBackground.seek(Duration.ZERO);
		pBackground.setCycleCount(MediaPlayer.INDEFINITE);
		pBackground.play();
	}
	
	// BACKGROUND PARA VENTANAS PRINCIPALES
	public void setBackgroundTheme(String pTheme)
	{
		if (pTheme != null && !pTheme.equals(backgroundThemeName)){
			backgroundThemeName = pTheme;
			setBackgroundTheme(pTheme, backgroundThemePlayer);
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
			setBackgroundTheme(pTheme, floatWindowBackgroundThemePlayer);
		}
	}
	
	public void stopFloatWindowBackground()
	{
		if (floatWindowBackgroundThemePlayer != null){
			floatWindowBackgroundThemePlayer.stop();
			if (floatWindowBackgroundThemePlayer.getOnEndOfMedia() != null) floatWindowBackgroundThemePlayer.getOnEndOfMedia().run();
			floatWindowBackgroundThemePlayer.dispose();
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
		}
		setSFX(pTheme);
		efectPlayer.play();
	}
}
