package com.natixis.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;

import com.natixis.shoppingcart.cart.Cart;
import com.natixis.shoppingcart.item.Item;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class CartTest {

  private final Item item =
      Item.builder().price(2).name("ItemName1").description("Description1").code("Code1").build();

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
    var item2 = Item.builder().price(price).name(name).description(description).code(code).build();
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
        Item.builder()
            .price(4)
            .name("Item2")
            .description("Item description 2")
            .code("item-code-2")
            .build();
    Collection<Item> items = List.of(item1, item2, item1); // Duplicate item1

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
}
