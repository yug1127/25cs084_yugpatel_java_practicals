/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 5: Abstract Account Hierarchy & Run-Time Polymorphism
 * Problem Statement: Make Account abstract and implement SavingsAccount,
 * CurrentAccount, and FixedDepositAccount subclasses with polymorphic interest
 * rates and withdrawal rules.
 */

// ==========================================
// 1. ABSTRACT ACCOUNT BASE CLASS
// ==========================================
abstract class Account {
    protected String accountNumber;
    protected long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(accountNumber + ": Deposited " + amount + " | New Balance: " + balance);
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && canWithdraw(amount)) {
            balance -= amount;
            System.out.println(accountNumber + ": Withdrew " + amount + " | Remaining Balance: " + balance);
            return true;
        }
        System.out.println(accountNumber + ": Withdrawal of " + amount + " rejected!");
        return false;
    }

    public long getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
}

// ==========================================
// 2. SAVINGS ACCOUNT SUBCLASS
// ==========================================
class SavingsAccount extends Account {
    private long minBalance;

    public SavingsAccount(String accNum, long balance, long minBalance) {
        super(accNum, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return (balance - amount) >= minBalance;
    }

    public long getMinBalance() { return minBalance; }
}

// ==========================================
// 3. CURRENT ACCOUNT SUBCLASS
// ==========================================
class CurrentAccount extends Account {
    private long overdraftLimit;

    public CurrentAccount(String accNum, long balance, long overdraftLimit) {
        super(accNum, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return (balance - amount) >= -overdraftLimit;
    }

    public long getOverdraftLimit() { return overdraftLimit; }
}

// ==========================================
// 4. FIXED DEPOSIT ACCOUNT SUBCLASS
// ==========================================
class FixedDepositAccount extends Account {
    public FixedDepositAccount(String accNum, long balance) {
        super(accNum, balance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        // Locked until maturity
        return false;
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical05 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 5: Abstract Account & Polymorphism");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        Account savings = new SavingsAccount("SA001", 10000, 2000);
        Account current = new CurrentAccount("CA001", 5000, 3000);
        Account fixed = new FixedDepositAccount("FD001", 50000);

        // 1. Polymorphic interest rates (Matches Expected Output in Doc)
        System.out.println("[PART 1: Polymorphic Interest Rates]");
        System.out.println("SavingsAccount interestRate() -> " + savings.interestRate());
        System.out.println("CurrentAccount interestRate() -> " + current.interestRate());
        System.out.println("FixedDepositAccount interestRate() -> " + fixed.interestRate() + "\n");

        // 2. Polymorphic withdrawal checks
        System.out.println("[PART 2: Polymorphic Withdrawal Rules]");
        System.out.println("SavingsAccount (balance 10000, min 2000) withdraw 8500:");
        savings.withdraw(8500); // 10000 - 8500 = 1500 < 2000 -> Rejected

        System.out.println("\nSavingsAccount withdraw 7000:");
        savings.withdraw(7000); // 10000 - 7000 = 3000 >= 2000 -> Approved

        System.out.println("\nCurrentAccount (balance 5000, overdraft 3000) withdraw 7000:");
        current.withdraw(7000); // 5000 - 7000 = -2000 >= -3000 -> Approved

        System.out.println("\nFixedDepositAccount withdraw 1000:");
        fixed.withdraw(1000); // Always rejected before maturity

        System.out.println("\nPractical 05 completed successfully.");
    }
}

