package com.natixis.shoppingcart.cart;

import static org.junit.jupiter.api.Assertions.*;

import com.natixis.shoppingcart.item.Item;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class CartTest {

  private final Item item =
      Item.builder() //
          .code("Code1")
          .name("ItemName1")
          .description("Description1")
          .price(2)
          .build();

  @Test
  void testEmptyCart() {
    var cart = Cart.emptyCart();
    assertNotNull(cart, "Cart instance should not be null");
    assertTrue(cart.getItems().isEmpty(), "Cart should be empty");
  }

  @Test
  void testSingleItemCart_withNonNullItem() {
    var cart = Cart.singleItemCart(item);

    assertNotNull(cart, "Cart instance should not be null");
    assertEquals(1, cart.getItems().size(), "Cart should contain exactly one item");
    assertTrue(cart.getItems().contains(item), "Cart should contain the provided item");
  }

  @Test
  void testSingleItemCart_withNullItem() {
    assertThrows(
        NullPointerException.class,
        () -> Cart.singleItemCart(null),
        "Creating a cart with a null item should throw NullPointerException");
  }

  @Test
  void testMultipleItemsCart_withEmptyCollection() {
    var cart = Cart.multipleItemsCart(List.of());

    assertNotNull(cart, "Cart instance should not be null");
    assertTrue(cart.getItems().isEmpty(), "Cart should be empty for an empty collection");
  }

  @ParameterizedTest
  @CsvSource({ //
    "2, ItemName2, Description2, Code2", //
    "2, ItemName3, Description3, Code3" //
  })
  void testMultipleItemsCart_withMultipleItems(
      int price, String name, String description, String code) {
    var items = new HashSet<Item>();
    var item2 =
        Item.builder() //
            .code(code)
            .name(name)
            .description(description)
            .price(price)
            .build();
    items.add(this.item);
    items.add(item2);
    var cart = Cart.multipleItemsCart(items);

    assertNotNull(cart, "Cart instance should not be null");
    assertEquals(
        items.size(), cart.getItems().size(), "Cart should contain all items from the collection");
    assertTrue(cart.getItems().containsAll(items), "Cart should contain all the provided items");
  }

  @Test
  void testMultipleItemsCart_withDuplicates() {
    var item1 = item;
    var item2 =
        Item.builder() //
            .code("Code2")
            .name("ItemName2")
            .description("Description2")
            .price(4)
            .build();
    Collection<Item> items = List.of(item1, item2, item1);

    var cart = Cart.multipleItemsCart(items);

    assertNotNull(cart, "Cart instance should not be null");
    assertEquals(
        Set.of(item1, item2).size(),
        cart.getItems().size(),
        "Cart should contain unique items only");
  }

  @Test
  void testMultipleItemsCart_withNullCollection() {
    assertThrows(
        NullPointerException.class,
        () -> Cart.multipleItemsCart(null),
        "Creating a cart with a null collection should throw NullPointerException");
  }

  @Nested
  class ToJsonTest {

    @Test
    void testToJson_withEmptyCart() {
      var cart = Cart.emptyCart();
      var json = cart.toJson();

      assertNotNull(json, "JSON output should not be null");
      assertEquals(0, json.getJsonObject("items:").size(), "Items object should be empty");
      assertEquals(0, json.getJsonNumber("sum:").doubleValue(), "Sum should be 0");
    }

    @Test
    void testToJson_withSingleItemCart() {
      var cart = Cart.singleItemCart(item);
      var json = cart.toJson();

      assertNotNull(json, "JSON output should not be null");
      assertEquals(
          1, json.getJsonObject("items:").size(), "Items object should contain exactly one item");
      assertEquals(
          item.getPrice(),
          json.getJsonNumber("sum:").doubleValue(),
          "Sum should match the price of the single item");
      assertEquals(
          item.getPrice(),
          json.getJsonObject("items:").getJsonNumber(item.getCode()).doubleValue(),
          "Item price should match in the JSON output");
    }

    @Test
    void testToJson_withMultipleItemsCart() {
      var item2 =
          Item.builder()
              .code("Code2")
              .name("ItemName2")
              .description("Description2")
              .price(4)
              .build();
      var cart = Cart.multipleItemsCart(List.of(item, item2));
      var json = cart.toJson();

      assertNotNull(json, "JSON output should not be null");
      assertEquals(
          2, json.getJsonObject("items:").size(), "Items object should contain all provided items");
      assertEquals(
          item.getPrice() + item2.getPrice(),
          json.getJsonNumber("sum:").doubleValue(),
          "Sum should match the total price of all items in the cart");
      assertEquals(
          item.getPrice(),
          json.getJsonObject("items:").getJsonNumber(item.getCode()).doubleValue(),
          "First item's price should match in the JSON output");
      assertEquals(
          item2.getPrice(),
          json.getJsonObject("items:").getJsonNumber(item2.getCode()).doubleValue(),
          "Second item's price should match in the JSON output");
    }
  }
}
