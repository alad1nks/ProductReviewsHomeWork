package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.example.Constants.*;

public class Controller {
    private final Model model;
    public Controller() throws IOException, SQLException {
        model = new Model();
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

        server.createContext(
                "/product", new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        if (POST.equals(exchange.getRequestMethod()) && !exchange.getRequestURI().toString().contains("review")) {
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
                        } else if (POST.equals(exchange.getRequestMethod())) {
                            System.out.println("postreview");
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
                        } else if (DELETE.equals(exchange.getRequestMethod())) {
                            try {
                                long id = Long.parseLong(exchange.getRequestURI().toString().split("/")[2]);
                                System.out.println(id);
                                model.deleteProduct(id);
                            } catch (NumberFormatException | SQLException ex) {

                            }
                        } else if (GET.equals(exchange.getRequestMethod())) {

                        }
                        exchange.close();
                    }
                }
        );

        server.createContext("/hello", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                System.out.println("hello");
                String respText = "hello";
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
                exchange.close();
            }
        });

        server.setExecutor(null);
        server.start();

    }
}
