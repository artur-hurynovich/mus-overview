package by.hurynovich.mus_overview.exception;

public class SubgroupCreationException extends Exception {
    public SubgroupCreationException() {
        super();
    }

    public SubgroupCreationException(String message) {
        super(message);
    }

    public SubgroupCreationException(Exception e) {
        super(e);
    }

    public SubgroupCreationException(String message, Exception e) {
        super(message, e);
    }
}
