import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane; // library we used to build the gui
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

// The main window. Extends Application because this is a JavaFX app. JavaFX
// starts the program by calling start() below.
public class BankFormApp extends Application {

    // The input boxes the user types into
    private TextField txtFName, txtLName, txtNIN, txtSecondNIN, txtEmail, txtConfirmEmail, txtPhone, txtDeposit;
    private PasswordField txtPIN, txtConfirmPIN;
    private ComboBox<Integer> comboYear, comboDay;
    private ComboBox<String> comboMonth, comboType, comboBranch;
    private TextArea areaSummary;

    // The label for the second NIN, kept so we can hide it together with its box
    private Label lblSecondNIN;

    // The red error messages shown next to each field
    private Label errFName, errLName, errNIN, errSecondNIN, errEmail, errConfirmEmail, errPhone, errPIN, errConfirmPIN, errDOB, errType, errBranch, errDeposit;

    // Counts how many accounts each branch has opened, so we can number them.
    // Key = branch code like "KLA", value = how many so far.
    private static Map<String, Integer> branchCounters = new HashMap<>();

    // Builds the window and shows it. The Stage is the OS window, the Scene is
    // everything inside it.
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("First Bank Uganda - New Account Form");

        // Lay the form out as a grid of rows and columns
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20)); // space around the edges
        grid.setHgap(10); // gap between columns
        grid.setVgap(10); // gap between rows

        // Each row: column 0 = label, column 1 = input box, column 2 = error message
        grid.add(new Label("First Name:"), 0, 0);
        txtFName = new TextField(); grid.add(txtFName, 1, 0);
        errFName = new Label(); errFName.setStyle("-fx-text-fill: red;"); grid.add(errFName, 2, 0);

        grid.add(new Label("Last Name:"), 0, 1);
        txtLName = new TextField(); grid.add(txtLName, 1, 1);
        errLName = new Label(); errLName.setStyle("-fx-text-fill: red;"); grid.add(errLName, 2, 1);

        grid.add(new Label("National ID (NIN):"), 0, 2);
        txtNIN = new TextField(); grid.add(txtNIN, 1, 2);
        errNIN = new Label(); errNIN.setStyle("-fx-text-fill: red;"); grid.add(errNIN, 2, 2);

        // Second NIN sits right under the first one. Only used by Joint accounts,
        // so it stays hidden until "Joint" is chosen (toggled further down).
        lblSecondNIN = new Label("Second NIN:");
        grid.add(lblSecondNIN, 0, 3);
        txtSecondNIN = new TextField(); grid.add(txtSecondNIN, 1, 3);
        errSecondNIN = new Label(); errSecondNIN.setStyle("-fx-text-fill: red;"); grid.add(errSecondNIN, 2, 3);
        setSecondNINVisible(false); // start hidden

        grid.add(new Label("Email:"), 0, 4);
        txtEmail = new TextField(); grid.add(txtEmail, 1, 4);
        errEmail = new Label(); errEmail.setStyle("-fx-text-fill: red;"); grid.add(errEmail, 2, 4);

        grid.add(new Label("Confirm Email:"), 0, 5);
        txtConfirmEmail = new TextField(); grid.add(txtConfirmEmail, 1, 5);
        errConfirmEmail = new Label(); errConfirmEmail.setStyle("-fx-text-fill: red;"); grid.add(errConfirmEmail, 2, 5);

        grid.add(new Label("Phone Number:"), 0, 6);
        txtPhone = new TextField("+256"); grid.add(txtPhone, 1, 6);
        errPhone = new Label(); errPhone.setStyle("-fx-text-fill: red;"); grid.add(errPhone, 2, 6);

        grid.add(new Label("PIN (4-6 digits):"), 0, 7);
        txtPIN = new PasswordField(); grid.add(txtPIN, 1, 7);
        errPIN = new Label(); errPIN.setStyle("-fx-text-fill: red;"); grid.add(errPIN, 2, 7);

        grid.add(new Label("Confirm PIN:"), 0, 8);
        txtConfirmPIN = new PasswordField(); grid.add(txtConfirmPIN, 1, 8);
        errConfirmPIN = new Label(); errConfirmPIN.setStyle("-fx-text-fill: red;"); grid.add(errConfirmPIN, 2, 8);

        // Date of birth uses three dropdowns for year, month and day
        grid.add(new Label("Date of Birth:"), 0, 9);
        comboYear = new ComboBox<>(FXCollections.observableArrayList());
        comboYear.setPromptText("Year");
        comboYear.setPrefWidth(90);
        for (int i = 2026; i >= 1940; i--) comboYear.getItems().add(i);

        comboMonth = new ComboBox<>(FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"));
        comboMonth.setPromptText("Month");
        comboMonth.setPrefWidth(120);

        comboDay = new ComboBox<>();
        comboDay.setPromptText("Day");
        comboDay.setPrefWidth(80);

        // When the month or year changes, refresh the day list (handles leap years)
        comboMonth.setOnAction(e -> updateDays());
        comboYear.setOnAction(e -> updateDays());

        // Stack each dropdown under its own little label
        VBox yearBox = new VBox(2, new Label("Year"), comboYear);
        VBox monthBox = new VBox(2, new Label("Month"), comboMonth);
        VBox dayBox = new VBox(2, new Label("Day"), comboDay);

        // Put the three dropdowns side by side
        HBox dobBox = new HBox(10, yearBox, monthBox, dayBox);
        grid.add(dobBox, 1, 9);
        errDOB = new Label(); errDOB.setStyle("-fx-text-fill: red;"); grid.add(errDOB, 2, 9);

        // Account type and branch dropdowns
        grid.add(new Label("Account Type:"), 0, 10);
        comboType = new ComboBox<>(FXCollections.observableArrayList("Savings", "Current", "Fixed Deposit", "Student", "Joint"));
        grid.add(comboType, 1, 10);
        errType = new Label(); errType.setStyle("-fx-text-fill: red;"); grid.add(errType, 2, 10);

        // Show or hide the second NIN box whenever the account type changes
        comboType.setOnAction(e -> setSecondNINVisible("Joint".equals(comboType.getValue())));

        grid.add(new Label("Branch:"), 0, 11);
        comboBranch = new ComboBox<>(FXCollections.observableArrayList("Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"));
        grid.add(comboBranch, 1, 11);
        errBranch = new Label(); errBranch.setStyle("-fx-text-fill: red;"); grid.add(errBranch, 2, 11);

        grid.add(new Label("Opening Deposit (UGX):"), 0, 12);
        txtDeposit = new TextField(); grid.add(txtDeposit, 1, 12);
        errDeposit = new Label(); errDeposit.setStyle("-fx-text-fill: red;"); grid.add(errDeposit, 2, 12);

        // Buttons and the read-only summary box at the bottom
        Button btnSubmit = new Button("Submit");
        Button btnReset = new Button("Reset");
        grid.add(btnSubmit, 0, 13);
        grid.add(btnReset, 1, 13);

        areaSummary = new TextArea();
        areaSummary.setEditable(false);
        areaSummary.setPromptText("Account Summary is Below:");

        // Stack the form, a heading and the summary box vertically
        VBox mainLayout = new VBox(10, grid, new Label("Account Summary:"), areaSummary);
        mainLayout.setPadding(new Insets(10));

        // Wire the buttons to their actions
        btnSubmit.setOnAction(e -> handleSubmission());
        btnReset.setOnAction(e -> resetForm());

        // Show the window
        primaryStage.setScene(new Scene(mainLayout, 850, 750));
        primaryStage.show();
        handleSubmission();
    }

    // Shows or hides the second NIN row. setManaged(false) also makes the row
    // collapse so it doesn't leave an empty gap when hidden.
    private void setSecondNINVisible(boolean show) {
        lblSecondNIN.setVisible(show);   lblSecondNIN.setManaged(show);
        txtSecondNIN.setVisible(show);   txtSecondNIN.setManaged(show);
        errSecondNIN.setVisible(show);   errSecondNIN.setManaged(show);
        if (!show) txtSecondNIN.clear(); // don't keep a stale value when switching away
    }

    // Fills the day dropdown with the right number of days for the chosen month/year
    private void updateDays() {
        if (comboMonth.getValue() == null || comboYear.getValue() == null) return;

        int year = comboYear.getValue();
        String month = comboMonth.getValue();
        int days = 31;

        if (month.equals("February")) {
            // Leap year: divisible by 4, but not by 100 unless also by 400
            days = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28; // ? 29 : 28 means if true, it's 29, if false, it's 28. it shorthand for if-else statement
        } else if (month.equals("April") || month.equals("June") || month.equals("September") || month.equals("November")) {
            days = 30;
        }

        comboDay.getItems().clear();
        for (int i = 1; i <= days; i++) comboDay.getItems().add(i);
    }

    // Puts a validator message on its error label. Returns true when the field is
    // valid (empty message), false when there's an error, so callers can AND the
    // results together to know if the whole form passed.
    private boolean show(Label errorLabel, String message) {
        errorLabel.setText(message);
        return message.isEmpty();
    }

    // Checks every field, and if all is well creates the account and saves it
    private void handleSubmission() {
        // Wipe any error messages from the last attempt
        errFName.setText("");
        errLName.setText("");
        errNIN.setText("");
        errSecondNIN.setText("");
        errEmail.setText("");
        errConfirmEmail.setText("");
        errPhone.setText("");
        errPIN.setText("");
        errConfirmPIN.setText("");
        errDOB.setText("");
        errType.setText("");
        errBranch.setText("");
        errDeposit.setText("");

        // Tracks whether any field failed. The actual rules live in BankValidator.
        boolean valid = true;

        // Names
        String fName = txtFName.getText() == null ? "" : txtFName.getText().trim();
        String lName = txtLName.getText() == null ? "" : txtLName.getText().trim();
        valid &= show(errFName, BankValidator.name(fName));
        valid &= show(errLName, BankValidator.name(lName));

        // National ID
        String nin = txtNIN.getText() == null ? "" : txtNIN.getText().trim();
        valid &= show(errNIN, BankValidator.nin(nin));

        // Email, then the confirm-email match check
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String confirmEmail = txtConfirmEmail.getText() == null ? "" : txtConfirmEmail.getText().trim();
        String emailErr = BankValidator.email(email);
        if (!emailErr.isEmpty()) {
            valid &= show(errEmail, emailErr);
        } else if (!email.equals(confirmEmail)) {
            valid &= show(errConfirmEmail, "Emails do not match.");
        }

        // Phone
        String phone = txtPhone.getText() == null ? "" : txtPhone.getText().trim();
        valid &= show(errPhone, BankValidator.phone(phone));

        // PIN, then the confirm-PIN match check
        String pin = txtPIN.getText() == null ? "" : txtPIN.getText();
        String confirmPin = txtConfirmPIN.getText() == null ? "" : txtConfirmPIN.getText();
        String pinErr = BankValidator.pin(pin);
        if (!pinErr.isEmpty()) {
            valid &= show(errPIN, pinErr);
        } else if (!pin.equals(confirmPin)) {
            valid &= show(errConfirmPIN, "PINs do not match.");
        }

        // Account Type and Branch are dropdowns, so just check something was picked
        String type = comboType.getValue();
        String branch = comboBranch.getValue();
        if (type == null) valid &= show(errType, "Account Type is required.");
        if (branch == null) valid &= show(errBranch, "Branch is required.");

        // Second NIN is only required for Joint accounts
        String secondNin = txtSecondNIN.getText() == null ? "" : txtSecondNIN.getText().trim();
        if ("Joint".equals(type)) {
            valid &= show(errSecondNIN, BankValidator.secondNin(secondNin, nin));
        }

        // Date of birth and age
        Integer yearVal = comboYear.getValue();
        String monthStr = comboMonth.getValue();
        Integer dayVal = comboDay.getValue();
        int dobYear = yearVal != null ? yearVal : 2000;
        int dobMonth = monthStr != null ? (comboMonth.getItems().indexOf(monthStr) + 1) : 1;
        int dobDay = dayVal != null ? dayVal : 1;

        if (yearVal == null || monthStr == null || dayVal == null) {
            valid &= show(errDOB, "DOB is required.");
        } else {
            LocalDate birthDate = LocalDate.of(dobYear, dobMonth, dobDay);
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            valid &= show(errDOB, BankValidator.age(age, type));
        }

        // Deposit: the form parses the number, the validator supplies the minimum
        String depositStr = txtDeposit.getText() == null ? "" : txtDeposit.getText().trim();
        double deposit = 0.0;
        if (depositStr.isEmpty()) {
            valid &= show(errDeposit, "Deposit is required.");
        } else {
            try {
                deposit = Double.parseDouble(depositStr);
                if (type != null) {
                    double minDeposit = BankValidator.minimumDeposit(type);
                    if (deposit < minDeposit) {
                        valid &= show(errDeposit, "Min is " + String.format("%,.0f", minDeposit) + " UGX.");
                    }
                }
            } catch (NumberFormatException nfe) {
                valid &= show(errDeposit, "Invalid number.");
            }
        }

        // we stop if validation has failed
        if (!valid) return;

        try {
            // making unique account number from the branch code and its running count
            String branchCode;
            if ("Kampala".equalsIgnoreCase(branch)) {
                branchCode = "KLA";
            } else if ("Mbale".equalsIgnoreCase(branch)) {
                branchCode = "MBL";
            } else if ("Mbarara".equalsIgnoreCase(branch)) {
                branchCode = "MBR";
            } else {
                branchCode = branch.substring(0, 3).toUpperCase();
            }
            int count = branchCounters.getOrDefault(branchCode, 0) + 1;
            branchCounters.put(branchCode, count);
            String accNum = String.format("%s-2026-%06d", branchCode, count);

            // creating the right account type
            Account account;
            switch (type) {
                case "Savings":
                    account = new SavingsAccount(accNum, fName, lName, nin, email, phone, pin, dobYear, dobMonth, dobDay, branch, deposit);
                    break;
                case "Student":
                    account = new StudentAccount(accNum, fName, lName, nin, email, phone, pin, dobYear, dobMonth, dobDay, branch, deposit);
                    break;
                case "Current":
                    account = new CurrentAccount(accNum, fName, lName, nin, email, phone, pin, dobYear, dobMonth, dobDay, branch, deposit);
                    break;
                case "Fixed Deposit":
                    account = new FixedDepositAccount(accNum, fName, lName, nin, email, phone, pin, dobYear, dobMonth, dobDay, branch, deposit);
                    break;
                case "Joint":
                    account = new JointAccount(accNum, fName, lName, nin, secondNin, email, phone, pin, dobYear, dobMonth, dobDay, branch, deposit);
                    break;
                default:
                    throw new Exception("Invalid Account Type");
            }

            // show summary and save
            areaSummary.setText(account.toString());
            saveToDatabase(account);

        } catch (Exception ex) {
            areaSummary.setText("Error: " + ex.getMessage());
        }
    }

    // hands the account to the database manager to be written into sqlite
    private void saveToDatabase(Account acc) {
        try {
            DatabaseManager.saveAccount(acc);
            System.out.println("Record appended to SQLite database successfully.");
        } catch (Exception e) {
            System.err.println("Error saving to database: " + e.getMessage());
        }
    }

    // this method resets the whole form back to empty
    private void resetForm() {
        txtFName.clear(); txtLName.clear(); txtNIN.clear();
        txtEmail.clear(); txtConfirmEmail.clear(); txtPhone.setText("+256");
        txtPIN.clear(); txtConfirmPIN.clear(); txtDeposit.clear();
        comboYear.setValue(null);
        comboMonth.setValue(null);
        comboDay.setValue(null);
        areaSummary.clear();
        setSecondNINVisible(false); // hide the Joint-only field again
        errSecondNIN.setText("");
    }

    public static void main(String[] args) {
        // Make sure the database table exists before opening the window
        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
        // Start the JavaFX app
        launch(args);
    }
}
