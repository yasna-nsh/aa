package view;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.User;
import model.game.GameTemplate;

import java.io.File;
import java.util.ArrayList;

public class CommonActions {
    private static final ColorAdjust colorAdjust = new ColorAdjust();
    private static final ArrayList<MediaPlayer> tracks = new ArrayList<>();

    static {
        File musicDirectory = new File("src/main/resources/music");
        File[] allFiles = musicDirectory.listFiles(pathname -> !pathname.isDirectory());
        MediaPlayer mediaPlayer;
        if (allFiles != null) {
            for (File file : allFiles) {
                mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                tracks.add(mediaPlayer);
            }
        }
    }

    public static void setColor(boolean hasColor, Pane root) {
        colorAdjust.setSaturation(hasColor ? 0 : -1);
        root.setEffect(colorAdjust);
    }

    public static ToggleButton[] initializeOptions(ToggleGroup toggleGroup, String defaultOption, String... options) {
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) oldValue.setSelected(true);
        });
        ToggleButton[] newToggles = new ToggleButton[options.length];
        for (int i = 0; i < newToggles.length; i++) {
            newToggles[i] = new ToggleButton(options[i]);
            newToggles[i].setFocusTraversable(false);
            newToggles[i].setToggleGroup(toggleGroup);
            newToggles[i].getStyleClass().add("option");
            if (defaultOption.equals(options[i])) newToggles[i].setSelected(true);
        }
        return newToggles;
    }

    public static Slider showBallCount(Label ballCountLabel) {
        Slider ballSlider = new Slider(GameTemplate.minBallCount, GameTemplate.maxBallCount, User.getCurrentUser().getDefaultBallCount());
        ballCountLabel.setText("ball count = " + String.format("%02d", (int) ballSlider.getValue()));
        ballSlider.setBlockIncrement(1);
        ballSlider.setOrientation(Orientation.VERTICAL);
        ballSlider.valueProperty().addListener(observable -> ballCountLabel.setText("ball count = " + String.format("%02d", (int) ballSlider.getValue())));
        return ballSlider;
    }

    public static ArrayList<ToggleButton> initializeMusic(ToggleGroup musicGroup) {
        ArrayList<ToggleButton> newButtons = new ArrayList<>();
        File musicDirectory = new File("src/main/resources/music");
        File[] allFiles = musicDirectory.listFiles(pathname -> !pathname.isDirectory());
        if (allFiles == null) return newButtons;
        ToggleButton newButton = null;
        for (File allFile : allFiles) {
            newButton = new ToggleButton();
            newButton.setFocusTraversable(false);
            newButton.setToggleGroup(musicGroup);
            newButton.setText(allFile.getName().substring(0, allFile.getName().lastIndexOf('.')));
            newButton.getStyleClass().add("option");
            newButtons.add(newButton);
            if (newButton.getText().equals(User.getCurrentUser().getDefaultMusic())) newButton.setSelected(true);
        }
        if (User.getCurrentUser().getDefaultMusic().isEmpty() && newButton != null) newButton.setSelected(true);
        return newButtons;
    }

    public static ArrayList<MediaPlayer> getAllTracks() {
        return tracks;
    }

    public static MediaPlayer getSelectedTrack() {
        for (MediaPlayer track : tracks)
            if (track.getMedia().getSource().contains(User.getCurrentUser().getDefaultMusic()))
                return track;
        return null;
    }

    public static void stopAllTracks() {
        for (MediaPlayer track : tracks)
            track.stop();
    }

    public static void pauseAllTracks() {
        for (MediaPlayer track : tracks)
            track.pause();
    }
}
