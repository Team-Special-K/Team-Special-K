import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonReportAssets extends ButtonReport {

    Algorithms algorithm;

    public ButtonReportAssets(JPanel outputArea, Algorithms algorithm, String name) {
        super(outputArea, name);
        this.algorithm = algorithm;
    }

    /*
     * Calculates and sorts by users total spent
     */
    @Override
    public void calculateResult(){

        Db db = Db.getInstance();

        String joinAssetQuery = "SELECT orders.cust_email,orders.product_quantity,"
                              + "products.wholesale_cost FROM orders "
                              + "JOIN products ON orders.product_id = products.product_id;";

        ResultSet allOrders = db.sendSqlStatement(joinAssetQuery);

        ArrayList<Tup<Double,String>> results = null;
        try {
            results = algorithm.getUsersSpent(allOrders);
        } catch (SQLException e) { 
            System.exit(1);
        }
        result = algorithm.getSortedTuples(results, MAX_RESULTS);
    }

    /*
     * Displays results in this JPanel outputArea
     */
    @Override
    void displayResult() {
        outputArea.removeAll();
        for(var item : result.subList(0, MAX_RESULTS)){
            JLabel line = new JLabel();
            line.setFont(new Font("Helvetica", Font.PLAIN, 13));
            line.setText(item.y + "\s\s$" + String.format("%.2f", item.x));
            outputArea.add(line);
        }
        outputArea.revalidate();   
    }
}
