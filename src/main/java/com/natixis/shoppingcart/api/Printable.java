package com.natixis.shoppingcart.api;

import jakarta.json.JsonObject;

public interface Printable {
  JsonObject toJson();
}
