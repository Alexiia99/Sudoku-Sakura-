package com.sudoku.sakura.controllers;

import com.sudoku.sakura.Main;
import com.sudoku.sakura.models.GameState;
import com.sudoku.sakura.utils.AudioManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VictoryController {

    @FXML private Label timeLabel;
    @FXML private Label hintsLabel;
    @FXML private Label difficultyLabel;

    private static GameState lastGameState;

    @FXML
    public void initialize() {
        if (lastGameState != null) {
            timeLabel.setText("⏱️  Tiempo: " + lastGameState.getFormattedTime());
            int hintsUsed = GameState.MAX_HINTS - lastGameState.getHintsRemaining();
            hintsLabel.setText("💡 Pistas usadas: " + hintsUsed);
            difficultyLabel.setText("⭐ Dificultad: " + lastGameState.getDifficulty().getDisplayName());
        }
    }

    @FXML
    private void handleNewGame() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/difficulty.fxml", 600, 700);
    }

    @FXML
    private void handleMainMenu() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/menu.fxml", 600, 700);
    }

    public static void setGameState(GameState gameState) {
        lastGameState = gameState;
    }
}