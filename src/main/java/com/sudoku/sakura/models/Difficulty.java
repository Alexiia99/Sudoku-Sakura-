package com.sudoku.sakura.models;

public enum Difficulty {
    EASY("Fácil", 30, 35, ".🌸🌸🌸"),
    NORMAL("Normal", 40, 45, "🌸🌸🌸🌸"),
    HARD("Difícil", 50, 55, "🌸🌸🌸🌸🌸");

    private final String displayName;
    private final int minEmptyCells;
    private final int maxEmptyCells;
    private final String stars;

    Difficulty(String displayName, int minEmptyCells, int maxEmptyCells, String stars) {
        this.displayName = displayName;
        this.minEmptyCells = minEmptyCells;
        this.maxEmptyCells = maxEmptyCells;
        this.stars = stars;
    }

    public String getDisplayName() { return displayName; }
    public int getMinEmptyCells() { return minEmptyCells; }
    public int getMaxEmptyCells() { return maxEmptyCells; }
    public String getStars() { return stars; }

    public int getRandomEmptyCells() {
        return minEmptyCells + (int)(Math.random() * (maxEmptyCells - minEmptyCells + 1));
    }
}