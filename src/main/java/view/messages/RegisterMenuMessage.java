package view.messages;

public enum RegisterMenuMessage {
    EMPTY_USERNAME ("enter username"),
    EMPTY_PASSWORD ("enter password"),
    PASSWORDS_DONT_MATCH ("password and its confirmation don't match"),
    TAKEN_USERNAME ("username is taken"),
    SUCCESSFUL_REGISTER ("register successful");

    private final String message;

    RegisterMenuMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
