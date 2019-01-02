package by.hurynovich.mus_overview.exception;

public class SubgroupCreationException extends Exception {
    public SubgroupCreationException() {
        super();
    }

    public SubgroupCreationException(final String message) {
        super(message);
    }

    public SubgroupCreationException(final Exception e) {
        super(e);
    }

    public SubgroupCreationException(final String message, final Exception e) {
        super(message, e);
    }
}
