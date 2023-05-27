module aa {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    exports view;
    opens view to javafx.fxml;
    exports view.transitions;
    opens view.transitions to javafx.fxml;
    exports model.game;
    opens model.game to javafx.fxml, com.google.gson;
    exports controller;
    opens controller to com.google.gson;
    exports model;
    opens model to com.google.gson;
    exports view.messages;
}