import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.*;


public class Analytics extends QueryBuilder {


    //test
    public Analytics() {


        // Initially set table name to null, this will change when the other methods are called
        tableName = null;

        // Call our instance method to get database
        Db db = Db.getInstance();





    }


    // methods that pulls information from the tables and stores it in different arrays for me to use. 

    public void getProducts(Db db) {
        tableName = "Products";

        // this statement returns the entire table to us as a result set
        ResultSet a = db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");

        ResultSetMetaData metadata = a.getMetaData();
        int columnCount = metadata.getColumnCount();
        
        // this should parse the information we get back from the above sql statement and allow us to put it into an array

        // with this array we should be able to do the neccesary operations

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

    
    public void getOrders(Db db) {
        tableName = "Orders";

        // this statement returns the entire table to us as a result set
        ResultSet a = db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");

        ResultSetMetaData metadata = a.getMetaData();
        int columnCount = metadata.getColumnCount();
        
        // this should parse the information we get back from the above sql statement and allow us to put it into an array

        // with this array we should be able to do the neccesary operations

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

    



    // method that builds gui for computer to use. This should display all of the analytics information.






    // Below here I will put any methods I use to calculate meaningful statistics. 





}