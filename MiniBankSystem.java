import java.io.*;
import java.util.*;

public class MiniBankSystem {

    static Scanner scanner = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    static User loggedInUser = null;
    static String fileName = "users.txt"; // File to store users' data

    public static void main(String[] args) {
        loadUsersFromFile(); // Load users from the file when the system starts
        int option;
        
        do {
            // Main Menu
            System.out.println("Mini Bank Management System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.print("Enter your option: ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    // Register a new user
    private static void register() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        if (isEmailUnique(email)) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter initial balance: ");
            double balance = scanner.nextDouble();

            int newId = users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1;

            User newUser = new User(newId, name, email, password, balance);
            users.add(newUser);

            saveUsersToFile(); // Save updated users list to file
            System.out.println("Registration successful!");
        } else {
            System.out.println("Email already exists. Please use a different email.");
        }
    }

    // Login user
    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> user = users.stream()
                                   .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                                   .findFirst();

        if (user.isPresent()) {
            loggedInUser = user.get();
            System.out.println("Login successful! Welcome, " + loggedInUser.getName());
            showUserMenu();
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    // Show the user menu after successful login
    private static void showUserMenu() {
        int option;
        do {
            System.out.println("1. Check Balance");
            System.out.println("2. Profile");
            System.out.println("3. Cash In / Cash Out");
            System.out.println("0. Logout");
            System.out.print("Enter your option: ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    viewProfile();
                    break;
                case 3:
                    cashInOrOut();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    // Check balance
    private static void checkBalance() {
        System.out.println("Your current balance is: " + loggedInUser.getBalance());
    }

    // View profile
    private static void viewProfile() {
        System.out.println("User Profile: ");
        System.out.println(loggedInUser);
    }

    // Cash In / Cash Out
    private static void cashInOrOut() {
        System.out.println("1. Cash In");
        System.out.println("2. Cash Out");
        System.out.print("Enter your option: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline
    
        if (option == 1) {
            System.out.print("Enter amount to cash in: ");
            double amount = scanner.nextDouble();
            loggedInUser.setBalance(loggedInUser.getBalance() + amount);
            System.out.println("Amount cashed in successfully!");
    
            saveUsersToFile(); // Save updated data to file after cash in
        } else if (option == 2) {
            System.out.print("Enter amount to cash out: ");
            double amount = scanner.nextDouble();
            if (amount <= loggedInUser.getBalance()) {
                loggedInUser.setBalance(loggedInUser.getBalance() - amount);
                System.out.println("Amount cashed out successfully!");
    
                saveUsersToFile(); // Save updated data to file after cash out
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }
   

    // Logout
    private static void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully!");
    }

    // Check if email is unique
    private static boolean isEmailUnique(String email) {
        return users.stream().noneMatch(u -> u.getEmail().equals(email));
    }

    // Load users from the file
    private static void loadUsersFromFile() {
        File file = new File(fileName);

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        String email = parts[2];
                        String password = parts[3];
                        double balance = Double.parseDouble(parts[4]);
                        users.add(new User(id, name, email, password, balance));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading from file: " + e.getMessage());
            }
        }
    }

    // Save users to the file
    private static void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (User user : users) {
                writer.write(user.getId() + "," + user.getName() + "," + user.getEmail() + ","
                        + user.getPassword() + "," + user.getBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
