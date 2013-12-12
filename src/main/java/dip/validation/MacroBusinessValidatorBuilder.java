package dip.validation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A helper class to build {@link MacroBusinessValidator} while keeping it immutable.
 * 
 * @author nocquidant
 */
public class MacroBusinessValidatorBuilder {

  private Collection<BusinessValidator> validators = new ArrayList<>();

  public static MacroBusinessValidatorBuilder getInstance() {
    return new MacroBusinessValidatorBuilder();
  }

  public MacroBusinessValidatorBuilder() {
  }

  public MacroBusinessValidatorBuilder with(BusinessValidator aValue) {
    validators.add(aValue);
    return this;
  }

  public MacroBusinessValidator build() {
    return new MacroBusinessValidator(validators.toArray(new BusinessValidator[validators.size()]));
  }
}