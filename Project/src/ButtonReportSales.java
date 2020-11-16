import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.swing.JPanel;


public class ButtonReportSales extends ButtonReport {

    private final Algorithms algorithm;

    public ButtonReportSales(JPanel outputArea, JPanel graphArea, Algorithms algorithm, String name, DateFields dateRange) {
        super(outputArea, graphArea, name);
        this.algorithm = algorithm;
        this.dateRange = dateRange;
    }

    /* 
     * Calculates sales total timeline of dateRange.
     */
    @Override
    protected void calculateResult()  {

        ResultSet dbResults = algorithm.queryDailySales();

        KType convertedResults = null;
        if(dbResults != null){
            convertedResults = Algorithms.convertResultSetKType(dbResults);
        }

        KType filteredByDate = Algorithms.filterByDate(dateRange, convertedResults);

        KType dailyDailySales = algorithm.getDailySales(filteredByDate);
        result = KType.sortKTypeBy(0,10,dailyDailySales);
    }

    /*
     * Displays formatted results up to MAX_RESULTS results. 
     */
    @Override
    protected void displayResult() {
        outputArea.removeAll();
        var textResults = formatTextResults();
        for(var line : textResults){
            outputArea.add(line);
        }
        displayGraph("Total Sales");
        outputArea.revalidate();  
    }

    /*
     * Formats a line of x, y results for display. 
     */
    @Override
    protected String formatTextResultLine(String x, String y) {
        LocalDateTime epoch = Instant.ofEpochMilli(Long.parseLong(y) * 1000)
                                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        String date = epoch.toString().substring(0, MAX_RESULTS);

        return (x + "\s\s" + date);
    }
}
