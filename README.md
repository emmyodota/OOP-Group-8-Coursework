# First Bank Uganda - Account Opening Application

Welcome to the **First Bank Uganda** project. This is a desktop application built using **JavaFX** (for the graphical user interface) and **SQLite** (for storing account information).

It will show you how to run the project and explain how the different files and classes connect, share data, and work together.

---

## 1. How to Run the Project

To run this application, you need:
1. **Java JDK 21 or newer** installed on a computer.
2. **Apache Maven** installed.

### The Run Command
Open a terminal in macOS or command prompt/Powershell in Windows, navigate to the project directory (`Group 8`), and run:

```bash
mvn javafx:run
```

* **Automation:** Maven reads the `pom.xml` file, automatically, and downloads the correct JavaFX libraries, sets up the classpath, and runs the application.

  * **What is a `pom.xml`?** POM stands for **Project Object Model**. Think of this file as the **shopping list** and **recipe book** for our project. It contains:
    1. **Project Information:** The name, group, and version of our application.
    2. **Dependencies:** A list of external libraries our project needs to run (such as the JavaFX modules and the SQLite database driver). Maven reads this list and downloads the required files.
    3. **Plugins:** Tools that add extra features to Maven, such as the `javafx-maven-plugin` that creates the custom `mvn javafx:run` command.

### The `target/` Directory and Cleaning the Build
As maven compiles and runs the project, we notice a folder called `target/` appearing in our project directory.

* **What is it?** The `target/` directory is the **output folder** where maven puts the compiled `.class` files (bytecode), packaged JARs, and temporary build resources.

---

## 2. Understanding the Code Architecture (How Files Interact)

This project has several Java files, and each one has a specific job:

| File | Job |
| --- | --- |
| `BankFormApp.java` | Builds the GUI, collects user input, and creates the account objects |
| `Account.java` | The abstract class that holds the shared fields and rules |
| `SavingsAccount.java` | A savings account type that inherits from `Account` |
| `CurrentAccount.java` | A current account type that inherits from `Account` |
| `StudentAccount.java` | A student account type that inherits from `Account` |
| `FixedDepositAccount.java` | A fixed deposit account type that inherits from `Account` |
| `JointAccount.java` | A joint account type that inherits from `Account` |
| `DatabaseManager.java` | Saves the account into the SQLite database (`FirstBank.db`) |

The files connect in four steps:

1. **The user fills the form.** `BankFormApp.java` collects the input and, based on the account type chosen, creates one specific object: a `SavingsAccount`, `CurrentAccount`, `StudentAccount`, `FixedDepositAccount`, or `JointAccount`.

2. **Each account type inherits from `Account`.** All five subclasses extend `Account.java`, so they reuse its fields (name, NIN, deposit) and only add their own rules, such as the minimum deposit and age requirements for the student account.

3. **The object is handed to the database layer.** `BankFormApp.java` passes the finished account to `DatabaseManager.saveAccount(...)`. It only knows it as an `Account`, not which specific type it is.

4. **The data is saved.** `DatabaseManager.java` reads the values through the getter methods and writes them into the SQLite file `FirstBank.db`.

In short, input flows **from the form, into a specific account object, up through the shared `Account` parent, and finally into the database**.

---

## 3. How and what code flows to another
In object-oriented programming, classes interact by passing data, inheriting behaviors, and enforcing rules.

### A. Inheritance: Passing Data using (`super`)
Classes like `SavingsAccount` and `CurrentAccount` are child classes. They inherit from the parent class `Account` using the `extends` keyword.

* **The Flow:** When we created a `SavingsAccount`, we passed details like `firstName`, `lastName`, and `openingDeposit` to the class constructor.

* **How it works:** Because `Account` (the parent) already knows how to store these details, the child class immediately forwards these values to the parent constructor using the `super()` keyword:
  ```java

  // Inside SavingsAccount.java
  public SavingsAccount(String accountNumber, String firstName, ...) {
      // Passes these values up to Account.java
      super(accountNumber, firstName, ...);
  }
  ```
* **Why do this?** It avoids code duplication. After writing the code once in `Account.java`, and all five account types reuse it.

### B. Encapsulation(Getters)
In `Account.java`, all variables are marked as `private` (e.g., `private String firstName;`).
* **The Rule:** No external class can directly read or change these variables. For example, `DatabaseManager` cannot do `acc.firstName`.
* **The Flow:** To let other classes read this data safely, `Account` provides public getter methods (like `getFirstName()`).
* **Why do this?** This is called encapsulation. It prevents external classes from accidentally modifying important data, while still allowing them to read it when necessary.

### C. Polymorphism: dynamic rules
It allows us to treat different child classes as if they were the general parent class `Account`, but Java still runs the specific child class rules at runtime.

* **The Flow:**
  1. In `BankFormApp.java`, we declared a general variable: `Account account;`
  2. Depending on what the user selects in the GUI, we assign a specific child object to it:
     ```java
     account = new StudentAccount(...); // or SavingsAccount, etc.
     ```
  3. We then check if the deposit is enough:
     ```java
     if (deposit < account.getMinimumDeposit()) { ... }
     ```
* **How the logic flows:** Even though the variable type is the general `Account`, Java automatically looks inside the object, finds that it is actually a `StudentAccount`, and runs the `getMinimumDeposit()` method defined in `StudentAccount.java` (which returns `10000.0`). If it were a `CurrentAccount`, it would run the method in `CurrentAccount.java` (which returns `200000.0`).

### D. Saving to the database (Objects as parameters)
Once the account is validated, it is saved using `DatabaseManager.saveAccount(account)`.

* **The Flow:**
  1. `BankFormApp` passes the `account` object to `DatabaseManager`.
  2. `DatabaseManager` does not need to know whether the account is a Student, Savings, or Current account. It simply treats it as an `Account`. Because it works with the parent Account type. Any subclass is acceptable due to polymorphism.
  3. It extracts the information using the getter methods:
     ```java
     pstmt.setString(1, acc.getAccountNumber());
     pstmt.setString(2, acc.getFirstName());
     ```
  4. It determines the account type dynamically using Java Reflection:
     ```java
     pstmt.setString(8, acc.getClass().getSimpleName()); // e.g., "SavingsAccount"
     ```
  5. The data is written into the SQLite database file (`FirstBank.db`).

---

## 4. Summary of OOP concepts used

* **Class**: (e.g., `Account`).
* **Object**: An instance of the class created with `new` (e.g., a specific student's account).
* **Inheritance (`extends`)**: Creating new classes using code from an existing class.
* **Abstract class (`abstract`)**: A class that serves as parent class for other classes and cannot be created directly.
* **Encapsulation**: Hiding internal data (`private`) and exposing it only through safe channels (`public` getters).
* **Polymorphism**: The ability of a single variable of a parent type to behave differently depending on the actual child object it holds.
