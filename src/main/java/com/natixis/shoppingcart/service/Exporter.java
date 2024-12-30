package com.natixis.shoppingcart.service;

import com.natixis.shoppingcart.api.Printable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import lombok.Data;

@Data
public class Exporter {

  private static final String FILE_PATH = "file.path";
  private static final String FILE_NAME = "file.name";

  private final Properties config;


  public void writeToFile(Printable printable) throws IOException {
    var filePath = config.getProperty(FILE_PATH);
    var fileName = config.getProperty(FILE_NAME);
    var path = Path.of(filePath, fileName);
    Files.writeString(path, printable.toJson().toString());
  }
}
