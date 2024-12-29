package com.natixis.shoppingcart.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

  private final double price;
  private final String name;
  private final String description;
  private final String code;

}
