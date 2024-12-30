package com.natixis.shoppingcart.service;

import com.natixis.shoppingcart.cart.Cart;
import com.natixis.shoppingcart.item.Item;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class InputReader {

  private static final System.Logger LOG = System.getLogger(InputReader.class.getName());

  private final Scanner scanner;
  private final Properties config;
  private final Exporter exporter;

  public int choice() {
    try {
      return Integer.parseInt(scanner.nextLine().trim());
    } catch (final NumberFormatException e) {
      return -1;
    }
  }

  public void addItemTo(Cart cart) {
    System.out.println("Enter item name: ");
    final var name = scanner.nextLine();
    System.out.println("Enter item description: ");
    var description = scanner.nextLine();
    double price;
    try {
      System.out.println("Enter item price: ");
      price = Double.parseDouble(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("\nInvalid price format. Please try again.\n");
      return;
    }
    final var item = Item.builder().name(name).description(description).price(price).build();
    cart.addItem(item);
    System.out.println("Added item: " + item.getName());
    LOG.log(System.Logger.Level.DEBUG, "Item added: " + item.getName());
  }

  public void removeItemFrom(final Cart cart) {
    System.out.println("Enter item name to remove:");
    final var toRemove = scanner.nextLine().trim();
    var isRemoved = cart.removeItem(toRemove);
    if (isRemoved) {
      System.out.println("\nThe item has been removed from the cart\n");
      LOG.log(System.Logger.Level.DEBUG, "Item removed: " + toRemove);
    } else {
      System.out.println("\nThe item \"" + toRemove + "\" was not found!\n");
    }
  }

  public void prettyPrint(final Cart cart) {
    System.out.println("This is your cart: ");
    System.out.println(Exporter.prettyPrint(cart.itemsWithPrices()));
  }

  public void exportTo(Cart cart) throws IOException {
    System.out.println("Exporting your cart...");
    exporter.writeToFile(cart);
    System.out.println("\nCart exported to file at: " + config.getProperty("file.path"));
  }
}
