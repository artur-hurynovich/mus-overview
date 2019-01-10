package by.hurynovich.mus_overview.exception;

public class OverviewUpdatingException extends Exception {
    public OverviewUpdatingException() {
        super();
    }

    public OverviewUpdatingException(final String message) {
        super(message);
    }

    public OverviewUpdatingException(final Exception e) {
        super(e);
    }

    public OverviewUpdatingException(final String message, final Exception e) {
        super(message, e);
    }
}