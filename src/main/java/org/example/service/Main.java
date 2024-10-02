// Main class concerns the user interface and is responsible for handling user input and displaying the menu.
// It uses the Warehouse class to perform operations on products.


package org.example.service;

import org.example.entities.Category;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final WarehouseService warehouseService = new WarehouseService();

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
                    case 8 -> getAllCategoriesThatHasAtLeastOneProduct();
                    case 9 -> getNumberOfProductsInCategory();
                    case 10 -> getNumberOfProductsStartingWithEachLetter();
                    case 11 -> getAllProductsWithMaxRatingCreatedThisMonthSortedByDate();
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
        System.out.println("8. Get All Categories That Has At Least One Product");
        System.out.println("9. Get Number Of Products In Category");
        System.out.println("10. Get Number Of Products Starting With Each Letter");
        System.out.println("11. Get All Products With Max Rating Created This Month Sorted By Date");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        int id = getUniqueProductId();
        String name = getProductName();
        Category category = getProductCategory();
        int rating = getProductRating();

        try {
            warehouseService.addProduct(id, name, category, rating, LocalDateTime.now());
            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllProducts() {
        var products = warehouseService.getAllProducts();
        products.forEach(System.out::println);
    }

    private static void getProductById() {
        int id = getProductId();
        var product = warehouseService.getProductById(id);
        product.ifPresentOrElse(System.out::println, () -> System.out.println("Product not found."));
    }

    private static void updateProduct() {
        int id = getProductId();
        if (warehouseService.getProductById(id).isEmpty()) {
            System.out.println("Product ID does not exist. Please enter a valid ID.");
            return;
        }

        String newName = getProductName();
        Category newCategory = getProductCategory();
        int newRating = getProductRating();

        try {
            boolean updated = warehouseService.updateProduct(id, newName, newCategory, newRating);
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
                var products = warehouseService.getAllProductsByCategorySortedByProductName(category);
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
                var products = warehouseService.getAllProductsCreatedAfterASpecificDate(date);
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
        var products = warehouseService.getAllProductsThatHasBeenModifiedSinceCreation();
        if (products.isEmpty()) {
            System.out.println("No products have been modified since creation.");
        } else {
            products.forEach(System.out::println);
        }
    }

    private static void getAllCategoriesThatHasAtLeastOneProduct() {
        var categories = warehouseService.getAllCategoriesThatHasAtLeastOneProduct();
        if (categories.isEmpty()) {
            System.out.println("No categories found with products.");
        } else {
            categories.forEach(System.out::println);
        }
    }

    private static void getNumberOfProductsInCategory() {
        while (true) {
            System.out.print("Enter product category (ELECTRONICS, CLOTHING, BOOKS, TOYS): ");
            try {
                Category category = Category.valueOf(scanner.nextLine().toUpperCase());
                long count = warehouseService.getNumberOfProductsInCategory(category);
                System.out.println("Number of products in category " + category + ": " + count);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category. Please enter a valid category.");
            }
        }
    }

    private static void getNumberOfProductsStartingWithEachLetter() {
        var counts = warehouseService.getNumberOfProductsStartingWithEachLetter();
        counts.forEach((letter, count) -> System.out.println(letter + ": " + count));
    }

    private static void getAllProductsWithMaxRatingCreatedThisMonthSortedByDate() {
        var products = warehouseService.getAllProductsWithMaxRatingCreatedThisMonthSortedByDate();
        if (products.isEmpty()) {
            System.out.println("No products found with max rating created this month.");
        } else {
            products.forEach(System.out::println);
        }
    }

    private static int getProductId() {
        while (true) {
            System.out.print("Enter product ID: ");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                warehouseService.validateProductId(id);
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
                warehouseService.checkIfProductIdExists(id);
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