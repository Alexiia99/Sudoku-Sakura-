package com.sudoku.sakura.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private static AudioManager instance;

    private MediaPlayer backgroundMusic;
    private MediaPlayer soundEffect;

    private boolean musicEnabled = true;
    private boolean soundEnabled = true;

    private double musicVolume = 0.5;
    private double effectVolume = 0.5;

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playBackgroundMusic() {
        if (!musicEnabled) return;

        try {
            String musicPath = getClass().getResource("/audio/background.mp3").toExternalForm();
            Media music = new Media(musicPath);

            if (backgroundMusic != null) {
                backgroundMusic.stop();
            }

            backgroundMusic = new MediaPlayer(music);
            backgroundMusic.setVolume(musicVolume);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusic.play();

        } catch (Exception e) {
            System.err.println("No se pudo cargar la música. Añade /audio/background.mp3 a resources");
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            backgroundMusic.pause();
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PAUSED) {
            backgroundMusic.play();
        }
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume);
        }
    }

    public void setSoundVolume(double volume) {
        this.effectVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getSoundVolume() {
        return effectVolume;
    }

    public void playSoundEffect(SoundEffect effect) {
        if (!soundEnabled) return;

        try {
            String soundPath = getClass().getResource(effect.getPath()).toExternalForm();
            Media sound = new Media(soundPath);

            if (soundEffect != null) {
                soundEffect.stop();
            }

            soundEffect = new MediaPlayer(sound);
            soundEffect.setVolume(effectVolume);
            soundEffect.play();

        } catch (Exception e) {
            // No es crítico si falla
        }
    }

    public void toggleMusic() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    public void toggleSound() {
        soundEnabled = !soundEnabled;
    }

    public boolean isMusicEnabled() { return musicEnabled; }
    public boolean isSoundEnabled() { return soundEnabled; }

    public enum SoundEffect {
        CLICK("/audio/click.wav"),
        HINT("/audio/hint.wav"),
        VICTORY("/audio/victory.wav"),
        DEFEAT("/audio/defeat.wav"),
        ERROR("/audio/error.wav");

        private final String path;

        SoundEffect(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}