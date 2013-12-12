package dip.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * A multiple business validator is a convenient class to run several validators at once.
 * <p>
 * The method {@link #getInvalids()} returns validators which failed. It is used to build the resulting business
 * exception.
 * 
 * @author nocquidant
 */
public class MultipleBusinessValidator implements BusinessValidator {
    private BusinessValidator[] validators;
    private boolean[] invalidFlags;

    public MultipleBusinessValidator(BusinessValidator... validators) {
        if (validators.length == 0) {
            throw new IllegalArgumentException("Please specify at least one validator.");
        }
        this.validators = validators;
        invalidFlags = new boolean[validators.length];
    }

    @Override
    public boolean isValid() {
        boolean invalid = false;
        for (int i = 0; i < validators.length; i++) {
            if (!validators[i].isValid()) {
                invalidFlags[i] = true;
            }
            invalid |= invalidFlags[i];
        }
        return !invalid;
    }

    @Override
    public String getViolationCode() {
        throw new UnsupportedOperationException("Use getInvalidValidators() method.");
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Use getInvalidValidators() method.");
    }

    public List<BusinessValidator> getInvalids() {
        List<BusinessValidator> result = new ArrayList<>();
        for (int i = 0; i < validators.length; i++) {
            if (invalidFlags[i] == true) {
                result.add(validators[i]);
            }
        }
        return result;
    }
}
