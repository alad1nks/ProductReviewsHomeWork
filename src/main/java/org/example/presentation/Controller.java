package org.example.presentation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.domain.Model;
import org.example.entities.ProductRequest;
import org.example.entities.ReviewRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.example.consts.Constants.*;

public class Controller {
    private final Model model;
    public Controller() throws IOException, SQLException {
        model = new Model();
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext(
                "/product", exchange -> {
                    if (POST.equals(exchange.getRequestMethod()) && !exchange.getRequestURI().toString().contains("review")) {
                        postProduct(exchange);
                    } else if (POST.equals(exchange.getRequestMethod())) {
                        postReview(exchange);
                    } else if (DELETE.equals(exchange.getRequestMethod())) {
                        deleteProduct(exchange);
                    } else if (GET.equals(exchange.getRequestMethod())) {
                        getProduct(exchange);
                    }
                    exchange.close();
                }
        );
        server.setExecutor(null);
        server.start();

    }

    private void postProduct(HttpExchange exchange) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = bufferedReader.read()) != -1) {
            buf.append((char) b);
        }
        ProductRequest productRequest = objectMapper.readValue(buf.toString(), ProductRequest.class);
        try {
            model.postProduct(productRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bufferedReader.close();
        inputStreamReader.close();
    }

    private void postReview(HttpExchange exchange) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = bufferedReader.read()) != -1) {
            buf.append((char) b);
        }
        ReviewRequest reviewRequest = objectMapper.readValue(buf.toString(), ReviewRequest.class);
        long postId = Long.parseLong(exchange.getRequestURI().toString().split("/")[2]);
        try {
            model.postReview(postId, reviewRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bufferedReader.close();
        inputStreamReader.close();
    }

    private void deleteProduct(HttpExchange exchange) {
        try {
            long id = Long.parseLong(exchange.getRequestURI().toString().split("/")[2]);
            model.deleteProduct(id);
        } catch (NumberFormatException | SQLException ignored) {

        }
    }

    private void getProduct(HttpExchange exchange) {
        long postId = Long.parseLong(exchange.getRequestURI().toString().split("/")[2]);
        try {
            String respText = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).writer().withDefaultPrettyPrinter().writeValueAsString(model.getProduct(postId));
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
