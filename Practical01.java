/**
 * CHAROTAR UNIVERSITY OF SCIENCE AND TECHNOLOGY (CHARUSAT)
 * FACULTY OF TECHNOLOGY AND ENGINEERING | B.Tech CSE (Sem 3, 2026-27)
 * SUBJECT: OBJECT-ORIENTED PROGRAMMING (CEUE203)
 * Student Name: Yug Patel | Student ID: 25cs084
 * GitHub Repo: https://github.com/yugpatel25/oop-minibank-25cs084
 *
 * Practical 1: Vending Machine, Toll Booth, and MiniBank Menu Shell
 * Problem Statement: Implement a Vending Machine using enums and switch expressions,
 * a Toll Booth tracking vehicle counts, and the initial Menu Shell for the MiniBank console project.
 */

import java.util.Scanner;

// ==========================================
// 1. VENDING MACHINE WITH ENUMS & SWITCH
// ==========================================
enum Coin { ONE, TWO, FIVE, TEN }

class VendingMachine {
    public static void run(Scanner scanner) {
        int snackPrice = 15;
        int total = 0;
        System.out.println("--- Vending Machine (Price: " + snackPrice + ") ---");

        while (total < snackPrice) {
            System.out.print("Enter coin (ONE, TWO, FIVE, TEN): ");
            if (!scanner.hasNext()) break;
            String coinName = scanner.next().toUpperCase();
            try {
                Coin coin = Coin.valueOf(coinName);
                int value = switch (coin) {
                    case ONE -> 1;
                    case TWO -> 2;
                    case FIVE -> 5;
                    case TEN -> 10;
                };
                total += value;
                System.out.println("Total so far: " + total);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid coin! Try again.");
            }
        }
        if (total >= snackPrice) {
            System.out.println("Paid. Change: " + (total - snackPrice));
        }
    }
}

// ==========================================
// 2. TOLL BOOTH WITH RECORDS & SWITCH
// ==========================================
record Vehicle(String number, String type) {}

class TollBooth {
    public static void run(Scanner scanner) {
        System.out.println("\n--- Toll Booth System ---");
        long totalToll = 0;
        int bikeCount = 0, carCount = 0, truckCount = 0;

        while (scanner.hasNext()) {
            System.out.print("Enter vehicle number (or 'done' to exit): ");
            String number = scanner.next();
            if (number.equalsIgnoreCase("done")) break;

            System.out.print("Enter vehicle type (bike, car, truck): ");
            if (!scanner.hasNext()) break;
            String type = scanner.next().toLowerCase();

            Vehicle vehicle = new Vehicle(number, type);
            long toll = switch (type) {
                case "bike" -> { bikeCount++; yield 20; }
                case "car" -> { carCount++; yield 50; }
                case "truck" -> { truckCount++; yield 150; }
                default -> { System.out.println("Unknown type, toll = 0"); yield 0; }
            };
            totalToll += toll;
            System.out.println("Logged vehicle: " + vehicle.number() + " (" + vehicle.type() + ") | Toll: " + toll);
        }

        System.out.println("Total toll: " + totalToll);
        String mostFrequent = "none";
        int max = Math.max(bikeCount, Math.max(carCount, truckCount));
        if (max > 0) {
            if (max == carCount) mostFrequent = "car";
            else if (max == bikeCount) mostFrequent = "bike";
            else mostFrequent = "truck";
        }
        System.out.println("Most frequent: " + mostFrequent);
    }
}

// ==========================================
// 3. MINIBANK MENU SHELL
// ==========================================
record BankInfo(String name, String branch) {}
enum MenuOption { OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT }

class MiniBank {
    public static void run(Scanner scanner) {
        BankInfo info = new BankInfo("MiniBank Corp", "Main Branch");
        System.out.println("\n=== " + info.name() + " (" + info.branch() + ") ===");
        
        MenuOption choice = null;
        do {
            System.out.println("\n--- Menu ---");
            MenuOption[] options = MenuOption.values();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Choose an option (1-" + options.length + "): ");
            
            if (!scanner.hasNextInt()) {
                if (!scanner.hasNext()) break;
                System.out.println("Invalid input.");
                scanner.next();
                continue;
            }
            
            int index = scanner.nextInt() - 1;
            if (index < 0 || index >= options.length) {
                System.out.println("Invalid choice.");
                continue;
            }
            
            choice = options[index];
            switch (choice) {
                case OPEN_ACCOUNT -> System.out.println("Open Account placeholder.");
                case DEPOSIT -> System.out.println("Deposit placeholder.");
                case WITHDRAW -> System.out.println("Withdraw placeholder.");
                case TRANSFER -> System.out.println("Transfer placeholder.");
                case EXIT -> System.out.println("Goodbye!");
            }
        } while (choice != MenuOption.EXIT);
    }
}

// ==========================================
// MAIN TEST RUNNER
// ==========================================
public class Practical01 {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Practical 1: Vending Machine, Toll Booth & MiniBank");
        System.out.println("Student: Yug Patel | ID: 25cs084");
        System.out.println("==================================================\n");

        if (args.length > 0 && args[0].equalsIgnoreCase("--interactive")) {
            Scanner console = new Scanner(System.in);
            VendingMachine.run(console);
            TollBooth.run(console);
            MiniBank.run(console);
            console.close();
            return;
        }

        // Automated demonstration matching the practical document execution
        System.out.println("[DEMO PART 1: Vending Machine]");
        String vendingInput = "TEN\nFIVE\n";
        VendingMachine.run(new Scanner(vendingInput));

        System.out.println("\n[DEMO PART 2: Toll Booth]");
        String tollInput = "GJ01AB1234 car\nGJ01CD5678 car\nGJ01EF9999 bike\ndone\n";
        TollBooth.run(new Scanner(tollInput));

        System.out.println("\n[DEMO PART 3: MiniBank Menu Shell]");
        String bankInput = "5\n"; // Choose EXIT
        MiniBank.run(new Scanner(bankInput));

        System.out.println("\nPractical 01 completed successfully.");
    }
}

