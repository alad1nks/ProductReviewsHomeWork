package org.example;


import java.sql.SQLException;

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
}
