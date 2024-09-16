// Main class concerns the user interface and is responsible for handling user input and displaying the menu.
// It uses the Warehouse class to perform operations on products.


package org.example.service;

import org.example.entities.Category;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Warehouse warehouse = new Warehouse();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addProduct();
                    case 2 -> getAllProducts();
                    case 3 -> getProductById();
                    case 4 -> updateProduct();
                    case 5 -> getAllProductsByCategorySortedByProductName();
                    case 6 -> getAllProductsCreatedAfterASpecificDate();
                    case 7 -> getAllProductsThatHasBeenModifiedSinceCreation();
                    case 0 -> exit = true;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Add Product");
        System.out.println("2. Get All Products");
        System.out.println("3. Get Product By ID");
        System.out.println("4. Update Product");
        System.out.println("5. Get All Products By Category Sorted By Product Name");
        System.out.println("6. Get All Products Created After A Specific Date");
        System.out.println("7. Get All Products That Have Been Modified Since Creation");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        int id;
        while (true) {
            System.out.print("Enter product ID: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id <= 0) {
                    System.out.println("Invalid input. Product ID must be a number.");
                    continue;
                }
                if (warehouse.getProductById(id).isPresent()) {
                    System.out.println("Product ID already exists. Please enter a unique ID.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product ID must be a number.");
            }
        }

        String name;
        while (true) {
            System.out.print("Enter product name: ");
            name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Product name cannot be empty.");
            }
        }

        Category category;
        while (true) {
            System.out.print("Enter product category (ELECTRONICS, CLOTHING, BOOKS, TOYS): ");
            try {
                category = Category.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category.");
            }
        }

        int rating;
        while (true) {
            System.out.print("Enter product rating between 1-10: ");
            try {
                rating = Integer.parseInt(scanner.nextLine());
                if (rating >= 1 && rating <= 10) {
                    break;
                } else {
                    System.out.println("Invalid rating. Rating must be between 1-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product rating must be a number.");
            }
        }

        warehouse.addProduct(id, name, category, rating);
        System.out.println("Product added successfully.");
    }

    private static void getAllProducts() {
        var products = warehouse.getAllProducts();
        products.forEach(System.out::println);
    }

    private static void getProductById() {
        int id;
        while (true) {
            System.out.print("Enter product ID: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id <= 0) {
                    System.out.println("Invalid input. Product ID must be a number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product ID must be a number.");
            }
        }

        var product = warehouse.getProductById(id);
        product.ifPresentOrElse(System.out::println, () -> System.out.println("Product not found."));
    }

    private static void updateProduct() {
        int id;
        while (true) {
            System.out.print("Enter product ID: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id <= 0) {
                    System.out.println("Invalid input. Product ID must be a number.");
                    continue;
                }
                if (warehouse.getProductById(id).isEmpty()) {
                    System.out.println("Product ID does not exist. Please enter a valid ID.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product ID must be a number.");
            }
        }

        String newName;
        while (true) {
            System.out.print("Enter new product name: ");
            newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Product name cannot be empty.");
            }
        }

        Category newCategory;
        while (true) {
            System.out.print("Enter new product category (ELECTRONICS, CLOTHING, BOOKS, TOYS): ");
            try {
                newCategory = Category.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category.");
            }
        }

        int newRating;
        while (true) {
            System.out.print("Enter new product rating between 1-10: ");
            try {
                newRating = Integer.parseInt(scanner.nextLine());
                if (newRating >= 1 && newRating <= 10) {
                    break;
                } else {
                    System.out.println("Invalid rating. Rating must be between 1-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product rating must be a number.");
            }
        }

        boolean updated = warehouse.updateProduct(id, newName, newCategory, newRating);
        System.out.println("Product updated: " + updated);
    }

    private static void getAllProductsByCategorySortedByProductName() {
        while (true) {
            System.out.print("Enter product category (ELECTRONICS, CLOTHING, BOOKS, TOYS): ");
            try {
                Category category = Category.valueOf(scanner.nextLine().toUpperCase());
                var products = warehouse.getAllProductsByCategorySortedByProductName(category);
                products.forEach(System.out::println);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category. Please enter a valid category.");
            }
        }
    }

    private static void getAllProductsCreatedAfterASpecificDate() {
        while (true) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            try {
                LocalDateTime date = LocalDateTime.parse(scanner.nextLine() + "T00:00:00");
                var products = warehouse.getAllProductsCreatedAfterASpecificDate(date);
                products.forEach(System.out::println);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter a valid date in the format YYYY-MM-DD.");
            }
        }
    }

    private static void getAllProductsThatHasBeenModifiedSinceCreation() {
        var products = warehouse.getAllProductsThatHasBeenModifiedSinceCreation();
        products.forEach(System.out::println);
    }
}