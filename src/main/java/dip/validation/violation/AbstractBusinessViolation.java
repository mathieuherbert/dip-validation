package dip.validation.violation;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;

import dip.validation.BusinessViolation;

public abstract class AbstractBusinessViolation implements BusinessViolation {
    private static final long serialVersionUID = 1L;

    @Property
    @Override
    public abstract String getMessage();

    @Property
    @Override
    public abstract String getCode();

    @Override
    public String toString() {
        return Pojomatic.toString(this);
    }
}
