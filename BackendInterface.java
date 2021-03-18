import java.util.List;
import java.util.NoSuchElementException;

public interface BackendInterface {

  public boolean restock(Product product, int amount)
      throws NoSuchElementException, IllegalArgumentException;

  public boolean destock(Product product, int amount, boolean isSold)
      throws NoSuchElementException, IllegalArgumentException;
  // decreases the quantity of a product. Also
  // adds to the profit

  public boolean addNewProduct(Product product);
  // Adds a new product
  // to the tree.
  // Returns true if
  // successful, false otherwise

  public Product getProduct(String productName);


  public List<String> getUpdatedProducts();

  public List<Product> getAllProducts();

  public double getProfit(); // Returns the total money gotten from sold products, minus the total
                            // money spent on restocked or added products.

  public List<Product> needsRestock(int amount); // Returns an array of Products with less than
                            // AMOUNT quantity available
}
