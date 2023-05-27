package view;

import controller.DataHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;

public class AvatarMenuFXMLController {
    private String username;
    private String password;
    public ImageView currentAvatar;
    public GridPane avatarTable;
    public VBox leftPanel;
    private final ArrayList<ImageView> allAvatars = new ArrayList<>();

    public void setUsernameAndPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @FXML
    public void initialize() {
        File imageDirectory = new File("src/main/resources/images/avatar");
        File randomAvatar = new File("src/main/resources/images/avatar/random.png");
        File browseAvatar = new File("src/main/resources/images/avatar/files.png");
        File guestAvatar = new File("src/main/resources/images/avatar/guest.png");
        File[] allFiles = imageDirectory.listFiles(pathname -> !pathname.isDirectory());
        if (allFiles == null) return;
        int columnCount = 4;
        int index = 0;
        for (File file : allFiles) {
            if (file.equals(guestAvatar)) continue;
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(file.getAbsolutePath()));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(100);
            if (file.equals(randomAvatar)) imageView.setOnMouseClicked(event -> chooseRandomAvatar());
            else if (file.equals(browseAvatar)) imageView.setOnMouseClicked(event -> chooseFromFiles());
            else {
                imageView.setOnMouseClicked(this::chooseAvatar);
                allAvatars.add(imageView);
            }
            avatarTable.getChildren().add(imageView);
            GridPane.setConstraints(imageView, index % columnCount, index / columnCount);
            index++;
        }
        if (User.getCurrentUser() == null) chooseRandomAvatar();
        else {
            for (ImageView avatar : allAvatars)
                if (avatar.getImage().getUrl().equals(User.getCurrentUser().getAvatarFilePath())) {
                    selectAvatar(avatar);
                    break;
                }
            currentAvatar.setImage(new Image(User.getCurrentUser().getAvatarFilePath()));
        }
    }

    public void chooseAvatar(MouseEvent mouseEvent) {
        selectAvatar((ImageView) mouseEvent.getSource());
    }

    public void chooseRandomAvatar() {
        int randomIndex = new Random().nextInt(allAvatars.size());
        selectAvatar(allAvatars.get(randomIndex));
    }

    public void chooseFromFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose an image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
        try {
            File selectedFile = fileChooser.showOpenDialog(StartMenu.stage);
            if (selectedFile == null) return;
            String copiedPath = "src/main/resources/images/avatar/custom/" + username + selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.'));
            Files.copy(selectedFile.toPath(), Path.of(copiedPath), StandardCopyOption.REPLACE_EXISTING);
            currentAvatar.setImage(new Image(new File(copiedPath).getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectAvatar(ImageView selectedAvatar) {
        for (ImageView avatar : allAvatars)
            avatar.setOpacity(1);
        if (selectedAvatar == null) return;
        selectedAvatar.setOpacity(0.5);
        currentAvatar.setImage(selectedAvatar.getImage());
    }

    public void cancelRegister() throws Exception {
        if (User.getCurrentUser() == null) new RegisterMenu(false).start(StartMenu.stage);
        else new ProfileMenu("").start(StartMenu.stage);
    }

    public void completeRegister() throws Exception {
        if (User.getCurrentUser() == null) {
            User.addUser(username, password, currentAvatar.getImage().getUrl());
            DataHandler.getInstance().updateDatabase();
            new RegisterMenu(true).start(StartMenu.stage);
        } else {
            User.getCurrentUser().setAvatarFilePath(currentAvatar.getImage().getUrl());
            new ProfileMenu("avatar changed successfully").start(StartMenu.stage);
        }
    }
}
