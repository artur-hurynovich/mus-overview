package by.hurynovich.mus_overview.exception;

public class GroupCreationException extends Exception {
    public GroupCreationException() {
        super();
    }

    public GroupCreationException(String message) {
        super(message);
    }

    public GroupCreationException(Exception e) {
        super(e);
    }

    public GroupCreationException(String message, Exception e) {
        super(message, e);
    }
}
