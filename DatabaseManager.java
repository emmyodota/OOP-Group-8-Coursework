import java.sql.*;

// this class handles saving to our sqlite database
public class DatabaseManager {

    // where to find(or create) the database file
    private static final String DB_URL = "jdbc:sqlite:FirstBank.db";

    // creating our accounts table
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Accounts (" +
                     "account_number TEXT PRIMARY KEY, " +
                     "first_name TEXT, " +
                     "last_name TEXT, " +
                     "nin TEXT, " +
                     "email TEXT, " +
                     "phone TEXT, " +
                     "branch TEXT, " +
                     "account_type TEXT, " +
                     "opening_deposit REAL" +
                     ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("SQLite database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    // saving accounts to the database using INSERT INTO
    public static void saveAccount(Account acc) {
        // The '?' placeholders get filled in safely below. This stops SQL injection.
        String sql = "INSERT INTO Accounts (account_number, first_name, last_name, nin, " +
                     "email, phone, branch, account_type, opening_deposit) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, acc.getAccountNumber());
            pstmt.setString(2, acc.getFirstName());
            pstmt.setString(3, acc.getLastName());
            pstmt.setString(4, acc.getNin());
            pstmt.setString(5, acc.getEmail());
            pstmt.setString(6, acc.getPhoneNumber());
            pstmt.setString(7, acc.getBranch());

            pstmt.setString(8, acc.getClass().getSimpleName());
            pstmt.setDouble(9, acc.getOpeningDeposit());

            pstmt.executeUpdate();
            System.out.println("Account " + acc.getAccountNumber() + " successfully saved to SQLite.");

        } catch (SQLException e) {
            System.err.println("Error saving account: " + e.getMessage());
        }
    }
}
