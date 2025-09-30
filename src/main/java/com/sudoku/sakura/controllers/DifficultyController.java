package com.sudoku.sakura.controllers;

import com.sudoku.sakura.Main;
import com.sudoku.sakura.models.Difficulty;
import com.sudoku.sakura.models.GameState;
import com.sudoku.sakura.models.SudokuGenerator;
import com.sudoku.sakura.utils.AudioManager;
import com.sudoku.sakura.utils.SaveManager;
import javafx.fxml.FXML;

public class DifficultyController {

    private static Difficulty selectedDifficulty;

    @FXML
    private void handleEasy() {
        startGame(Difficulty.EASY);
    }

    @FXML
    private void handleNormal() {
        startGame(Difficulty.NORMAL);
    }

    @FXML
    private void handleHard() {
        startGame(Difficulty.HARD);
    }

    @FXML
    private void handleBack() {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);
        Main.loadScene("views/menu.fxml", 600, 700);
    }

    private void startGame(Difficulty difficulty) {
        AudioManager.getInstance().playSoundEffect(AudioManager.SoundEffect.CLICK);

        selectedDifficulty = difficulty;

        SudokuGenerator generator = new SudokuGenerator();
        int emptyCells = difficulty.getRandomEmptyCells();
        int[][] puzzle = generator.generatePuzzle(emptyCells);
        int[][] solution = generator.getSolution();

        GameState gameState = new GameState(puzzle, solution, difficulty);
        SaveManager.saveGame(gameState);

        Main.loadScene("views/game.fxml", 650, 800);
    }

    public static Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }
}