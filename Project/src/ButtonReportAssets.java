import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ButtonReportAssets extends ButtonReport {

    private final Algorithms algorithm;

    public ButtonReportAssets(JPanel outputArea, Algorithms algorithm, String name, 
                                JTextField dateStart, JTextField dateEnd) {
        super(outputArea, name);    
        this.algorithm = algorithm;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    /*
     * Sorts users by total spent within date range
     */
    @Override
    protected void calculateResult(){

        Db db = Db.getInstance();
        String joinAssetQuery = "SELECT orders.cust_email,orders.product_quantity,"
                              + "products.wholesale_cost, orders.date FROM orders "
                              + "JOIN products ON orders.product_id = products.product_id;";
        ResultSet allOrders = db.sendSqlStatement(joinAssetQuery);

        ArrayList<String[]> filteredOrders = null;
        String EOL = "T00:00:00.000000000";

        LocalDateTime dateStartNow = LocalDateTime.parse(dateStart.getText() + EOL);
        LocalDateTime dateEndNow = LocalDateTime.parse(dateEnd.getText() + EOL);

        try{
            filteredOrders = Algorithms.filterByDate(dateStartNow, dateEndNow, allOrders);
        } catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        assert(filteredOrders != null);

        ArrayList<Tup<Double,String, LocalDateTime>> results = algorithm.getUsersSpent(filteredOrders);

        int size = results.size() < MAX_RESULTS ? results.size() : MAX_RESULTS;
        result = algorithm.getSortedTuples(results, size);
    }

    /*
     * Displays results in this JPanel outputArea
     */
    @Override
    protected void displayResult() {
        outputArea.removeAll();
        int size = result.size() < MAX_RESULTS ? result.size() : MAX_RESULTS;
        for(var item : result.subList(0, size)){
            JLabel line = new JLabel();
            line.setFont(new Font("Helvetica", Font.PLAIN, 13));
            line.setText(item.y + "\s\s$" + String.format("%.2f", item.x));
            outputArea.add(line);
        }
        outputArea.revalidate();   
    }
}
