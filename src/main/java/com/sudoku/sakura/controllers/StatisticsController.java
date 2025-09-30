package com.sudoku.sakura.controllers;

import com.sudoku.sakura.Main;
import com.sudoku.sakura.models.Difficulty;
import com.sudoku.sakura.models.Statistics;
import com.sudoku.sakura.utils.AudioManager;
import com.sudoku.sakura.utils.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class StatisticsController {

    @FXML private Label gamesPlayedLabel;
    @FXML private Label gamesWonLabel;
    @FXML private Label gamesLostLabel;
    @FXML private Label winRateLabel;
    @FXML private Label easyBestLabel;
    @FXML private Label normalBestLabel;
    @FXML private Label hardBestLabel;
    @FXML private Label avgTimeLabel;
    @FXML private Label hintsUsedLabel;
    @FXML private Label currentStreakLabel;
    @FXML private Label bestStreakLabel;

    @FXML
    public void initialize() {
        Statistics stats = SaveManager.loadStatistics();
        updateUI(stats);
    }

    private void updateUI(Statistics stats) {
        gamesPlayedLabel.setText(String.valueOf(stats.getGamesPlayed()));
        gamesWonLabel.setText(String.valueOf(stats.getGamesWon()));
        gamesLostLabel.setText(String.valueOf(stats.getGamesLost()));
        winRateLabel.setText(String.format("%.0f%%", stats.getWinRate()));

        easyBestLabel.setText(stats.getBestTimeFormatted(Difficulty.EASY) + " ⭐");
        normalBestLabel.setText(stats.getBestTimeFormatted(Difficulty.NORMAL) + " ⭐");
        hardBestLabel.setText(stats.getBestTimeFormatted(Difficulty.HARD) + " ⭐");

        avgTimeLabel.setText(stats.getAverageTimeFormatted());
        hintsUsedLabel.setText(String.valueOf(stats.getTotalHintsUsed()));
        currentStreakLabel.setText("🔥 " + stats.getCurrentStreak());
        bestStreakLabel.setText("🔥 " + stats.getBestStreak());
    }

    @FXML
    private void handleReset() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reiniciar Estadísticas");
        alert.setHeaderText("¿Estás seguro?");
        alert.setContentText("Se borrarán todas tus estadísticas. Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Statistics stats = new Statistics();
                SaveManager.saveStatistics(stats);
                updateUI(stats);
                AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
            }
        });
    }

    @FXML
    private void handleBack() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/menu.fxml", 600, 700);
    }
}