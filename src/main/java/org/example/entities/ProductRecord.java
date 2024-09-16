package org.example.entities;

import java.time.LocalDateTime;

public record ProductRecord(int id, String name, Category category, int rating, LocalDateTime createdDate, LocalDateTime modifiedDate) {
}