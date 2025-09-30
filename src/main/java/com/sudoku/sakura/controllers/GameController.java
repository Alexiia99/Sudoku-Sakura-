package com.sudoku.sakura.controllers;

import com.sudoku.sakura.Main;
import com.sudoku.sakura.models.GameState;
import com.sudoku.sakura.models.Statistics;
import com.sudoku.sakura.utils.AudioManager;
import com.sudoku.sakura.utils.SaveManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameController {

    @FXML private Label livesLabel;
    @FXML private Label timerLabel;
    @FXML private Label hintsLabel;
    @FXML private Label difficultyLabel;
    @FXML private GridPane boardGrid;
    @FXML private Button hintButton;
    @FXML private Button verifyButton;
    @FXML private Button pauseButton;

    private GameState gameState;
    private StackPane[][] cellPanes;
    private Label[][] cellLabels;
    private StackPane selectedCell;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Timeline timer;

    @FXML
    public void initialize() {
        gameState = SaveManager.loadGame();
        if (gameState == null) {
            Main.loadScene("views/menu.fxml", 600, 700);
            return;
        }

        initializeBoard();
        updateUI();
        startTimer();
    }

    private void initializeBoard() {
        cellPanes = new StackPane[9][9];
        cellLabels = new Label[9][9];

        int[][] currentBoard = gameState.getCurrentBoard();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                StackPane cellPane = createCell(row, col, currentBoard[row][col]);
                cellPanes[row][col] = cellPane;
                boardGrid.add(cellPane, col, row);
            }
        }
    }

    private StackPane createCell(int row, int col, int value) {
        StackPane pane = new StackPane();
        pane.setPrefSize(60, 60);
        pane.setAlignment(Pos.CENTER);

        Label label = new Label(value == 0 ? "" : String.valueOf(value));
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        cellLabels[row][col] = label;

        if (gameState.isOriginalCell(row, col)) {
            pane.getStyleClass().add("sudoku-cell-given");
        } else {
            pane.getStyleClass().add("sudoku-cell");
        }

        addBorderStyle(pane, row, col);

        final int r = row;
        final int c = col;
        pane.setOnMouseClicked(e -> selectCell(r, c));

        pane.getChildren().add(label);
        return pane;
    }

    private void addBorderStyle(StackPane pane, int row, int col) {
        boolean thickRight = (col == 2 || col == 5);
        boolean thickBottom = (row == 2 || row == 5);

        if (thickRight && thickBottom) {
            pane.getStyleClass().add("sudoku-cell-thick-both");
        } else if (thickRight) {
            pane.getStyleClass().add("sudoku-cell-thick-right");
        } else if (thickBottom) {
            pane.getStyleClass().add("sudoku-cell-thick-bottom");
        }
    }

    private void selectCell(int row, int col) {
        if (gameState.isOriginalCell(row, col) || gameState.isGameOver()) {
            return;
        }

        if (selectedCell != null) {
            selectedCell.getStyleClass().remove("sudoku-cell-selected");
        }

        selectedRow = row;
        selectedCol = col;
        selectedCell = cellPanes[row][col];
        selectedCell.getStyleClass().add("sudoku-cell-selected");
    }

    @FXML
    private void handleNumber1() { placeNumber(1); }
    @FXML
    private void handleNumber2() { placeNumber(2); }
    @FXML
    private void handleNumber3() { placeNumber(3); }
    @FXML
    private void handleNumber4() { placeNumber(4); }
    @FXML
    private void handleNumber5() { placeNumber(5); }
    @FXML
    private void handleNumber6() { placeNumber(6); }
    @FXML
    private void handleNumber7() { placeNumber(7); }
    @FXML
    private void handleNumber8() { placeNumber(8); }
    @FXML
    private void handleNumber9() { placeNumber(9); }

    @FXML
    private void handleClear() {
        if (selectedRow >= 0 && selectedCol >= 0) {
            gameState.clearCell(selectedRow, selectedCol);
            cellLabels[selectedRow][selectedCol].setText("");
            SaveManager.saveGame(gameState);
        }
    }

    private void placeNumber(int number) {
        if (selectedRow < 0 || selectedCol < 0) {
            return;
        }

        boolean correct = gameState.placeNumber(selectedRow, selectedCol, number);
        cellLabels[selectedRow][selectedCol].setText(String.valueOf(number));

        if (!correct) {
            AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.ERROR);
            cellPanes[selectedRow][selectedCol].getStyleClass().add("sudoku-cell-error");

            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(e -> {
                cellPanes[selectedRow][selectedCol].getStyleClass().remove("sudoku-cell-error");
            });
            pause.play();
        }

        updateUI();
        SaveManager.saveGame(gameState);

        if (gameState.isGameOver()) {
            handleGameOver();
        }
    }

    @FXML
    private void handleHint() {
        int[] hint = gameState.useHint();

        if (hint == null) {
            return;
        }

        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.HINT);

        int row = hint[0];
        int col = hint[1];
        int number = hint[2];

        cellLabels[row][col].setText(String.valueOf(number));
        cellPanes[row][col].getStyleClass().add("sudoku-cell-hint");

        updateUI();
        SaveManager.saveGame(gameState);

        if (gameState.isGameOver()) {
            handleGameOver();
        }
    }

    @FXML
    private void handleVerify() {
        if (!gameState.isBoardComplete()) {
            showAlert("Completa todas las celdas primero 🌸");
            return;
        }

        if (gameState.isBoardCorrect()) {
            gameState.placeNumber(0, 0, gameState.getCurrentBoard()[0][0]); // Trigger victory
            handleGameOver();
        } else {
            showAlert("Hay errores en el tablero. ¡Sigue intentando! 💪");
        }
    }

    @FXML
    private void handlePause() {
        timer.pause();
        gameState.setPaused(true);
        SaveManager.saveGame(gameState);
        Main.loadScene("views/pause.fxml", 500, 600);
    }

    @FXML
    private void handleMenu() {
        timer.stop();
        SaveManager.saveGame(gameState);
        Main.loadScene("views/menu.fxml", 600, 700);
    }

    private void handleGameOver() {
        timer.stop();

        Statistics stats = SaveManager.loadStatistics();

        if (gameState.isVictory()) {
            AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.VICTORY);
            stats.recordWin(
                    gameState.getDifficulty(),
                    gameState.getElapsedTime(),
                    GameState.MAX_HINTS - gameState.getHintsRemaining()
            );
            SaveManager.saveStatistics(stats);
            VictoryController.setGameState(gameState);
            SaveManager.deleteSavedGame();
            Main.loadScene("views/victory.fxml", 500, 650);
        } else {
            AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.DEFEAT);
            stats.recordLoss(
                    gameState.getElapsedTime(),
                    GameState.MAX_HINTS - gameState.getHintsRemaining()
            );
            SaveManager.saveStatistics(stats);
            DefeatController.setGameState(gameState);
            SaveManager.deleteSavedGame();
            Main.loadScene("views/defeat.fxml", 500, 650);
        }
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            gameState.incrementTime();
            timerLabel.setText(gameState.getFormattedTime());
            SaveManager.saveGame(gameState);
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateUI() {
        String hearts = "";
        for (int i = 0; i < GameState.MAX_LIVES; i++) {
            hearts += i < gameState.getLives() ? "♥ " : "♡ ";
        }
        livesLabel.setText(hearts.trim());
        livesLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #FF6B6B; -fx-font-weight: 700;");

        hintsLabel.setText("Pistas: " + gameState.getHintsRemaining());
        timerLabel.setText(gameState.getFormattedTime());
        difficultyLabel.setText("Dificultad: " + gameState.getDifficulty().getDisplayName());

        if (gameState.getHintsRemaining() <= 0) {
            hintButton.setDisable(true);
            hintButton.setOpacity(0.5);
        }
    }

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle("Sudoku Sakura");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}