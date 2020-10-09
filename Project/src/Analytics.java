import java.sql.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import com.mysql.cj.result.SqlDateValueFactory;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import org.graalvm.compiler.phases.common.AddressLoweringPhase.AddressLowering;

import java.io.*;

public class Analytics {


    static DecimalFormat dollars = new DecimalFormat("$#,##0.00");
    
    // test
    public Analytics() throws SQLSyntaxErrorException, SQLException, FileNotFoundException {

        // Call our instance method to get database
        Db db = Db.getInstance();

        //OrderQuery orders = new OrderQuery();
        db.sendSqlStatement("USE " + QueryBuilder.DBNAME + ";");
        //db.sendSqlStatement(orders.createTable());
        //db.loadCsv("customer_orders_A_team5.csv", orders);

        String joinAssetQuery = "select orders.cust_email,orders.product_quantity, products.wholesale_cost from orders join products on orders.product_id = products.product_id;";
        ResultSet ordersTable = db.sendSqlStatement(joinAssetQuery);

        ArrayList<Tup<Double, String>> sumTotal = totalSpent(ordersTable);
        sumTotal = sortTuple(sumTotal, 10);

        GUI(db, sumTotal);

    }

    public static void main(String[] args) throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        Analytics data = new Analytics();
    }

    // methods that pulls information from the tables and stores it in different
    // arrays for me to use.

    public static ResultSet getProducts(Db db) throws SQLException, SQLSyntaxErrorException {
        String tableName = "Products";
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");
    }

    public static ResultSet getOrders(Db db) throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        String tableName = "Orders";
        return db.sendSqlStatement("SELECT cust_email, product_id, product_quantity FROM " + tableName + ";");
    }

    // these methods will take in whatever data we receive and calculate the
    // meaningful statistics about our database
    // each method will be named after what it calculates and our gui will call all
    // of them
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
        while (assetArray.next()) {
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
    public static void bestCustomers(Db db) throws SQLException, SQLSyntaxErrorException, FileNotFoundException {

        OrderQuery orders = new OrderQuery();

        db.sendSqlStatement(orders.dbUse());

        ResultSet orderTable = getOrders(db);

        ProductQuery item = new ProductQuery();

        db.sendSqlStatement(item.dbUse());

        // LinkedList ordersSorted = new LinkedList(orderTable, db);

    }

    // this method calculates how many orders we have for a given day and the dollar
    // amount of these orders
    // @param date, takes in a string for a given date and calculates stats for that
    // day
    public static String orderStats(Db db, String date) throws SQLException, SQLSyntaxErrorException {

        OrderQuery orders = new OrderQuery();
        ProductQuery item = new ProductQuery();
        int numOrders = 0;
        double totalCost = 0;
        double orderTotal = 0;

        db.sendSqlStatement(orders.dbUse());

        ResultSet orderTable = db.sendSqlStatement(
                "SELECT cust_email, product_id, product_quantity FROM orders WHERE date = '" + date + "';");

        db.sendSqlStatement(item.dbUse());

        while (orderTable.next()) {
            numOrders++;
            ResultSet itemCost = db.sendSqlStatement(
                    "SELECT sale_price FROM Products WHERE product_id = '" + orderTable.getString(2) + "';");

            while (itemCost.next()) {
                orderTotal = Double.parseDouble(orderTable.getString(3)) * Double.parseDouble(itemCost.getString(1));
                orderTotal = Math.round(orderTotal * 100.0) / 100.0;
            }

            totalCost += orderTotal;

        }

        return "For date: " + date + " Number of orders: " + numOrders + " Total cost of orders: " + totalCost;

    }

    public static void GUI(Db db, ArrayList<Tup<Double, String>> sumTotal ) throws SQLException, SQLSyntaxErrorException {
        JFrame frame;
        JPanel north;
        JTextField text;
        JButton button;
        JPanel south;
        JTextArea out;
        JTextArea text_best;
        JTextArea assets;

        frame = new JFrame("Analytics");
        frame.setBounds(400, 400, 450, 450);


        Box titleTextData = Box.createHorizontalBox();
        JLabel titleData = new JLabel("Please enter a date");
        titleTextData.add(titleData);

        // make jpanel with text box, text area, and search button in it
        north = new JPanel();
        south = new JPanel();
        text = new JTextField(50);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        north.add(titleTextData);
        north.add(text);
        button = new JButton("Get Orders");
        button.setPreferredSize(new Dimension(100, 50));
        out = new JTextArea(5, 20);
        north.add(button);
        north.add(out);
        frame.add(north, BorderLayout.NORTH);
        frame.add(south, BorderLayout.SOUTH);
        frame.setVisible(true);
        out.setEditable(false);

        Box titleTextAssets = Box.createHorizontalBox();
        JLabel titleAssets = new JLabel("Assets");
        titleTextAssets.add(titleAssets);

        Box titleText = Box.createHorizontalBox();
        JLabel title = new JLabel("Top 10 Best Customers");
        titleText.add(title);

        assets = new JTextArea(1, 20);
        text_best = new JTextArea(10,20);
        south.add(titleTextAssets);
        south.add(assets);
        south.add(titleText);
        south.add(text_best);

        String total_assets = dollars.format(calcAssets(db)) + "";
        

        assets.append(total_assets);

        for(int i = 0; i < 10; i++){
            String email = sumTotal.get(i).y;
            double totalSpent = sumTotal.get(i).x;
            text_best.append((i + 1) + ". "+ email + " Amount spent: " + totalSpent + "\n");
        }

        frame.repaint();

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.setText(null);
                String str = text.getText();

                String data = "";
                try {
                    data = orderStats(db, str);
                } catch (SQLSyntaxErrorException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            

            out.append(data + "\n");
            frame.repaint();
         }
      });



    }
 
   



}