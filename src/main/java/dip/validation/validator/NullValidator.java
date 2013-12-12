package dip.validation.validator;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class NullValidator extends AbstractBusinessValidator {
    public NullValidator(String violationCode) {
        super(violationCode);
    }

    @Override
    public boolean isValid() {
        return (getValue() == null);
    }
}
