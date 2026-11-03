/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 2: Thermostat, Cinema Show, and Encapsulated Customer/Account
 * Problem Statement: Implement Smart Thermostat, Cinema Show booking with state management,
 * and encapsulate Customer and Account classes for MiniBank.
 */

// ==========================================
// 1. THERMOSTAT CLASS
// ==========================================
class Thermostat {
    private String location;
    private int temperature;
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;

    public Thermostat(String location, int startTemp) {
        this.location = location;
        this.temperature = (startTemp >= MIN && startTemp <= MAX) ? startTemp : 22;
        activeCount++;
    }

    public Thermostat(String location) {
        this(location, 22);
    }

    public void raise() {
        if (temperature < MAX) {
            temperature++;
            System.out.println(location + " temp raised to: " + temperature);
        } else {
            System.out.println("Already at maximum (30)");
        }
    }

    public void lower() {
        if (temperature > MIN) {
            temperature--;
            System.out.println(location + " temp lowered to: " + temperature);
        } else {
            System.out.println("Already at minimum (16)");
        }
    }

    public int getTemperature() { return temperature; }
    public String getLocation() { return location; }
    public static int getActiveCount() { return activeCount; }
}

// ==========================================
// 2. CINEMA SHOW BOOKING CLASS
// ==========================================
class CinemaShow {
    private String movieTitle;
    private int totalSeats;
    private int bookedSeats;

    public CinemaShow(String movieTitle, int totalSeats) {
        this.movieTitle = movieTitle;
        this.totalSeats = totalSeats;
        this.bookedSeats = 0;
    }

    public boolean bookSeats(int count) {
        if (count > 0 && (bookedSeats + count) <= totalSeats) {
            bookedSeats += count;
            System.out.println("Booked " + count + " seats for '" + movieTitle + "'. Available: " + getAvailableSeats());
            return true;
        }
        System.out.println("Failed to book " + count + " seats for '" + movieTitle + "'. Not enough available seats!");
        return false;
    }

    public boolean cancelSeats(int count) {
        if (count > 0 && bookedSeats >= count) {
            bookedSeats -= count;
            System.out.println("Cancelled " + count + " seats. Available: " + getAvailableSeats());
            return true;
        }
        System.out.println("Invalid cancellation request.");
        return false;
    }

    public int getAvailableSeats() { return totalSeats - bookedSeats; }
    public String getMovieTitle() { return movieTitle; }
}

// ==========================================
// 3. ENCAPSULATED CUSTOMER & ACCOUNT CLASSES
// ==========================================
class Customer {
    private String name, email, mobile;
    private final String customerId;
    private static long customerCounter = 100;

    private static synchronized String generateCustomerId() {
        return "CUST" + (++customerCounter);
    }

    public Customer(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = generateCustomerId();
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }

    public void setEmail(String email) { this.email = email; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    @Override
    public String toString() {
        return "Customer[" + customerId + " | " + name + " | " + email + " | " + mobile + "]";
    }
}

class Account {
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;
    private static long accountCounter = 0;

    private static synchronized String generateAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = Math.max(openingBalance, 0);
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public long getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Bal: " + balance;
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical02 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 2: Thermostat, Cinema Show & Account/Cust");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        // 1. Thermostat Demo
        System.out.println("[PART 1: Smart Thermostat]");
        Thermostat livingRoom = new Thermostat("Living Room", 24);
        livingRoom.raise();
        livingRoom.lower();
        System.out.println("Active Thermostats count: " + Thermostat.getActiveCount() + "\n");

        // 2. Cinema Show Demo
        System.out.println("[PART 2: Cinema Show Booking]");
        CinemaShow show = new CinemaShow("Inception", 50);
        show.bookSeats(5);
        show.bookSeats(60); // Should fail
        System.out.println();

        // 3. Customer & Account Demo (Matches Expected Output in Doc)
        System.out.println("[PART 3: Encapsulated Customer & Account]");
        Customer cust = new Customer("Riya", "riya@example.com", "9876543210");
        System.out.println("Created: " + cust);

        Account acc = new Account("Riya", 5000);
        System.out.println("Initial State: " + acc);

        acc.deposit(2000);
        System.out.println(acc.getAccountNumber() + " | " + acc.getOwnerName() + " | Bal: " + acc.getBalance() + " (after deposit 2000)");

        boolean w1 = acc.withdraw(3000);
        System.out.println(acc.getAccountNumber() + " | " + acc.getOwnerName() + " | Bal: " + acc.getBalance() + " (after successful withdraw 3000)");

        boolean w2 = acc.withdraw(10000);
        System.out.println(acc.getAccountNumber() + " | " + acc.getOwnerName() + " | Bal: " + acc.getBalance() + " (after failed withdraw 10000 -> returns " + w2 + ")");

        System.out.println("\nPractical 02 completed successfully.");
    }
}

