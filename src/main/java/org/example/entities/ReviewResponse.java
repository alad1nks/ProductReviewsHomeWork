package org.example.entities;

public class ReviewResponse {
    private final String message;
    private final int rating;

    public ReviewResponse(String message, int rating) {
        this.message = message;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
}
