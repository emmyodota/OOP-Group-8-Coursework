// A student account has a low minimum deposit, but only for applicants aged 18 to 25
public class StudentAccount extends Account {

    // Passes all the details up to the Account class
    public StudentAccount(String accountNumber, String firstName, String lastName, String nin,
                          String email, String phoneNumber, String pin,
                          int dobYear, int dobMonth, int dobDay,
                          String branch, double openingDeposit) {
        super(accountNumber, firstName, lastName, nin, email, phoneNumber, pin,
              dobYear, dobMonth, dobDay, branch, openingDeposit);
    }

    // The smallest amount you can open this account with
    @Override
    public double getMinimumDeposit() {
        return 10000.0; // 10,000 UGX
    }

    // What makes a student account different
    @Override
    public String getSpecialRules() {
        return "Applicant age must be 18-25";
    }
}
