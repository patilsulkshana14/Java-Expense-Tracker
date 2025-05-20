import java.io.*;
import java.util.*;

class Transaction {
    String type;       // income or expense
    String category;   // salary, food, etc.
    double amount;
    String month;

    Transaction(String type, String category, double amount, String month) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.month = month;
    }

    public String toString() {
        return type + "," + category + "," + amount + "," + month;
    }
}

public class ExpenseTracker {
    static List<Transaction> transactions = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Show Summary");
            System.out.println("4. Save to File");
            System.out.println("5. Load from File");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (ch) {
                case 1: addTransaction("income"); break;
                case 2: addTransaction("expense"); break;
                case 3: showSummary(); break;
                case 4: saveToFile(); break;
                case 5: loadFromFile(); break;
                case 6: exit = true; break;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void addTransaction(String type) {
        System.out.print("Enter month (e.g. Jan): ");
        String month = sc.nextLine();

        String category;
        if (type.equals("income")) {
            System.out.print("Enter category (salary/business): ");
        } else {
            System.out.print("Enter category (food/rent/travel): ");
        }
        category = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine(); // clear buffer

        transactions.add(new Transaction(type, category, amount, month));
        System.out.println("Transaction added successfully.");
    }

    static void showSummary() {
        Map<String, Double> incomeMap = new HashMap<>();
        Map<String, Double> expenseMap = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.type.equals("income")) {
                incomeMap.put(t.month, incomeMap.getOrDefault(t.month, 0.0) + t.amount);
            } else {
                expenseMap.put(t.month, expenseMap.getOrDefault(t.month, 0.0) + t.amount);
            }
        }

        System.out.println("\n--- Monthly Summary ---");
        Set<String> months = new HashSet<>();
        months.addAll(incomeMap.keySet());
        months.addAll(expenseMap.keySet());

        for (String m : months) {
            double income = incomeMap.getOrDefault(m, 0.0);
            double expense = expenseMap.getOrDefault(m, 0.0);
            double balance = income - expense;

            System.out.println(m + ": Income = " + income + ", Expense = " + expense + ", Balance = " + balance);
        }
    }

    static void saveToFile() {
        try {
            FileWriter fw = new FileWriter("data.txt");
            for (Transaction t : transactions) {
                fw.write(t.toString() + "\n");
            }
            fw.close();
            System.out.println("Data saved to data.txt");
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    static void loadFromFile() {
        try {
            File file = new File("data.txt");
            Scanner fileScanner = new Scanner(file);
            transactions.clear();

            System.out.println("\n--- Loaded Transactions ---");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String type = parts[0];
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String month = parts[3];
                    Transaction t = new Transaction(type, category, amount, month);
                    transactions.add(t);
                    System.out.println("Type: " + type + ", Category: " + category + ", Amount: " + amount + ", Month: " + month);
                }
            }
            fileScanner.close();
            System.out.println("Data loaded from file.");
        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
    }
}
