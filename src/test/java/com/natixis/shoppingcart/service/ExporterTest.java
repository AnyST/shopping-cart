package com.natixis.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.*;

import com.natixis.shoppingcart.api.Printable;
import jakarta.json.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExporterTest {

  public static final String FILE_PATH = "file.path";
  public static final String FILE_NAME = "file.name";
  public static final String CART_JSON = "cart.json";
  private Exporter exporter;
  private Path tempDir;

  @BeforeEach
  void setUp() throws IOException {
    tempDir = Files.createTempDirectory("testExporter");
    var properties = new Properties();
    properties.setProperty(FILE_PATH, tempDir.toString());
    properties.setProperty(FILE_NAME, CART_JSON);
    exporter = new Exporter(properties);
  }

  @AfterEach
  void tearDown() {
    tempDir.toFile().deleteOnExit();
  }

  @Test
  void testWriteToFile() throws IOException {
    Printable printable = () -> Json.createObjectBuilder().add("key", "value").build();
    exporter.writeToFile(printable);
    Path outputFilePath = tempDir.resolve(CART_JSON);
    assertTrue(Files.exists(outputFilePath), "Output file should exist after write operation");

    var content = Files.readString(outputFilePath);
    var expected =
        """
        {
            "key": "value"
        }""";
    assertEquals(expected, content, "File content should match the expected JSON");
  }

  @Test
  void testWriteToFile_EmptyContent() throws IOException {
    Printable printable = () -> Json.createObjectBuilder().build();
    exporter.writeToFile(printable);
    var outputFilePath = tempDir.resolve(CART_JSON);
    assertTrue(
        Files.exists(outputFilePath), "Output file should still exist even for empty content");
    var content = Files.readString(outputFilePath);
    var expected =
        """
      {
      }""";
    assertEquals(expected, content, "File content should be empty for empty Printable content");
  }

  @Test
  void testWriteToFile_CreateDirectory() throws IOException {
    var newDirPath = tempDir.resolve("newDir");
    var properties = new Properties();
    properties.setProperty(FILE_PATH, newDirPath.toString());
    properties.setProperty(FILE_NAME, CART_JSON);
    var newExporter = new Exporter(properties);
    assertFalse(Files.exists(newDirPath), "Directory should not exist before test");
    Printable  printable = () -> Json.createObjectBuilder().add("key", "value").build();
    newExporter.writeToFile(printable);
    assertTrue(Files.exists(newDirPath), "Directory should be created during writeToFile");
    assertTrue(
      Files.exists(newDirPath.resolve(CART_JSON)), "File should exist inside the created directory");
  }

  @Test
  void testWriteToFile_InvalidDirectoryPath() {
    var invalidProperties = new Properties();
    invalidProperties.setProperty(FILE_PATH, "/invalid/path/that/cannot/be/created");
    invalidProperties.setProperty(FILE_NAME, CART_JSON);
    var invalidExporter = new Exporter(invalidProperties);
    Printable printable = () -> Json.createObjectBuilder().add("key", "value").build();
    var ex =
        Assertions.assertThrows(IOException.class, () -> invalidExporter.writeToFile(printable));
    assertNotNull(ex, "An IOException should be thrown when the directory cannot be created");
  }
}
