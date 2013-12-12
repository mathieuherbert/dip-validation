package dip.validation.violation;

import java.util.Collection;

import dip.validation.BusinessViolation;

public interface BusinessViolationProvider {
    Collection<BusinessViolation> getViolations();
}
