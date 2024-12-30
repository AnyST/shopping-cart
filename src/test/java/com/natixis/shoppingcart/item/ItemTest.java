package com.natixis.shoppingcart.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

final class ItemTest {

  @Nested
  class ToJson {

    @Test
    void testToJson() {
      var item =
          Item.builder() //
              .name("ItemName1")
              .description("Description1")
              .price(2)
              .build();
      var json = item.toJson();
      assertNotNull(json, "JSON object should not be null");
      assertEquals(2, json.getJsonNumber("price").doubleValue(), "Price should match");
    }
  }
}
