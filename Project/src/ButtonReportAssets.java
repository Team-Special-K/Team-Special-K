import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.swing.JPanel;

public class ButtonReportAssets extends ButtonReport {

    private final Algorithms algorithm;

    public ButtonReportAssets(JPanel outputArea, JPanel graphArea, Algorithms algorithm, String name, DateFields dateRange) {
        super(outputArea, graphArea, name);
        this.algorithm = algorithm;
        this.dateRange = dateRange;
    }

    /* 
     * Calculates sum of assets and sorts for the given dateRange.
     */
    @Override
    protected void calculateResult()  {

        double sumAssets = algorithm.getSumAssets();

        ResultSet dbResults = null;
        if (sumAssets != 0) {
            dbResults = algorithm.getAssets();
        }

        KType convertedResults = null;
        if (dbResults != null) {
            convertedResults = Algorithms.convertResultSetKType(dbResults);
        }

        if (convertedResults != null) {
            sumAssets += Algorithms.getAssetsStartRange(convertedResults, dateRange);

            KType filteredByDate = Algorithms.filterByDate(dateRange, convertedResults);
            KType dailyAssetsSold = algorithm.getDailyAssetsSold(filteredByDate);
            KType dailyAssetsSoldFromStart = algorithm.calcAssetsFromStart(sumAssets, dailyAssetsSold);

            result = KType.sortKTypeBy(0,10,dailyAssetsSoldFromStart);
        }
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
        displayGraph("Total Assets");
        outputArea.revalidate();  
    }

    /*
     * Formats a line of x, y results for display. 
     */
    @Override
    protected String formatTextResultLine(String x, String y) {
        DecimalFormat num = new DecimalFormat("#,###.00");
        num.setMaximumFractionDigits(2);

        LocalDateTime epoch = Instant.ofEpochMilli(Long.parseLong(y) * 1000)
                                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        String date = epoch.toString().substring(0, MAX_RESULTS);

        return ("$" + num.format(Double.parseDouble(x)) + "\s\s" + date);
    }
}
