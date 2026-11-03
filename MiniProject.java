/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING WITH JAVA (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * Course Mini Project: MiniBank Console Banking System
 * Source Reference: 25CS084_YugPatel_MiniProject.docx
 *
 * Description:
 * This file integrates all five core components of the MiniBank project into a single,
 * self-contained Java source file:
 *  1. BankAccount (Abstract Base Class - Serializable)
 *  2. SavingsAccount (Derived Class - Interest & Minimum Balance)
 *  3. CurrentAccount (Derived Class - Overdraft Facility)
 *  4. BankManager (Service Layer - CRUD, Authentication & Transfers)
 *  5. MiniProject / Main (Console UI Driver & Interactive Menu System)
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// ==========================================
// 1. BASE CLASS: BankAccount
// ==========================================
abstract class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String accountNumber;
    protected String holderName;
    protected int pin;
    protected double balance;
    protected String accountType;

    public BankAccount(String accountNumber, String holderName, int pin, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = initialBalance;
        this.accountType = accountType;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }

    public boolean validatePin(int inputPin) {
        return this.pin == inputPin;
    }

    public void changePin(int oldPin, int newPin) {
        if (validatePin(oldPin)) {
            this.pin = newPin;
            System.out.println("PIN successfully updated.");
        } else {
            System.out.println("Error: Incorrect old PIN.");
        }
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Successfully deposited ₹" + amount);
        } else {
            System.out.println("Error: Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Successfully withdrew ₹" + amount);
            return true;
        } else {
            System.out.println("Error: Insufficient balance or invalid amount.");
            return false;
        }
    }

    public abstract void calculateInterest();
    
    @Override
    public String toString() {
        return String.format("[%s] AccNo: %s | Holder: %s | Balance: ₹%.2f", accountType, accountNumber, holderName, balance);
    }
}

// ==========================================
// 2. DERIVED CLASS: SavingsAccount
// ==========================================
class SavingsAccount extends BankAccount {
    private static final double INTEREST_RATE = 0.04; // 4% per annum
    private static final double MIN_BALANCE = 1000.0;

    public SavingsAccount(String accountNumber, String holderName, int pin, double initialBalance) {
        super(accountNumber, holderName, pin, initialBalance, "Savings");
    }

    @Override
    public boolean withdraw(double amount) {
        if (balance - amount < MIN_BALANCE) {
            System.out.println("Error: Withdrawal violates minimum balance requirement of ₹" + MIN_BALANCE);
            return false;
        }
        return super.withdraw(amount);
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        deposit(interest);
        System.out.println("Savings Interest Credited (4%): ₹" + interest);
    }
}

// ==========================================
// 3. DERIVED CLASS: CurrentAccount
// ==========================================
class CurrentAccount extends BankAccount {
    private static final double OVERDRAFT_LIMIT = 25000.0;

    public CurrentAccount(String accountNumber, String holderName, int pin, double initialBalance) {
        super(accountNumber, holderName, pin, initialBalance, "Current");
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (balance + OVERDRAFT_LIMIT) >= amount) {
            balance -= amount;
            System.out.println("Successfully withdrew ₹" + amount + " (Using Overdraft Facility)");
            return true;
        } else {
            System.out.println("Error: Exceeds overdraft limit of ₹" + OVERDRAFT_LIMIT);
            return false;
        }
    }

    @Override
    public void calculateInterest() {
        System.out.println("Current Accounts do not accrue interest.");
    }
}

// ==========================================
// 4. SERVICE CLASS: BankManager
// ==========================================
class BankManager {
    private Map<String, BankAccount> accounts = new HashMap<>();

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean authenticate(String accountNumber, int pin) {
        BankAccount acc = accounts.get(accountNumber);
        return acc != null && acc.validatePin(pin);
    }

    public void transferFunds(String fromAccNum, String toAccNum, double amount, int pin) {
        BankAccount fromAcc = accounts.get(fromAccNum);
        BankAccount toAcc = accounts.get(toAccNum);

        if (fromAcc == null || toAcc == null) {
            System.out.println("Error: One or both account numbers are invalid.");
            return;
        }

        if (!fromAcc.validatePin(pin)) {
            System.out.println("Error: Authentication failed. Incorrect PIN.");
            return;
        }

        if (fromAcc.withdraw(amount)) {
            toAcc.deposit(amount);
            System.out.println("Fund Transfer Successful: ₹" + amount + " transferred from " + fromAccNum + " to " + toAccNum);
        } else {
            System.out.println("Fund Transfer Failed due to insufficient funds.");
        }
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts registered in MiniBank.");
            return;
        }
        System.out.println("\n--- All Registered Bank Accounts ---");
        for (BankAccount acc : accounts.values()) {
            System.out.println(acc);
        }
    }
}

// ==========================================
// 5. CONSOLE UI & DRIVER: MiniProject / Main
// ==========================================
public class MiniProject {
    public static void main(String[] args) {
        BankManager manager = new BankManager();
        Scanner scanner = new Scanner(System.in);

        // Pre-populate sample accounts for demonstration
        manager.addAccount(new SavingsAccount("MB101", "Yug Patel", 1234, 15000.0));
        manager.addAccount(new CurrentAccount("MB102", "Jaiminkumar Patel", 5678, 50000.0));

        boolean running = true;
        System.out.println("=================================================");
        System.out.println("       WELCOME TO MINIBANK CONSOLE SYSTEM        ");
        System.out.println("=================================================");

        while (running) {
            System.out.println("\n--- MINIBANK MAIN MENU ---");
            System.out.println("1. Create New Savings Account");
            System.out.println("2. Create New Current Account");
            System.out.println("3. Login & Access Account (Deposit / Withdraw / Transfer)");
            System.out.println("4. Admin: View All Accounts");
            System.out.println("5. Exit Application");
            System.out.print("Enter your choice (1-5): ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Account Number (e.g., MB103): ");
                    String accNo = scanner.nextLine();
                    System.out.print("Enter Holder Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Set 4-digit PIN: ");
                    int pin = scanner.nextInt();
                    System.out.print("Enter Initial Deposit (Min ₹1000): ");
                    double initDep = scanner.nextDouble();
                    
                    if (initDep < 1000) {
                        System.out.println("Error: Minimum initial deposit for Savings is ₹1000.");
                        break;
                    }
                    manager.addAccount(new SavingsAccount(accNo, name, pin, initDep));
                    System.out.println("Savings Account created successfully!");
                }
                case 2 -> {
                    System.out.print("Enter Account Number (e.g., MB201): ");
                    String accNo = scanner.nextLine();
                    System.out.print("Enter Holder Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Set 4-digit PIN: ");
                    int pin = scanner.nextInt();
                    System.out.print("Enter Initial Deposit: ");
                    double initDep = scanner.nextDouble();
                    
                    manager.addAccount(new CurrentAccount(accNo, name, pin, initDep));
                    System.out.println("Current Account created successfully!");
                }
                case 3 -> {
                    System.out.print("Enter Account Number: ");
                    String accNo = scanner.nextLine();
                    System.out.print("Enter 4-digit PIN: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();

                    if (manager.authenticate(accNo, pin)) {
                        BankAccount currentAcc = manager.getAccount(accNo);
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n--- Account Dashboard (" + currentAcc.getHolderName() + ") ---");
                            System.out.println("1. Check Balance");
                            System.out.println("2. Deposit Money");
                            System.out.println("3. Withdraw Money");
                            System.out.println("4. Transfer Funds");
                            System.out.println("5. Calculate & Credit Interest");
                            System.out.println("6. Change PIN");
                            System.out.println("7. Logout");
                            System.out.print("Choose option (1-7): ");

                            int subChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (subChoice) {
                                case 1 -> System.out.println("Current Balance: ₹" + currentAcc.getBalance());
                                case 2 -> {
                                    System.out.print("Enter deposit amount: ");
                                    double dep = scanner.nextDouble();
                                    currentAcc.deposit(dep);
                                }
                                case 3 -> {
                                    System.out.print("Enter withdrawal amount: ");
                                    double wit = scanner.nextDouble();
                                    currentAcc.withdraw(wit);
                                }
                                case 4 -> {
                                    System.out.print("Enter recipient Account Number: ");
                                    String targetAcc = scanner.nextLine();
                                    System.out.print("Enter transfer amount: ");
                                    double amt = scanner.nextDouble();
                                    manager.transferFunds(accNo, targetAcc, amt, pin);
                                }
                                case 5 -> currentAcc.calculateInterest();
                                case 6 -> {
                                    System.out.print("Enter old PIN: ");
                                    int oldP = scanner.nextInt();
                                    System.out.print("Enter new PIN: ");
                                    int newP = scanner.nextInt();
                                    currentAcc.changePin(oldP, newP);
                                }
                                case 7 -> {
                                    loggedIn = false;
                                    System.out.println("Logged out successfully.");
                                }
                                default -> System.out.println("Invalid option.");
                            }
                        }
                    } else {
                        System.out.println("Authentication Failed: Invalid Account Number or PIN.");
                    }
                }
                case 4 -> manager.displayAllAccounts();
                case 5 -> {
                    running = false;
                    System.out.println("Thank you for using MiniBank. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
        scanner.close();
    }
}

// Retaining original entry-point class for compatibility
class Main {
    public static void main(String[] args) {
        MiniProject.main(args);
    }
}

