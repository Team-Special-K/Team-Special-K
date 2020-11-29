import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.protocol.Resultset;

public class EventSimulator {
    
    static Db db = Db.getInstance();
    static String tableName = "products";

    public static void main(String[] args) throws SQLException {

        // Call the thing here to see if it works
        JFrame frame = new JFrame("Demo"); 

        frame.add(getPanel());
        frame.setBounds(400, 400, 450, 450);
        frame.setVisible(true);
        frame.repaint();


    }

    public static JPanel getPanel() throws SQLException {

        db.sendSqlStatement("USE firstdb;");
        // create jpanel

        JPanel demoTest = new JPanel();
        JTextField productIDField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JButton updateBuyerButton = new JButton("Buyer");
        JButton updateSupplierButton = new JButton("Supplier");
        JButton getNewQuantityButton = new JButton("Update");
        JTextArea currentOutput = new JTextArea(10, 20);


        demoTest.setLayout(new BoxLayout(demoTest, BoxLayout.Y_AXIS));

        formatButton(updateBuyerButton);
        formatButton(updateSupplierButton);
        formatButton(getNewQuantityButton);

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

        demoTest.add(productIDField);
        demoTest.add(getNewQuantityButton);
        demoTest.add(quantityField);
        demoTest.add(updateBuyerButton);
        demoTest.add(updateSupplierButton);
        demoTest.add(currentOutput);

        return demoTest;

    }

    public static void formatButton(JButton button) {
        button.setFont(new Font("Helvetica", Font.PLAIN, 17));
        button.setPreferredSize(new Dimension(100, 50));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(new Color(220, 239, 252));
        button.setBorder(BorderFactory.createLineBorder(new Color(3, 140, 247), 0, true));
        button.setFocusable(false);
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
