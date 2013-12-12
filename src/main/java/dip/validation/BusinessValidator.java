package dip.validation;

public interface BusinessValidator {
    String getViolationCode();

    Object getValue();

    boolean isValid();
}
