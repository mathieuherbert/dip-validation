package dip.validation.validator;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class NotBlankValidator extends AbstractBusinessValidator {
    public NotBlankValidator(String violationCode) {
        super(violationCode);
    }

    @Override
    public boolean isValid() {
        if (getValue() == null) { // null values are valid
            return true;
        }

        if (!(getValue() instanceof CharSequence)) {
            throw new IllegalArgumentException("Value must be instance of CharSequence: " + getValue().getClass());
        }

        CharSequence value = (CharSequence) getValue();
        return value.toString().trim().length() > 0;
    }
}