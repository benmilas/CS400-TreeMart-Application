// --== CS400 File Header Information ==--
// Name: Ben Milas
// Email: bmilas@wisc.edu
// Team: KE red
// Role: Front End Developer
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: Test 1 was broken down to just test exiting from the main menu screen, as
// originally it was too similar to Test 2 to distinguish between the two. Now, Test 2 focuses more
// on closing and changing the print and update inventory menus. Tests 3-5 struggled to compile when
// running them directly from the Frontend.run() method (I'm assuming it's because of the load
// timer and the mix between scnr.nextLine() for product names and scnr.next() for everything else.
// Therefore, I added a "Testing Mode" method in the front end that directly instantiates the
// scanners, back end, and inventory.csv file so I could directly enter methods without using the
// run() method, and this allowed me to test that these methods work as intended.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * 
 * A class that runs the JUnit 5 tests for the frontend of the TreeMart application.
 * 
 * @author Ben Milas
 */
public class FrontEndDeveloperTests {
  // rewrite this to "inventory.csv" before every test to make sure it doesn't accidentally get
  // updated, which would mess up tests that add products
  private final String OLD_INVENTORY = "1,Pepper,1,6,9.90,10.99\r\n"
      + "2,Zucchini,1,3,5.94,8.99\r\n" + "3,Cod,3,9,7.92,12.99\r\n" + "4,Sprouts,4,3,7.92,8.99\r\n"
      + "5,Flour,2,8,4.95,6.99\r\n" + "6,Oranges,1,2,6.93,10.99\r\n" + "7,Octopus,5,4,4.95,7.99\r\n"
      + "8,Rootbeer,9,11,8.91,10.99\r\n" + "9,Water,4,12,4.95,7.99\r\n"
      + "10,Hummus,2,5,8.91,10.99\r\n" + "11,Lamb,3,1,9.90,13.99\r\n"
      + "12,Soup,10,7,8.91,12.99\r\n" + "13,Cake,12,4,6.93,7.99\r\n"
      + "14,Onions,11,7,4.95,9.99\r\n" + "15,Wine,4,11,5.94,8.99\r\n" + "16,Vodka,4,1,4.95,5.99\r\n"
      + "17,Tarragon,7,1,7.92,9.99\r\n" + "18,Lobster,9,4,8.91,13.99\r\n"
      + "19,Lemonade,6,1,4.95,6.99\r\n" + "20,Cheez It,10,2,6.93,9.99\r\n"
      + "21,Sherry,6,5,5.94,7.99\r\n" + "22,Sparkling Wine,1,2,8.91,12.99\r\n"
      + "23,Cilantro,10,3,5.94,10.99\r\n" + "24,Spinach,6,8,4.95,6.99\r\n"
      + "25,Potatoes,10,1,4.95,8.99\r\n" + "26,Beets,9,5,7.92,8.99\r\n"
      + "27,Tea,12,4,8.91,12.99\r\n" + "28,Pork,4,1,4.95,5.99\r\n"
      + "29,Filo Dough,8,4,8.91,11.99\r\n" + "30,Cookie Dough,2,2,8.91,10.99\r\n"
      + "31,White Baguette,8,8,6.93,9.99\r\n" + "32,Bag,9,5,6.93,10.99\r\n"
      + "33,Cookies,1,2,8.91,12.99\r\n" + "34,Sauerkraut,6,3,9.90,11.99\r\n"
      + "35,Snapple Raspberry Tea,5,10,8.91,11.99\r\n" + "36,Pastry,4,12,7.92,10.99\r\n"
      + "37,Salmon,7,10,8.91,10.99\r\n" + "38,Capon,1,4,7.92,11.99\r\n"
      + "39,Yogurt,3,1,8.91,12.99\r\n" + "40,Sesame Seeds,7,8,5.94,8.99\r\n"
      + "41,Allspice,5,11,6.93,10.99\r\n" + "42,Lemon Pepper,11,10,5.94,7.99\r\n"
      + "43,Juice,11,3,4.95,6.99\r\n" + "44,Muffin,11,4,8.91,11.99\r\n"
      + "45,Chilli Paste,12,5,6.93,8.99\r\n" + "46,Maple Syrup,4,3,4.95,8.99\r\n"
      + "47,Milk,9,8,6.93,9.99\r\n" + "48,Goat,1,9,7.92,11.99\r\n"
      + "49,Bay Leaf,3,6,9.90,11.99\r\n" + "50,Chocolate Liqueur,4,4,7.92,12.99\r\n"
      + "51,Mushroom,9,10,6.93,9.99\r\n" + "52,Apricots,4,9,4.95,8.99\r\n"
      + "53,Garlic,9,9,4.95,5.99\r\n" + "54,Shortbread,12,2,7.92,10.99\r\n"
      + "55,Shrimp,10,4,8.91,11.99\r\n" + "56,Ice Cream Bar,8,3,5.94,8.99\r\n"
      + "57,Chips,11,6,8.91,10.99\r\n" + "58,Salt And Pepper,9,11,6.93,11.99\r\n"
      + "59,Syrup,7,12,7.92,11.99\r\n" + "60,Bread,1,6,5.94,7.99\r\n"
      + "61,Lettuce,5,6,9.90,12.99\r\n" + "62,Bonito Flakes,8,4,6.93,10.99\r\n"
      + "63,Lentils,2,12,7.92,11.99\r\n" + "64,Peas,3,3,8.91,10.99\r\n"
      + "65,Horseradish,6,2,5.94,6.99\r\n" + "66,Crab,2,4,8.91,13.99\r\n"
      + "67,Gatorade,5,6,8.91,11.99\r\n" + "68,Jam,12,10,7.92,9.99\r\n"
      + "69,Quail,4,6,9.90,14.99\r\n" + "70,Wheat,3,2,4.95,5.99\r\n"
      + "71,Scallops,6,2,7.92,11.99\r\n" + "72,Tomatoes,2,11,6.93,8.99\r\n"
      + "73,Olives,3,2,7.92,8.99\r\n" + "74,Bouillion,9,2,5.94,10.99\r\n"
      + "75,Rosemary,10,3,9.90,13.99\r\n" + "76,Thyme,8,10,9.90,14.99\r\n"
      + "77,Honey,5,10,5.94,10.99\r\n" + "78,Asparagus,10,8,8.91,11.99\r\n"
      + "79,Sugar,9,5,8.91,11.99\r\n" + "80,Sausage,5,4,6.93,10.99\r\n"
      + "81,Venison,1,10,8.91,13.99\r\n" + "82,Salami,11,3,6.93,10.99\r\n"
      + "83,Raspberries,12,4,5.94,9.99\r\n" + "84,Carrots,12,8,5.94,10.99\r\n"
      + "85,Arizona Tea,2,5,5.94,6.99\r\n" + "86,Crackers,10,7,5.94,8.99\r\n"
      + "87,Cream,3,9,8.91,12.99\r\n" + "88,Chutney Sauce,1,9,9.90,11.99\r\n"
      + "89,Gingerale,5,12,7.92,11.99\r\n" + "90,Dried Figs,8,6,4.95,8.99\r\n"
      + "91,Beans,1,12,9.90,12.99\r\n" + "92,Guava,9,9,7.92,9.99\r\n"
      + "93,Curry Powder,1,8,7.92,10.99\r\n" + "94,Rice,2,9,6.93,9.99\r\n"
      + "95,Bacardi Limon,9,8,5.94,6.99\r\n" + "96,Country Roll,1,8,5.94,6.99\r\n"
      + "97,Coke,11,6,4.95,5.99\r\n" + "98,Black Currants,5,2,8.91,10.99\r\n"
      + "99,Mustard,7,6,6.93,10.99\r\n" + "100,Eggroll,9,12,6.93,7.99\r\n"
      + "101,Onion Powder,3,2,8.91,9.99\r\n" + "102,Turkey,3,9,7.92,11.99\r\n"
      + "103,Breadfruit,5,3,6.93,8.99\r\n" + "104,Tomato Paste,1,9,4.95,5.99\r\n"
      + "105,Vinegar,2,9,7.92,8.99\r\n" + "106,Daikon Radish,10,3,5.94,8.99\r\n"
      + "107,Squid,5,7,7.92,11.99\r\n" + "108,Samosa,4,10,5.94,8.99\r\n"
      + "109,Bacardi Raspberry,1,4,8.91,12.99\r\n" + "110,Iced Tea,9,11,6.93,10.99\r\n"
      + "111,Trout,3,10,4.95,7.99\r\n" + "112,Banana,8,10,9.90,14.99\r\n"
      + "113,Halibut,1,10,9.90,10.99\r\n" + "114,Ham,4,8,5.94,7.99\r\n"
      + "115,Blackberries,12,6,7.92,9.99\r\n" + "116,Canada Dry,10,6,7.92,12.99\r\n"
      + "117,Cumin,7,4,7.92,11.99\r\n" + "118,Yams,12,2,4.95,9.99\r\n"
      + "119,Poppy Seed,7,2,9.90,11.99\r\n" + "120,Chocolate Bar,5,6,8.91,12.99\r\n"
      + "121,Fruit Mix,9,7,8.91,11.99\r\n" + "122,Eggplant,3,2,8.91,10.99\r\n"
      + "123,Parsley,4,1,8.91,11.99\r\n" + "124,Mangoes,5,8,9.90,11.99\r\n"
      + "125,Vanilla Beans,12,2,8.91,9.99\r\n" + "126,Jack Daniels,1,7,5.94,9.99\r\n"
      + "127,Cocoa Powder,12,8,8.91,9.99\r\n" + "128,Kumquat,2,4,6.93,9.99\r\n"
      + "129,Smoked Paprika,12,9,9.90,13.99\r\n" + "130,Vol Au Vents,12,11,4.95,7.99\r\n"
      + "131,Ice Cream,11,4,9.90,13.99\r\n" + "132,Clams,4,10,5.94,8.99\r\n"
      + "133,Fireball Whisky,11,8,4.95,6.99\r\n" + "134,Chicken Breast,11,5,9.90,14.99\r\n"
      + "135,Duck,8,4,7.92,11.99\r\n" + "136,Bols Melon Liqueur,4,2,9.90,12.99\r\n"
      + "137,Coconut,11,2,6.93,9.99\r\n" + "138,Assorted Desserts,7,5,8.91,11.99\r\n"
      + "139,Tuna,9,7,6.93,10.99\r\n" + "140,Chocolate,8,5,5.94,9.99\r\n"
      + "141,Swiss Chard,8,5,6.93,10.99\r\n" + "142,Nut,8,12,9.90,14.99\r\n"
      + "143,Kahlua,6,9,7.92,9.99\r\n" + "144,Mayonnaise,1,10,9.90,10.99\r\n"
      + "145,Water Chestnut,7,10,8.91,10.99\r\n" + "146,Sunflower Seeds,9,3,4.95,7.99\r\n"
      + "147,Melon,12,3,5.94,8.99\r\n" + "148,Tequila,12,5,4.95,9.99\r\n"
      + "149,Cloves,5,7,9.90,14.99\r\n" + "150,Rabbit,9,8,5.94,6.99\r\n"
      + "151,Radish,1,7,9.90,13.99\r\n" + "152,Kellogs Special K Cereal,6,4,6.93,11.99\r\n"
      + "153,Strawberries,7,2,5.94,9.99\r\n" + "154,Rum,1,1,6.93,11.99\r\n"
      + "155,Sage,7,5,4.95,8.99\r\n" + "156,Squash,3,8,4.95,9.99\r\n"
      + "157,Dragon Fruit,2,12,7.92,11.99\r\n" + "158,Dr. Pepper,2,4,5.94,6.99\r\n"
      + "159,Bagel,6,8,9.90,12.99\r\n" + "160,Chervil,8,7,8.91,9.99\r\n"
      + "161,Tortillas,4,6,7.92,9.99\r\n" + "162,Kohlrabi,10,5,6.93,10.99\r\n"
      + "163,Hot Chocolate,8,6,5.94,6.99\r\n" + "164,Calypso,2,1,8.91,12.99\r\n"
      + "165,Beef,11,10,6.93,9.99\r\n" + "166,Cream Of Tartar,12,12,6.93,7.99\r\n"
      + "167,Spoon,3,10,4.95,5.99\r\n" + "168,Corn,5,4,5.94,8.99\r\n"
      + "169,Yukon Jack,7,7,6.93,11.99\r\n" + "170,Puff Pastry,11,10,5.94,9.99\r\n"
      + "171,Leeks,11,7,4.95,8.99\r\n" + "172,Cherries,9,4,4.95,5.99\r\n"
      + "173,Relish,9,9,7.92,12.99\r\n" + "174,Chives,2,5,6.93,8.99\r\n"
      + "175,Oil,1,12,7.92,12.99\r\n" + "176,Pasta,5,2,4.95,5.99\r\n"
      + "177,Sea Bass,9,7,6.93,9.99\r\n" + "178,Truffle,6,12,8.91,9.99\r\n"
      + "179,Pears,8,5,4.95,5.99\r\n" + "180,Tobasco Sauce,8,10,9.90,12.99\r\n"
      + "181,Breakfast Quesadillas,5,12,5.94,9.99\r\n" + "182,Puree,8,3,9.90,12.99\r\n"
      + "183,Veal,12,3,6.93,8.99\r\n" + "184,Bandage,9,6,9.90,13.99\r\n"
      + "185,Cheese,1,11,8.91,13.99\r\n" + "186,Nacho Chips,6,11,6.93,11.99\r\n"
      + "187,Whole Chicken,5,9,7.92,11.99\r\n" + "188,Pineapple,4,10,5.94,7.99\r\n"
      + "189,Beer,10,8,4.95,7.99\r\n" + "190,Chicken Thigh,6,11,7.92,12.99\r\n"
      + "191,Ginger,7,3,6.93,11.99\r\n" + "192,Spice,5,9,5.94,6.99\r\n"
      + "193,Brandy,2,10,9.90,14.99\r\n" + "194,Sauce,10,12,7.92,8.99\r\n"
      + "195,Tofu,10,11,6.93,10.99\r\n" + "196,Cabbage,4,6,7.92,11.99\r\n"
      + "197,Coffee,10,9,4.95,8.99\r\n" + "198,Basil,9,7,9.90,14.99\r\n"
      + "199,Okra,3,3,6.93,11.99\r\n" + "200,Flounder,6,9,5.94,9.99\r\n";

  /**
   * Resets the System Out's and System In's to the defaults before each test, and rewrites the
   * inventory.csv file back into its default state.
   */
  @BeforeEach
  public void setup() {
    System.setOut(System.out);
    System.setIn(System.in);
    try {
      File f = new File("inventory.csv");
      FileWriter fr = new FileWriter(f);
      fr.write(OLD_INVENTORY);
      fr.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test 1 will ensure that the exit function from the main menu works as intended. By entering 'x'
   * from the main menu, the program is exited.
   */
  @Test
  public void testEnterXToExit() {
    // test exiting the whole system by entering 'x'
    InputStream inputStreamSimulator = new ByteArrayInputStream("x".getBytes());
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    // set the output to the stream captor to read the output of the front end
    System.setOut(new PrintStream(outputStreamCaptor));
    // set the input stream to our input (with an x to test of the program exists)
    System.setIn(inputStreamSimulator);
    String[] args = new String[] {"inventory.csv"};
    Backend backend = new Backend(args);
    Frontend frontend = new Frontend();
    frontend.run(backend);
    String appOutput = outputStreamCaptor.toString();
    // check that the program closes
    assertTrue(appOutput.contains("Closing app..."));
  }

  /**
   * Test 2 will ensure that the User Interface can switch between modes based on the user's input.
   * After returning to the main screen from either the update inventory menu or print menu screens,
   * the user should be able to continue changing products from both menus without the program
   * terminating. By entering 'x' on the update inventory menu or print menu screens, the user
   * should be returned to the main menu screen. It will also ensure that the program can be exited
   * through the main menu.
   */
  @Test
  public void testSwitchModes() {
    // test switching between modes works as intended
    String input = "u" + System.lineSeparator() + "x" + System.lineSeparator() + "p"
        + System.lineSeparator() + "x" + System.lineSeparator() + "x";
    InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStreamSimulator);
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    // set the output to the stream captor to read the output of the front end
    System.setOut(new PrintStream(outputStreamCaptor));
    // set the input stream to our input (with an x to test of the program exists)
    System.setIn(inputStreamSimulator);
    Frontend.main(null);
    String appOutput = outputStreamCaptor.toString();
    // check that the program 1) closes and 2) visits both the print and update inventory menus
    assertTrue(appOutput.contains("Closing app...") && appOutput.contains("Print Menu")
        && appOutput.contains("Update Inventory Menu"));
  }

  /**
   * Test 3 will ensure that the functionality of writing back to the .csv file works as intended.
   * By entering 'f' from the print menu screen, the entire inventory will be printed in
   * ascending-ID order through in-order traversal, and that data will be transferred back into the
   * .csv file so the inventory is updated within the project.
   */
  @Test
  public void testWritetoCSV() {
    // set the input stream to insert a new product (Chimichanga) into the RB-tree and see if the
    // inventory.csv file was updated to contain the information regarding that product
    String input = "y" + System.lineSeparator() + "Chimichanga" + System.lineSeparator() + "10"
        + System.lineSeparator() + "8.04" + System.lineSeparator() + "10.99"
        + System.lineSeparator() + "n" + System.lineSeparator() + "y";
    InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStreamSimulator);
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    // set the output to the stream captor to read the output of the front end
    System.setOut(new PrintStream(outputStreamCaptor));
    Frontend frontend = new Frontend();
    frontend.testingMode();
    frontend.addMode();
    frontend.fileMode();
    try {
      File f = new File("inventory.csv");
      Scanner scnr = new Scanner(f);
      boolean containsChimichanga = false;
      // scan the file, if the product information regarding Chimichanga is there, flip to true
      while (scnr.hasNextLine()) {
        if (scnr.nextLine().contains("Chimichanga,0,10,8.04,10.99"))
          containsChimichanga = true;
      }
      assertTrue(f.exists());
      assertTrue(containsChimichanga);
      scnr.close();
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  /**
   * Test 4 will ensure that the summary function under the Print Menu works as intended. By
   * entering 's' from the print menu screen, it will print to the console a summary of the changes
   * that occurred to the inventory since it was loaded in on startup, including changes to stock
   * and newly added items, as well as overall profit (or loss ☹ ) from those changes.
   */
  @Test
  public void testPrintSummary() {
    // set the input stream to insert a new product (Pizza) into the RB-tree and see if the
    // printed summary contains information about whether the item was added or not
    String input = "y" + System.lineSeparator() + "Pizza" + System.lineSeparator() + "10"
        + System.lineSeparator() + "8.04" + System.lineSeparator() + "10.99"
        + System.lineSeparator() + "n" + System.lineSeparator() + "x";
    InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStreamSimulator);
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    // set the output to the stream captor to read the output of the front end
    System.setOut(new PrintStream(outputStreamCaptor));
    Frontend frontend = new Frontend();
    frontend.testingMode();
    frontend.addMode();
    frontend.summaryMode();
    String appOutput = outputStreamCaptor.toString();
    assertTrue(appOutput.contains("Added 10 Pizza to the store's inventory"));
  }

  /**
   * Test 5 will ensure that the print by name-lookup works as intended. By entering 'p' from the
   * print menu screen, the user will be asked to enter a specific name of a product that TreeMart
   * sells. If the ID exists in the RB-tree, the description of that item will be printed to the
   * console.
   */
  @Test
  public void testPrintByLookup() {
    // set the input stream to lookup information about the product "Pork"
    String input = "Pork" + System.lineSeparator() + "n" + System.lineSeparator() + "x";
    InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStreamSimulator);
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    // set the output to the stream captor to read the output of the front end
    System.setOut(new PrintStream(outputStreamCaptor));
    Frontend frontend = new Frontend();
    frontend.testingMode();
    frontend.lookupMode();
    String appOutput = outputStreamCaptor.toString();
    assertTrue(appOutput.contains("Name: Pork\tQuantity Available: 1\tQuantity Sold: 4\tCost: 4.95"
        + "\tRetail Price: 5.99"));
  }
}
