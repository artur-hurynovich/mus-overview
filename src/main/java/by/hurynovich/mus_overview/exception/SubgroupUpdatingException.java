package by.hurynovich.mus_overview.exception;

public class SubgroupUpdatingException extends Exception {
    public SubgroupUpdatingException() {
        super();
    }

    public SubgroupUpdatingException(final String message) {
        super(message);
    }

    public SubgroupUpdatingException(final Exception e) {
        super(e);
    }

    public SubgroupUpdatingException(final String message, final Exception e) {
        super(message, e);
    }
}
