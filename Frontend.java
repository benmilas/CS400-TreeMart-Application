// --== CS400 File Header Information ==--
// Name: Ben Milas
// Email: bmilas@wisc.edu
// Team: KE red
// Role: Front End Developer
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This class runs the front end of the TreeMart Inventory application.
 * 
 * @author Ben Milas
 */
public class Frontend {

  public enum Mode {
    main, update, print, add, decrease, increase, file, lookup, summary, exit
  } // Globally get and set the mode

  private static Scanner scnr;
  private Mode mode; // controls what is prompted / printed to the user
  private String input;
  private Backend backend;
  private final String CSV_FILE_NAME = "inventory.csv"; // file that is read / written to

  /**
   * Calls the run() method and launches the application
   * 
   * @param args input parameters,
   */
  public static void main(String[] args) {
    args = new String[] {"inventory.csv"};
    Backend backend = new Backend(args);
    Frontend frontend = new Frontend();
    frontend.run(backend);
  }

  /**
   * Runs the TreeMart inventory application by displaying a message, taking input, parsing that
   * input, and performing actions as necessary.
   * 
   * @param back the backend of the application
   */
  public void run(Backend back) {
    mode = Mode.main;
    scnr = new Scanner(System.in);
    backend = back;
    while (mode != Mode.exit) {
      clearScreen();
      displayMessage();
      input = scnr.nextLine();
      parseInput(input.toLowerCase());
    }
    // closes app & clear screen
    System.out.println("Closing app...");
    loadingTimer();
    clearScreen();
  }

  /**
   * Instantiates the scanner and backend for JUnit tests that struggle with input alternating from
   * scnr.nextLine() and scnr.next() (see FrontEndDeveloperTests header for more details)
   */
  public void testingMode() {
    scnr = new Scanner(System.in);
    String[] args = new String[] {CSV_FILE_NAME};
    Backend backend = new Backend(args);
    this.backend = backend;
  }

  /**
   * Clears and flushes the screen from the Linux command line.
   */
  public void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  /**
   * Displays the header and appropriate prompt and/or message based on which mode the application
   * is in. Also displays the current profit at the bottom of the main menu.
   */
  public void displayMessage() {
    printHeader();
    switch (mode) {
      case main:
        System.out.println("Welcome to TreeMart™ - Your one-stop shop for all your grocery needs!\n"
            + "This application allows you to update and print information about the \ninventory"
            + " that TreeMart™ sells, which is stored in a red-black tree.\n");
        System.out.println("Enter a character to change modes\n");
        System.out.println("u: Update Inventory Mode\np: Print Mode\nx: Exit\n");
        System.out
            .println("Total TreeMart™ Profit: $" + String.format("%.2f", backend.getProfit()));
        break;
      case update:
        System.out.println("Update Inventory Menu\n");
        System.out.println("Enter a character to perform an action\n");
        System.out.println(
            "a: Add New Product\nd: Decrease Product's Stock\ni: Increase Product's Stock\n"
                + "x: Return to Main Menu\n");
        break;
      case print:
        System.out.println("Print Menu\n");
        System.out.println("Enter a character to perform an action\n");
        System.out.println("f: Print Inventory to .csv File\nl: Lookup Product Information\n"
            + "s: Print Summary of Changes\nx: Return to Main Menu\n");
        break;
      case add:
        addMode();
        break;
      case decrease:
        decreaseMode();
        break;
      case increase:
        increaseMode();
        break;
      case file:
        fileMode();
        break;
      case lookup:
        lookupMode();
        break;
      case summary:
        summaryMode();
        break;
      case exit:
        break;
      default:
        break;
    }

    return;
  }

  /**
   * Parses the input and delegates that input to various mode methods based on the current mode
   * 
   * @param input the String being parsed
   */
  public void parseInput(String input) {

    switch (mode) {
      case main:
        mainMode(input);
        break;
      case update:
        updateMode(input);
        break;
      case print:
        printMode(input);
        break;
      case exit:
        break;
      default:
        break;
    }
  }

  /**
   * Changes mode based on a parsed String that is being checked against default mode-changing
   * characters. The application will close if the parsed input's first character is 'x'
   * 
   * @param input the user's input that is being checked and parsed.
   */
  public void mainMode(String input) {
    if (input.equals(""))
      return;

    if (input.charAt(0) == 'x') {
      mode = Mode.exit;
      return;
    }
    if (input.charAt(0) == 'u') {
      mode = Mode.update;
      return;
    }
    if (input.charAt(0) == 'p') {
      mode = Mode.print;
      return;
    }

    return;
  }

  /**
   * Changes the mode to various submodes within the update menu based on a parsed String that is
   * being checked against default mode-changing characters. The application will return to the main
   * menu if the parsed input's first character is 'x'
   * 
   * @param input the user's input that is being checked and parsed.
   */
  public void updateMode(String input) {
    if (input.equals(""))
      return;

    if (input.charAt(0) == 'x') {
      mode = Mode.main;
      return;
    }

    if (input.charAt(0) == 'a') {
      mode = Mode.add;
      return;
    }

    if (input.charAt(0) == 'd') {
      mode = Mode.decrease;
      return;
    }

    if (input.charAt(0) == 'i') {
      mode = Mode.increase;
      return;
    }
  }

  /**
   * Changes the mode to various submodes within the print menu based on a parsed String that is
   * being checked against default mode-changing characters. The application will return to the main
   * menu if the parsed input's first character is 'x'
   * 
   * @param input the user's input that is being checked and parsed.
   */
  public void printMode(String input) {
    if (input.equals(""))
      return;

    if (input.charAt(0) == 'x') {
      mode = Mode.main;
      return;
    }

    if (input.charAt(0) == 'f') {
      mode = Mode.file;
      return;
    }

    if (input.charAt(0) == 'l') {
      mode = Mode.lookup;
      return;
    }

    if (input.charAt(0) == 's') {
      mode = Mode.summary;
      return;
    }
  }

  /**
   * Prompts the user to add a new product to TreeMart's inventory. The product will fail to be
   * added if the user's input doesn't match the required format, or if a product with that name
   * already exists in the RB-tree. Entering 'n' will return to the update menu screen.
   */
  public void addMode() {
    System.out.println("Add New Product Menu\n");
    System.out.println(
        "\nWould you like to add a new product to TreeMart™'s inventory?\n(Type 'y' or 'n')");
    char response = scnr.next().charAt(0);
    while (response != 'y' && response != 'n') {
      System.out.println("Invalid response. (type 'y' or 'n')");
      response = scnr.next().charAt(0);
    }
    if (response == 'y') {
      do {
        try {
          clearScreen();
          printHeader();
          // parses input and only allows valid quantities and costs
          System.out.println("What is the name of the product you want to add?");
          scnr.nextLine(); // to allow JUnit tests to have input streams read in input
          String productName = scnr.nextLine().trim();
          System.out.println("How much quantity are you adding to the inventory?");
          int quantityAvailable = Integer.parseInt(scnr.next());
          if (quantityAvailable < 0)
            throw new IllegalArgumentException();
          System.out.println("How much does it cost per product for the store to stock?");
          double cost = Double.parseDouble(scnr.next());
          if (cost <= 0)
            throw new IllegalArgumentException();
          System.out.println("What is the retail price per product?");
          double retailPrice = Double.parseDouble(scnr.next());
          if (retailPrice <= 0)
            throw new IllegalArgumentException();
          Product newProduct = new Product(productName, 0, quantityAvailable, cost, retailPrice);
          clearScreen();
          printHeader();
          // adds the product if it doesn't already exist in the RB-tree
          if (backend.addNewProduct(newProduct))
            System.out.println("Product added successfully!\n");
          else
            System.out.println("Failed to add product, as it is already sold at TreeMart™.\n");
          // prompt entry of another new product
          System.out.println("\nWould you like to add another product?\n(Type 'y' or 'n')");
          response = scnr.next().charAt(0);
          while (response != 'y' && response != 'n') {
            System.out.println("Invalid response. (type 'y' or 'n')");
            response = scnr.next().charAt(0);
          }
        } catch (IllegalArgumentException iae) {
          System.out.println("Invalid input. Please try again.\n");
          loadingTimer();
          return;
        }
      } while (response != 'n');
    }
    mode = Mode.update;
    return;
  }

  /**
   * Prompts the user to decrease the quantity available of product in TreeMart's inventory. The
   * quantity available will fail to be decreased if the user's input isn't within the bounds of
   * that product's current stock. Entering 'n' will return to the update menu screen.
   */
  public void decreaseMode() {
    System.out.println("Decrease Product Stock Menu\n");
    System.out.println("\nWould you like to lower the stock of a product?\n(Type 'y' or 'n')");
    char response = scnr.next().charAt(0);
    while (response != 'y' && response != 'n') {
      System.out.println("Invalid response. (type 'y' or 'n')");
      response = scnr.next().charAt(0);
    }
    if (response == 'y')
      do {
        try {
          clearScreen();
          printHeader();
          System.out.println("What is the name of the product you want to lower the stock of?");
          scnr.nextLine(); // to allow JUnit tests to have input streams read in input
          String productName = scnr.nextLine();
          Product product = backend.getProduct(productName);
          System.out.println(
              "How many quantities are you lowering it?\n(You can lower the quantity available of "
                  + product.getName() + " by a maximum of " + product.getQuantityAvailable() + ")");
          int amount = Integer.parseInt(scnr.next());
          // amount must be between 0 and the current quantity available
          if (amount > product.getQuantityAvailable() || amount < 0)
            throw new IllegalArgumentException();
          System.out.println(
              "Did you sell these items (as opposed to them being spoiled, spilled, or lost)?"
                  + "\n(Type 'y' or 'n')");
          boolean isSold = false;
          response = scnr.next().charAt(0);
          while (response != 'y' && response != 'n') {
            System.out.println("Invalid response. (type 'y' or 'n')");
            response = scnr.next().charAt(0);
          }
          if (response == 'y')
            isSold = true;
          // if isSold is true, the removal of this product will be positive profit
          // if isSold is false, the removal of this product will not affect the profit
          if (backend.destock(product, amount, isSold))
            System.out.println(product.getName() + " successfully had its stock decreased!");
        } catch (NoSuchElementException nsee) {
          System.out.println("That product is not sold at TreeMart™. Please try again.");
          loadingTimer();
        } catch (IllegalArgumentException iae) {
          System.out.println("Invalid input. Please try again.");
          loadingTimer();
        } finally {
          System.out.println(
              "\nWould you like to lower the stock of another product?\n(Type 'y' or 'n')");
          if (scnr.hasNext()) {
            response = scnr.next().charAt(0);
            while (response != 'y' && response != 'n') {
              System.out.println("Invalid response. (type 'y' or 'n')");
              response = scnr.next().charAt(0);
            }
          }
        }
      } while (response != 'n');
    if (response == 'n')
      mode = Mode.update;
  }

  /**
   * Prompts the user to view products that are low on stock and/or increase the quantity available
   * of product in TreeMart's inventory. If you want to view products that are low on stock, the
   * screen will print out up to 5 products that are at or below the quantity level you input. The
   * quantity available will fail to be increased if the user's input isn't a non-negative integer.
   * Entering 'n' will return to the update menu screen.
   */
  public void increaseMode() {
    System.out.println("Increase Product Stock Menu\n");
    System.out
        .println("\nWould you like to check which products are low on stock?\n(Type 'y' or 'n')");
    char response = scnr.next().charAt(0);
    while (response != 'y' && response != 'n') {
      System.out.println("Invalid response. (type 'y' or 'n')");
      response = scnr.next().charAt(0);
    }
    if (response == 'y') {
      try {
        System.out.println("What is the lowest quantity available that would like to see?");
        int amount = Integer.parseInt(scnr.next());
        if (amount < 0)
          throw new IllegalArgumentException();
        System.out
            .println("Here are up to 5 products at or below " + amount + " quantity available:\n");
        ArrayList<Product> needRestock = backend.needsRestock(amount);
        for (int i = 0; i < Math.min(5, needRestock.size()); i++) {
          System.out.println(needRestock.get(i).toStringCondensed());
        }
      } catch (IllegalArgumentException iae) {
        System.out.println("Invalid input. Please try again.\n");
        loadingTimer();
        mode = Mode.add;
        return;
      }
    }
    System.out.println("\nWould you like to restock a product?\n(Type 'y' or 'n')");
    response = scnr.next().charAt(0);
    while (response != 'y' && response != 'n') {
      System.out.println("Invalid response. (type 'y' or 'n')");
      response = scnr.next().charAt(0);
    }
    if (response == 'y')
      do {
        try {
          clearScreen();
          printHeader();
          System.out.println("What is the name of the product you want to restock?");
          scnr.nextLine(); // to allow JUnit tests to have input streams read in input
          String productName = scnr.nextLine();
          Product product = backend.getProduct(productName);
          System.out.println("How many quantities are you restocking?");
          int amount = Integer.parseInt(scnr.next());
          if (backend.restock(product, amount))
            System.out.println(product.getName() + " successfully restocked!");
        } catch (NoSuchElementException nsee) {
          System.out.println("That product is not sold at TreeMart™. Please try again.");
        } catch (NumberFormatException nfe) {
          System.out.println("Invalid input. Please try again.");
        } finally {
          System.out.println("\nWould you like to restock another product?\n(Type 'y' or 'n')");
          response = scnr.next().charAt(0);
          while (response != 'y' && response != 'n') {
            System.out.println("Invalid response. (type 'y' or 'n')");
            response = scnr.next().charAt(0);
          }
        }
      } while (response != 'n');
    if (response == 'n')
      mode = Mode.update;
  }

  /**
   * Prompts the user to update the .csv file which contains the information regarding the
   * inventory. In the process, the backend is updated to reflect this new .csv file, and the
   * summary of changes is cleared. Entering 'n' or simply updating the file will return the user to
   * the print menu.
   */
  public void fileMode() {
    System.out.println("Print Inventory to .csv File\n");
    System.out.println(
        "\nWould you like to update the .csv file to reflect your new inventory?\n(Type 'y' or 'n')");
    char response = scnr.next().charAt(0);
    while (response != 'y' && response != 'n') {
      System.out.println("Invalid response. (type 'y' or 'n')");
      response = scnr.next().charAt(0);
    }
    if (response == 'y') {
      // printing to csv and updating the backend to reflect those changes
      backend.printToCSV();
      String[] args = new String[] {CSV_FILE_NAME};
      backend = new Backend(args);
      System.out.println("File successfully updated. Returning to Print Menu...");
      loadingTimer();
    }
    mode = Mode.print;
    return;
  }

  /**
   * Prompts the user to lookup a product in the TreeMart inventory based on its name. Entering 'n'
   * after looking up a product will return the user to the print menu.
   */
  public void lookupMode() {
    System.out.println("Lookup Product Information\n");
    System.out.println("What is the name of the product you want to look up?");
    try {
      String productName = scnr.nextLine();
      Product product = backend.getProduct(productName);
      System.out.println(product.toString());
    } catch (NoSuchElementException e) {
      System.out.println("That product is not sold at TreeMart™. Please try again.");
      loadingTimer();
    } finally {
      System.out.println(
          "\nWould you like to look up information about another product?\n(Type 'y' or 'n')");
      char response = scnr.next().charAt(0);
      while (response != 'y' && response != 'n') {
        System.out.println("Invalid response. (type 'y' or 'n')");
        response = scnr.next().charAt(0);
      }
      if (response == 'n') {
        mode = Mode.print;
        return;
      }
    }
  }

  /**
   * Prints to the user a summary of the changes that have been made to the inventory since the .csv
   * file was last updated. Entering 'x' returns the user to the print menu.
   */
  public void summaryMode() {
    System.out.println("Print Inventory Summary\n");
    System.out.println("Enter 'x' to return to the Print Menu.\n");
    // default message if nothing is changed
    if (backend.getUpdatedProducts().size() == 0)
      System.out.println("No changes have been made since the inventory was last updated.\n\n"
          + "Total TreeMart™ Profit: $" + String.format("%.2f", backend.getProfit()));
    else {
      System.out.println("Summary of Changes:\n");
      for (String s : backend.getUpdatedProducts())
        System.out.println(s);
      System.out
          .println("\nTotal TreeMart™ Profit: $" + String.format("%.2f", backend.getProfit()));
    }
    char response = scnr.next().charAt(0);
    if (response == 'x') {
      mode = Mode.print;
      return;
    }
  }

  /**
   * Private method that delays the screen from updating for 2 seconds to simulate loading and give
   * the user enough time to read the screen
   */
  private void loadingTimer() {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

  /**
   * Private method that prints the header of the user interface
   */
  private void printHeader() {
    System.out.println("    ******* ****** ***** ***** *      *    -     ****** *******");
    System.out.println("       *    *    * *     *     * *  * *   / \\    *    *    *");
    System.out.println("       *    *****  ***** ***** *  **  *  /   \\   *****     *");
    System.out.println("       *    *   *  *     *     *      * /_____\\  *   *     *");
    System.out.println("       *    *    * ***** ***** *      *   |_|    *    *    *");
    System.out.println("---------------------------------------------------------------------\n");
  }
}
