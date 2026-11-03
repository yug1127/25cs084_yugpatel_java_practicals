/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 7: Custom Annotations & Reflection Validation
 * Problem Statement: Define custom @Id and @Positive annotations with retention
 * and target meta-annotations, and write an AnnotationValidator using reflection.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ==========================================
// 1. CUSTOM META-ANNOTATIONS
// ==========================================
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Id {}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Positive {
    String message() default "must be > 0";
}

// ==========================================
// 2. MODEL CLASS WITH ANNOTATIONS
// ==========================================
class Account {
    @Id
    private String accountNumber;

    private String ownerName;

    @Positive
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public long getBalance() { return balance; }
}

// ==========================================
// 3. REFLECTION-BASED VALIDATOR
// ==========================================
class AnnotationValidator {
    public static String[] validate(Object obj) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();
        if (obj == null) {
            return new String[] { "Target object is null" };
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Positive.class)) {
                Object val = field.get(obj);
                if (val instanceof Number num && num.doubleValue() <= 0) {
                    errors.add(field.getName() + " " + field.getAnnotation(Positive.class).message());
                }
            }
        }
        return errors.toArray(new String[0]);
    }

    public static String findIdField(Object obj) throws IllegalAccessException {
        if (obj == null) return null;
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName() + " = " + field.get(obj);
            }
        }
        return "No @Id field found";
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical07 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 7: Custom Annotations & Reflection");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        try {
            // Test 1: Account with invalid negative balance (Matches Expected Output in Doc)
            System.out.println("[PART 1: Validating Account with negative balance = -100]");
            Account invalidAcc = new Account("AC0001", "Riya", -100);
            String[] errors = AnnotationValidator.validate(invalidAcc);
            System.out.println("Account with balance = -100 validated:");
            System.out.println("Errors returned: " + Arrays.toString(errors) + "\n");

            // Test 2: Account with valid balance
            System.out.println("[PART 2: Validating Account with positive balance = 5000]");
            Account validAcc = new Account("AC0002", "Yug", 5000);
            String[] validErrors = AnnotationValidator.validate(validAcc);
            System.out.println("Errors returned for valid account: " + Arrays.toString(validErrors) + "\n");

            // Test 3: Detecting @Id via reflection
            System.out.println("[PART 3: Inspecting @Id annotation]");
            System.out.println("Id Field for validAcc: " + AnnotationValidator.findIdField(validAcc));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("\nPractical 07 completed successfully.");
    }
}

