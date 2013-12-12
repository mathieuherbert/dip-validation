package dip.validation;

import java.io.Serializable;

public interface BusinessViolation extends Serializable {
    String getMessage();

    String getCode();
}
