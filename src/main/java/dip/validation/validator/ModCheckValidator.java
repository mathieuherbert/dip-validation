package dip.validation.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Mod check validator for MOD10 and MOD11 algorithms http://en.wikipedia.org/wiki/Luhn_algorithm
 * http://en.wikipedia.org/wiki/Check_digit
 * <p>
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class ModCheckValidator extends AbstractBusinessValidator {
    public static enum ModType {
        /**
         * Represents a MOD10 algorithm (Also known as Luhn algorithm)
         */
        MOD10,
        /**
         * Represents a MOD11 algorithm
         */
        MOD11
    }

    private static final String NUMBERS_ONLY_REGEXP = "[^0-9]";
    private static final int DEC_RADIX = 10;

    /**
     * Multiplier used by the mod algorithms
     */
    private int multiplier;

    /**
     * The start index for the checksum calculation
     */
    private int startIndex;

    /**
     * The end index for the checksum calculation
     */
    private int endIndex;

    /**
     * The index of the checksum digit
     */
    private int checkDigitIndex;

    /**
     * The type of checksum algorithm
     */
    private ModType modType;

    private boolean ignoreNonDigitCharacters;

    public ModCheckValidator(String violationCode) {
        super(violationCode);
    }

    public ModCheckValidator(String violationCode, ModType modType, int multiplier, int startIndex, int endIndex,
            int checkDigitIndex, boolean ignoreNonDigitCharacters) {
        super(violationCode);

        this.modType = modType;
        this.multiplier = multiplier;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.checkDigitIndex = checkDigitIndex;
        this.ignoreNonDigitCharacters = ignoreNonDigitCharacters;

        if (this.startIndex < 0) {
            throw new IllegalArgumentException("Start index cannot be negative: " + this.startIndex);
        }

        if (this.endIndex < 0) {
            throw new IllegalArgumentException("End index cannot be negative: " + this.endIndex);
        }

        if (this.startIndex > this.endIndex) {
            throw new IllegalArgumentException("Invalid range: " + this.startIndex + " > " + this.endIndex);
        }

        if ((checkDigitIndex > 0) && (startIndex <= checkDigitIndex) && (endIndex > checkDigitIndex)) {
            throw new IllegalArgumentException("An explicitly specified check digit must lie outside the interval: ["
                    + this.startIndex + ", " + this.endIndex + "].");
        }
    }

    @Override
    public boolean isValid() {
        if (getValue() == null) { // null values are valid
            return true;
        }

        if (!(getValue() instanceof CharSequence)) {
            throw new IllegalArgumentException("Value must be instance of CharSequence: " + getValue().getClass());
        }

        CharSequence value = (CharSequence) getValue();

        String valueAsString = value.toString();
        if (ignoreNonDigitCharacters) {
            valueAsString = valueAsString.replaceAll(NUMBERS_ONLY_REGEXP, "");
        }

        try {
            valueAsString = extractVerificationString(valueAsString);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        List<Integer> digits;
        try {
            digits = extractDigits(valueAsString);
        } catch (NumberFormatException e) {
            return false;
        }

        boolean isValid;

        if (modType.equals(ModType.MOD10)) {
            isValid = passesMod10Test(digits, multiplier);
        } else {
            isValid = passesMod11Test(digits, multiplier);
        }
        return isValid;
    }

    private String extractVerificationString(String value) throws IndexOutOfBoundsException {
        // the whole string should be verified (check digit is implicit)
        if (endIndex == Integer.MAX_VALUE) {
            return value;
        }

        String verificationString = value.substring(startIndex, endIndex);

        // append the check digit of explicitly specified
        if (checkDigitIndex > 0) {
            verificationString = verificationString + value.charAt(checkDigitIndex);
        }

        return verificationString;
    }

    /**
     * Parses the {@link String} value as a {@link List} of {@link Integer} objects
     * 
     * @param value
     *            the input string to be parsed
     * 
     * @return List of {@code Integer} objects.
     * 
     * @throws NumberFormatException
     *             in case ant of the characters is not a digit
     */
    private List<Integer> extractDigits(final String value) throws NumberFormatException {
        List<Integer> digits = new ArrayList<Integer>(value.length());
        char[] chars = value.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                digits.add(Character.digit(c, DEC_RADIX));
            } else {
                throw new IllegalArgumentException("Character: " + c + " is not a digit.");
            }
        }
        return digits;
    }

    /**
     * Mod10 (Luhn) algorithm implementation
     * 
     * @param digits
     *            The digits over which to calculate the checksum
     * @param multiplier
     *            Multiplier used in the algorithm
     * 
     * @return {@code true} if the mod 10 result matches the check digit, {@code false} otherwise
     */
    public static boolean passesMod10Test(final List<Integer> digits, final int multiplier) {
        int modResult = mod10(digits, multiplier);
        return modResult == 0;
    }

    /**
     * Calculate Mod10 (Luhn)
     * 
     * @param digits
     *            the digits for which to calculate the checksum
     * @param multiplier
     *            multiplier for the modulo algorithm
     * 
     * @return the result of the mod10 calculation
     */
    private static int mod10(final List<Integer> digits, final int multiplier) {
        int sum = 0;
        boolean even = false;
        for (int index = digits.size() - 1; index >= 0; index--) {
            int digit = digits.get(index);
            if (even) {
                digit *= multiplier;
            }
            if (digit > 9) {
                digit = (digit / 10) + (digit % 10);
            }
            sum += digit;
            even = !even;
        }
        return sum % 10;
    }

    /**
     * Check if the input passes the mod11 test
     * 
     * @param digits
     *            The digits over which to calculate the mod 11 algorithm
     * @param multiplier
     *            the multiplier for the modulo algorithm
     * 
     * @return {@code true} if the mod 11 result matches the check digit, {@code false} otherwise
     */
    public static boolean passesMod11Test(final List<Integer> digits, int multiplier) {
        int modResult = mod11(digits, multiplier);
        return modResult == 0;
    }

    /**
     * Calculate Mod11
     * 
     * @param digits
     *            the digits for which to calculate the checksum
     * @param multiplier
     *            multiplier for the modulo algorithm
     * 
     * @return the result of the mod11 calculation
     */
    private static int mod11(final List<Integer> digits, final int multiplier) {
        int sum = 0;
        int weight = 1;

        for (int index = digits.size() - 1; index >= 0; index--) {
            sum += digits.get(index) * weight++;
            if (weight > multiplier) {
                weight = 2;
            }
        }
        int mod = 11 - (sum % 11);
        return (mod > 9) ? 0 : mod;
    }
}