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
public class FutureValidator extends AbstractBusinessValidator {
  private FutureValidatorFor inner;

  public FutureValidator(String violationCode) {
    super(violationCode);
  }

  public FutureValidator(String violationCode, Object time) {
    this(violationCode);
    if (time instanceof Calendar) {
      inner = new FutureValidatorForCalendar((Calendar) time);
    }
    if (time instanceof Date) {
      inner = new FutureValidatorForDate((Date) time);
    }
    if (time instanceof ReadableInstant) {
      inner = new FutureValidatorForReadableInstant((ReadableInstant) time);
    }
    if (time instanceof ReadablePartial) {
      inner = new FutureValidatorForReadablePartial((ReadablePartial) time);
    }
    throw new IllegalArgumentException("Cannot manage time object: " + time);
  }

  @Override
  public boolean isValid() {
    return inner.isValid();
  }

  static interface FutureValidatorFor {
    boolean isValid();
  }

  static class FutureValidatorForCalendar implements FutureValidatorFor {
    private Calendar cal;

    FutureValidatorForCalendar(Calendar cal) {
      this.cal = cal;
    }

    @Override
    public boolean isValid() {
      // null values are valid
      if (cal == null) {
        return true;
      }
      return cal.after(Calendar.getInstance());
    }
  }

  static class FutureValidatorForDate implements FutureValidatorFor {
    private Date date;

    FutureValidatorForDate(Date date) {
      this.date = date;
    }

    @Override
    public boolean isValid() {
      // null values are valid
      if (date == null) {
        return true;
      }
      return date.after(new Date());
    }
  }

  static class FutureValidatorForReadableInstant implements FutureValidatorFor {
    private ReadableInstant value;

    FutureValidatorForReadableInstant(ReadableInstant value) {
      this.value = value;
    }

    @Override
    public boolean isValid() {
      // null values are valid
      if (value == null) {
        return true;
      }

      return value.isAfter(null);
    }
  }

  static class FutureValidatorForReadablePartial implements FutureValidatorFor {
    private ReadablePartial value;

    FutureValidatorForReadablePartial(ReadablePartial value) {
      this.value = value;
    }

    @Override
    public boolean isValid() {
      // null values are valid
      if (value == null) {
        return true;
      }

      return value.toDateTime(null).isAfterNow();
    }
  }

}
