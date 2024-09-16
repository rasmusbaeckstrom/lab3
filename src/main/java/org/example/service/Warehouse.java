package org.example.service;

import org.example.entities.Product;
import org.example.entities.ProductRecord;
import org.example.entities.Category;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final List<Product> products = new ArrayList<>();

    //Method to add a product to the warehouse
    public void addProduct(String id, String name, Category category, int rating) {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        Product product = new Product(id, name, category, rating, LocalDateTime.now());
        products.add(product);
    }

    //Method to get all products from the warehouse
    public List<ProductRecord> getAllProducts() {
        List<ProductRecord> productRecords;
        productRecords = products.stream()
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    //Method to get a product by id
    public Optional<ProductRecord> getProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .findFirst();
    }

    //Method to update a product
    public boolean updateProduct(String id, String newName, Category newCategory, int newRating) {
        Optional<Product> productOpt = products.stream()
                .filter(p -> p.getId().equals(id))
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

    //Method to get all products by category sorted by product name
    public List<ProductRecord> getAllProductsByCategorySortedByProductName(Category category) {
        List<ProductRecord> productRecords = products.stream()
                .filter(p -> p.getCategory().equals(category))
                .sorted(Comparator.comparing(Product::getName))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    //Method to get all products created after a specific date
    public List<ProductRecord> getAllProductsCreatedAfterASpecificDate(LocalDateTime date) {
        List<ProductRecord> productRecords = products.stream()
                .filter(p -> p.getCreatedDate().isAfter(date))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    public List<ProductRecord> getAllProductsThatHasBeenModifiedSinceCreation() {
        List<ProductRecord> productRecords = products.stream()
                .filter(p -> p.getModifiedDate().isAfter(p.getCreatedDate()))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }









    //Move this to a new class
    public static void main(String[] args) {
        System.out.println("Implementing a Warehouse");

        //Add products to the warehouse
        var warehouse = new Warehouse();
        warehouse.addProduct("1", "Product 1", Category.ELECTRONICS, 5);
        warehouse.addProduct("2", "Product 2", Category.BOOKS, 4);
        warehouse.addProduct("3", "Product 3", Category.CLOTHING, 3);
        warehouse.addProduct("4", "Product 4", Category.TOYS, 2);

//        //Get all products from the warehouse
//        var products = warehouse.getAllProducts();
//        products.forEach(System.out::println);


//        //Get a product by id
//        var product = warehouse.getProductById("2");
//        product.ifPresent(System.out::println);


//        //Get a product by id that does not exist
//        var product2 = warehouse.getProductById("5");
//        product2.ifPresentOrElse(System.out::println, () -> System.out.println("Product not found"));


//        //Update a product
//        var updated = warehouse.updateProduct("3", "Product 3 Updated", Category.CLOTHING, 4);
//        System.out.println("Product updated: " + updated);

//        //Get all products from the warehouse after updating
//        var products2 = warehouse.getAllProducts();
//        products2.forEach(System.out::println);


//        //Get all products by category sorted by product name
//        var productsByCategory = warehouse.getAllProductsByCategorySortedByProductName(Category.ELECTRONICS);
//        productsByCategory.forEach(System.out::println);


//        // Create a specific dat4
//        LocalDateTime specificDate = LocalDateTime.of(2024, 9, 14, 0, 0);

//        // Get all products created after a specific date
//        var productsCreatedAfterSpecificDate = warehouse.getAllProductsCreatedAfterASpecificDate(specificDate);
//        productsCreatedAfterSpecificDate.forEach(System.out::println);


//        // Update a product
//        warehouse.updateProduct("1", "Product 1 Updated", Category.BOOKS, 9);

//        // Get all products that has been modified since creation
//        var productsModifiedSinceCreation = warehouse.getAllProductsThatHasBeenModifiedSinceCreation();
//        productsModifiedSinceCreation.forEach(System.out::println);


    }
}