// --== CS400 File Header Information ==--
// Name: Brian Castellano
// Email: bkcastellano@wisc.edu
// Team: KE Red
// Role: Data Wrangler
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class ProductDataReader implements ProductDataReaderInterface {

  @Override
  public ArrayList<Product> readDataSet(Reader inputFileReader)
      throws IOException, DataFormatException {

    // Checks if the file input actually exists
    if (inputFileReader == null) {
      throw new FileNotFoundException("The file was not found");
    }

    // An array that stores all of the movies, and will be returned at the end
    ArrayList<Product> products = new ArrayList<Product>();

    try {
      // Using BufferedReader to process the CSV file
      BufferedReader b = new BufferedReader(inputFileReader);

      String read = b.readLine(); // Starts at the first product, will be iterated later

      while (read != null) { // Stops when the line is null, indicating the end of the CSV
        String[] values = read.split("\\s*,\\s*");
        // Creates a new movie object with the relevant information
        Product p = new Product(values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]),
            Double.parseDouble(values[4]), Double.parseDouble(values[5]));
        products.add(p);

        read = b.readLine();
      }

    }

    catch (Exception e) {
      e.printStackTrace();
      throw new IOException();
    }

    return products;
  }

}
