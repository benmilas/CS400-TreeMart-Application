// --== CS400 File Header Information ==--
// Name: Yash Butani
// Email: @wisc.edu
// Team: Blue
// Group: KE
// TA: Keren
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

/**
 * Product interface from the Product class will implement
 * 
 * @author Ben Milas
 */
public interface ProductInterface {
  public double getRetailPrice();

  public double getCost();

  public int getQuantityAvailable();

  public int getQuantitySold();
  
  public void setRetailPrice(double rp);

  public void setCost(double cost);

  public void setQuantityAvailable(int qa);

  public void setQuantitySold(int qs);

  public String getName();

  public int getID();
}
