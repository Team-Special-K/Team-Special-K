


import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class Test {


   /*
   *  In the Main method we will be calling the various classes and methods we have already built 
   *  to be sure that they work properly
   */
   
   public static void main(String[] args) throws SQLException {
   
      Db test = new Db();
   
      ProductQuery pqTest = new ProductQuery("TestSku");
   
      ResultSet testResult = dataBaseTest(test, pqTest);
      
      // call method to add all lines from doc into database
      
      addEntries("inventory_team5.csv", test);
      
      
      // call method to check a random line in the database
      checkEntry("OHD8WMCL4QV2", test);
   
   }
   
   
   
   
   
   /*
   *  This method takes in a database and checks if it exists
   *  if it doesn't it exist, the method creates it
   */
     
   public static ResultSet dataBaseTest(Db test, ProductQuery pqTest) {
   
      
      ResultSet result = test.sendSqlStatement(pqTest.dbExist());
      
      
      if(!result.next()){test.sendSqlStatement(pqTest.createDb());}
      
      return result;
      
   
   }



   /*
   *  This method pulls the information from a text file and places it into a product query file
   *  which is then added to the database
   *  @param Db test takes in a database to add the information to
   *  @param fileName is the name of the document you are using to add information
   */

   public static void addEntries(String fileName, Db test) {
      
      // read in excel file
      
      File inputDataFile = new File(fileName);
      Scanner inputFile = new Scanner(inputDataFile);
      
      // generate a product query to be used to add each thing to the table 
      ProductQuery pq;
      
      // gets rid of header row from table
      String useless = inputFile.nextLine();
      
      // as long as the file isnt empty keep looping through the input file
      while(inputFile.hasNext()) {
      
         String line = inputFile.nextLine();
         
         // split the string at each delimiter and return an array of the lines entries
         
         String[] data = line.split("[,]", 0);
         
         String productID = data[0];
         String quantity = data[1];
         String wholesale = data[2];
         String salePrice = data[3];
         String supplier = data[4];
         
         // generate new productQuery with items in hashmap already
         pq = new ProductQuery(productID, quantity, wholesale, salePrice, supplier);
         
         // adds a new row to the table for each new item 
         test.sendSqlStatement(pq.addRow());         
         
            
      } 
   
   }
   
   /*
   *  This method checks the database for a given productID and then 
   *  returns the information associated with it
   *  @param productID takes in a string to check the database for
   */
   
   public static void checkEntry(String productID, Db test) {
      
      ProductQuery item = new ProductQuery(productID);
      
      ResultSet a = test.sendSqlStatement(item.getRow());
      
      while(a.next()){System.out.println(a.getRow());}
      
   
   }













}
