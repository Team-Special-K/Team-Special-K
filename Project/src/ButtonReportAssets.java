import java.awt.*;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.swing.JLabel;
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
            convertedResults = Algorithms.convertResultSetKType(dbResults, false);
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
     * Formats a line of x, y results for display. 
     */
    @Override
    protected JPanel formatLineToPanel(String x, String y) {
        LocalDateTime epoch = Instant.ofEpochMilli(Long.parseLong(y) * 1000)
                                    .atOffset(ZoneOffset.UTC).toLocalDateTime();
        String date = epoch.toString().substring(0, MAX_RESULTS);

        JPanel line = new JPanel(new GridLayout(1, 2));

        JLabel firstResult = new JLabel("$" + new DecimalFormat("#0.00").format(Double.parseDouble(x)), JLabel.CENTER);
        firstResult.setFont(textResultFont);
        firstResult.setForeground(Gui.BG_COLOR);
        line.add(firstResult);

        JLabel secondResult = new JLabel(date, JLabel.CENTER);
        secondResult.setFont(textResultFont);
        line.add(secondResult);

        return line;
    }
}
