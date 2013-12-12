package dip.validation.validator;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class NotNullValidator extends AbstractBusinessValidator {
    public NotNullValidator(String violationCode) {
        super(violationCode);
    }

    @Override
    public boolean isValid() {
        return (getValue() != null);
    }
}
