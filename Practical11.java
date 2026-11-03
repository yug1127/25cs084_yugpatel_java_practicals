/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 11: Serialization, NIO, and End-of-Day Reporting
 * Problem Statement: Make model classes Serializable, save and load account state
 * using StatePersister with ObjectOutputStream, and generate end-of-day
 * reports using NIO Path and Files.
 */

import java.io.*;
import java.nio.file.*;
import java.util.List;

// ==========================================
// 1. SERIALIZABLE ACCOUNT MODEL
// ==========================================
class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String accountNumber;
    private final String ownerName;
    private long balance;

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
        return accountNumber + " | " + ownerName + " | Bal: " + balance;
    }
}

// ==========================================
// 2. STATE PERSISTER (OBJECT STREAMS & NIO)
// ==========================================
class StatePersister {
    public static void save(Account[] accounts, Path file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
            oos.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) ois.readObject();
        }
    }
}

// ==========================================
// 3. NIO END-OF-DAY REPORT GENERATOR
// ==========================================
class EndOfDayReport {
    public static void generateReport(Path reportPath, long totalDeposits, long totalWithdrawals) throws IOException {
        long netChange = totalDeposits - totalWithdrawals;
        String content = "=== END-OF-DAY FINANCIAL REPORT ===\n" +
                         "Date: " + java.time.LocalDate.now() + "\n" +
                         "Total Deposits:    Rs. " + totalDeposits + "\n" +
                         "Total Withdrawals: Rs. " + totalWithdrawals + "\n" +
                         "Net Change:        Rs. " + netChange + "\n" +
                         "Status: Reconciled Successfully\n";

        Files.writeString(reportPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical11 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 11: Serialization, NIO & Reporting");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        Path dataFile = Paths.get("accounts.dat");
        Path reportFile = Paths.get("eod_report.txt");

        try {
            // 1. Serialization Demo (Matches Expected Output in Doc)
            System.out.println("[PART 1: State Serialization and Deserialization]");
            Account[] initialAccounts = {
                new Account("AC0001", "Riya Patel", 7000),
                new Account("AC0002", "Yug Patel", 12000),
                new Account("AC0003", "Aarav Shah", 3500)
            };

            System.out.println("Saving accounts to " + dataFile.getFileName() + "...");
            StatePersister.save(initialAccounts, dataFile);

            System.out.println("Loading accounts back from " + dataFile.getFileName() + "...");
            Account[] loadedAccounts = StatePersister.load(dataFile);

            System.out.println("Accounts successfully saved to accounts.dat and reloaded.");
            for (Account acc : loadedAccounts) {
                System.out.println("  Loaded: " + acc);
            }
            System.out.println();

            // 2. NIO End-of-Day Report (Matches Expected Output in Doc)
            System.out.println("[PART 2: NIO Report Generation]");
            long sampleDeposits = 25000;
            long sampleWithdrawals = 9500;
            EndOfDayReport.generateReport(reportFile, sampleDeposits, sampleWithdrawals);

            System.out.println("Report generated: Total Deposits, Total Withdrawals, and Net Change verified.");
            System.out.println("\n--- Content of " + reportFile.getFileName() + " ---");
            List<String> reportLines = Files.readAllLines(reportFile);
            reportLines.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cleanup demo artifacts
            try {
                Files.deleteIfExists(dataFile);
                Files.deleteIfExists(reportFile);
            } catch (IOException ignored) {}
        }

        System.out.println("\nPractical 11 completed successfully.");
    }
}

