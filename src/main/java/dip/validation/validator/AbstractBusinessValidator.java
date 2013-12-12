package dip.validation.validator;

import java.io.Serializable;

import dip.validation.BusinessValidatorMutable;

public abstract class AbstractBusinessValidator implements BusinessValidatorMutable {
  private String violationCode;

  private Serializable valueToValidate;

  protected AbstractBusinessValidator(String violationCode) {
    this.violationCode = violationCode;
  }

  protected AbstractBusinessValidator(String violationCode, Object valueToValidate) {
    checkSerializable(valueToValidate);
    this.violationCode = violationCode;
    this.valueToValidate = (Serializable) valueToValidate;
  }

  // Keep it private
  private void checkSerializable(Object valueToValidate) {
    if ((valueToValidate != null) && !(valueToValidate instanceof Serializable)) {
      throw new IllegalArgumentException("Object to validate must be Serializable.");
    }
  }

  @Override
  public String getViolationCode() {
    return violationCode;
  }

  @Override
  public Object getValue() {
    return valueToValidate;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends BusinessValidatorMutable> T setValue(Object valueToValidate) {
    checkSerializable(valueToValidate);
    this.valueToValidate = (Serializable) valueToValidate;
    return (T) this;
  }
}
