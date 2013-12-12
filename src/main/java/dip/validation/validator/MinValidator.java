package dip.validation.validator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class MinValidator extends AbstractBusinessValidator {
    private long minValue;

    public MinValidator(String violationCode) {
        super(violationCode);
    }

    public MinValidator(String violationCode, long minValue) {
        this(violationCode);
        this.minValue = minValue;
    }

    @Override
    public boolean isValid() {
        Object value = getValue();
        if (value == null) { // null values are valid
            return true;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(BigDecimal.valueOf(minValue)) != -1;
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).compareTo(BigInteger.valueOf(minValue)) != -1;
        } else if (value instanceof Number) {
            long longValue = ((Number) value).longValue();
            return longValue >= minValue;
        } else if (value instanceof CharSequence) {
            try {
                return new BigDecimal(((CharSequence) value).toString()).compareTo(BigDecimal.valueOf(minValue)) != -1;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        throw new IllegalArgumentException("Cannot find 'value' property for " + value.getClass());
    }
}
