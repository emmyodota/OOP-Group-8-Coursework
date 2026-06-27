// SavingsAccount is a type of Account, so it inherits everything from Account Superclass
public class SavingsAccount extends Account {

    // Just passes all the details up to the Account class to set up
    public SavingsAccount(String accountNumber, String firstName, String lastName, String nin,
                          String email, String phoneNumber, String pin,
                          int dobYear, int dobMonth, int dobDay,
                          String branch, double openingDeposit) {
        super(accountNumber, firstName, lastName, nin, email, phoneNumber, pin,
              dobYear, dobMonth, dobDay, branch, openingDeposit);
    }

    // The smallest amount you're allowed to open this account with
    @Override
    public double getMinimumDeposit() {
        return 50000.0; // 50,000 UGX
    }

    // The rules that make a savings account different from other accounts
    @Override
    public String getSpecialRules() {
        return "Earns interest, no overdraft";
    }
}
