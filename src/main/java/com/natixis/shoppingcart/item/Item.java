package com.natixis.shoppingcart.item;

import com.natixis.shoppingcart.api.Printable;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item implements Printable, Comparable<Item>{

  private final UUID code = UUID.randomUUID();
  private final String name;
  private final String description;
  private final double price;

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("code", code.toString())
        .add("name", name)
        .add("description", description)
        .add("price", price)
        .build();
  }

  @Override
  public int compareTo(final Item o) {
    return name.compareTo(o.name);
  }
}
