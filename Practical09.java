/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 9: Multithreading, Race Conditions, and Synchronization
 * Problem Statement: Implement AccountWorker implementing Runnable to perform
 * concurrent deposits, observe race conditions without synchronization,
 * and fix them with synchronized blocks.
 */

// ==========================================
// 1. ACCOUNT CLASSES (UNSYNC & SYNC)
// ==========================================
class Account {
    private final String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Unsynchronized deposit prone to race conditions under concurrency
    public void unsafeDeposit(long amount) {
        long current = this.balance;
        // Deliberate tiny pause to increase likelihood of thread preemption
        try { Thread.sleep(0, 500); } catch (InterruptedException ignored) {}
        this.balance = current + amount;
    }

    // Thread-safe deposit
    public synchronized void deposit(long amount) {
        long current = this.balance;
        try { Thread.sleep(0, 500); } catch (InterruptedException ignored) {}
        this.balance = current + amount;
    }

    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

// ==========================================
// 2. ACCOUNT WORKER RUNNABLE
// ==========================================
class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;
    private final boolean useSync;

    public AccountWorker(Account account, int times, long amount, boolean useSync) {
        this.account = account;
        this.times = times;
        this.amount = amount;
        this.useSync = useSync;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            if (useSync) {
                synchronized (account) {
                    account.deposit(amount);
                }
            } else {
                account.unsafeDeposit(amount);
            }
        }
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical09 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 9: Multithreading & Synchronization");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        final int NUM_THREADS = 10;
        final int ITERATIONS = 1000;
        final long DEPOSIT_AMOUNT = 1;
        final long EXPECTED_TOTAL = NUM_THREADS * ITERATIONS * DEPOSIT_AMOUNT; // 10000

        // 1. Without synchronization (Race Condition)
        System.out.println("[PART 1: Without Synchronization - Testing Race Condition]");
        Account unsafeAccount = new Account("UNSAFE01", 0);
        Thread[] unsafeThreads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(new AccountWorker(unsafeAccount, ITERATIONS, DEPOSIT_AMOUNT, false));
            unsafeThreads[i].start();
        }

        for (Thread t : unsafeThreads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("Expected Balance: " + EXPECTED_TOTAL);
        System.out.println("Actual Balance:   " + unsafeAccount.getBalance());
        if (unsafeAccount.getBalance() < EXPECTED_TOTAL) {
            System.out.println("Without synchronization: Balance < " + EXPECTED_TOTAL + " (race condition / data loss observed!)");
        } else {
            System.out.println("Race condition not manifested on single run (system timing).");
        }
        System.out.println();

        // 2. With synchronization (Thread Safety Guaranteed)
        System.out.println("[PART 2: With Synchronization - Thread Safe]");
        Account safeAccount = new Account("SAFE01", 0);
        Thread[] safeThreads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            safeThreads[i] = new Thread(new AccountWorker(safeAccount, ITERATIONS, DEPOSIT_AMOUNT, true));
            safeThreads[i].start();
        }

        for (Thread t : safeThreads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("Expected Balance: " + EXPECTED_TOTAL);
        System.out.println("Actual Balance:   " + safeAccount.getBalance());
        System.out.println("With synchronization: Balance = exactly " + safeAccount.getBalance() + " every run.");

        System.out.println("\nPractical 09 completed successfully.");
    }
}

