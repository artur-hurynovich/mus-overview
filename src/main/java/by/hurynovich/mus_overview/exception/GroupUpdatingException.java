package by.hurynovich.mus_overview.exception;

public class GroupUpdatingException extends Exception {
    public GroupUpdatingException() {
        super();
    }

    public GroupUpdatingException(final String message) {
        super(message);
    }

    public GroupUpdatingException(final Exception e) {
        super(e);
    }

    public GroupUpdatingException(final String message, final Exception e) {
        super(message, e);
    }
}