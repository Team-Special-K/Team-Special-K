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

        // Call our instance method to get database
        Db db = Db.getInstance();

        double test = calcAssets(db);

        System.out.println("Total number of assets: " + test);

        String output = orderStats(db, "10/5/2020");

        System.out.println(output);


    }

    public static void main(String[] args)throws SQLException, SQLSyntaxErrorException {
        Analytics data = new Analytics();
    }

    // methods that pulls information from the tables and stores it in different arrays for me to use. 

    public static ResultSet getProducts(Db db) throws SQLException, SQLSyntaxErrorException {
        String tableName = "Products";
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");
    }

    
    public static ResultSet getOrders(Db db) throws SQLException, SQLSyntaxErrorException {
        String tableName = "Orders";
        return db.sendSqlStatement("SELECT email, product_id, quantity FROM " + tableName + ";");
    }

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

    // this method calculates how many orders we have for a given day and the dollar amount of these orders
    // @param date, takes in a string for a given date and calculates stats for that day
    public static String orderStats(Db db, String date) throws SQLException, SQLSyntaxErrorException {

        OrderQuery orders = new OrderQuery();
        ProductQuery item = new ProductQuery();
        int numOrders = 0;
        double totalCost = 0;
        double orderTotal = 0;

        db.sendSqlStatement(orders.dbUse());

        ResultSet orderTable = db.sendSqlStatement("SELECT email, product_id, quantity FROM orders WHERE date = '"+ date+ "';");

        db.sendSqlStatement(item.dbUse());

        while(orderTable.next()) {
            numOrders++;
            ResultSet itemCost = db.sendSqlStatement("SELECT sale_price FROM Products WHERE product_id = '" + orderTable.getString(2) + "';");

            while(itemCost.next()) {
                orderTotal = Double.parseDouble(orderTable.getString(3)) * Double.parseDouble(itemCost.getString(1));
                orderTotal = Math.round(orderTotal * 100.0) / 100.0; 
            }

            totalCost += orderTotal;
            
        }

        return "For date: " + date + " Number of orders: "  + numOrders + " Total cost of orders: " + totalCost; 

    }





}