package com.sudoku.sakura.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuGenerator {

    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;

    private int[][] solution;
    private int[][] puzzle;

    public int[][] generatePuzzle(int emptyCells) {
        solution = new int[SIZE][SIZE];
        fillBoard(solution);

        puzzle = copyBoard(solution);
        removeNumbers(puzzle, emptyCells);

        return puzzle;
    }

    public int[][] getSolution() {
        return copyBoard(solution);
    }

    private boolean fillBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    List<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i <= SIZE; i++) {
                        numbers.add(i);
                    }
                    Collections.shuffle(numbers);

                    for (int num : numbers) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;

                            if (fillBoard(board)) {
                                return true;
                            }

                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(int[][] board, int count) {
        int removed = 0;
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < SIZE * SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        for (int pos : positions) {
            if (removed >= count) break;

            int row = pos / SIZE;
            int col = pos % SIZE;

            if (board[row][col] != 0) {
                board[row][col] = 0;
                removed++;
            }
        }
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Verificar fila
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c] == num) return false;
        }

        // Verificar columna
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col] == num) return false;
        }

        // Verificar caja 3x3
        int boxRow = (row / BOX_SIZE) * BOX_SIZE;
        int boxCol = (col / BOX_SIZE) * BOX_SIZE;

        for (int r = boxRow; r < boxRow + BOX_SIZE; r++) {
            for (int c = boxCol; c < boxCol + BOX_SIZE; c++) {
                if (board[r][c] == num) return false;
            }
        }

        return true;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }
}
