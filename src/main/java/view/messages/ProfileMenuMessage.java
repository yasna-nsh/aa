package view.messages;

public enum ProfileMenuMessage {
    EMPTY_NEW_USERNAME("enter new username"),
    EMOTY_NEW_PASSWORD("enter new password"),
    EMPTY_PASSWORD("enter password"),
    INCORRECT_PASSWORD("password is incorrect"),
    TAKEN_USERNAME("username is taken"),
    CHANGE_USERNAME_SUCCESSFUL("username changed successfully"),
    CHANGE_PASSWORD_SUCCESSFUL("password changed successfully");

    private final String message;

    ProfileMenuMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
