package model.game;

import java.util.ArrayList;

public class Game {
    private static final int shootTimeInMillis = 1200;
    private static final int initialTimeInSecs = 120;
    private static final int freezeCapacity = 10;
    private static final int ballScore = 50;
    private static final int phaseScore = 200;
    private static final int timeScoreCoefficient = 10;
    private Difficulty difficulty;
    private final GameTemplate template;
    private final int ballCount;
    private int phase;
    private double shootingSiteX;
    private double shootingSiteY;
    private double shootAngle;
    private boolean isClockwise;
    private int timeSpent;
    private int freezeProgress;
    private final ArrayList<Ball> attachedBalls;
    private final ArrayList<Ball> remainingBalls;
    private int changeAngleSign;
    private int score;

    public Game(Difficulty difficulty, GameTemplate template, int ballCount) {
        this.difficulty = difficulty;
        this.template = template;
        this.ballCount = ballCount;
        phase = 1;
        shootAngle = 0;
        isClockwise = true;
        timeSpent = 0;
        freezeProgress = 0;
        attachedBalls = new ArrayList<>();
        remainingBalls = new ArrayList<>();
        for (double ballDegree : template.getBallDegrees())
            attachedBalls.add(new Ball(ballDegree));
        for (int i = 0; i < ballCount; i++) remainingBalls.add(new Ball());
        changeAngleSign = 1;
        score = 0;
    }

    public Game(Game game) {
        difficulty = game.difficulty;
        template = game.template;
        ballCount = game.ballCount;
        phase = game.phase;
        shootingSiteX = game.shootingSiteX;
        shootingSiteY = game.shootingSiteY;
        shootAngle = game.shootAngle;
        isClockwise = game.isClockwise;
        timeSpent = game.timeSpent;
        freezeProgress = game.freezeProgress;
        attachedBalls = new ArrayList<>(game.attachedBalls);
        remainingBalls = new ArrayList<>(game.remainingBalls);
        changeAngleSign = game.changeAngleSign;
        score = game.score;
    }

    public static int getShootTimeInMillis() {
        return shootTimeInMillis;
    }

    public static int getInitialTimeInSecs() {
        return initialTimeInSecs;
    }

    public static int getFreezeCapacity() {
        return freezeCapacity;
    }

    public static int getBallScore() {
        return ballScore;
    }

    public static int getPhaseScore() {
        return phaseScore;
    }

    public static int getTimeScoreCoefficient() {
        return timeScoreCoefficient;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public GameTemplate getTemplate() {
        return template;
    }

    public int getBallCount() {
        return ballCount;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public double getShootingSiteX() {
        return shootingSiteX;
    }

    public void setShootingSiteX(double shootingSiteX) {
        this.shootingSiteX = shootingSiteX;
    }

    public double getShootingSiteY() {
        return shootingSiteY;
    }

    public void setShootingSiteY(double shootingSiteY) {
        this.shootingSiteY = shootingSiteY;
    }

    public double getShootAngle() {
        return shootAngle;
    }

    public void changeShootAngle(double change) {
        shootAngle += changeAngleSign * change;
        if (shootAngle >= 15) {
            shootAngle = 15;
            changeAngleSign *= -1;
        } else if (shootAngle <= -15) {
            shootAngle = -15;
            changeAngleSign *= -1;
        }
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public void setClockwise(boolean clockwise) {
        isClockwise = clockwise;
    }

    public int getFreezeProgress() {
        return freezeProgress;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void increaseFreezeProgress() {
        if (freezeProgress < freezeCapacity) freezeProgress++;
    }

    public void resetFreezeProgress() {
        freezeProgress = 0;
    }

    public ArrayList<Ball> getAttachedBalls() {
        return attachedBalls;
    }

    public ArrayList<Ball> getRemainingBalls() {
        return remainingBalls;
    }

    public void attachBall(Ball ball) {
        remainingBalls.remove(ball);
        attachedBalls.add(ball);
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int change) {
        score += change;
    }
}