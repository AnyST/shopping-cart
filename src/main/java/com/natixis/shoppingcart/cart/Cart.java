package com.natixis.shoppingcart.cart;

import com.natixis.shoppingcart.item.Item;
import java.util.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {

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
}
