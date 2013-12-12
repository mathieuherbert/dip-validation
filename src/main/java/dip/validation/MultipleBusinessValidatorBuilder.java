package dip.validation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A helper class to build {@link MultipleBusinessValidator} while keeping it immutable.
 * 
 * @author nocquidant
 */
public class MultipleBusinessValidatorBuilder {

  private Collection<BusinessValidator> validators = new ArrayList<>();

  public static MultipleBusinessValidatorBuilder getInstance() {
    return new MultipleBusinessValidatorBuilder();
  }

  public MultipleBusinessValidatorBuilder() {
  }

  public MultipleBusinessValidatorBuilder with(BusinessValidator aValue) {
    validators.add(aValue);
    return this;
  }

  public MultipleBusinessValidator build() {
    return new MultipleBusinessValidator(validators.toArray(new BusinessValidator[validators.size()]));
  }
}