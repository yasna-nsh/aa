package model.game;

public enum GameTemplate {
    BLANK("blank"),
    PENTAGON("pentagon", 5),
    OCTAGON("octagon", 8),
    SMILEY_FACE("smiley face", 0, 60, 140, 160, 180, 200, 220, 240, 260, 280);

    public static final int minBallCount = 5;
    public static final int maxBallCount = 40;
    private final String templateName;
    private final double[] ballDegrees;

    GameTemplate(String templateName, double... ballDegrees) {
        this.templateName = templateName;
        this.ballDegrees = ballDegrees;
    }

    GameTemplate(String templateName, int count) {
        this.templateName = templateName;
        double[] degrees = new double[count];
        for (int i = 0; i < count; i++)
            degrees[i] = (double) i * 360 / count;
        ballDegrees = degrees;
    }

    public static String[] getStringValues() {
        String[] result = new String[values().length];
        for (int i = 0; i < result.length; i++) result[i] = values()[i].getTemplateName();
        return result;
    }

    public static GameTemplate getTemplateByName(String name) {
        for (GameTemplate gameTemplate : values())
            if (gameTemplate.templateName.equalsIgnoreCase(name)) return gameTemplate;
        return null;
    }

    public String getTemplateName() {
        return templateName;
    }

    public double[] getBallDegrees() {
        return ballDegrees;
    }
}
