package view.messages;

public enum LoginMenuMessage {
    EMPTY_USERNAME("enter username"),
    EMPTY_PASSWORD("enter password"),
    USER_NOT_FOUND("there is no user with this username"),
    INCORRECT_PASSWORD("password is incorrect"),
    LOGIN_SUCCESSFUL("login successful");

    private final String message;

    LoginMenuMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
