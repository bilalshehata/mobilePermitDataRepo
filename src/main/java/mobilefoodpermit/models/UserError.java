package mobilefoodpermit.models;


public class UserError {
    private final String message;

    public UserError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
