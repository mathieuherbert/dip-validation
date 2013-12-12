package dip.validation;

import java.util.Collection;

/**
 * A macro business validator is a convenient class to run several validators regarding a same business rule. Thus, all
 * inner validators must share the same code and the same value.
 * 
 * @author nocquidant
 */
public class MacroBusinessValidator implements BusinessValidator {
  private BusinessValidator[] validators;

  private Collection<BusinessValidator> vccc;

  public MacroBusinessValidator() {

  }

  public MacroBusinessValidator(BusinessValidator... validators) {
    if (validators.length == 0) {
      throw new IllegalArgumentException("Please specify at least one validator.");
    }
    String code = validators[0].getViolationCode();
    Object value = validators[0].getValue();
    for (int i = 1; i < validators.length; i++) {
      if (!code.equals(validators[i].getViolationCode())) {
        throw new IllegalArgumentException("A macro validator is used for a single violation code.");
      }
      if (value != validators[i].getValue()) {
        throw new IllegalArgumentException("A macro validator is used for a single value.");
      }
    }
    this.validators = validators;
  }

  @Override
  public boolean isValid() {
    for (BusinessValidator each : validators) {
      if (!each.isValid()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getViolationCode() {
    return validators[0].getViolationCode();
  }

  @Override
  public Object getValue() {
    return validators[0].getValue();
  }

  public MacroBusinessValidator setValue(Object value) {
    for (BusinessValidator each : validators) {
      if (!(each instanceof BusinessValidatorMutable)) {
        throw new IllegalArgumentException(
            "Post setting the value for all validators requires implementing BusinessValidatorMutable");
      }
    }
    for (BusinessValidator each : validators) {
      ((BusinessValidatorMutable) each).setValue(value);
    }
    return this;
  }
}
