/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 8: Custom Exception Hierarchy & Fault-Tolerant Operations
 * Problem Statement: Create a checked BankException hierarchy including
 * InsufficientFundsException carrying shortfall, InvalidAmountException,
 * and robust try-catch handling.
 */

// ==========================================
// 1. CUSTOM BANK EXCEPTION HIERARCHY
// ==========================================
class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends BankException {
    private final long shortfall;

    public InsufficientFundsException(long shortfall) {
        super("Insufficient funds. Short by: " + shortfall);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}

class InvalidAmountException extends BankException {
    public InvalidAmountException(String msg) {
        super(msg);
    }
}

// ==========================================
// 2. FAULT-TOLERANT ACCOUNT CLASS
// ==========================================
class Account {
    private final String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be strictly positive: " + amount);
        }
        balance += amount;
        System.out.println("Deposited " + amount + " | New Balance: " + balance);
    }

    public void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be strictly positive: " + amount);
        }
        if (amount > balance) {
            long shortfall = amount - balance;
            throw new InsufficientFundsException(shortfall);
        }
        balance -= amount;
        System.out.println("Withdrew " + amount + " | Remaining Balance: " + balance);
    }

    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical08 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 8: Custom Exception Hierarchy");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        Account acc = new Account("AC0001", 1000);
        System.out.println("Initial Account Created: " + acc.getAccountNumber() + " with balance " + acc.getBalance() + "\n");

        // 1. Testing Insufficient Funds (Matches Expected Output in Doc)
        System.out.println("[PART 1: Attempting to withdraw 5000 from balance 1000]");
        try {
            acc.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            System.out.println("Shortfall amount retrieved from exception: " + e.getShortfall());
        } catch (InvalidAmountException e) {
            System.out.println("Invalid amount: " + e.getMessage());
        }
        System.out.println("Balance remains unchanged at " + acc.getBalance() + ".\n");

        // 2. Testing Invalid Amount Exception
        System.out.println("[PART 2: Attempting negative deposit of -500]");
        try {
            acc.deposit(-500);
        } catch (InvalidAmountException e) {
            System.out.println("Caught expected InvalidAmountException: " + e.getMessage());
        }
        System.out.println("Balance remains unchanged at " + acc.getBalance() + ".\n");

        // 3. Testing Successful Transaction
        System.out.println("[PART 3: Successful deposit and withdrawal]");
        try {
            acc.deposit(3000); // 1000 + 3000 = 4000
            acc.withdraw(1500); // 4000 - 1500 = 2500
            System.out.println("Final Verified Balance: " + acc.getBalance());
        } catch (BankException e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        System.out.println("\nPractical 08 completed successfully.");
    }
}

