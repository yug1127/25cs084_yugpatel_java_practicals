/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 12: Collections Framework (HashMap, ArrayList, Comparator)
 * Problem Statement: Upgrade MiniBank storage from arrays to AccountStore using
 * HashMap keyed by account number, maintain transaction history, and sort
 * accounts by balance using Comparator.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ==========================================
// 1. ACCOUNT MODEL
// ==========================================
class Account {
    private final String accountNumber;
    private final String ownerName;
    private final long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public long getBalance() { return balance; }

    @Override
    public String toString() {
        return accountNumber + " (" + ownerName + "): Rs. " + balance;
    }
}

// ==========================================
// 2. ACCOUNT STORE USING COLLECTIONS
// ==========================================
class AccountStore {
    private final Map<String, Account> store = new HashMap<>();
    private final List<String> history = new ArrayList<>();

    public void add(Account acc) {
        store.put(acc.getAccountNumber(), acc);
        logTransaction("Added account " + acc.getAccountNumber() + " for " + acc.getOwnerName());
    }

    public Account get(String accNum) {
        return store.get(accNum);
    }

    public void logTransaction(String log) {
        history.add(log);
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public List<Account> sortedByBalance() {
        List<Account> list = new ArrayList<>(store.values());
        // Sort descending by balance using Comparator
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    public int totalAccounts() {
        return store.size();
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical12 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 12: Collections Framework & Comparator");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        AccountStore store = new AccountStore();

        // Populate accounts
        Account a1 = new Account("AC0001", "Riya", 5000);
        Account a2 = new Account("AC0002", "Yug", 9000);
        Account a3 = new Account("AC0003", "Kavya", 2000);

        store.add(a1);
        store.add(a2);
        store.add(a3);

        // 1. HashMap lookup (Matches Expected Output in Doc)
        System.out.println("[PART 1: HashMap Lookup by Key]");
        Account retrieved = store.get("AC0002");
        if (retrieved != null) {
            System.out.println("get(\"AC0002\") retrieved successfully: " + retrieved);
        } else {
            System.out.println("Account not found.");
        }
        System.out.println();

        // 2. Comparator sorting (Matches Expected Output in Doc)
        System.out.println("[PART 2: Sorting Accounts by Balance Descending]");
        List<Account> sorted = store.sortedByBalance();
        StringBuilder sb = new StringBuilder("Sorted list by balance: ");
        for (int i = 0; i < sorted.size(); i++) {
            sb.append(sorted.get(i).getBalance());
            if (i < sorted.size() - 1) sb.append(", ");
        }
        System.out.println(sb.toString() + ".");

        System.out.println("\nDetailed Sorted Listing:");
        sorted.forEach(acc -> System.out.println("  " + acc));
        System.out.println();

        // 3. History ArrayList Tracking
        System.out.println("[PART 3: Transaction History Log]");
        store.getHistory().forEach(h -> System.out.println("  [LOG] " + h));

        System.out.println("\nPractical 12 completed successfully.");
    }
}

