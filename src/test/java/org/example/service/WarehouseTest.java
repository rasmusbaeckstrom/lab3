package org.example.service;

import org.example.entities.Category;
import org.example.entities.ProductRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    @Test
    void testAddProduct() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);
        Optional<ProductRecord> product = warehouse.getProductById(1);
        assertTrue(product.isPresent());
        assertEquals("Laptop", product.get().name());
        assertEquals(Category.ELECTRONICS, product.get().category());
        assertEquals(8, product.get().rating());
    }

    @Test
    void testGetAllProducts() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);
        warehouse.addProduct(2, "Shirt", Category.CLOTHING, 7);
        List<ProductRecord> products = warehouse.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);
        Optional<ProductRecord> product = warehouse.getProductById(1);
        assertTrue(product.isPresent());
        assertEquals("Laptop", product.get().name());
        assertEquals(1, product.get().id());

        Optional<ProductRecord> nonExistingProduct = warehouse.getProductById(999);
        assertFalse(nonExistingProduct.isPresent());
    }

    @Test
    void testUpdateProduct() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);

        boolean updated = warehouse.updateProduct(1, "Gaming Laptop", Category.ELECTRONICS, 9);
        assertTrue(updated);
        Optional<ProductRecord> product = warehouse.getProductById(1);
        assertTrue(product.isPresent());
        assertEquals("Gaming Laptop", product.get().name());
        assertEquals(9, product.get().rating());

        boolean notUpdated = warehouse.updateProduct(999, "Non-Existent Product", Category.ELECTRONICS, 5);
        assertFalse(notUpdated);
    }

    @Test
    void testGetAllProductsByCategorySortedByProductName() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);
        warehouse.addProduct(2, "Smartphone", Category.ELECTRONICS, 9);
        warehouse.addProduct(3, "Tablet", Category.ELECTRONICS, 7);
        warehouse.addProduct(4, "Camera", Category.ELECTRONICS, 6);
        warehouse.addProduct(5, "Headphones", Category.ELECTRONICS, 8);
        warehouse.addProduct(6, "Shirt", Category.CLOTHING, 7);

        List<ProductRecord> products = warehouse.getAllProductsByCategorySortedByProductName(Category.ELECTRONICS);
        assertEquals(5, products.size());
        assertEquals("Camera", products.get(0).name());
        assertEquals("Headphones", products.get(1).name());
        assertEquals("Laptop", products.get(2).name());
        assertEquals("Smartphone", products.get(3).name());
        assertEquals("Tablet", products.get(4).name());
    }

    @Test
    void testGetAllProductsCreatedAfterASpecificDate() {
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);
        List<ProductRecord> products = warehouse.getAllProductsCreatedAfterASpecificDate(date);
        assertEquals(1, products.size());
    }

    @Test
    void testGetAllProductsThatHasBeenModifiedSinceCreation() throws InterruptedException {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8);
        Thread.sleep(10); // Short delay to ensure the timestamp is different
        warehouse.updateProduct(1, "Gaming Laptop", Category.ELECTRONICS, 9);
        List<ProductRecord> products = warehouse.getAllProductsThatHasBeenModifiedSinceCreation();
        assertEquals(1, products.size());
    }
}