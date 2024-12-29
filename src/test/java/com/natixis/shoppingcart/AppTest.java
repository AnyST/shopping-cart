package com.natixis.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {

  @Test
  void smokeTest() {
    assertDoesNotThrow(() -> App.main());
  }
}
