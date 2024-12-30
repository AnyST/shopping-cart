package com.natixis.shoppingcart.service;

import com.natixis.shoppingcart.api.Printable;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Properties;
import lombok.Data;

@Data
public class Exporter {

  private static final System.Logger LOG = System.getLogger(Exporter.class.getName());
  private static final String FILE_PATH = "file.path";
  private static final String FILE_NAME = "file.name";

  private final Properties config;

  public void writeToFile(Printable printable) throws IOException {
    var filePath = Path.of(config.getProperty(FILE_PATH));
    var fileName = Path.of(config.getProperty(FILE_NAME));
    if (!Files.isDirectory(filePath)) {
      Files.createDirectory(filePath);
      LOG.log(System.Logger.Level.DEBUG, "Directory created: " + filePath);
    }
    var path = filePath.resolve(fileName);
    Files.writeString(path, prettyPrint(printable.toJson()));
    LOG.log(System.Logger.Level.DEBUG, "Written to path: " + path);
  }

  public static String prettyPrint(final JsonObject jo) {
    var writerFactory =
        Json.createWriterFactory(Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true));
    final var sw = new StringWriter();
    try (var w = writerFactory.createWriter(sw)) {
      w.write(jo);
      return sw.toString();
    }
  }
}
