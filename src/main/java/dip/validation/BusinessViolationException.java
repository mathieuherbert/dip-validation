package dip.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public final class BusinessViolationException extends BusinessValidationException {
    private static final long serialVersionUID = 1L;

    private final List<Violation> parts;

    public BusinessViolationException(String violationCode, String message, Object value) {
        super(message);
        parts = new ArrayList<>(1);
        parts.add(new Violation(violationCode, message, value));
    }

    public BusinessViolationException(List<String> violationCodes, List<String> messages, List<Object> values) {
        super(messages.get(0));
        int s = Math.min(violationCodes.size(), messages.size());
        s = Math.min(s, values.size());
        parts = new ArrayList<>(s);
        for (int i = 0; i < s; i++) {
            parts.add(new Violation(violationCodes.get(i), messages.get(i), values.get(i)));
        }
    }

    public List<Violation> getParts() {
        return Collections.unmodifiableList(parts);
    }

    @Override
    public String toString() {
        return Pojomatic.toString(this);
    }

    @AutoProperty
    public static class Violation implements Serializable {
        private static final long serialVersionUID = 1L;

        private String code;
        private String message;
        private Object value;

        public Violation(String code, String message, Object value) {
            this.code = code;
            this.message = message;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return Pojomatic.toString(this);
        }
    }
}
