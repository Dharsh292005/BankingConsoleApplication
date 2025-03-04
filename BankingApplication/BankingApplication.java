import java.util.*;

class Account {
    private String accountNumber;
    private String name;
    private String password;
    private double balance;
    private List<String> transactions;

    public Account(String accountNumber, String name, String password, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.password = password;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
        transactions.add("Account created with balance: " + initialDeposit);
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: " + amount + " | New Balance: " + balance);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        transactions.add("Withdrew: " + amount + " | New Balance: " + balance);
        return true;
    }

    public boolean transfer(Account toAccount, double amount) {
        if (amount > balance) {
            return false;
        }
        this.withdraw(amount);
        toAccount.deposit(amount);
        transactions.add("Transferred: " + amount + " to " + toAccount.getAccountNumber());
        return true;
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History for " + name + " (" + accountNumber + "):");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
    }
}

public class BankingApplication {
    private static Map<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Banking System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Exiting... Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();

        Account account = new Account(accountNumber, name, password, initialDeposit);
        accounts.put(accountNumber, account);
        System.out.println("Account created successfully!");
    }

    private static void login() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        if (!accounts.containsKey(accountNumber)) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        Account account = accounts.get(accountNumber);

        if (!account.authenticate(password)) {
            System.out.println("Incorrect password!");
            return;
        }

        System.out.println("Welcome, " + account.getName() + "!");
        accountMenu(account);
    }

    private static void accountMenu(Account account) {
        while (true) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Balance: " + account.getBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    System.out.println("Deposit successful!");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (account.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawal successful!");
                    } else {
                        System.out.println("Insufficient funds!");
                    }
                    break;
                case 4:
                    System.out.print("Enter recipient account number: ");
                    String recipientAccNum = scanner.nextLine();
                    if (!accounts.containsKey(recipientAccNum)) {
                        System.out.println("Recipient account not found!");
                        break;
                    }
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    if (account.transfer(accounts.get(recipientAccNum), transferAmount)) {
                        System.out.println("Transfer successful!");
                    } else {
                        System.out.println("Insufficient funds!");
                    }
                    break;
                case 5:
                    account.showTransactionHistory();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
