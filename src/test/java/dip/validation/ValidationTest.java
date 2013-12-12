package dip.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


import dip.validation.BusinessValidator;
import dip.validation.BusinessViolationException;
import dip.validation.MacroBusinessValidator;
import dip.validation.MultipleBusinessValidator;
import dip.validation.BusinessViolationProviderImpl.Violation;
import dip.validation.config.BusinessValidationConfig;
import dip.validation.validator.AbstractBusinessValidator;
import dip.validation.validator.EmailValidator;
import dip.validation.validator.NotNullValidator;
import dip.validation.validator.SizeValidator;
import dip.validation.violation.BusinessViolationExceptionBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessValidationConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ValidationTest {
    static {
        System.setProperty("violation.base.dir", "dip.validation");
    }

    @Autowired
    BusinessViolationExceptionBuilder builder;

    @Test
    public void validationRight() {
        try {
            EmailValidator validator = new EmailValidator("666");
            validator.setValue("monemail@macomp.com");
            if (!validator.isValid()) {
                throw builder.buildException(validator);
            }

        } catch (BusinessViolationException e) {
            Assert.fail("No BusinessViolationException should occured.");
        }
    }

    @Test
    public void validationWrong() {
        try {
            EmailValidator validator = new EmailValidator("666").setValue("monemail@");

            if (!validator.isValid()) {
                throw builder.buildException(validator);
            }

            Assert.fail("BusinessViolationExceptionBuilder should have been thrown here.");
        } catch (BusinessViolationException e) {
            Assert.assertEquals(1, e.getParts().size());
            Assert.assertEquals("monemail@", e.getParts().get(0).getValue());
            Assert.assertEquals(Violation.V666.getMessage(), e.getParts().get(0).getMessage());
        }
    }

    @Test
    public void validationMacro() {
        try {
            NotNullValidator validator1 = new NotNullValidator("123");
            SizeValidator validator2 = new SizeValidator("123", 4, 6);

            MacroBusinessValidator validator = new MacroBusinessValidator(validator1, validator2);
            if (!validator.setValue("val").isValid()) {
                throw builder.buildException(validator);
            }

            Assert.fail("BusinessViolationExceptionBuilder should have been thrown here.");
        } catch (BusinessViolationException e) {
            Assert.assertEquals(1, e.getParts().size());
        }
    }

    @Test
    public void validationMultiple() {
        try {
            NotNullValidator validator1 = new NotNullValidator("123").setValue(null);
            SizeValidator validator2 = new SizeValidator("456", 5, 6).setValue("val2");

            MultipleBusinessValidator validator = new MultipleBusinessValidator(validator1, validator2);
            if (!validator.isValid()) {
                throw builder.buildException(validator);
            }

            Assert.fail("BusinessViolationExceptionBuilder should have been thrown here.");
        } catch (BusinessViolationException e) {
            Assert.assertEquals(2, e.getParts().size());
        }
    }

    @Test
    public void validationCustom() {
        try {
            final Integer[] values = new Integer[] { 4, 5, 6 };
            BusinessValidator validator = new AbstractBusinessValidator("999", values) {

                @Override
                public boolean isValid() {
                    for (Integer element : values) {
                        if (element > 5) {
                            return false;
                        }
                    }
                    return true;
                }
            };

            if (!validator.isValid()) {
                throw builder.buildException(validator);
            }

            Assert.fail("BusinessViolationExceptionBuilder should have been throw here.");
        } catch (BusinessViolationException e) {
            Assert.assertEquals(1, e.getParts().size());
        }
    }
}