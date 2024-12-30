package com.natixis.shoppingcart.cart;

import com.natixis.shoppingcart.api.Printable;
import com.natixis.shoppingcart.item.Item;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart implements Printable {

  private final Set<Item> items = new HashSet<>();

  public static Cart emptyCart() {
    return new Cart();
  }

  public JsonObject itemsWithPrices() {
    var sum = items.stream().map(Item::getPrice).reduce(0d, Double::sum);
    var itemsWithPrices = Json.createObjectBuilder();
    items.forEach(i -> itemsWithPrices.add(i.getName(), i.getPrice()));
    return Json.createObjectBuilder() //
        .add("sum", sum)
        .add("items", itemsWithPrices.build())
        .build();
  }

  @Override
  public JsonObject toJson() {
    var arr = Json.createArrayBuilder();
    items.forEach(i -> arr.add(i.toJson()));
    return Json.createObjectBuilder().add("cart", arr.build()).build();
  }

  public void addItem(final Item item) {
    this.items.add(item);
  }

  public boolean removeItem(final String name) {
    return this.items.removeIf(i -> i.getName().equals(name));
  }
}
