package org.example.entities;

import java.time.LocalDateTime;

public record ProductRecord(String id, String name, Category category, int rating, LocalDateTime createdDate, LocalDateTime modifiedDate) {
    public ProductRecord {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
    }
}