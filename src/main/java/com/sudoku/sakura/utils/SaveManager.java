package com.sudoku.sakura.utils;

import com.sudoku.sakura.models.GameState;
import com.sudoku.sakura.models.Statistics;
import java.io.*;

public class SaveManager {

    private static final String SAVE_DIR = "saves";
    private static final String GAME_SAVE_FILE = SAVE_DIR + "/game.dat";
    private static final String STATS_FILE = SAVE_DIR + "/stats.dat";

    public static boolean saveGame(GameState gameState) {
        try {
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(GAME_SAVE_FILE))) {
                oos.writeObject(gameState);
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    public static GameState loadGame() {
        File file = new File(GAME_SAVE_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(GAME_SAVE_FILE))) {
            return (GameState) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar: " + e.getMessage());
            return null;
        }
    }

    public static boolean hasSavedGame() {
        return new File(GAME_SAVE_FILE).exists();
    }

    public static void deleteSavedGame() {
        File file = new File(GAME_SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean saveStatistics(Statistics stats) {
        try {
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(STATS_FILE))) {
                oos.writeObject(stats);
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
            return false;
        }
    }

    public static Statistics loadStatistics() {
        File file = new File(STATS_FILE);
        if (!file.exists()) {
            return new Statistics();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STATS_FILE))) {
            return (Statistics) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
            return new Statistics();
        }
    }
}