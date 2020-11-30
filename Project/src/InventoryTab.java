import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class InventoryTab {

    public static JPanel getPanel() throws SQLException {

        Db db = Db.getInstance();

        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setBorder(new TitledBorder(new EtchedBorder(), "Display Area"));

        // create the middle panel components

        JTextArea display = new JTextArea(16, 58);

        // get all of the items from inventory
        ResultSet a = db.sendSqlStatement("SELECT * FROM Products;");

        java.sql.ResultSetMetaData metadata = a.getMetaData();
        int columncount = metadata.getColumnCount();

        String header = "Row, Quantity,  Product ID,         Sell Price,   Wholesale Cost \n";

        display.append(header);

        while (a.next()) {
            String line = "";
            for (int i = 1; i < columncount; i++) {
                line += a.getString(i) + "        ";
            }
            line += "\n";
            display.append(line);
        }


        display.setEditable(false); // set textArea non-editable




        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Add Textarea in to middle panel
        inventoryPanel.add(scroll);

        return inventoryPanel;
    }

    




}
