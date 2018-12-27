package by.hurynovich.mus_overview.exception;

public class OverviewCreationException extends Exception {
    public OverviewCreationException() {
        super();
    }

    public OverviewCreationException(String message) {
        super(message);
    }

    public OverviewCreationException(Exception e) {
        super(e);
    }

    public OverviewCreationException(String message, Exception e) {
        super(message, e);
    }
}
