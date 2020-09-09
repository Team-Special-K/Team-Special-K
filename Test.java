
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Test {


   /*
   *  In the Main method we will be calling the various classes and methods we have already built 
   *  to be sure that they work properly
   */
   public static void main(String[] args) throws SQLException, FileNotFoundException {
	  
      Db database = new Db();
      ProductQuery query = new ProductQuery("TestSku");
      
      database.sendSqlStatement("USE "+ QueryBuilder.DBNAME + ";");
     
      //create db if it doesnt exist 
      ResultSet result = database.sendSqlStatement(query.dbExist());//sears.sendSqlStatement(q.dbExist());
      if(!result.next()){database.sendSqlStatement(query.createDb());}
      
      
      result = database.sendSqlStatement(query.tableExist());//sears.sendSqlStatement(q.dbExist());
      if(!result.next()){database.sendSqlStatement(query.createTable());}
	
      ResultSet testResult = databaseTest(query, database);
      
      // call method to add all lines from doc into database
      //addEntries("inventory_team5.csv", database);
      
      // call method to check a random line in the database
      checkEntry("OHD8WMCL4QV2", database);
      insertEntry("1", "2", "3", "4", "5", database);
   }
   
   
   /*
   *  This method takes in a database and checks if it exists
   *  if it doesn't it exist, the method creates it
   */  
   public static ResultSet databaseTest(ProductQuery query, Db db) throws SQLException {
      
      ResultSet result = db.sendSqlStatement(query.dbExist());
           
      if(!result.next()){db.sendSqlStatement(query.createDb());}
      
      return result;
   }



   /*
   *  This method pulls the information from a text file and places it into a product query file
   *  which is then added to the database
   *  @param Db test takes in a database to add the information to
   *  @param fileName is the name of the document you are using to add information
   */

   public static void addEntries(String fileName, Db test) throws FileNotFoundException {
      
      // read in excel file
      
      File inputDataFile = new File(fileName);
      Scanner inputFile = new Scanner(inputDataFile);
      
      // generate a product query to be used to add each thing to the table 
      ProductQuery pq;
      
      // gets rid of header row from table
      inputFile.nextLine();
      
      // as long as the file isnt empty keep looping through the input file
      while(inputFile.hasNext()) {
    	 
         String line = inputFile.nextLine();
         
         // split the string at each delimiter and return an array of the lines entries
         
         String[] data = line.split("[,]", 0);
         
         String productID = data[0];
         int quantity = Integer.parseInt(data[1]);
         float wholesale = Float.parseFloat(data[2]);
         float salePrice = Float.parseFloat(data[3]);
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
   
   public static void checkEntry(String productID, Db db) throws SQLException {
      
      ProductQuery item = new ProductQuery(productID);
      System.out.println(item.getRow());
      ResultSet a = db.sendSqlStatement(item.getRow());

      ResultSetMetaData metadata = a.getMetaData();
      int columnCount = metadata.getColumnCount();
            
      while(a.next()) {
         String row = "";
         List<String> array = new ArrayList<>();
         for(int i = 1; i <= columnCount; i++) {
            row += a.getString(i) + ", ";
            array.add(a.getString(i));
         }
         System.out.println(row); //This is the row as a string -> "field1, field2, field3, field4"
         System.out.println(array); //This is the row as an array -> [field1, field2, field3, field4]
      }
   }

   public static void insertEntry(String quanitiy, String productID, String wholesale, String salePrice, String supplierID, Db db) throws SQLException {
      ResultSet result = db.sendSqlStatement("INSERT INTO Products (`quantity`, `product_id`, `wholesale_cost`, `sale_price`, `supplier_id`) VALUES ('"+quanitiy+"', '"+productID+"', '"+wholesale+"', '"+salePrice+"', '"+supplierID+"')");
      
      if(result == null) {
         System.out.println("Entry was inserted.");
      }
   }













}