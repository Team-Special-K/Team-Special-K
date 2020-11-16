import java.awt.Font;
import java.sql.ResultSet;
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

        if (result == null || result.getSize() < 1) {
            outputArea.add(new JLabel("NO RESULTS"));
        }
        else {    
            int size = result.getSize() < MAX_RESULTS ? result.getSize() : MAX_RESULTS;
            Font lineFont = new Font("Helvetica", Font.PLAIN, 13);

            for(var item : result.data.subList(0, size)){
                JLabel line = new JLabel();
                line.setFont(lineFont);
                line.setText(item[0] + "\s\s$" + item[2]);
                outputArea.add(line);
            }
            displayGraph("Total Assets");
        }
        outputArea.revalidate();   
    }
}
