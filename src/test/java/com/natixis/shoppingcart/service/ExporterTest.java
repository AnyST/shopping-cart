package com.natixis.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.natixis.shoppingcart.api.Printable;
import jakarta.json.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExporterTest {

  private Exporter exporter;
  private Path tempDir;

  @BeforeEach
  void setUp() throws IOException {
    tempDir = Files.createTempDirectory("testExporter");
    Properties properties = new Properties();
    properties.setProperty("file.path", tempDir.toString());
    properties.setProperty("file.name", "cart.json");
    exporter = new Exporter(properties);
  }

  @Test
  void testWriteToFile() throws IOException {
    Printable printable = () -> Json.createObjectBuilder().add("key", "value").build();
    exporter.writeToFile(printable);
    Path outputFilePath = tempDir.resolve("cart.json");
    assertTrue(Files.exists(outputFilePath), "Output file should exist after write operation");

    String content = Files.readString(outputFilePath);
    assertEquals("{\"key\":\"value\"}", content, "File content should match the expected JSON");
  }

  @Test
  void testWriteToFile_EmptyContent() throws IOException {
    Printable printable = () -> Json.createObjectBuilder().build();
    exporter.writeToFile(printable);
    Path outputFilePath = tempDir.resolve("cart.json");
    assertTrue(
        Files.exists(outputFilePath), "Output file should still exist even for empty content");
    String content = Files.readString(outputFilePath);
    assertEquals("{}", content, "File content should be empty for empty Printable content");
  }
}
