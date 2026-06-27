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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The main window. Extends Application because this is a JavaFX app. JavaFX
// starts the program by calling start() below.
public class BankFormApp extends Application {

    // The input boxes the user types into
    private TextField txtFName, txtLName, txtNIN, txtEmail, txtConfirmEmail, txtPhone, txtDeposit;
    private PasswordField txtPIN, txtConfirmPIN;
    private ComboBox<Integer> comboYear, comboDay;
    private ComboBox<String> comboMonth, comboType, comboBranch;
    private TextArea areaSummary;

    // The red error messages shown next to each field
    private Label errFName, errLName, errNIN, errEmail, errConfirmEmail, errPhone, errPIN, errConfirmPIN, errDOB, errType, errBranch, errDeposit;

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

        grid.add(new Label("Email:"), 0, 3);
        txtEmail = new TextField(); grid.add(txtEmail, 1, 3);
        errEmail = new Label(); errEmail.setStyle("-fx-text-fill: red;"); grid.add(errEmail, 2, 3);

        grid.add(new Label("Confirm Email:"), 0, 4);
        txtConfirmEmail = new TextField(); grid.add(txtConfirmEmail, 1, 4);
        errConfirmEmail = new Label(); errConfirmEmail.setStyle("-fx-text-fill: red;"); grid.add(errConfirmEmail, 2, 4);

        grid.add(new Label("Phone Number:"), 0, 5);
        txtPhone = new TextField("+256"); grid.add(txtPhone, 1, 5);
        errPhone = new Label(); errPhone.setStyle("-fx-text-fill: red;"); grid.add(errPhone, 2, 5);

        grid.add(new Label("PIN (4-6 digits):"), 0, 6);
        txtPIN = new PasswordField(); grid.add(txtPIN, 1, 6);
        errPIN = new Label(); errPIN.setStyle("-fx-text-fill: red;"); grid.add(errPIN, 2, 6);

        grid.add(new Label("Confirm PIN:"), 0, 7);
        txtConfirmPIN = new PasswordField(); grid.add(txtConfirmPIN, 1, 7);
        errConfirmPIN = new Label(); errConfirmPIN.setStyle("-fx-text-fill: red;"); grid.add(errConfirmPIN, 2, 7);

        // Date of birth uses three dropdowns for year, month and day
        grid.add(new Label("Date of Birth:"), 0, 8);
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
        grid.add(dobBox, 1, 8);
        errDOB = new Label(); errDOB.setStyle("-fx-text-fill: red;"); grid.add(errDOB, 2, 8);

        // Account type and branch dropdowns
        grid.add(new Label("Account Type:"), 0, 9);
        comboType = new ComboBox<>(FXCollections.observableArrayList("Savings", "Current", "Fixed Deposit", "Student", "Joint"));
        grid.add(comboType, 1, 9);
        errType = new Label(); errType.setStyle("-fx-text-fill: red;"); grid.add(errType, 2, 9);

        grid.add(new Label("Branch:"), 0, 10);
        comboBranch = new ComboBox<>(FXCollections.observableArrayList("Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"));
        grid.add(comboBranch, 1, 10);
        errBranch = new Label(); errBranch.setStyle("-fx-text-fill: red;"); grid.add(errBranch, 2, 10);

        grid.add(new Label("Opening Deposit (UGX):"), 0, 11);
        txtDeposit = new TextField(); grid.add(txtDeposit, 1, 11);
        errDeposit = new Label(); errDeposit.setStyle("-fx-text-fill: red;"); grid.add(errDeposit, 2, 11);

        // Buttons and the read-only summary box at the bottom
        Button btnSubmit = new Button("Submit");
        Button btnReset = new Button("Reset");
        grid.add(btnSubmit, 0, 12);
        grid.add(btnReset, 1, 12);

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

    // Checks every field, and if all is well creates the account and saves it
    private void handleSubmission() {
        // Wipe any error messages from the last attempt
        errFName.setText("");
        errLName.setText("");
        errNIN.setText("");
        errEmail.setText("");
        errConfirmEmail.setText("");
        errPhone.setText("");
        errPIN.setText("");
        errConfirmPIN.setText("");
        errDOB.setText("");
        errType.setText("");
        errBranch.setText("");
        errDeposit.setText("");

        List<String> errors = new ArrayList<>();

        // Validate first and last names
        String fName = txtFName.getText() == null ? "" : txtFName.getText().trim();
        String lName = txtLName.getText() == null ? "" : txtLName.getText().trim();

        if (fName.isEmpty()) {
            errFName.setText("First Name is required.");
            errors.add("First Name is required.");
        } else if (!fName.matches("^[a-zA-Z]+$")) { // regex that matches lowercase and uppercase from a to z
            errFName.setText("Letters only.");
            errors.add("First Name must contain letters only.");
        } else if (fName.length() < 2 || fName.length() > 30) {
            errFName.setText("Must be 2-30 chars.");
            errors.add("First Name must be between 2 and 30 characters.");
        }

        if (lName.isEmpty()) {
            errLName.setText("Last Name is required.");
            errors.add("Last Name is required.");
        } else if (!lName.matches("^[a-zA-Z]+$")) {
            errLName.setText("Letters only.");
            errors.add("Last Name must contain letters only.");
        } else if (lName.length() < 2 || lName.length() > 30) {
            errLName.setText("Must be 2-30 chars.");
            errors.add("Last Name must be between 2 and 30 characters.");
        }

        // Validate National ID (NIN)
        String nin = txtNIN.getText() == null ? "" : txtNIN.getText().trim();
        if (nin.isEmpty()) {
            errNIN.setText("NIN is required.");
            errors.add("National ID (NIN) is required.");
        } else if (!nin.matches("^[A-Z0-9]{14}$")) { // exactly 14 uppercase alphanumeric characters
            errNIN.setText("Exactly 14 uppercase alphanumeric chars.");
            errors.add("National ID (NIN) must be exactly 14 alphanumeric uppercase characters.");
        }

        // Validate Email
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String confirmEmail = txtConfirmEmail.getText() == null ? "" : txtConfirmEmail.getText().trim();
        if (email.isEmpty()) {
            errEmail.setText("Email is required.");
            errors.add("Email is required.");
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) { // regex that matches a valid email format,
            errEmail.setText("Invalid email format.");
            errors.add("Email is in an invalid format.");
        } else if (!email.equals(confirmEmail)) {
            errConfirmEmail.setText("Emails do not match.");
            errors.add("Email and Confirm Email must match.");
        }

        // Validate Phone Number
        String phone = txtPhone.getText() == null ? "" : txtPhone.getText().trim();
        if (phone.isEmpty()) {
            errPhone.setText("Phone is required.");
            errors.add("Phone Number is required.");
        } else if (!phone.matches("^\\+256\\d{9}$")) {
            errPhone.setText("Must follow +256XXXXXXXXX.");
            errors.add("Phone Number must follow the Ugandan format +256XXXXXXXXX (9 digits after +256).");
        }

        // Validate PIN
        String pin = txtPIN.getText() == null ? "" : txtPIN.getText();
        String confirmPin = txtConfirmPIN.getText() == null ? "" : txtConfirmPIN.getText();
        if (pin.isEmpty()) {
            errPIN.setText("PIN is required.");
            errors.add("PIN is required.");
        } else if (!pin.matches("^\\d{4,6}$")) {
            errPIN.setText("Must be 4-6 digits.");
            errors.add("PIN must be numeric only and between 4 and 6 digits.");
        } else if (pin.chars().distinct().count() == 1) {
            errPIN.setText("Must not be identical digits.");
            errors.add("PIN must not consist of all-identical digits.");
        } else if (!pin.equals(confirmPin)) {
            errConfirmPIN.setText("PINs do not match.");
            errors.add("PIN and Confirm PIN must match.");
        }

        // Validate Account Type and Branch
        String type = comboType.getValue();
        String branch = comboBranch.getValue();
        if (type == null) {
            errType.setText("Account Type is required.");
            errors.add("Please select an Account Type.");
        }
        if (branch == null) {
            errBranch.setText("Branch is required.");
            errors.add("Please select a Branch.");
        }

        // Validate DOB and Age
        Integer yearVal = comboYear.getValue();
        String monthStr = comboMonth.getValue();
        Integer dayVal = comboDay.getValue();
        int dobYear = yearVal != null ? yearVal : 2000;
        int dobMonth = monthStr != null ? (comboMonth.getItems().indexOf(monthStr) + 1) : 1;
        int dobDay = dayVal != null ? dayVal : 1;

        if (yearVal == null || monthStr == null || dayVal == null) {
            errDOB.setText("DOB is required.");
            errors.add("Date of Birth is required.");
        } else {
            LocalDate birthDate = LocalDate.of(dobYear, dobMonth, dobDay);
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(birthDate, currentDate).getYears();

            if (age < 18 || age > 75) {
                errDOB.setText("Age must be 18-75.");
                errors.add("Age must be between 18 and 75 years inclusive (Current age: " + age + ").");
            } else if ("Student".equals(type) && (age < 18 || age > 25)) {
                errDOB.setText("Student age must be 18-25.");
                errors.add("Student account applicants must be between 18 and 25 years old (Current age: " + age + ").");
            }
        }

        // Validate Deposit
        String depositStr = txtDeposit.getText() == null ? "" : txtDeposit.getText().trim();
        double deposit = 0.0;
        if (depositStr.isEmpty()) {
            errDeposit.setText("Deposit is required.");
            errors.add("Opening Deposit is required.");
        } else {
            try {
                deposit = Double.parseDouble(depositStr);
                if (type != null) {
                    double minDeposit = 0.0;
                    if ("Savings".equals(type)) minDeposit = 50000.0;
                    else if ("Student".equals(type)) minDeposit = 10000.0;
                    else if ("Current".equals(type)) minDeposit = 200000.0;
                    else if ("Fixed Deposit".equals(type)) minDeposit = 1000000.0;
                    else if ("Joint".equals(type)) minDeposit = 100000.0;

                    if (deposit < minDeposit) {
                        errDeposit.setText("Min is " + String.format("%,.0f", minDeposit) + " UGX.");
                        errors.add("Minimum deposit for " + type + " is " + String.format("%,.0f", minDeposit) + " UGX.");
                    }
                }
            } catch (NumberFormatException nfe) {
                errDeposit.setText("Invalid number.");
                errors.add("Please enter a valid number for the opening deposit.");
            }
        }

        // we stop if validation has failed
        if (!errors.isEmpty()) return;

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
                    account = new JointAccount(accNum, fName, lName, nin, "N/A", email, phone, pin, dobYear, dobMonth, dobDay, branch, deposit);
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
