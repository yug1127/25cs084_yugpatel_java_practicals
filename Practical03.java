/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 3: Distinct Points, Card Check, and Object Methods
 * Problem Statement: Override toString, equals, and hashCode contract correctly in Account,
 * and add a nested Address class and clone support in Customer.
 */

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// ==========================================
// 1. DISTINCT POINTS WITH EQUALS & HASHCODE
// ==========================================
class Point {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

// ==========================================
// 2. PLAYING CARD CHECK (EQUALS & HASHCODE)
// ==========================================
class Card {
    private final String suit;
    private final String rank;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return Objects.equals(suit, card.suit) && Objects.equals(rank, card.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

// ==========================================
// 3. ACCOUNT WITH EQUALS, HASHCODE, TOSTRING
// ==========================================
class Account {
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public long getBalance() { return balance; }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " | Active: " + active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}

// ==========================================
// 4. CUSTOMER WITH NESTED ADDRESS & CLONING
// ==========================================
class Customer implements Cloneable {
    private String name, email;
    private Address address;

    public static class Address implements Cloneable {
        private String line, city, pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                return new Address(line, city, pincode);
            }
        }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }

    public Customer(String name, String email, Address address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Address getAddress() { return address; }

    @Override
    public Customer clone() {
        try {
            Customer cloned = (Customer) super.clone();
            if (this.address != null) {
                cloned.address = this.address.clone(); // deep copy
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Customer[name=" + name + ", email=" + email + ", address=" + address + "]";
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical03 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 3: Distinct Points, Card & Object Methods");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        // 1. Points Demonstration
        System.out.println("[PART 1: Distinct Points & Hash Collisions]");
        Set<Point> points = new HashSet<>();
        Point p1 = new Point(2, 3);
        Point p2 = new Point(2, 3);
        Point p3 = new Point(4, 5);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("Total unique points in set: " + points.size() + " " + points + "\n");

        // 2. Card Check Demonstration
        System.out.println("[PART 2: Card Equality]");
        Card c1 = new Card("Ace", "Spades");
        Card c2 = new Card("Ace", "Spades");
        System.out.println("c1: " + c1 + " | c2: " + c2);
        System.out.println("c1.equals(c2): " + c1.equals(c2) + "\n");

        // 3. Account Object Methods Demonstration (Matches Expected Output in Doc)
        System.out.println("[PART 3: Account toString, equals, and hashCode]");
        Account a1 = new Account("AC0001", "Riya", 4000);
        Account a2 = new Account("AC0001", "Riya Patel", 9999); // same account number
        Account a3 = new Account("AC0002", "Amit", 4000);

        System.out.println("Printing a1: " + a1);
        System.out.println("a1.equals(a2) -> " + a1.equals(a2));
        System.out.println("a1.equals(a3) -> " + a1.equals(a3));
        System.out.println("a1 instanceof Account -> " + (a1 instanceof Account) + "\n");

        // 4. Customer & Deep Clone Demonstration
        System.out.println("[PART 4: Nested Address & Deep Cloning]");
        Customer.Address addr = new Customer.Address("101 Silicon Valley", "Anand", "388001");
        Customer original = new Customer("Yug", "yug@example.com", addr);
        Customer cloned = original.clone();
        cloned.setName("Yug Cloned");
        cloned.getAddress().setCity("Ahmedabad");

        System.out.println("Original: " + original);
        System.out.println("Cloned:   " + cloned);
        System.out.println("Address deep-copied successfully: " + (!original.getAddress().getCity().equals(cloned.getAddress().getCity())));

        System.out.println("\nPractical 03 completed successfully.");
    }
}

