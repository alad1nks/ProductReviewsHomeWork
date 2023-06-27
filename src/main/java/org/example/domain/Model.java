package org.example.domain;


import org.example.data.DataBase;
import org.example.entities.ProductRequest;
import org.example.entities.ProductResponse;
import org.example.entities.ReviewRequest;
import org.example.entities.ReviewResponse;

import java.sql.SQLException;
import java.util.List;

public class Model {
    private final DataBase dataBase;
    public Model() throws SQLException {
        dataBase = new DataBase();
    }

    public void postProduct(ProductRequest productRequest) throws SQLException {
        dataBase.insertProduct(productRequest.getDescription());
    }

    public void deleteProduct(long id) throws SQLException {
        dataBase.deleteProduct(id);
    }

    public void postReview(long postId, ReviewRequest reviewRequest) throws SQLException {
        dataBase.insertReview(postId, reviewRequest.getMessage(), reviewRequest.getRating());
    }

    public ProductResponse getProduct(long id) throws SQLException {
        String description = dataBase.getDescription(id);
        List<ReviewResponse> reviews = dataBase.getReviews(id);
        float averageRating = 0f;
        for (ReviewResponse review : reviews) {
            averageRating += review.getRating();
        }
        averageRating /= reviews.size();
        return new ProductResponse(description, averageRating, reviews);
    }
}
