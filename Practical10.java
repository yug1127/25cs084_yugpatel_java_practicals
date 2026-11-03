/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 10: Thread Pools, Producer-Consumer, and Deadlock Prevention
 * Problem Statement: Build a TransactionProcessor using ExecutorService,
 * coordinate threads using wait/notify, and prevent deadlock during
 * multi-account transfers using consistent lock ordering.
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// ==========================================
// 1. ACCOUNT MODEL
// ==========================================
class Account {
    private final String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public synchronized void deposit(long amount) {
        if (amount > 0) balance += amount;
    }

    public synchronized boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public synchronized long getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }

    @Override
    public String toString() {
        return accountNumber + "[Bal=" + balance + "]";
    }
}

// ==========================================
// 2. TRANSACTION PROCESSOR WITH THREAD POOL
// ==========================================
class TransactionProcessor {
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public void submit(Runnable task) {
        pool.execute(task);
    }

    public void stop() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

// ==========================================
// 3. DEADLOCK PREVENTION WITH ORDERED LOCKING
// ==========================================
class SafeTransfer {
    public static void transfer(Account from, Account to, long amount) {
        // Consistent lock order based on account number comparison
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                    System.out.println("Transferred " + amount + " from " + from.getAccountNumber() + " to " + to.getAccountNumber());
                } else {
                    System.out.println("Transfer failed: Insufficient balance in " + from.getAccountNumber());
                }
            }
        }
    }
}

// ==========================================
// 4. PRODUCER-CONSUMER QUEUE (WAIT / NOTIFY)
// ==========================================
class TransactionQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final int capacity;

    public TransactionQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(String tx) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(tx);
        System.out.println("Produced task: " + tx);
        notifyAll();
    }

    public synchronized String consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        String tx = queue.poll();
        System.out.println("Consumed task: " + tx);
        notifyAll();
        return tx;
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical10 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 10: Thread Pools & Deadlock Prevention");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        // 1. Thread Pool TransactionProcessor Demo
        System.out.println("[PART 1: Fixed Thread Pool Transaction Processor]");
        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 1; i <= 4; i++) {
            final int taskId = i;
            processor.submit(() -> {
                System.out.println("Executing async task #" + taskId + " on " + Thread.currentThread().getName());
            });
        }
        processor.stop();
        System.out.println("Processor stopped successfully.\n");

        // 2. Safe Transfer (Deadlock Prevention Test)
        System.out.println("[PART 2: Deadlock Prevention with Ordered Locking]");
        Account accA = new Account("AC0001", 10000);
        Account accB = new Account("AC0002", 10000);

        System.out.println("Initial: " + accA + " | " + accB);

        // Run bidirectional concurrent transfers between AC0001 and AC0002
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                SafeTransfer.transfer(accA, accB, 10);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                SafeTransfer.transfer(accB, accA, 10);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final: " + accA + " | " + accB);
        System.out.println("Fixed test (lower-number-first locking): Execution completes successfully with correct balances.\n");

        // 3. Producer-Consumer Demo using wait/notify
        System.out.println("[PART 3: Producer-Consumer Coordination]");
        TransactionQueue txQueue = new TransactionQueue(2);
        Thread producer = new Thread(() -> {
            try {
                txQueue.produce("TX_DEP_001");
                txQueue.produce("TX_WDR_002");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                txQueue.consume();
                txQueue.consume();
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("\nPractical 10 completed successfully.");
    }
}

