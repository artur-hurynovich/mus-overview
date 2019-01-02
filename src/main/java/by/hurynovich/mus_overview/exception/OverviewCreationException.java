package by.hurynovich.mus_overview.exception;

public class OverviewCreationException extends Exception {
    public OverviewCreationException() {
        super();
    }

    public OverviewCreationException(final String message) {
        super(message);
    }

    public OverviewCreationException(final Exception e) {
        super(e);
    }

    public OverviewCreationException(final String message, final Exception e) {
        super(message, e);
    }
}
