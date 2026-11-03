/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 4: Password Checker, Chat Filter, and Validation Layer
 * Problem Statement: Build regex validators for mobile and email, a CommandParser
 * to parse input strings into Command records, and a StatementFormatter using StringBuilder.
 */

import java.util.regex.Pattern;

// ==========================================
// 1. VALIDATOR (MOBILE, EMAIL & PASSWORD)
// ==========================================
class Validator {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    // At least 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
}

// ==========================================
// 2. CHAT FILTER (STRING REPLACEMENT)
// ==========================================
class ChatFilter {
    private static final String[] BANNED_WORDS = { "spam", "abuse", "fraud", "hack" };

    public static String filter(String text) {
        if (text == null) return null;
        String clean = text;
        for (String word : BANNED_WORDS) {
            clean = clean.replaceAll("(?i)\\b" + Pattern.quote(word) + "\\b", "***");
        }
        return clean;
    }
}

// ==========================================
// 3. COMMAND PARSER & COMMAND RECORD
// ==========================================
enum TransactionType { DEPOSIT, WITHDRAW, TRANSFER }

record Command(TransactionType type, String accountNumber, long amount) {}

class CommandParser {
    public static Command parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Line cannot be empty");
        }
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Malformed line: Expected '<TYPE> <ACCOUNT_NUM> <AMOUNT>'");
        }
        TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
        String accNum = parts[1];
        long amount = Long.parseLong(parts[2]);
        return new Command(type, accNum, amount);
    }
}

// ==========================================
// 4. STATEMENT FORMATTER (STRINGBUILDER)
// ==========================================
class StatementFormatter {
    public static String buildStatement(String accountNumber, String[] history) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Statement for ").append(accountNumber).append(" ---\n");
        for (String tx : history) {
            sb.append("  * ").append(tx).append("\n");
        }
        sb.append("--------------------------------------\n");
        return sb.toString();
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical04 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 4: Password, Chat Filter & Validation");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        // 1. Validation Layer (Matches Expected Output in Doc)
        System.out.println("[PART 1: Regex Validations]");
        System.out.println("isValidMobile(\"9876543210\") -> " + Validator.isValidMobile("9876543210"));
        System.out.println("isValidMobile(\"12345\") -> " + Validator.isValidMobile("12345"));
        System.out.println("isValidEmail(\"test@charusat.edu.in\") -> " + Validator.isValidEmail("test@charusat.edu.in"));
        System.out.println("isValidEmail(\"invalid-email\") -> " + Validator.isValidEmail("invalid-email"));
        System.out.println("isValidPassword(\"SecureP@ss123\") -> " + Validator.isValidPassword("SecureP@ss123"));
        System.out.println("isValidPassword(\"weak\") -> " + Validator.isValidPassword("weak") + "\n");

        // 2. Chat Filter Demo
        System.out.println("[PART 2: Chat Filter]");
        String message = "Beware of spam and fraud when you hack into servers!";
        System.out.println("Original: " + message);
        System.out.println("Filtered: " + ChatFilter.filter(message) + "\n");

        // 3. Command Parser Demo (Matches Expected Output in Doc)
        System.out.println("[PART 3: Command Parser]");
        String rawCommand = "deposit AC0001 500";
        Command cmd = CommandParser.parse(rawCommand);
        System.out.println("Parsed Command: type=" + cmd.type() + ", accountNumber=" + cmd.accountNumber() + ", amount=" + cmd.amount() + "\n");

        // 4. Statement Formatter Demo
        System.out.println("[PART 4: Statement Formatter]");
        String[] sampleHistory = {
            "2026-11-01: Opened account with 5000",
            "2026-11-02: Deposit 2000",
            "2026-11-03: Withdraw 1500"
        };
        System.out.print(StatementFormatter.buildStatement(cmd.accountNumber(), sampleHistory));

        System.out.println("Practical 04 completed successfully.");
    }
}

