// A joint account is shared by two people, so it needs a second person's National ID
public class JointAccount extends Account {
    // Extra field that only joint accounts have
    private String secondaryNin;

    // Passes the shared details up to Account, then stores the second NIN
    public JointAccount(String accountNumber, String firstName, String lastName, String nin,
                        String secondaryNin, String email, String phoneNumber, String pin,
                        int dobYear, int dobMonth, int dobDay,
                        String branch, double openingDeposit) {
        super(accountNumber, firstName, lastName, nin, email, phoneNumber, pin,
              dobYear, dobMonth, dobDay, branch, openingDeposit);
        this.secondaryNin = secondaryNin;
    }

    // Lets other classes read the second person's NIN
    public String getSecondaryNin() {
        return secondaryNin;
    }

    // The smallest amount you can open this account with
    @Override
    public double getMinimumDeposit() {
        return 100000.0; // 100,000 UGX
    }

    // What makes a joint account different
    @Override
    public String getSpecialRules() {
        return "Requires a second NIN";
    }
}
