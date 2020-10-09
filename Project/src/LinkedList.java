import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.mysql.cj.result.SqlDateValueFactory;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;


import java.io.*;

public class LinkedList{

    node head = null;
    node current = head; 
    // node a, b; 
    static class node { 
        double amountPaid;
        String email; 
        node next; 
  
        public node(String email, double amountPaid) 
        { 
            this.email = email;
            this.amountPaid = amountPaid; 
        } 
    } 

    public LinkedList() {
        head = null;        
    }
    
    public LinkedList(ResultSet orders, Db db) throws SQLException, SQLSyntaxErrorException {

        ResultSetMetaData metadata = orders.getMetaData();
        int columncount = metadata.getColumnCount();

        System.out.println(columncount);

        while(orders.next()) {

                // get sale cost of product using product id
                ResultSet itemCost = db.sendSqlStatement("SELECT sale_price FROM Products WHERE product_id = '" + orders.getString(2) + "';");

                // calculate order total based on quantity and on cost
                double orderTotal = 0;
                while(itemCost.next()) {
                orderTotal = Double.parseDouble(orders.getString(3)) * Double.parseDouble(itemCost.getString(1)); 
                orderTotal = Math.round(orderTotal * 100.0) / 100.0;
                }
                // add to list using existing method
                System.out.println(orders.getString(1));
                push(orders.getString(1), orderTotal);
            
        }
        printList(head);

    }


    node sortedMerge(node a, node b) 
    { 
        node result = null; 
        /* Base cases */
        if (a == null) 
            return b; 
        if (b == null) 
            return a; 
  
        /* Pick either a or b, and recur */
        if (a.amountPaid <= b.amountPaid) { 
            result = a; 
            result.next = sortedMerge(a.next, b); 
        } 
        else { 
            result = b; 
            result.next = sortedMerge(a, b.next); 
        } 
        return result; 
    } 
  
    node mergeSort(node h) 
    { 
        // Base case : if head is null 
        if (h == null || h.next == null) { 
            return h; 
        } 
  
        // get the middle of the list 
        node middle = getMiddle(h); 
        node nextofmiddle = middle.next; 
  
        // set the next of middle node to null 
        middle.next = null; 
  
        // Apply mergeSort on left list 
        node left = mergeSort(h); 
  
        // Apply mergeSort on right list 
        node right = mergeSort(nextofmiddle); 
  
        // Merge the left and right lists 
        node sortedlist = sortedMerge(left, right); 
        return sortedlist; 
    } 
  
    // Utility function to get the middle of the linked list 
    public static node getMiddle(node head) 
    { 
        if (head == null) 
            return head; 
  
        node slow = head, fast = head; 
  
        while (fast.next != null && fast.next.next != null) { 
            slow = slow.next; 
            fast = fast.next.next; 
        } 
        return slow; 
    } 
  
    void push(String email, double new_data) 
    { 
        /* allocate node */
        current = head;
        while(current != null) {
            if (current.email.equals(email)) {
                current.amountPaid += new_data;
                return;
            }
            current = current.next;
        }

        node new_node = new node(email, new_data); 
  
        /* link the old list off the new node */
        new_node.next = head; 
  
        /* move the head to point to the new node */
        head = new_node; 
    } 
  
    // Utility function to print the linked list 
    void printList(node headref) 
    { 
        while (headref != null) { 
            System.out.print(headref.amountPaid + " "); 
            headref = headref.next; 
        } 
        
    } 
    


}
