package by.hurynovich.mus_overview.exception;

public class OverviewDeletingException extends Exception {
    public OverviewDeletingException() {
        super();
    }

    public OverviewDeletingException(final String message) {
        super(message);
    }

    public OverviewDeletingException(final Exception e) {
        super(e);
    }

    public OverviewDeletingException(final String message, final Exception e) {
        super(message, e);
    }
}