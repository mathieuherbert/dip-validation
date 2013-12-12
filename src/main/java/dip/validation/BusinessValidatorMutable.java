package dip.validation;

public interface BusinessValidatorMutable extends BusinessValidator {
    <T extends BusinessValidatorMutable> T setValue(Object valueToValidate);
}
