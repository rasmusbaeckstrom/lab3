// WarehouseTest contains unit tests for the Warehouse class.


package org.example.service;

import org.example.entities.Category;
import org.example.entities.ProductRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    @Test
    void testAddProduct() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        Optional<ProductRecord> product = warehouse.getProductById(1);
        assertTrue(product.isPresent());
        assertEquals("Laptop", product.get().name());
        assertEquals(Category.ELECTRONICS, product.get().category());
        assertEquals(8, product.get().rating());
    }

    @Test
    void testGetAllProducts() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        warehouse.addProduct(2, "Shirt", Category.CLOTHING, 7, LocalDateTime.now());
        List<ProductRecord> products = warehouse.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        Optional<ProductRecord> product = warehouse.getProductById(1);
        assertTrue(product.isPresent());
        assertEquals("Laptop", product.get().name());
        assertEquals(1, product.get().id());

        Optional<ProductRecord> nonExistingProduct = warehouse.getProductById(999);
        assertFalse(nonExistingProduct.isPresent());
    }

    @Test
    void testUpdateProduct() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());

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
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        warehouse.addProduct(2, "Smartphone", Category.ELECTRONICS, 9, LocalDateTime.now());
        warehouse.addProduct(3, "Tablet", Category.ELECTRONICS, 7, LocalDateTime.now());
        warehouse.addProduct(4, "Camera", Category.ELECTRONICS, 6, LocalDateTime.now());
        warehouse.addProduct(5, "Headphones", Category.ELECTRONICS, 8, LocalDateTime.now());
        warehouse.addProduct(6, "Shirt", Category.CLOTHING, 7, LocalDateTime.now());

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
        LocalDateTime productDate = date.plusHours(1);
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, productDate);
        List<ProductRecord> products = warehouse.getAllProductsCreatedAfterASpecificDate(date);
        assertEquals(1, products.size());
    }

    @Test
    void testGetAllProductsThatHasBeenModifiedSinceCreation() throws InterruptedException {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        Thread.sleep(10); // Short delay to ensure the timestamp is different
        warehouse.updateProduct(1, "Gaming Laptop", Category.ELECTRONICS, 9);
        List<ProductRecord> products = warehouse.getAllProductsThatHasBeenModifiedSinceCreation();
        assertEquals(1, products.size());
    }


    @Test
    void testUpdateProductWithEmptyName() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.updateProduct(1, "", Category.ELECTRONICS, 9);
        });
    }

    @Test
    void testGetAllProductsByCategoryWhenNoneExist() {
        List<ProductRecord> products = warehouse.getAllProductsByCategorySortedByProductName(Category.BOOKS);
        assertTrue(products.isEmpty());
    }

    @Test
    void testGetAllProductsCreatedAfterASpecificDateWhenNoneExist() {
        LocalDateTime date = LocalDateTime.now().plusDays(1); // Future date
        List<ProductRecord> products = warehouse.getAllProductsCreatedAfterASpecificDate(date);
        assertTrue(products.isEmpty());
    }

    @Test
    void testGetAllProductsThatHasBeenModifiedSinceCreationWhenNoneModified() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        List<ProductRecord> products = warehouse.getAllProductsThatHasBeenModifiedSinceCreation();
        assertTrue(products.isEmpty());
    }

    @Test
    void testAddProductWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.addProduct(1, "", Category.ELECTRONICS, 8, LocalDateTime.now());
        });
    }

    @Test
    void testAddProductWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.addProduct(1, null, Category.ELECTRONICS, 8, LocalDateTime.now());
        });
    }

    @Test
    void testAddProductWithInvalidRatingLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 0, LocalDateTime.now());
        });
    }

    @Test
    void testAddProductWithInvalidRatingHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 11, LocalDateTime.now());
        });
    }

    @Test
    void testUpdateProductWithNullName() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.updateProduct(1, null, Category.ELECTRONICS, 9);
        });
    }

    @Test
    void testUpdateProductWithInvalidRatingLow() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.updateProduct(1, "Laptop", Category.ELECTRONICS, 0);
        });
    }

    @Test
    void testUpdateProductWithInvalidRatingHigh() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.updateProduct(1, "Laptop", Category.ELECTRONICS, 11);
        });
    }

    @Test
    void testValidateProductId() {
        assertDoesNotThrow(() -> warehouse.validateProductId(1));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> warehouse.validateProductId(0));
        assertEquals("Product ID must be a positive number.", exception.getMessage());
    }

    @Test
    void testValidateProduct() {
        assertDoesNotThrow(() -> warehouse.validateProduct("Laptop", 5));
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> warehouse.validateProduct("", 5));
        assertEquals("Product name cannot be empty.", exception1.getMessage());
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> warehouse.validateProduct("Laptop", 0));
        assertEquals("Product rating must be between 1 and 10.", exception2.getMessage());
    }

    @Test
    void testCheckIfProductIdExists() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> warehouse.checkIfProductIdExists(1));
        assertEquals("Product ID already exists.", exception.getMessage());
    }

    @Test
    void testGetAllCategoriesThatHasAtLeastOneProduct() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        warehouse.addProduct(2, "T-shirt", Category.CLOTHING, 7, LocalDateTime.now());
        Set<Category> categories = warehouse.getAllCategoriesThatHasAtLeastOneProduct();
        assertEquals(2, categories.size());
        assertTrue(categories.contains(Category.ELECTRONICS));
        assertTrue(categories.contains(Category.CLOTHING));
    }

    @Test
    void testGetAllCategoriesThatHasAtLeastOneProductWhenNoneExist() {
        Set<Category> categories = warehouse.getAllCategoriesThatHasAtLeastOneProduct();
        assertTrue(categories.isEmpty());
    }

    @Test
    void testGetNumberOfProductsInCategory() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        warehouse.addProduct(2, "Smartphone", Category.ELECTRONICS, 9, LocalDateTime.now());
        warehouse.addProduct(3, "T-shirt", Category.CLOTHING, 7, LocalDateTime.now());
        assertEquals(2, warehouse.getNumberOfProductsInCategory(Category.ELECTRONICS));
        assertEquals(1, warehouse.getNumberOfProductsInCategory(Category.CLOTHING));
    }

    @Test
    void testGetNumberOfProductsInCategoryWhenNoneExist() {
        assertEquals(0, warehouse.getNumberOfProductsInCategory(Category.BOOKS));
    }

    @Test
    void testAddProductWithExistingId() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.addProduct(1, "Smartphone", Category.ELECTRONICS, 9, LocalDateTime.now());
        });
    }

    @Test
    void testUpdateProductWithInvalidId() {
        boolean updated = warehouse.updateProduct(999, "Non-Existent Product", Category.ELECTRONICS, 5);
        assertFalse(updated);
    }

    @Test
    void testGetAllProductsWhenNoneExist() {
        List<ProductRecord> products = warehouse.getAllProducts();
        assertTrue(products.isEmpty());
    }

    @Test
    void testGetNumberOfProductsStartingWithEachLetter() {
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 8, LocalDateTime.now());
        warehouse.addProduct(2, "Smartphone", Category.ELECTRONICS, 9, LocalDateTime.now());
        warehouse.addProduct(3, "T-shirt", Category.CLOTHING, 7, LocalDateTime.now());;
        warehouse.addProduct(4, "Shoes", Category.CLOTHING, 6, LocalDateTime.now());
        warehouse.addProduct(5, "Book", Category.BOOKS, 5, LocalDateTime.now());
        warehouse.addProduct(6, "Tablet", Category.ELECTRONICS, 7, LocalDateTime.now());

        var result = warehouse.getNumberOfProductsStartingWithEachLetter();

        assertEquals(4, result.size());
        assertEquals(1, result.get('L'));
        assertEquals(2, result.get('S'));
        assertEquals(2, result.get('T'));
        assertEquals(1, result.get('B'));
        assertFalse(result.containsKey('A')); // Ensure no unexpected keys
    }

    @Test
    void testGetAllProductsWithMaxRatingCreatedThisMonthSortedByDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Add products with max rating created this month
        warehouse.addProduct(1, "Laptop", Category.ELECTRONICS, 10, startOfMonth.plusDays(1));
        warehouse.addProduct(2, "Smartphone", Category.ELECTRONICS, 10, startOfMonth.plusDays(2));

        // Add products with max rating created before this month
        warehouse.addProduct(3, "Tablet", Category.ELECTRONICS, 10, startOfMonth.minusDays(1));

        // Add products with less than max rating created this month
        warehouse.addProduct(4, "Camera", Category.ELECTRONICS, 9, startOfMonth.plusDays(3));

        var result = warehouse.getAllProductsWithMaxRatingCreatedThisMonthSortedByDate();

        assertEquals(2, result.size());
        assertEquals("Smartphone", result.get(0).name());
        assertEquals("Laptop", result.get(1).name());
    }

    @Test
    void testGetAllProductsWithMaxRatingCreatedThisMonthSortedByDateWhenNoneExist() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Add products with less than max rating created this month
        warehouse.addProduct(1, "Camera", Category.ELECTRONICS, 9, startOfMonth.plusDays(1));

        // Add products with max rating created before this month
        warehouse.addProduct(2, "Tablet", Category.ELECTRONICS, 10, startOfMonth.minusDays(1));

        var result = warehouse.getAllProductsWithMaxRatingCreatedThisMonthSortedByDate();

        assertTrue(result.isEmpty());
    }
}