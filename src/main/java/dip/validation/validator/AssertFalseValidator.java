package dip.validation.validator;


/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class AssertFalseValidator extends AbstractBusinessValidator {
  public AssertFalseValidator(String violationCode) {
    super(violationCode);
  }

  @Override
  public boolean isValid() {
    Object value = getValue();
    // null values are valid
    if (value == null) {
      return true;
    }
    if (value instanceof Boolean) {
      return !((Boolean) value);
    }
    throw new IllegalArgumentException("Cannot find 'boolean' property for " + value.getClass());
  }
}
