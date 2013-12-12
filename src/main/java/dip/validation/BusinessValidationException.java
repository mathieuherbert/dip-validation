package dip.validation;

public class BusinessValidationException extends Exception {
    private static final long serialVersionUID = 1L;

    public BusinessValidationException() {
        super();
    }

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessValidationException(Throwable cause) {
        super(cause);
    }
}
