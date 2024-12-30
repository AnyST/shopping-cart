package com.natixis.shoppingcart.cart;

import static org.junit.jupiter.api.Assertions.*;

import com.natixis.shoppingcart.item.Item;
import jakarta.json.Json;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class CartTest {

  private static final Item ITEM_1 =
      Item.builder().name("Item1").description("Description1").price(10).build();
  private static final Item ITEM_2 =
      Item.builder().name("Item2").description("Description2").price(20).build();

  @Nested
  class EmptyCart {

    @Test
    void testEmptyCart() {
      var cart = Cart.emptyCart();
      assertNotNull(cart, "Cart instance should not be null");
      assertTrue(cart.getItems().isEmpty(), "Cart should be empty");
    }

    @Test
    void testEmptyCartItemsWithPrices() {
      var cart = Cart.emptyCart();
      var itemsWithPrices = cart.itemsWithPrices();
      assertNotNull(itemsWithPrices, "itemsWithPrices should not be null");
      assertTrue(itemsWithPrices.getJsonObject("items").isEmpty(), "Items object should be empty");
      assertEquals(
          0.0,
          itemsWithPrices.getJsonNumber("sum").doubleValue(),
          "Total price should be zero for an empty cart");
    }
  }

  @Nested
  class ToJson {

    @Test
    void testToJson_withEmptyCart() {
      var cart = Cart.emptyCart();
      var json = cart.toJson();
      assertNotNull(json, "JSON representation should not be null");
      assertEquals(
          Json.createObjectBuilder().add("cart", Json.createArrayBuilder().build()).build(),
          json,
          "JSON should represent an empty cart correctly");
    }

    @Test
    void testToJson_withItems() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      cart.addItem(ITEM_2);
      var json = cart.toJson();
      assertNotNull(json, "JSON representation should not be null");
      var arr = Json.createArrayBuilder().add(ITEM_1.toJson()).add(ITEM_2.toJson()).build();
      assertTrue(arr.contains(ITEM_1.toJson()), "ITEM_1 should be in the cart");
      assertTrue(arr.contains(ITEM_2.toJson()), "ITEM_2 should be in the cart");
    }
  }

  @Nested
  class AddItem {

    @Test
    void testAddSingleItem() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      assertEquals(1, cart.getItems().size(), "Cart should contain exactly one item.");
      assertTrue(cart.getItems().contains(ITEM_1), "Cart should contain ITEM_1.");
    }

    @Test
    void testAddDuplicateItem() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      cart.addItem(ITEM_1);
      assertEquals(1, cart.getItems().size(), "Cart should not allow duplicate items.");
    }

    @Test
    void testAddMultipleItems() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      cart.addItem(ITEM_2);
      assertEquals(2, cart.getItems().size(), "Cart should contain exactly two items.");
      assertTrue(
          cart.getItems().containsAll(Set.of(ITEM_1, ITEM_2)),
          "Cart should contain ITEM_1 and ITEM_2.");
    }
  }

  @Nested
  class RemoveItem {

    @Test
    void testRemoveExistingItem() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      cart.addItem(ITEM_2);
      var result = cart.removeItem(ITEM_1.getName());
      assertTrue(result, "Removing an existing item should return true");
      assertEquals(
          1, cart.getItems().size(), "Cart should contain exactly one item after removal.");
      assertFalse(cart.getItems().contains(ITEM_1), "Cart should no longer contain ITEM_1.");
    }

    @Test
    void testRemoveNonExistingItem() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      var result = cart.removeItem(ITEM_2.getName());
      assertFalse(result, "Removing a non-existing item should return false");
      assertEquals(
          1,
          cart.getItems().size(),
          "Cart size should not change when removing non-existing item.");
      assertTrue(cart.getItems().contains(ITEM_1), "Cart should still contain ITEM_1.");
    }

    @Test
    void testRemoveAllItems() {
      var cart = Cart.emptyCart();
      cart.addItem(ITEM_1);
      cart.addItem(ITEM_2);
      cart.removeItem(ITEM_1.getName());
      cart.removeItem(ITEM_2.getName());
      assertTrue(cart.getItems().isEmpty(), "Cart should be empty after removing all items.");
    }
  }
}
