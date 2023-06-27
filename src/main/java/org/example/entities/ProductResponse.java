package org.example.entities;

import java.util.List;

public class ProductResponse {
    private final String description;
    private final float averageRating;
    private final List<ReviewResponse> reviews;

    public ProductResponse(String description, float averageRating, List<ReviewResponse> reviews) {
        this.description = description;
        this.averageRating = averageRating;
        this.reviews = reviews;
    }
}
