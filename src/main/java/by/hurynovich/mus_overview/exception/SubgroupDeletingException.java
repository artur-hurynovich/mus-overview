package by.hurynovich.mus_overview.exception;

public class SubgroupDeletingException extends Exception {
    public SubgroupDeletingException() {
        super();
    }

    public SubgroupDeletingException(final String message) {
        super(message);
    }

    public SubgroupDeletingException(final Exception e) {
        super(e);
    }

    public SubgroupDeletingException(final String message, final Exception e) {
        super(message, e);
    }
}
