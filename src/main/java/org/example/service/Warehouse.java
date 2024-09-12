package org.example.service;

import org.example.entities.Product;
import org.example.entities.ProductRecord;
import org.example.entities.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Warehouse {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(String id, String name, Category category, int rating) {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        Product product = new Product(id, name, category, rating, LocalDateTime.now());
        products.add(product);
    }

    public List<ProductRecord> getAllProducts() {
        List<ProductRecord> productRecords;
        productRecords = products.stream()
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(productRecords);
    }

    public Optional<ProductRecord> getProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .map(p -> new ProductRecord(p.getId(), p.getName(), p.getCategory(), p.getRating(), p.getCreatedDate(), p.getModifiedDate()))
                .findFirst();
    }

    public static void main(String[] args) {
        System.out.println("Hello There!");

        var warehouse = new Warehouse();
        warehouse.addProduct("1", "Product 1", Category.ELECTRONICS, 5);
        warehouse.addProduct("2", "Product 2", Category.BOOKS, 4);
        warehouse.addProduct("3", "Product 3", Category.CLOTHING, 3);
        warehouse.addProduct("4", "Product 4", Category.TOYS, 2);

        var products = warehouse.getAllProducts();
        products.forEach(System.out::println);

        var product = warehouse.getProductById("2");
        product.ifPresent(System.out::println);

        var product2 = warehouse.getProductById("5");
        product2.ifPresentOrElse(System.out::println, () -> System.out.println("Product not found"));
    }
}