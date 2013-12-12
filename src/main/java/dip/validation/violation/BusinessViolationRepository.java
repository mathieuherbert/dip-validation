package dip.validation.violation;

import dip.validation.BusinessViolation;

public interface BusinessViolationRepository {
    BusinessViolation getViolation(String code);
}
