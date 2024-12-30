package com.natixis.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;

import com.natixis.shoppingcart.item.Item;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.Test;

class ItemTest {

  @Test
  void testToJson() {
    var item =
        Item.builder() //
            .code("Code1")
            .name("ItemName1")
            .description("Description1")
            .price(2)
            .build();
    JsonObject json = item.toJson();
    assertNotNull(json, "JSON object should not be null");
    assertEquals(2, json.getJsonNumber("price").doubleValue(), "Price should match");
  }
}
