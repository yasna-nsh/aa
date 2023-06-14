package model;

import javafx.scene.input.KeyCode;
import model.game.Difficulty;
import model.game.Game;
import model.game.GameScoringData;
import model.game.GameTemplate;
import view.CommonActions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final static ArrayList<User> allUsers = new ArrayList<>();
    private static User currentUser;
    private static User guest;
    private String username;
    private String password;
    private String avatarFilePath;
    private final HashMap<Difficulty, GameScoringData> highScores;
    private Game savedGame;
    private Difficulty defaultDifficulty;
    private GameTemplate defaultGameTemplate;
    private int defaultBallCount;
    private String defaultMusic;
    private ArrayList<KeyCode> defaultKeyCodes;
    private boolean isMusicMutedDefault;
    private boolean isSoundMutedDefault;
    private boolean isColorfulDefault;

    private User(String username, String password, String avatarFilePath) {
        this.username = username;
        this.password = password;
        this.avatarFilePath = avatarFilePath;
        highScores = new HashMap<>();
        savedGame = null;
        defaultDifficulty = Difficulty.NORMAL;
        defaultGameTemplate = GameTemplate.PENTAGON;
        for (Difficulty difficulty : Difficulty.values())
            highScores.put(difficulty, new GameScoringData(0, 0, defaultDifficulty, this.getUsername()));
        defaultBallCount = 10;
        String withExtension = new File(CommonActions.getAllTracks().get(0).getMedia().getSource()).getName();
        defaultMusic = withExtension.substring(0, withExtension.lastIndexOf('.'));
        defaultKeyCodes = new ArrayList<>();
        defaultKeyCodes.add(KeyCode.SPACE);
        defaultKeyCodes.add(KeyCode.TAB);
        defaultKeyCodes.add(KeyCode.RIGHT);
        defaultKeyCodes.add(KeyCode.LEFT);
        isMusicMutedDefault = false;
        isSoundMutedDefault = false;
        isColorfulDefault = true;
    }

    public static void addUser(String username, String password, String avatarFilePath) {
        User newUser = new User(username, password, avatarFilePath);
        allUsers.add(newUser);
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void updateAllUsers(ArrayList<User> allUsers) {
        User.allUsers.clear();
        if (allUsers != null) User.allUsers.addAll(allUsers);
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers)
            if (user.username.equals(username)) return user;
        return null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public static User getGuest() {
        File guestAvatar = new File("src/main/resources/images/avatar/guest.png");
        if (guest == null) guest = new User("", "", guestAvatar.getAbsolutePath());
        return guest;
    }

    public static void resetGuest() {
        guest = null;
    }

    public static ArrayList<GameScoringData> getScoreboardData(Difficulty selectedDifficulty) {
        ArrayList<GameScoringData> result = new ArrayList<>();
        GameScoringData gameScoringData;
        for (User user : allUsers) {
            if (selectedDifficulty == null) {
                for (Difficulty difficulty : Difficulty.values()) {
                    gameScoringData = user.getHighScore(difficulty);
                    if (gameScoringData != null) result.add(gameScoringData);
                }
            } else {
                gameScoringData = user.getHighScore(selectedDifficulty);
                if (gameScoringData != null) result.add(gameScoringData);
            }
        }
        result.sort((GameScoringData o1, GameScoringData o2) -> {
            if (o1.getScore() == o2.getScore() && o1.getTimeSpent() == o2.getTimeSpent())
                return o1.getUsername().compareTo(o2.getUsername());
            if (o1.getScore() > o2.getScore() || o1.getScore() == o2.getScore() && o1.getTimeSpent() < o2.getTimeSpent())
                return -1;
            return 1;
        });
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public void updateHighScore(Difficulty difficulty, int score, int timeSpent) {
        GameScoringData gameScoringData = highScores.get(difficulty);
        updateHighScoresUsername();
        if (gameScoringData.getScore() > score) return;
        if (gameScoringData.getScore() < score || gameScoringData.getTimeSpent() > timeSpent) {
            highScores.get(difficulty).setDifficulty(difficulty);
            highScores.get(difficulty).setScore(score);
            highScores.get(difficulty).setTimeSpent(timeSpent);
        }
    }

    public void updateHighScoresUsername() {
        for (Map.Entry<Difficulty, GameScoringData> highScore : highScores.entrySet())
            highScore.getValue().setUsername(this.getUsername());
    }

    public GameScoringData getHighScore(Difficulty difficulty) {
        GameScoringData result = highScores.get(difficulty);
        if (result.getScore() == 0 && result.getTimeSpent() == 0) return null;
        return result;
    }

    public Game getSavedGame() {
        return savedGame;
    }

    public void setSavedGame(Game savedGame) {
        this.savedGame = savedGame;
    }

    public Difficulty getDefaultDifficulty() {
        return defaultDifficulty;
    }

    public void setDefaultDifficulty(Difficulty defaultDifficulty) {
        this.defaultDifficulty = defaultDifficulty;
    }

    public GameTemplate getDefaultGameTemplate() {
        return defaultGameTemplate;
    }

    public void setDefaultGameTemplate(GameTemplate defaultGameTemplate) {
        this.defaultGameTemplate = defaultGameTemplate;
    }

    public int getDefaultBallCount() {
        return defaultBallCount;
    }

    public void setDefaultBallCount(int defaultBallCount) {
        this.defaultBallCount = defaultBallCount;
    }

    public String getDefaultMusic() {
        return defaultMusic;
    }

    public void setDefaultMusic(String defaultMusic) {
        this.defaultMusic = defaultMusic;
    }

    public ArrayList<KeyCode> getDefaultKeyCodes() {
        return defaultKeyCodes;
    }

    public void setDefaultKeyCodes(ArrayList<KeyCode> defaultKeyCodes) {
        this.defaultKeyCodes = defaultKeyCodes;
    }

    public boolean isMusicMutedDefault() {
        return isMusicMutedDefault;
    }

    public void setMusicMutedDefault(boolean musicMutedDefault) {
        isMusicMutedDefault = musicMutedDefault;
    }

    public boolean isSoundMutedDefault() {
        return isSoundMutedDefault;
    }

    public void setSoundMutedDefault(boolean soundMutedDefault) {
        isSoundMutedDefault = soundMutedDefault;
    }

    public boolean isColorfulDefault() {
        return isColorfulDefault;
    }

    public void setColorfulDefault(boolean colorfulDefault) {
        isColorfulDefault = colorfulDefault;
    }
}