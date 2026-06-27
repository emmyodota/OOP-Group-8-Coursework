// A fixed deposit account locks money for a term but earns the highest interest
public class FixedDepositAccount extends Account {

    // Passes all the details up to the Account class
    public FixedDepositAccount(String accountNumber, String firstName, String lastName, String nin,
                               String email, String phoneNumber, String pin,
                               int dobYear, int dobMonth, int dobDay,
                               String branch, double openingDeposit) {
        super(accountNumber, firstName, lastName, nin, email, phoneNumber, pin,
              dobYear, dobMonth, dobDay, branch, openingDeposit);
    }

    // The smallest amount you can open this account with
    @Override
    public double getMinimumDeposit() {
        return 1000000.0; // 1,000,000 UGX
    }

    // What makes a fixed deposit account different
    @Override
    public String getSpecialRules() {
        return "Locked term, highest interest";
    }
}
