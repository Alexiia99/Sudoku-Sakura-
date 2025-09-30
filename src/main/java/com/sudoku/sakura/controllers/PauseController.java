package com.sudoku.sakura.controllers;

import com.sudoku.sakura.Main;
import com.sudoku.sakura.models.GameState;
import com.sudoku.sakura.utils.AudioManager;
import com.sudoku.sakura.utils.SaveManager;
import javafx.fxml.FXML;

public class PauseController {

    @FXML
    private void handleResume() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);

        GameState gameState = SaveManager.loadGame();
        if (gameState != null) {
            gameState.setPaused(false);
            SaveManager.saveGame(gameState);
        }

        Main.loadScene("views/game.fxml", 650, 800);
    }

    @FXML
    private void handleRestart() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);

        SaveManager.deleteSavedGame();
        Main.loadScene("views/difficulty.fxml", 600, 700);
    }

    @FXML
    private void handleMainMenu() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/menu.fxml", 600, 700);
    }
}