// --== CS400 File Header Information ==--
// Name: Yash Butani
// Email: @wisc.edu
// Team: Blue
// Group: KE
// TA: Keren
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

/**
 * 
 * Product class which represents the type that all objects in the tree will be
 */
public class Product implements ProductInterface, Comparable<Product> {
  private static int IDCounter = 1;
  private int ID;
  private String name;
  private int quantity_sold;
  private int quantity_available;
  private Double cost;
  private Double retail_price;

  // please make sure you add in comments
  public Product(String name, int qs, int qa, double c, double rp) {
    this.ID = IDCounter;
    IDCounter++;
    this.name = name;
    this.quantity_sold = qs;
    this.quantity_available = qa;
    this.cost = c;
    this.retail_price = rp;
  }

  @Override
  public double getRetailPrice() {
    return retail_price;
  }

  @Override
  public double getCost() {
    return this.cost;
  }

  @Override
  public int getQuantityAvailable() {
    return this.quantity_available;
  }

  @Override
  public int getQuantitySold() {
    return this.quantity_sold;
  }

  @Override
  public void setRetailPrice(double rp) {
    retail_price = rp;
  }

  @Override
  public void setCost(double cost) {
    this.cost = cost;
  }

  @Override
  public void setQuantityAvailable(int qa) {
    quantity_available = qa;
  }

  @Override
  public void setQuantitySold(int qs) {
    quantity_sold = qs;
  }


  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getID() {
    return this.ID;
  }

  public void resetID() {
    ID = 0;
  }

  @Override
  public String toString() {
    return "ID: " + ID + "\tName: " + name + "\tQuantity Available: "
        + quantity_available + "\tQuantity Sold: " + quantity_sold + "\tCost: "
        + String.format("%.2f", cost) + "\tRetail Price: "
        + String.format("%.2f", retail_price);
  }

  public String toStringCondensed() {
    return name + "\tQuantity Available: " + quantity_available + "\tCost: "
        + String.format("%.2f", cost) + "\tRetail Price: "
        + String.format("%.2f", retail_price);
  }

  @Override
  public int compareTo(Product otherProduct) {
    if (this.ID > otherProduct.ID)
      return 1;
    else if (this.ID < otherProduct.ID)
      return -1;
    return 0;
  }


}
