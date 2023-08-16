import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class User {
    private String username;
    private String password;
    private BankAccount bankAccount;

    public User(String username, String password, BankAccount bankAccount) {
        this.username = username;
        this.password = password;
        this.bankAccount = bankAccount;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}

class ATM {
    private Map<String, User> users;
    private User currentUser;
    private Scanner scanner;
    private DecimalFormat decimalFormat;

    public ATM() {
        this.users = new HashMap<>();
        this.scanner = new Scanner(System.in);
        this.decimalFormat = new DecimalFormat("#,##0.00");
    }

    public void showMenu() {
        System.out.println("====================================");
        System.out.println("          Welcome to the ATM!");
        System.out.println("====================================");

        int choice;
        while (true) {
            if (currentUser == null) {
                System.out.println("1. Login");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1-3): ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        signUp();
                        break;
                    case 3:
                        System.out.println("Thank you for using our ATM. Have a great day!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("\nMain Menu:");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Logout");
                System.out.print("Enter your choice (1-4): ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        checkBalance();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        currentUser = null;
                        System.out.println("Logout successful.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private void checkBalance() {
        System.out.println("====================================");
        System.out.println("       Your Account Balance");
        System.out.println("====================================");
        System.out.println("Current balance: $" + decimalFormat.format(currentUser.getBankAccount().getBalance()));
    }

    private void deposit() {
        System.out.println("====================================");
        System.out.println("            Deposit Money");
        System.out.println("====================================");
        double amount = getValidAmount("Enter the amount to deposit: $");
        currentUser.getBankAccount().deposit(amount);
        System.out.println("Deposit successful. Your updated balance: $" + decimalFormat.format(currentUser.getBankAccount().getBalance()));
    }

    private void withdraw() {
        System.out.println("====================================");
        System.out.println("           Withdraw Money");
        System.out.println("====================================");
        double amount = getValidAmount("Enter the amount to withdraw: $");
        if (currentUser.getBankAccount().withdraw(amount)) {
            System.out.println("Withdrawal successful. Your updated balance: $" + decimalFormat.format(currentUser.getBankAccount().getBalance()));
        } else {
            System.out.println("Insufficient balance. Withdrawal failed.");
        }
    }

    private double getValidAmount(String prompt) {
        double amount;
        do {
            System.out.print(prompt);
            amount = scanner.nextDouble();
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
            }
        } while (amount <= 0);
        return amount;
    }

    private void login() {
        System.out.print("Enter your username: ");
        String username = scanner.next();
        System.out.print("Enter your password: ");
        String password = scanner.next();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void signUp() {
        System.out.print("Enter a new username: ");
        String username = scanner.next();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        System.out.print("Enter a new password: ");
        String password = scanner.next();
        BankAccount bankAccount = new BankAccount(1000);
        User newUser = new User(username, password, bankAccount);
        users.put(username, newUser);
        System.out.println("Sign up successful. Welcome, " + newUser.getUsername() + "!");
    }
}

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.showMenu();
    }
}
