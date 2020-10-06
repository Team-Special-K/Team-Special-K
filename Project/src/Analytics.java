import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.mysql.cj.result.SqlDateValueFactory;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import org.graalvm.compiler.phases.common.AddressLoweringPhase.AddressLowering;

import java.io.*;


public class Analytics {


    //test
    public Analytics() throws SQLSyntaxErrorException, SQLException {


        // Initially set table name to null, this will change when the other methods are called
        String tableName = null;

        
        // Call our instance method to get database
        Db db = Db.getInstance();

        OrderQuery orders = new OrderQuery();

        //  ResultSet a = db.getAllRows(orders, false);

        double test = calcAssets(db);

        bestCustomers(db);


    }

    public static void main(String[] args)throws SQLException, SQLSyntaxErrorException {
        
        Analytics data = new Analytics();


    }

    // methods that pulls information from the tables and stores it in different arrays for me to use. 

    public static ResultSet getProducts(Db db) {
        String tableName = "Products";

        // this statement returns the entire table to us as a result set
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");

    }

    
    public static ResultSet getOrders(Db db) {
        String tableName = "Orders";


       return db.sendSqlStatement("SELECT email, product_id, quantity FROM " + tableName + ";");

    }

    



    // method that builds gui for computer to use. This should display all of the analytics information.

    





 

    // these methods will take in whatever data we receive and calculate the meaningful statistics about our database
    // each method will be named after what it calculates and our gui will call all of them
    public static double calcAssets(Db db) throws SQLSyntaxErrorException, SQLException {
        
        // Establish connection with the products table using the ProductQuery
        ProductQuery pq = new ProductQuery();

        // Set the correct table
        db.sendSqlStatement(pq.dbUse());

        // Call method that constructs needed information
        ResultSet assetArray = getProducts(db);
        
        double sumAssets = 0;

        ResultSetMetaData metadata = assetArray.getMetaData();
        int columncount = metadata.getColumnCount();
        
        // As long as the result set isnt empty we check each row and column
        // in this case column 1 is the quantity and column 2 is the wholesale cost
        while(assetArray.next()) {
            for (int i = 1; i < columncount; i++) {
                
                double currentQuantity = Double.parseDouble(assetArray.getString(i));

                double currentCost = Double.parseDouble(assetArray.getString(i + 1));

                double currentAsset = currentQuantity * currentCost;

                sumAssets += currentAsset;

            }
        }
        
        System.out.println(sumAssets);

        return sumAssets;

    }

    // this method will create a list of our best customers
    public static void bestCustomers(Db db) throws SQLException, SQLSyntaxErrorException {

        OrderQuery orders = new OrderQuery();

        db.sendSqlStatement(orders.dbUse());

        ResultSet orderTable = getOrders(db);

        ProductQuery item = new ProductQuery();

        db.sendSqlStatement(item.dbUse());

        LinkedList ordersSorted = new LinkedList(orderTable, db);

    }




}