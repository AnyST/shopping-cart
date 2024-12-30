package com.natixis.shoppingcart;

import com.natixis.shoppingcart.cart.Cart;
import com.natixis.shoppingcart.service.Exporter;
import com.natixis.shoppingcart.service.InputReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public final class App {

  public static void main(String... args) throws IOException {

    final var scanner = new Scanner(System.in);
    final var config = loadConfig();
    final var exporter = new Exporter(config);
    final var reader = new InputReader(scanner, config, exporter);
    final var cart = Cart.emptyCart();

    while (true) {
      System.out.println("\nWelcome to the store\n");
      System.out.println("Choose an action:");
      System.out.println("1. Add item to cart");
      System.out.println("2. Remove item from cart");
      System.out.println("3. View cart");
      System.out.println("4. Export cart");
      System.out.println("5. Exit");

      System.out.println("\nEnter your choice:\n");
      final var choice = reader.choice();

      switch (choice) {
        case 1:
          reader.addItemTo(cart);
          break;
        case 2:
          reader.removeItemFrom(cart);
          break;
        case 3:
          reader.prettyPrint(cart);
          break;
        case 4:
          reader.exportTo(cart);
          break;
        case 5:
          System.out.println("\nThank you for shopping. Goodbye!");
          return;
        default:
          System.out.println("\nInvalid choice. Please enter 1, 2, 3, 4 or 5.\n");
      }
    }
  }

  private static Properties loadConfig() {
    try (var is = App.class.getResourceAsStream("/config.properties")) {
      final var config = new Properties();
      config.load(is);
      return config;
    } catch (final IOException e) {
      throw new IllegalStateException("Failed to load configuration file", e);
    }
  }
}
