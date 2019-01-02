package by.hurynovich.mus_overview.exception;

public class GroupCreationException extends Exception {
    public GroupCreationException() {
        super();
    }

    public GroupCreationException(final String message) {
        super(message);
    }

    public GroupCreationException(final Exception e) {
        super(e);
    }

    public GroupCreationException(final String message, final Exception e) {
        super(message, e);
    }
}
