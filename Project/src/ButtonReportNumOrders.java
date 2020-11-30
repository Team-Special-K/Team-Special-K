import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;


public class ButtonReportNumOrders extends ButtonReport {

    private final Algorithms algorithm;

    public ButtonReportNumOrders(JPanel outputArea, JPanel graphArea, Algorithms algorithm, String name, DateFields dateRange) {
        super(outputArea, graphArea, name);
        this.algorithm = algorithm;
        this.dateRange = dateRange;
    }

    /* 
     * Calculates number or orders
     */
    @Override
    protected void calculateResult()  {

        ResultSet dbResults = algorithm.queryNumOrders();

        KType convertedResults = null;
        if(dbResults != null){
            convertedResults = Algorithms.convertResultSetKType(dbResults, false);
        }
        KType filteredByDate = Algorithms.filterByDate(dateRange, convertedResults);

        KType dailyNumOrders = algorithm.getDailyNumOrders(filteredByDate);
        result = KType.sortKTypeBy(0,10,dailyNumOrders);
    }

    /*
     * Formats a line of x, y results for display. 
     */
    @Override
    protected JPanel formatLineToPanel(String x, String y) {
        LocalDateTime epoch = Instant.ofEpochMilli(Long.parseLong(y) * 1000)
                                    .atOffset(ZoneOffset.UTC).toLocalDateTime();
        String date = epoch.toString().substring(0, MAX_RESULTS);

        JPanel line = new JPanel(new GridLayout(1, 2));

        JLabel firstResult = new JLabel(new DecimalFormat("#0").format(Double.parseDouble(x)), JLabel.CENTER);
        firstResult.setFont(textResultFont);
        firstResult.setForeground(Gui.BG_COLOR);
        line.add(firstResult);

        JLabel secondResult = new JLabel(date, JLabel.CENTER);
        secondResult.setFont(textResultFont);
        line.add(secondResult);

        return line;
    }
}
