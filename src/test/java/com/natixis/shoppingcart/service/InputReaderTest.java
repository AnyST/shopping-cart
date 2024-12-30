package com.natixis.shoppingcart.service;

import com.natixis.shoppingcart.cart.Cart;
import com.natixis.shoppingcart.item.Item;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

final class InputReaderTest {

  private Scanner mockScanner;
  private Exporter mockExporter;
  private Properties mockConfig;
  private Cart mockCart;

  private InputReader subject;

  @BeforeEach
  void setUp() {
    mockCart = Mockito.mock(Cart.class);
    mockScanner = Mockito.mock(Scanner.class);
    mockConfig = Mockito.mock(Properties.class);
    mockExporter = Mockito.mock(Exporter.class);
    subject = new InputReader(mockScanner, mockConfig, mockExporter);
  }

  @Nested
  class Choice {

    @Test
    void testChoice_ValidNumericInput_ReturnsParsedInteger() {
      Mockito.when(mockScanner.nextLine()).thenReturn("5");
      int result = subject.choice();
      Assertions.assertEquals(5, result);
    }

    @Test
    void testChoice_InvalidNonNumericInput_ReturnsMinusOne() {
      Mockito.when(mockScanner.nextLine()).thenReturn("invalid");
      int result = subject.choice();
      Assertions.assertEquals(-1, result);
    }
  }

  @Nested
  class AddItem {

    public static final String ITEM_NAME = "Test Item";
    public static final String ITEM_DESCRIPTION = "Description";

    @Test
    void testAddItemTo_ValidInputs_AddsItemToCart() {
      Mockito.when(mockScanner.nextLine()).thenReturn(ITEM_NAME, ITEM_DESCRIPTION, "10.5");
      subject.addItemTo(mockCart);
      Mockito.verify(mockCart, Mockito.times(1))
          .addItem(
              Mockito.argThat(
                  i ->
                      ITEM_NAME.equals(i.getName())
                          && ITEM_DESCRIPTION.equals(i.getDescription())
                          && 10.5 == i.getPrice()));
    }

    @Test
    void testAddItemTo_InvalidPrice_DoesNotAddItem() {
      Mockito.when(mockScanner.nextLine()).thenReturn(ITEM_NAME, ITEM_DESCRIPTION, "invalid-price");
      subject.addItemTo(mockCart);
      Mockito.verify(mockCart, Mockito.never()).addItem(Mockito.any(Item.class));
    }
  }

  @Nested
  class RemoveItem {

    public static final String NONEXISTENT = "Nonexistent Item";
    public static final String EXISTING = "Existing Item";

    @Test
    void testRemoveItemFrom_ItemExists_RemovesItem() {
      Mockito.when(mockScanner.nextLine()).thenReturn(EXISTING);
      Mockito.when(mockCart.removeItem(EXISTING)).thenReturn(true);
      subject.removeItemFrom(mockCart);
      Mockito.verify(mockCart, Mockito.times(1)).removeItem(EXISTING);
    }

    @Test
    void testRemoveItemFrom_ItemDoesNotExist_DoesNotRemoveItem() {
      Mockito.when(mockScanner.nextLine()).thenReturn(NONEXISTENT);
      Mockito.when(mockCart.removeItem(NONEXISTENT)).thenReturn(false);
      subject.removeItemFrom(mockCart);
      Mockito.verify(mockCart, Mockito.times(1)).removeItem(NONEXISTENT);
    }
  }

  @Nested
  class PrettyPrint {

    @Test
    void testPrettyPrint_WithItems_PrintsExpectedOutput() {
      JsonObject itemsWithPrices =
          Json.createObjectBuilder().add("item1", 10.5).add("item2", 5.0).build();
      Mockito.when(mockCart.itemsWithPrices()).thenReturn(itemsWithPrices);
      try (var exporter = Mockito.mockStatic(Exporter.class)) {
        exporter
            .when(() -> Exporter.prettyPrint(itemsWithPrices))
            .thenReturn("item1: $10.5\nitem2: $5.0");
        subject.prettyPrint(mockCart);
        exporter.verify(() -> Exporter.prettyPrint(itemsWithPrices), Mockito.times(1));
      }
    }

    @Test
    void testPrettyPrint_EmptyCart_PrintsEmptyMessage() {
      JsonObject emptyCart = Json.createObjectBuilder().build();
      Mockito.when(mockCart.itemsWithPrices()).thenReturn(emptyCart);
      try (var exporter = Mockito.mockStatic(Exporter.class)) {
        exporter.when(() -> Exporter.prettyPrint(emptyCart)).thenReturn("Your cart is empty.");
        subject.prettyPrint(mockCart);
        exporter.verify(() -> Exporter.prettyPrint(emptyCart), Mockito.times(1));
      }
    }
  }

  @Nested
  class ExportTo {

    public static final String FILE_PATH = "file.path";

    @Test
    void testExportTo_Success_ExportsCartToFile() throws IOException {
      Mockito.doNothing().when(mockExporter).writeToFile(mockCart);
      Mockito.when(mockConfig.getProperty(FILE_PATH)).thenReturn("/mock/path");
      subject.exportTo(mockCart);
      Mockito.verify(mockExporter, Mockito.times(1)).writeToFile(mockCart);
      Mockito.verify(mockConfig, Mockito.times(1)).getProperty(FILE_PATH);
    }

    @Test
    void testExportTo_Failure_ThrowsIOException() throws IOException {
      Mockito.doThrow(new IOException("Mock IOException")).when(mockExporter).writeToFile(mockCart);
      Assertions.assertThrows(IOException.class, () -> subject.exportTo(mockCart));
      Mockito.verify(mockExporter, Mockito.times(1)).writeToFile(mockCart);
    }
  }
}
