package dip.validation;

import java.util.Arrays;
import java.util.Collection;

import dip.validation.BusinessViolation;
import dip.validation.violation.BusinessViolationProvider;

public class BusinessViolationProviderImpl implements BusinessViolationProvider {

    static enum Violation implements BusinessViolation {
        V666("666", "Humhh, cela ne ressemble pas à une adresse email."), // could be a key to get i18n message
        V123("123", "La règle 123 n'est pas respectée."), //
        V456("456", "La règle 456 n'est pas respectée."), //
        V999("999", "La règle 999 n'est pas respectée.");

        String code;
        String message;

        Violation(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getCode() {
            return code;
        }
    }

    @Override
    public Collection<BusinessViolation> getViolations() {
        BusinessViolation[] violations = Violation.values();
        return Arrays.asList(violations);
    }
}