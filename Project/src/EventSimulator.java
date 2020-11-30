import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.protocol.Resultset;

public class EventSimulator {
    
    static Db db = Db.getInstance();
    static String tableName = "products";

    public static JPanel getPanel() {

        db.sendSqlStatement("USE firstdb;");
        // create jpanel

        JPanel demoTest = new JPanel(new GridLayout(10, 1, 3, 0));
        JTextField productIDField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JButton updateBuyerButton = new JButton("Buyer");
        JButton updateSupplierButton = new JButton("Supplier");
        JButton getNewQuantityButton = new JButton("Update");
        JTextArea currentOutput = new JTextArea(10, 20);
        JPanel buttonLine = new JPanel();
        JPanel finalPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        JPanel empty = new JPanel();
        JLabel productLabel = new JLabel("Input Product ID");
        JLabel quantityLabel = new JLabel("Input Quantity");
        JLabel currentLabel = new JLabel("Current Quantity");

        empty.setPreferredSize(new Dimension(500,600));
        currentOutput.setEditable(false);

        demoTest.setBorder(BorderFactory.createEmptyBorder(0,6,0,6));

        buttonLine.setLayout(new GridLayout(1, 2, 3, 0));


        //demoTest.setLayout(new BoxLayout(demoTest, BoxLayout.Y_AXIS));


        ButtonReport.applyStyle(updateBuyerButton);
        ButtonReport.applyStyle(updateSupplierButton);
        ButtonReport.applyStyle(getNewQuantityButton);

        // make button that calls get quantity

        /*
         * This action listener checks the product id the user entered and the quantity
         * change field to simulate a buyer event for this amount
         */

         getNewQuantityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productID = productIDField.getText();
                System.out.println(productID);
                 try {
                     currentOutput.setText(getQuantity(productID));
                 } catch (SQLException e1) {
                     e1.printStackTrace();
                 }
            }

        });


        updateBuyerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productID = productIDField.getText();
                String amount = quantityField.getText();
                buyerEvent(productID, amount);
            }

        });

        /*
         * This action listener checks the product id the user entered and the quantity
         * change field to simulate a buyer event for this amount
         */

         
        updateSupplierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productID = productIDField.getText();
                String amount = quantityField.getText();
                supplierEvent(productID, amount);
            }

        });
        demoTest.add(productLabel);
        demoTest.add(productIDField);
        demoTest.add(getNewQuantityButton);
        demoTest.add(quantityLabel);
        demoTest.add(quantityField);
        buttonLine.add(updateBuyerButton);
        buttonLine.add(updateSupplierButton);
        demoTest.add(buttonLine);
        demoTest.add(currentLabel);
        demoTest.add(currentOutput);
        finalPanel.add(demoTest);
        finalPanel.add(empty);

        return finalPanel;

    }

    // method that submits the string for a buyer event
    public static void buyerEvent(String productID, String amount) {

        ProductQuery pq = new ProductQuery(productID);
        db.sendSqlStatement(pq.buyerEvent(amount));

    }

    // method that submits the string for a supplier event
    public static void supplierEvent(String productID, String amount) {

        ProductQuery pq = new ProductQuery(productID);
        db.sendSqlStatement(pq.supplierEvent(amount));
    ;

    }

    // gets quantity from database and returns it
    public static String getQuantity(String productID) throws SQLException {

        ResultSet a;
        a = db.sendSqlStatement("SELECT quantity FROM products WHERE product_id = '"+ productID +"';");
        //ResultSetMetaData = 
        String quantity = "";
        while(a.next()) {
        quantity = a.getString(1);
        }
        System.out.println(quantity);
        return quantity;

    }
    


}
