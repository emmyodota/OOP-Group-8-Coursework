// A current account allows overdraft but doesn't earn interest
public class CurrentAccount extends Account {

    // Passes all the details up to the Account class
    public CurrentAccount(String accountNumber, String firstName, String lastName, String nin,
                          String email, String phoneNumber, String pin,
                          int dobYear, int dobMonth, int dobDay,
                          String branch, double openingDeposit) {
        super(accountNumber, firstName, lastName, nin, email, phoneNumber, pin,
              dobYear, dobMonth, dobDay, branch, openingDeposit);
    }

    // The smallest amount you can open this account with
    @Override
    public double getMinimumDeposit() {
        return 200000.0; // 200,000 UGX
    }

    // What makes a current account different
    @Override
    public String getSpecialRules() {
        return "Overdraft allowed, no interest";
    }
}
