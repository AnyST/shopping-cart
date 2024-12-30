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

  private final Set<Item> items;

  public static Cart emptyCart() {
    return new Cart(Set.of());
  }

  public static Cart singleItemCart(Item item) {
    return new Cart(Set.of((Objects.requireNonNull(item))));
  }

  public static Cart multipleItemsCart(Collection<Item> items) {
    return new Cart((new HashSet<>(items)));
  }

  public JsonObject itemsWithPrices() {
    var sum = items.stream().map(Item::getPrice).reduce(0d, Double::sum);
    var itemsWithPrices = Json.createObjectBuilder();
    items.forEach(i -> itemsWithPrices.add(i.getCode(), i.getPrice()));
    return Json.createObjectBuilder() //
        .add("sum:", sum)
        .add("items:", itemsWithPrices.build())
        .build();
  }

  @Override
  public JsonObject toJson() {
    var arr = Json.createArrayBuilder();
    items.forEach(i -> arr.add(i.toJson()));
    return Json.createObjectBuilder().add("cart", arr.build()).build();
  }
}
