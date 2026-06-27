public abstract class Account {

    // setting private variables for the accounts
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String nin;
    private String email;
    private String phoneNumber;
    private String pin;
    private int dobYear;
    private int dobMonth;
    private int dobDay;
    private String branch;
    private double openingDeposit;

    // this runs when we create an account
    public Account(String accountNumber, String firstName, String lastName, String nin,
                   String email, String phoneNumber, String pin,
                   int dobYear, int dobMonth, int dobDay,
                   String branch, double openingDeposit) {
        this.accountNumber = accountNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.dobYear = dobYear;
        this.dobMonth = dobMonth;
        this.dobDay = dobDay;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
    }

    // abstract methods that child classes will fill in
    public abstract double getMinimumDeposit();
    public abstract String getSpecialRules();

    // creating getter methods for the private variables
    public String getAccountNumber() { return accountNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNin() { return nin; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPin() { return pin; }
    public int getDobYear() { return dobYear; }
    public int getDobMonth() { return dobMonth; }
    public int getDobDay() { return dobDay; }
    public String getBranch() { return branch; }
    public double getOpeningDeposit() { return openingDeposit; }

    // returns account summary in the GUI window
    @Override
    public String toString() {
        String typeName = this.getClass().getSimpleName().replace("Account", "");
        if ("FixedDeposit".equals(typeName)) {
            typeName = "Fixed Deposit";
        }
        return "ACC: " + accountNumber + " | " + firstName + " " + lastName +
               " | " + typeName + " | " + branch + " | DOB " + dobYear + "-" + String.format("%02d", dobMonth) + "-" + String.format("%02d", dobDay) +
               " | " + phoneNumber + " | Deposit " + String.format("%,.0f", openingDeposit) + " | " + email;
    }
}
