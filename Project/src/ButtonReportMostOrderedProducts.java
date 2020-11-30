import java.awt.*;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ButtonReportMostOrderedProducts extends ButtonReport {

    private final Algorithms algorithm;

    public ButtonReportMostOrderedProducts(JPanel outputArea, JPanel graphArea, Algorithms algorithm, 
                                        String name, DateFields dateRange) {
        super(outputArea, graphArea, name);
        this.algorithm = algorithm;
        this.dateRange = dateRange;
    }

    /* 
     * Calculates each products number of sales on a timeline of dateRange.
     */
    @Override
    protected void calculateResult()  {

        ResultSet dbResults = algorithm.queryMostOrderedProducts();

        KType convertedResults = null;
        if(dbResults != null){
            convertedResults = Algorithms.convertResultSetKType(dbResults);
        }

        KType filteredByDate = Algorithms.filterByDate(dateRange, convertedResults);

        KType productSold = algorithm.getMostOrderedProducts(filteredByDate);
        result = KType.sortKTypeBy(0,10,productSold);
        isBarGraph = true;
    }

    /*
     * Formats a line of x, y results for display. 
     */
    @Override
    protected JPanel formatLineToPanel(String x, String y) {
  
        JPanel line = new JPanel(new GridLayout(1, 2));

        JLabel firstResult = new JLabel(new DecimalFormat("#0").format(Double.parseDouble(x)), 
                                        JLabel.CENTER);
        firstResult.setFont(textResultFont);
        firstResult.setForeground(Gui.BG_COLOR);
        line.add(firstResult);

        JLabel secondResult = new JLabel(y, JLabel.CENTER);
        secondResult.setFont(textResultFont);
        line.add(secondResult);

        return line;
    }
}
