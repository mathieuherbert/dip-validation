package dip.validation.validator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class MaxValidator extends AbstractBusinessValidator {
  private long maxValue;

  public MaxValidator(String violationCode) {
    super(violationCode);
  }

  public MaxValidator(String violationCode, long maxValue) {
    this(violationCode);
    this.maxValue = maxValue;
  }

  @Override
  public boolean isValid() {
    Object value = getValue();
    if (value == null) {// null values are valid
      return true;
    }

    if (value instanceof BigDecimal) {
      return ((BigDecimal) value).compareTo(BigDecimal.valueOf(maxValue)) != 1;
    } else if (value instanceof BigInteger) {
      return ((BigInteger) value).compareTo(BigInteger.valueOf(maxValue)) != 1;
    } else if (value instanceof Number) {
      long longValue = ((Number) value).longValue();
      return longValue <= maxValue;
    } else if (value instanceof CharSequence) {
      try {
        return new BigDecimal(((CharSequence) value).toString()).compareTo(BigDecimal.valueOf(maxValue)) != 1;
      } catch (NumberFormatException nfe) {
        return false;
      }
    }
    throw new IllegalArgumentException("Cannot find 'value' property for " + value.getClass());
  }
}
