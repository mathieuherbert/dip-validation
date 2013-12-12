package dip.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class PatternValidator extends AbstractBusinessValidator {
    private Pattern pattern;

    public PatternValidator(String violationCode) {
        super(violationCode);
    }

    public PatternValidator(String violationCode, Pattern pattern) {
        this(violationCode);
        this.pattern = pattern;
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

        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}
