// Holds all the form validation rules in one place, away from the GUI code.
public class BankValidator {

    // The email pattern we accept (letters/digits/some symbols, then @domain.tld)
    private static final String EMAIL_REGEX =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // First or last name: required, letters only, 2 to 30 characters
    public static String name(String value) {
        if (value.isEmpty()) return "Required.";
        if (!value.matches("^[a-zA-Z]+$")) return "Letters only.";
        if (value.length() < 2 || value.length() > 30) return "Must be 2-30 chars.";
        return "";
    }

    // National ID: required, exactly 14 uppercase letters/digits
    public static String nin(String value) {
        if (value.isEmpty()) return "NIN is required.";
        if (!value.matches("^[A-Z0-9]{14}$")) return "Exactly 14 uppercase alphanumeric chars.";
        return "";
    }

    // Second NIN for joint accounts: same rules as the first, plus it must differ
    public static String secondNin(String value, String firstNin) {
        if (value.isEmpty()) return "Second NIN is required.";
        if (!value.matches("^[A-Z0-9]{14}$")) return "Exactly 14 uppercase alphanumeric chars.";
        if (value.equals(firstNin)) return "Must differ from the first NIN.";
        return "";
    }

    // Email: required and must match the pattern above
    public static String email(String value) {
        if (value.isEmpty()) return "Email is required.";
        if (!value.matches(EMAIL_REGEX)) return "Invalid email format.";
        return "";
    }

    // Phone: required and in the Ugandan +256XXXXXXXXX format
    public static String phone(String value) {
        if (value.isEmpty()) return "Phone is required.";
        if (!value.matches("^\\+256\\d{9}$")) return "Must follow +256XXXXXXXXX.";
        return "";
    }

    // PIN: required, 4 to 6 digits, and not all the same digit
    public static String pin(String value) {
        if (value.isEmpty()) return "PIN is required.";
        if (!value.matches("^\\d{4,6}$")) return "Must be 4-6 digits.";
        if (value.chars().distinct().count() == 1) return "Must not be identical digits.";
        return "";
    }

    // Age limits: everyone must be 18 to 75, and students must be 18 to 25
    public static String age(int age, String type) {
        if (age < 18 || age > 75) return "Age must be 18-75.";
        if ("Student".equals(type) && (age < 18 || age > 25)) return "Student age must be 18-25.";
        return "";
    }

    // The minimum opening deposit (in UGX) for each account type
    public static double minimumDeposit(String type) {
        if ("Savings".equals(type)) return 50000.0;
        if ("Student".equals(type)) return 10000.0;
        if ("Current".equals(type)) return 200000.0;
        if ("Fixed Deposit".equals(type)) return 1000000.0;
        if ("Joint".equals(type)) return 100000.0;
        return 0.0;
    }
}
