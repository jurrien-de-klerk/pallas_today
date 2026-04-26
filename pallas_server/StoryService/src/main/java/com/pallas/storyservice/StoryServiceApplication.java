package com.pallas.storyservice;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class StoryServiceApplication {
  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

    server.createContext("/hello", StoryServiceApplication::handleHello);

    server.setExecutor(null);
    server.start();

    System.out.println("StoryService started on port 8080");
  }

  private static void handleHello(HttpExchange exchange) throws IOException {
    if (!"GET".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    byte[] response = "world!".getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
    exchange.sendResponseHeaders(200, response.length);

    try (OutputStream outputStream = exchange.getResponseBody()) {
      outputStream.write(response);
    }
  }
}
