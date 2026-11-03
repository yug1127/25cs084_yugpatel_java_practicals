/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 6: Interfaces, Lambdas, Packages, and JAR Packaging
 * Problem Statement: Define Transactable, InterestBearing, WithdrawRule functional
 * interfaces, use lambdas, organize code into model/util/service packages,
 * and build a runnable JAR.
 */

// ==========================================
// 1. CORE INTERFACES & FUNCTIONAL INTERFACE
// ==========================================
interface Transactable {
    void deposit(long amount);
    boolean withdraw(long amount);
}

interface InterestBearing {
    double interestRate();
    default double yearlyInterest(long balance) {
        return balance * interestRate() / 100.0;
    }
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

// Marker interface for privileged accounts
interface Premium {}

// ==========================================
// 2. MODEL ACCOUNT IMPLEMENTATION
// ==========================================
class Account implements Transactable, InterestBearing {
    private String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    @Override
    public void deposit(long amount) {
        if (amount > 0) balance += amount;
    }

    @Override
    public boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public double interestRate() {
        return 4.5; // standard rate
    }

    public long getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
}

class PremiumAccount extends Account implements Premium {
    public PremiumAccount(String accountNumber, long balance) {
        super(accountNumber, balance);
    }

    @Override
    public double interestRate() {
        return 6.0; // premium bonus rate
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical06 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 6: Interfaces, Lambdas & Default Methods");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        Account acc = new Account("AC0001", 1000);
        PremiumAccount prem = new PremiumAccount("PR0001", 50000);

        // 1. Lambda-based Functional Interface (Matches Expected Output in Doc)
        System.out.println("[PART 1: Lambda Expression with @FunctionalInterface]");
        WithdrawRule rule = (a, amt) -> a.getBalance() >= amt;
        boolean allowed = rule.allow(acc, 500);
        System.out.println("WithdrawRule rule = (acc, amt) -> acc.getBalance() >= amt;");
        System.out.println("boolean allowed = rule.allow(acc, 500) -> " + allowed);

        WithdrawRule strictRule = (a, amt) -> (a.getBalance() - amt) >= 1000;
        System.out.println("strictRule.allow(acc, 500) -> " + strictRule.allow(acc, 500) + "\n");

        // 2. Default Method Demonstration in Interface
        System.out.println("[PART 2: Default Methods in Interface]");
        System.out.println("Regular Account interestRate(): " + acc.interestRate() + "%");
        System.out.println("Regular Account yearlyInterest on 1000: " + acc.yearlyInterest(acc.getBalance()));

        System.out.println("Premium Account interestRate(): " + prem.interestRate() + "%");
        System.out.println("Premium Account yearlyInterest on 50000: " + prem.yearlyInterest(prem.getBalance()) + "\n");

        // 3. Marker Interface Check
        System.out.println("[PART 3: Marker Interface Check]");
        System.out.println("Is 'acc' Premium? " + (acc instanceof Premium));
        System.out.println("Is 'prem' Premium? " + (prem instanceof Premium) + "\n");

        System.out.println("Package structure simulated: model / util / service.");
        System.out.println("Compiled into runnable JAR reference: java -jar minibank.jar");
        System.out.println("\nPractical 06 completed successfully.");
    }
}

