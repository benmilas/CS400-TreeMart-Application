import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

public class Backend implements BackendInterface {

  private RedBlackTree<Product> tree;
  private ArrayList<Product> products;
  private ArrayList<String> updatedProducts;
  private HashTableMap<String, Product> hashMap;
  private double profit;

  public Backend(Reader reader) {
    tree = new RedBlackTree<Product>();
    updatedProducts = new ArrayList<String>();
    hashMap = new HashTableMap<String, Product>();
    profit = 0;
    ProductDataReader productReader = new ProductDataReader();
    try {
      products = productReader.readDataSet(reader);
      for (Product p : products) {
        tree.insert(p);
        hashMap.put(p.getName().toLowerCase(), p);
        profit += p.getQuantitySold() * p.getRetailPrice();
        profit -= p.getQuantityAvailable() * p.getCost();
      }
    } catch (IOException | DataFormatException e) {
      e.printStackTrace();
    }
  }

  public Backend(String[] args) {
    FileReader fr;
    tree = new RedBlackTree<Product>();
    updatedProducts = new ArrayList<String>();
    hashMap = new HashTableMap<String, Product>();
    profit = 0;
    ProductDataReader productReader = new ProductDataReader();
    try {
      fr = new FileReader(new File(args[0]));
      products = productReader.readDataSet(fr);
      for (Product p : products) {
        tree.insert(p);
        hashMap.put(p.getName().toLowerCase(), p);
        profit += p.getQuantitySold() * p.getRetailPrice();
        profit -= p.getQuantityAvailable() * p.getCost();
      }
    } catch (IOException | DataFormatException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean restock(Product product, int amount)
      throws NoSuchElementException, IllegalArgumentException {
    if (amount <= 0)
      throw new IllegalArgumentException("Restock amount must be positive");
    if (tree.contains(product)) {
      product.setQuantityAvailable(product.getQuantityAvailable() + amount);
      double restockCost = product.getCost() * amount;
      updatedProducts.add("Restocked " + amount + " amount of " + product.getName()
          + ". Effect on profit: - $" + String.format("%.2f", restockCost));
      profit -= restockCost;
      return true;
    }
    return false;
  }

  @Override
  public boolean destock(Product product, int amount, boolean isSold)
      throws NoSuchElementException, IllegalArgumentException {
    if (amount <= 0)
      throw new IllegalArgumentException("Destock amount must be positive");
    if (tree.contains(product)) {
      product.setQuantityAvailable(product.getQuantityAvailable() - amount);;
      double destockCost = product.getRetailPrice() * amount;
      if (isSold) {
        updatedProducts.add("Sold " + amount + " amount of " + product.getName()
            + ". Effect on profit: + $" + String.format("%.2f", destockCost));
        profit += destockCost;
        product.setQuantitySold(product.getQuantitySold() + amount);
      } else
        updatedProducts
            .add("Lost " + amount + " amount of " + product.getName() + ". Effect on profit: none");
      // product was lost, does not contribute positively to profit calculations

      return true;
    }
    return false;
  }

  @Override
  public boolean addNewProduct(Product product) {
    boolean toReturn = hashMap.put(product.getName().toLowerCase(), product);
    if (toReturn) {
      products.add(product);
      tree.insert(product);
      double addCost = product.getCost() * product.getQuantityAvailable();
      profit -= product.getCost() * product.getQuantityAvailable();
      updatedProducts.add("Added " + product.getQuantityAvailable() + " " + product.getName()
          + " to the store's inventory. Effect on profit: - $" + String.format("%.2f", addCost));
    }
    return toReturn;
  }

  @Override
  public Product getProduct(String productName) {
    Product product = hashMap.get(productName.trim().toLowerCase());
    return product;
  }

  @Override
  public List<String> getUpdatedProducts() {
    return updatedProducts;
  }

  @Override
  public List<Product> getAllProducts() {
    return products;
  }

  @Override
  public double getProfit() {
    return profit;
  }

  @Override
  public ArrayList<Product> needsRestock(int amount) {
    ArrayList<Product> needRestock = new ArrayList<Product>();
    for (Product p : tree) {
      if (p.getQuantityAvailable() <= amount)
        needRestock.add(p);
    }
    return needRestock;
  }

  public boolean printToCSV() {
    try {
      final String CSV_FILE_NAME = "inventory.csv";
      FileWriter myWriter = new FileWriter(CSV_FILE_NAME);
      for (Product p : this.getAllProducts()) {
        myWriter.write(p.getID() + "," + p.getName() + "," + p.getQuantitySold() + ","
            + p.getQuantityAvailable() + "," + String.format("%.2f", p.getCost()) + ","
            + String.format("%.2f", p.getRetailPrice()) + "\n");
      }
      myWriter.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
