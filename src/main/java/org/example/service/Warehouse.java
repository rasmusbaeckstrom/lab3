// Warehouse class concerns the business logic of the application.
// It contains a list of products and provides methods to add, update and retrieve products.
// It uses the Product, ProductRecord and Category classes to represent products and categories.


package org.example.service;

import org.example.entities.Product;
import org.example.entities.ProductRecord;
import org.example.entities.Category;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final List<Product> products = new ArrayList<>();

    // Method to add a product
    public void addProduct(int id, String name, Category category, int rating) {
        Product product = new Product(id, name, category, rating, LocalDateTime.now());
        products.add(product);
    }

    // Method to get all products
    public List<ProductRecord> getAllProducts() {
        List<ProductRecord> productRecords = products.stream()
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    // Method to get a product by ID
    public Optional<ProductRecord> getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .findFirst();
    }

    // Method to update a product
    public boolean updateProduct(int id, String newName, Category newCategory, int newRating) {
        Optional<Product> productOpt = products.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(newName);
            product.setCategory(newCategory);
            product.setRating(newRating);
            return true;
        } else {
            return false;
        }
    }

    // Method to get all products by category sorted by product name
    public List<ProductRecord> getAllProductsByCategorySortedByProductName(Category category) {
        List<ProductRecord> productRecords = products.stream()
                .filter(p -> p.getCategory().equals(category))
                .sorted(Comparator.comparing(Product::getName))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    // Method to get all products created after a specific date
    public List<ProductRecord> getAllProductsCreatedAfterASpecificDate(LocalDateTime date) {
        List<ProductRecord> productRecords = products.stream()
                .filter(p -> p.getCreatedDate().isAfter(date))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    // Method to get all products that have been modified since creation
    public List<ProductRecord> getAllProductsThatHasBeenModifiedSinceCreation() {
        List<ProductRecord> productRecords = products.stream()
                .filter(p -> p.getModifiedDate().isAfter(p.getCreatedDate()))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }
}