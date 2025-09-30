package com.sudoku.sakura.controllers;

import com.sudoku.sakura.Main;
import com.sudoku.sakura.utils.AudioManager;
import com.sudoku.sakura.utils.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.application.Platform;

public class MenuController {

    @FXML private Button newGameButton;
    @FXML private Button continueButton;
    @FXML private Button statisticsButton;
    @FXML private Button exitButton;
    @FXML private Slider musicVolumeSlider;
    @FXML private Slider soundVolumeSlider;
    @FXML private Label musicVolumeLabel;
    @FXML private Label soundVolumeLabel;

    @FXML
    public void initialize() {
        if (!SaveManager.hasSavedGame()) {
            continueButton.setDisable(true);
            continueButton.setOpacity(0.5);
        }

        // Configurar sliders
        setupVolumeControls();
    }

    private void setupVolumeControls() {
        AudioManager audio = AudioManager.getInstance();

        // Inicializar valores
        musicVolumeSlider.setValue(audio.getMusicVolume() * 100);
        soundVolumeSlider.setValue(audio.getSoundVolume() * 100);

        updateMusicVolumeLabel();
        updateSoundVolumeLabel();

        // Listeners para cambios en tiempo real
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audio.setMusicVolume(newVal.doubleValue() / 100.0);
            updateMusicVolumeLabel();
        });

        soundVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audio.setSoundVolume(newVal.doubleValue() / 100.0);
            updateSoundVolumeLabel();

            // Reproducir efecto de prueba al cambiar volumen
            if (Math.abs(newVal.doubleValue() - oldVal.doubleValue()) > 5) {
                audio.playSoundEffect(AudioManager.SoundEffect.CLICK);
            }
        });
    }

    private void updateMusicVolumeLabel() {
        int volume = (int) musicVolumeSlider.getValue();
        musicVolumeLabel.setText(volume + "%");
    }

    private void updateSoundVolumeLabel() {
        int volume = (int) soundVolumeSlider.getValue();
        soundVolumeLabel.setText(volume + "%");
    }

    @FXML
    private void handleNewGame() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/difficulty.fxml", 600, 700);
    }

    @FXML
    private void handleContinue() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/game.fxml", 650, 800);
    }

    @FXML
    private void handleStatistics() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/statistics.fxml", 650, 750);
    }

    @FXML
    private void handleExit() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Platform.exit();
    }
}