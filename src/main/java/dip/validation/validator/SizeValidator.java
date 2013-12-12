package dip.validation.validator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class SizeValidator extends AbstractBusinessValidator {
    private Integer min;
    private Integer max;

    public SizeValidator(String violationCode) {
        super(violationCode);
    }

    public SizeValidator(String violationCode, Integer min, Integer max) {
        this(violationCode);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid() {
        Object value = getValue();
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return doCheckFor(((String) value).length());
        }
        if (value instanceof Collection) {
            return doCheckFor(((Collection<?>) value).size());
        }
        if (value instanceof Map) {
            return doCheckFor(((Map<?, ?>) value).size());
        }
        if (value.getClass().isArray()) {
            return doCheckFor(Array.getLength(value));
        }
        throw new IllegalArgumentException("Cannot find 'size' property for " + value.getClass());
    }

    private boolean doCheckFor(int s) {
        return ((s >= min) && (s <= max));
    }
}
