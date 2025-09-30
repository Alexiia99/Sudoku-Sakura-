package com.sudoku.sakura.models;

import java.io.Serializable;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private int[][] currentBoard;
    private int[][] originalPuzzle;
    private int[][] solution;
    private Difficulty difficulty;

    private int lives;
    private int hintsRemaining;
    private long elapsedTime;

    private boolean isPaused;
    private boolean isGameOver;
    private boolean isVictory;

    public static final int MAX_LIVES = 3;
    public static final int MAX_HINTS = 3;

    public GameState(int[][] puzzle, int[][] solution, Difficulty difficulty) {
        this.originalPuzzle = copyBoard(puzzle);
        this.currentBoard = copyBoard(puzzle);
        this.solution = copyBoard(solution);
        this.difficulty = difficulty;

        this.lives = MAX_LIVES;
        this.hintsRemaining = MAX_HINTS;
        this.elapsedTime = 0;
        this.isPaused = false;
        this.isGameOver = false;
        this.isVictory = false;
    }

    public boolean placeNumber(int row, int col, int number) {
        if (isGameOver || originalPuzzle[row][col] != 0) {
            return true;
        }

        currentBoard[row][col] = number;

        if (number != 0 && number != solution[row][col]) {
            lives--;
            if (lives <= 0) {
                isGameOver = true;
                isVictory = false;
            }
            return false;
        }

        if (isBoardComplete() && isBoardCorrect()) {
            isGameOver = true;
            isVictory = true;
        }

        return true;
    }

    public int[] useHint() {
        if (hintsRemaining <= 0 || isGameOver) {
            return null;
        }

        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (currentBoard[row][col] == 0 && originalPuzzle[row][col] == 0) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return null;
        }

        int[] cell = emptyCells.get((int)(Math.random() * emptyCells.size()));
        int row = cell[0];
        int col = cell[1];
        int number = solution[row][col];

        currentBoard[row][col] = number;
        hintsRemaining--;

        if (isBoardComplete() && isBoardCorrect()) {
            isGameOver = true;
            isVictory = true;
        }

        return new int[]{row, col, number};
    }

    public boolean isBoardComplete() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (currentBoard[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBoardCorrect() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (currentBoard[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearCell(int row, int col) {
        if (originalPuzzle[row][col] == 0) {
            currentBoard[row][col] = 0;
        }
    }

    public boolean isOriginalCell(int row, int col) {
        return originalPuzzle[row][col] != 0;
    }

    public void incrementTime() {
        if (!isPaused && !isGameOver) {
            elapsedTime++;
        }
    }

    public String getFormattedTime() {
        long minutes = elapsedTime / 60;
        long seconds = elapsedTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    // Getters y Setters
    public int[][] getCurrentBoard() { return currentBoard; }
    public int[][] getOriginalPuzzle() { return originalPuzzle; }
    public int[][] getSolution() { return solution; }
    public Difficulty getDifficulty() { return difficulty; }
    public int getLives() { return lives; }
    public int getHintsRemaining() { return hintsRemaining; }
    public long getElapsedTime() { return elapsedTime; }
    public boolean isPaused() { return isPaused; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isVictory() { return isVictory; }

    public void setPaused(boolean paused) { this.isPaused = paused; }
    public void setElapsedTime(long time) { this.elapsedTime = time; }
}