package dip.validation.validator;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class PastValidator extends AbstractBusinessValidator {
    private PastValidatorFor inner;

    public PastValidator(String violationCode) {
        super(violationCode);
    }

    public PastValidator(String violationCode, Object time) {
        this(violationCode);
        if (time instanceof Calendar) {
            inner = new PastValidatorForCalendar((Calendar) time);
        }
        if (time instanceof Date) {
            inner = new PastValidatorForDate((Date) time);
        }
        if (time instanceof ReadableInstant) {
            inner = new PastValidatorForReadableInstant((ReadableInstant) time);
        }
        if (time instanceof ReadablePartial) {
            inner = new PastValidatorForReadablePartial((ReadablePartial) time);
        }
        throw new IllegalArgumentException("Cannot manage time object: " + time);
    }

    @Override
    public boolean isValid() {
        return inner.isValid();
    }

    static interface PastValidatorFor {
        boolean isValid();
    }

    static class PastValidatorForCalendar implements PastValidatorFor {
        private Calendar cal;

        PastValidatorForCalendar(Calendar cal) {
            this.cal = cal;
        }

        @Override
        public boolean isValid() {
            // null values are valid
            if (cal == null) {
                return true;
            }
            return cal.before(Calendar.getInstance());
        }
    }

    static class PastValidatorForDate implements PastValidatorFor {
        private Date date;

        PastValidatorForDate(Date date) {
            this.date = date;
        }

        @Override
        public boolean isValid() {
            // null values are valid
            if (date == null) {
                return true;
            }
            return date.before(new Date());
        }
    }

    static class PastValidatorForReadableInstant implements PastValidatorFor {
        private ReadableInstant value;

        PastValidatorForReadableInstant(ReadableInstant value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            // null values are valid
            if (value == null) {
                return true;
            }
            return value.isBefore(null);
        }
    }

    static class PastValidatorForReadablePartial implements PastValidatorFor {
        private ReadablePartial value;

        PastValidatorForReadablePartial(ReadablePartial value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            // null values are valid
            if (value == null) {
                return true;
            }
            return value.toDateTime(null).isBeforeNow();
        }
    }

}
