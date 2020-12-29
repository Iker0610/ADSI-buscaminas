package is.buscaminas.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class SFXPlayer {

    // Atributos
    private static SFXPlayer mSFXPlayer;
    private MediaPlayer backgroundThemePlayer;
    private MediaPlayer efectPlayer;

    private SFXPlayer () {}

    // Singleton
    public static SFXPlayer getSFXPlayer ()
    {
        if (mSFXPlayer == null) mSFXPlayer = new SFXPlayer();
        return mSFXPlayer;
    }

    // Métodos
    public void setBackgroundTheme (String pTheme)
    {
        if (backgroundThemePlayer != null) {
            // Si se esta ejecutando un tema, se para y se libera la memoria
            backgroundThemePlayer.stop();
            backgroundThemePlayer.dispose();
        }

        // Se arranca el nuevo tema
        Media backgroundTheme = new Media(new File("src/main/resources/is/buscaminas/sfx/themes/" + pTheme + ".mp3").toURI().toString());
        backgroundThemePlayer = new MediaPlayer(backgroundTheme);
        backgroundThemePlayer.seek(Duration.ZERO);
        backgroundThemePlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundThemePlayer.play();
    }

    public void stopBackground ()
    {
        backgroundThemePlayer.stop();
    }

    public void playAbsoluteSFX (String pTheme)
    {
        if (efectPlayer != null) {
            // Si se esta ejecutando un efecto, se para y se libera la memoria
            efectPlayer.stop();
            if (efectPlayer.getOnEndOfMedia() != null) {
                efectPlayer.getOnEndOfMedia().run();
            }
            efectPlayer.dispose();
        }

        backgroundThemePlayer.stop();
        // Se ejecuta el nuevo efecto
        Media effect = new Media(new File("src/main/resources/is/buscaminas/sfx/effects/" + pTheme + ".wav").toURI().toString());
        efectPlayer = new MediaPlayer(effect);
        efectPlayer.setOnEndOfMedia(() -> backgroundThemePlayer.play());
        efectPlayer.play();
    }


    public void playSFX (String pTheme)
    {
        if (efectPlayer != null) {
            // Si se esta ejecutando un efecto, se para y se libera la memoria
            efectPlayer.stop();
            if (efectPlayer.getOnEndOfMedia() != null) {
                efectPlayer.getOnEndOfMedia().run();
            }
            efectPlayer.dispose();
        }

        // Se baja el volumen del tema principal
        if (backgroundThemePlayer != null) backgroundThemePlayer.setVolume(0.3);

        // Se ejecuta el nuevo efecto
        Media effect = new Media(new File("src/main/resources/is/buscaminas/sfx/effects/" + pTheme + ".wav").toURI().toString());
        efectPlayer = new MediaPlayer(effect);
        efectPlayer.play();

        // Se resetea el volumen del tema de fondo
        if (backgroundThemePlayer != null) backgroundThemePlayer.setVolume(1);
    }
}
