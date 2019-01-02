package by.hurynovich.mus_overview.exception;

public class GroupDeletingException extends Exception {
    public GroupDeletingException() {
        super();
    }

    public GroupDeletingException(final String message) {
        super(message);
    }

    public GroupDeletingException(final Exception e) {
        super(e);
    }

    public GroupDeletingException(final String message, final Exception e) {
        super(message, e);
    }
}
