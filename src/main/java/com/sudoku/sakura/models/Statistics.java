package com.sudoku.sakura.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int totalHintsUsed;
    private int currentStreak;
    private int bestStreak;
    private Map<Difficulty, Long> bestTimes;
    private long totalTimePlayed;

    public Statistics() {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.totalHintsUsed = 0;
        this.currentStreak = 0;
        this.bestStreak = 0;
        this.totalTimePlayed = 0;
        this.bestTimes = new HashMap<>();
    }

    public void recordWin(Difficulty difficulty, long timeTaken, int hintsUsed) {
        gamesPlayed++;
        gamesWon++;
        totalHintsUsed += hintsUsed;
        totalTimePlayed += timeTaken;
        currentStreak++;

        if (currentStreak > bestStreak) {
            bestStreak = currentStreak;
        }

        if (!bestTimes.containsKey(difficulty) || timeTaken < bestTimes.get(difficulty)) {
            bestTimes.put(difficulty, timeTaken);
        }
    }

    public void recordLoss(long timePlayed, int hintsUsed) {
        gamesPlayed++;
        gamesLost++;
        totalHintsUsed += hintsUsed;
        totalTimePlayed += timePlayed;
        currentStreak = 0;
    }

    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) gamesWon / gamesPlayed * 100;
    }

    public long getAverageTime() {
        if (gamesPlayed == 0) return 0;
        return totalTimePlayed / gamesPlayed;
    }

    public String getBestTimeFormatted(Difficulty difficulty) {
        if (!bestTimes.containsKey(difficulty)) {
            return "--:--";
        }
        long time = bestTimes.get(difficulty);
        long minutes = time / 60;
        long seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getAverageTimeFormatted() {
        long time = getAverageTime();
        long minutes = time / 60;
        long seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void reset() {
        gamesPlayed = 0;
        gamesWon = 0;
        gamesLost = 0;
        totalHintsUsed = 0;
        currentStreak = 0;
        bestStreak = 0;
        totalTimePlayed = 0;
        bestTimes.clear();
    }

    // Getters
    public int getGamesPlayed() { return gamesPlayed; }
    public int getGamesWon() { return gamesWon; }
    public int getGamesLost() { return gamesLost; }
    public int getTotalHintsUsed() { return totalHintsUsed; }
    public int getCurrentStreak() { return currentStreak; }
    public int getBestStreak() { return bestStreak; }
    public long getTotalTimePlayed() { return totalTimePlayed; }
    public Map<Difficulty, Long> getBestTimes() { return bestTimes; }
}