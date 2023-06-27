package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.Constants.*;

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
        statement.executeUpdate(SQL_DELETE_PRODUCT);
    }

    public void insertReview(long postId, String message, int rating) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();
        String SQL_INSERT_PRODUCT = "INSERT INTO reviews VALUES (DEFAULT, " + postId + ", '" + message + "', " + rating + ")";
        System.out.println(SQL_INSERT_PRODUCT);
        statement.executeUpdate(SQL_INSERT_PRODUCT);
    }

}
