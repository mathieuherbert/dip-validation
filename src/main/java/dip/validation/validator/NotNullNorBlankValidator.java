package dip.validation.validator;

public class NotNullNorBlankValidator extends AbstractBusinessValidator {
  public NotNullNorBlankValidator(String violationCode) {
    super(violationCode);
  }

  @Override
  public boolean isValid() {
    if (getValue() == null) {
      return false;
    }

    if (!(getValue() instanceof CharSequence)) {
      throw new IllegalArgumentException("Value must be instance of CharSequence: " + getValue().getClass());
    }

    CharSequence value = (CharSequence) getValue();
    return value.toString().trim().length() > 0;
  }
}
