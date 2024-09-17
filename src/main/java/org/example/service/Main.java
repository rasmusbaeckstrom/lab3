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
        System.out.println("7. Get All Products That Has Been Modified Since Creation");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        int id = getUniqueProductId();
        String name = getProductName();
        Category category = getProductCategory();
        int rating = getProductRating();

        try {
            warehouse.addProduct(id, name, category, rating);
            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllProducts() {
        var products = warehouse.getAllProducts();
        products.forEach(System.out::println);
    }

    private static void getProductById() {
        int id = getProductId();
        var product = warehouse.getProductById(id);
        product.ifPresentOrElse(System.out::println, () -> System.out.println("Product not found."));
    }

    private static void updateProduct() {
        int id = getProductId();
        if (warehouse.getProductById(id).isEmpty()) {
            System.out.println("Product ID does not exist. Please enter a valid ID.");
            return;
        }

        String newName = getProductName();
        Category newCategory = getProductCategory();
        int newRating = getProductRating();

        try {
            boolean updated = warehouse.updateProduct(id, newName, newCategory, newRating);
            System.out.println("Product updated: " + updated);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllProductsByCategorySortedByProductName() {
        while (true) {
            System.out.print("Enter product category (ELECTRONICS, CLOTHING, BOOKS, TOYS): ");
            try {
                Category category = Category.valueOf(scanner.nextLine().toUpperCase());
                var products = warehouse.getAllProductsByCategorySortedByProductName(category);
                if (products.isEmpty()) {
                    System.out.println("No products found in this category.");
                } else {
                    products.forEach(System.out::println);
                }
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
                if (products.isEmpty()) {
                    System.out.println("No products found created after this date.");
                } else {
                    products.forEach(System.out::println);
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter a valid date in the format YYYY-MM-DD.");
            }
        }
    }

    private static void getAllProductsThatHasBeenModifiedSinceCreation() {
        var products = warehouse.getAllProductsThatHasBeenModifiedSinceCreation();
        if (products.isEmpty()) {
            System.out.println("No products have been modified since creation.");
        } else {
            products.forEach(System.out::println);
        }
    }

    private static int getProductId() {
        while (true) {
            System.out.print("Enter product ID: ");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                warehouse.validateProductId(id);
                return id;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product ID must be a number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int getUniqueProductId() {
        while (true) {
            int id = getProductId();
            try {
                warehouse.checkIfProductIdExists(id);
                return id;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getProductName() {
        while (true) {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
            System.out.println("Product name cannot be empty.");
        }
    }

    private static Category getProductCategory() {
        while (true) {
            System.out.print("Enter product category (ELECTRONICS, CLOTHING, BOOKS, TOYS): ");
            try {
                return Category.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category.");
            }
        }
    }

    private static int getProductRating() {
        while (true) {
            System.out.print("Enter product rating between 1-10: ");
            try {
                int rating = Integer.parseInt(scanner.nextLine());
                if (rating >= 1 && rating <= 10) {
                    return rating;
                }
                System.out.println("Invalid rating. Rating must be between 1-10.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Product rating must be a number.");
            }
        }
    }
}