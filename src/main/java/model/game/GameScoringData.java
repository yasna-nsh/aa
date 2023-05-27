package model.game;

import model.User;

public class GameScoringData {
    private int score;
    private int timeSpent;
    private Difficulty difficulty;
    private String username;

    public GameScoringData(int score, int timeSpent, Difficulty difficulty, String username) {
        this.score = score;
        this.timeSpent = timeSpent;
        this.difficulty = difficulty;
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
