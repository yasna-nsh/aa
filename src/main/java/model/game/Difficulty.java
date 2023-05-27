package model.game;

public enum Difficulty {
    EASY("easy", 3, 1.2, 7),
    NORMAL("normal", 2, 1.5, 5),
    HARD("hard", 1, 1.8, 3);

    private final String name;
    private final int spinDuration;
    private final double windSpeed;
    private final int freezeTime;

    Difficulty(String name, int spinDuration, double windSpeed, int freezeTime) {
        this.name = name;
        this.spinDuration = spinDuration;
        this.windSpeed = windSpeed;
        this.freezeTime = freezeTime;
    }

    public static String[] getStringValues() {
        String[] result = new String[values().length];
        for (int i = 0; i < result.length; i++) result[i] = values()[i].getName();
        return result;
    }

    public static Difficulty getDifficultyByName(String name) {
        for (Difficulty difficulty : values())
            if (difficulty.name.equalsIgnoreCase(name)) return difficulty;
        return null;
    }

    public String getName() {
        return name;
    }

    public int getSpinDuration() {
        return spinDuration;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getFreezeTime() {
        return freezeTime;
    }
}
