package dip.validation.violation;

import java.util.ArrayList;
import java.util.List;

import dip.validation.BusinessValidator;
import dip.validation.BusinessViolation;
import dip.validation.BusinessViolationException;
import dip.validation.MultipleBusinessValidator;

/**
 * Build and fill a {@link BusinessViolationException} for a given validator.
 * 
 * @see {@link #buildException(BusinessValidator)}
 * 
 * @author nocquidant
 */
public class BusinessViolationExceptionBuilder {
    private BusinessViolationRepository repo;

    protected BusinessViolationExceptionBuilder() {

    }

    public BusinessViolationExceptionBuilder(BusinessViolationRepository repo) {
        this.repo = repo;
    }

    private void checkViolation(BusinessViolation violation, String code) {
        if (violation == null) {
            throw new IllegalStateException("Please check that a business violation is defined for the code: " + code);
        }
    }

    public BusinessViolationException buildException(BusinessValidator validator) {
        if (validator instanceof MultipleBusinessValidator) {
            return buildExceptionMultiple((MultipleBusinessValidator) validator);
        }
        BusinessViolation violation = repo.getViolation(validator.getViolationCode());
        checkViolation(violation, validator.getViolationCode());
        return new BusinessViolationException(violation.getCode(), violation.getMessage(), validator.getValue());
    }

    private BusinessViolationException buildExceptionMultiple(MultipleBusinessValidator validator) {
        List<String> codes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        for (BusinessValidator each : validator.getInvalids()) {
            String code = each.getViolationCode();
            BusinessViolation violation = repo.getViolation(code);
            checkViolation(violation, code);
            messages.add(violation.getMessage());
            codes.add(code);
            values.add(each.getValue());
        }

        return new BusinessViolationException(codes, messages, values);
    }
}
