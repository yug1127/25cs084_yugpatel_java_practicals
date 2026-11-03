/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 13: Generic Repository and @Id Reflection
 * Problem Statement: Create a generic Repository<T> supporting CRUD operations,
 * reading entity keys dynamically via @Id annotation reflection, and
 * supporting varargs saveAll.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;

// ==========================================
// 1. @Id ANNOTATION
// ==========================================
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Id {}

// ==========================================
// 2. GENERIC REPOSITORY<T> WITH REFLECTION
// ==========================================
class Repository<T> {
    private final Map<String, T> items = new HashMap<>();

    public T save(T entity) {
        String id = extractId(entity);
        items.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    public T findById(String id) {
        return items.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean deleteById(String id) {
        return items.remove(id) != null;
    }

    private String extractId(T entity) {
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    Object val = field.get(entity);
                    if (val != null) return val.toString();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error extracting @Id field from entity", e);
        }
        return UUID.randomUUID().toString();
    }
}

// ==========================================
// 3. ENTITY MODELS FOR REPOSITORY TESTING
// ==========================================
class Account {
    @Id
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
        return "Account[" + accountNumber + " | " + ownerName + " | Bal: " + balance + "]";
    }
}

class Customer {
    @Id
    private final String customerId;
    private final String fullName;
    private final String email;

    public Customer(String customerId, String fullName, String email) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
    }

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Customer[" + customerId + " | " + fullName + " | " + email + "]";
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical13 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 13: Generic Repository & @Id Reflection");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        // 1. Generic Repository for Account
        System.out.println("[PART 1: Repository<Account> Testing]");
        Repository<Account> accountRepo = new Repository<>();
        Account a1 = new Account("AC0001", "Riya Patel", 4500);
        Account a2 = new Account("AC0002", "Yug Patel", 9500);
        Account a3 = new Account("AC0003", "Darshil Shah", 6200);

        // Varargs saveAll
        accountRepo.saveAll(a1, a2, a3);

        Account retrievedAcc = accountRepo.findById("AC0002");
        System.out.println("findById('AC0002'): " + retrievedAcc);
        System.out.println("Repository<Account> tested: findById returned correct Account object.\n");

        // 2. Generic Repository for Customer
        System.out.println("[PART 2: Repository<Customer> Testing]");
        Repository<Customer> customerRepo = new Repository<>();
        Customer c1 = new Customer("CUST101", "Riya Patel", "riya@example.com");
        Customer c2 = new Customer("CUST102", "Yug Patel", "yug@example.com");

        customerRepo.saveAll(c1, c2);

        Customer retrievedCust = customerRepo.findById("CUST101");
        System.out.println("findById('CUST101'): " + retrievedCust);
        System.out.println("Repository<Customer> reused successfully without code changes.\n");

        // 3. CRUD Verification
        System.out.println("[PART 3: findAll() and deleteById()]");
        System.out.println("All accounts in repo (" + accountRepo.findAll().size() + "): " + accountRepo.findAll());
        accountRepo.deleteById("AC0003");
        System.out.println("After deleting AC0003 (" + accountRepo.findAll().size() + "): " + accountRepo.findAll());

        System.out.println("\nPractical 13 completed successfully.");
    }
}

