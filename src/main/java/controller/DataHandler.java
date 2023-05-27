package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DataHandler {
    private static final DataHandler instance = new DataHandler();

    private DataHandler() {
    }

    public static DataHandler getInstance() {
        return instance;
    }

    public void updateDatabase() {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/Data/userData.json");
            fileWriter.write(new Gson().toJson(User.getAllUsers()));
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInAppData() {
        try {
            User.updateAllUsers(new Gson().fromJson(new String(Files.readAllBytes(Path.of("src/main/resources/Data/userData.json")))
                    , new TypeToken<List<User>>() {}.getType()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}