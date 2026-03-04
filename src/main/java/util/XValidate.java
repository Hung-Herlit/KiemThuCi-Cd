package util;

import java.util.Date;

/**
 *
 * @author hungddv
 */
public class XValidate {

    public static boolean isNothing(String str) {
        return str == null || str.isBlank() || str.isEmpty();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidForNumber(String str) {
        return !isNothing(str) && isInteger(str);
    }

    public static boolean isValidEmail(String str) {
        return str != null && str.matches("\\w+@\\w+(\\.\\w+){1,2}");
    }

    public static boolean isValidDate(String str) {
        try {
            Date ngay = XDate.parse(str, "MM-dd-yyyy");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidFloat(String str) {
        try {
            Double.valueOf(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidLength(String str, int min, int max) {
        return str != null && str.length() >= min && str.length() <= max;
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean containsSpecialChar(String str) {
        return str != null && !str.matches("[a-zA-Z0-9\\s]+");
    }

    public static boolean isValidPhoneNumber(String str) {
        return str != null && str.matches("0\\d{9}");
    }

    public static boolean isValidPassword(String str) {
        return str != null && str.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$");
    }

    public static boolean validateText(String str, int min, int max) {
    return !isNothing(str)
            && !containsSpecialChar(str)
                && isValidLength(str, min, max);
    }
}
