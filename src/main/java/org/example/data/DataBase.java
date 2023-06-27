package org.example.data;

import org.example.entities.ReviewResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.consts.Constants.*;

public class DataBase {
    public DataBase() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_CREATE_TABLE_PRODUCTS = "CREATE TABLE IF NOT EXISTS products (" +
                "id BIGINT GENERATED ALWAYS AS IDENTITY," +
                "description TEXT" +
                ");";
        String SQL_CREATE_TABLE_REVIEWS = "CREATE TABLE IF NOT EXISTS reviews (" +
                "id BIGINT GENERATED ALWAYS AS IDENTITY," +
                "post_id BIGINT," +
                "message TEXT," +
                "rating INT" +
                ");";
        statement.execute(SQL_CREATE_TABLE_PRODUCTS);
        statement.execute(SQL_CREATE_TABLE_REVIEWS);
    }

    public void insertProduct(String description) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_INSERT_PRODUCT = "INSERT INTO products VALUES (DEFAULT, '" + description + "')";
        statement.executeUpdate(SQL_INSERT_PRODUCT);
    }

    public void deleteProduct(long id) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_DELETE_PRODUCT = "DELETE FROM products WHERE id = " + id;
        String SQL_DELETE_REVIEWS = "DELETE FROM reviews WHERE post_id = " + id;
        statement.executeUpdate(SQL_DELETE_PRODUCT);
        statement.executeUpdate(SQL_DELETE_REVIEWS);
    }

    public void insertReview(long postId, String message, int rating) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_INSERT_PRODUCT = "INSERT INTO reviews VALUES (DEFAULT, " + postId + ", '" + message + "', " + rating + ")";
        System.out.println(SQL_INSERT_PRODUCT);
        statement.executeUpdate(SQL_INSERT_PRODUCT);
    }

    public String getDescription(long id) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_SELECT_DESCRIPTION = "SELECT description FROM products WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_DESCRIPTION);
        resultSet.next();
        return resultSet.getString("description");
    }

    public List<ReviewResponse> getReviews(long id) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_SELECT_DESCRIPTION = "SELECT message, rating FROM reviews WHERE post_id = " + id;
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_DESCRIPTION);
        ArrayList<ReviewResponse> reviews = new ArrayList<>();
        while (resultSet.next()) {
            reviews.add(new ReviewResponse(resultSet.getString("message"), resultSet.getInt("rating")));
        }
        return reviews;
    }
}
