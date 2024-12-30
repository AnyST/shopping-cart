package com.natixis.shoppingcart.item;

import com.natixis.shoppingcart.api.Printable;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item implements Printable {

  private final String code;
  private final String name;
  private final String description;
  private final double price;

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("code", code)
        .add("name", name)
        .add("description", description)
        .add("price", price)
        .build();
  }
}
