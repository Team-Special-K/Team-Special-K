import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.awt.Color;
import javax.imageio.ImageIO;


public class Graph {

    private XYSeries series;
    private DefaultCategoryDataset barGraphSeries; 
    private JFreeChart chart;
    private final Color frameColor = new Color(238,238,238);
    public static final String fileName = "export";

    public Graph(String name) {
        series = new XYSeries(name);
        barGraphSeries = new DefaultCategoryDataset();
    }

    /*
     * Adds dateEpoch, y-value to XYseries.
     */
    protected void addPoints(ArrayList<double[]> valDates) {
        for (var i : valDates) {
            series.add(i[0], i[1]);
        }
    }

  /*
     * Adds y, x values to bar graph series.
     */
    protected void addBarGraphPoints(ArrayList<String[]> valDates) {
        for (var i : valDates) {
            barGraphSeries.addValue(Double.parseDouble(i[1]), i[0], "");
        }
    }

    /*
     * Creates graph using XY series points.
     */
    protected void createLineGraph(String yAxisName) {

        assert(series.getItemCount() > 0);

        XYDataset data = new XYSeriesCollection(series);
        chart = ChartFactory.createTimeSeriesChart("", "", yAxisName, data);
        chart.setBackgroundPaint(frameColor);
        chart.removeLegend();

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(frameColor);
        plot.setRangeGridlinePaint(frameColor);

        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setDateFormatOverride(DateFormat.getDateInstance());
    }

    /*
     * Creates bar graph using barGraphSeries.
     */
    protected void createBarGraph(String yAxisName) {

        assert(barGraphSeries.getRowCount() > 0);

        chart = ChartFactory.createBarChart("", "", yAxisName, barGraphSeries);
        chart.getLegend().setVisible(false);
        chart.setBackgroundPaint(frameColor);
        chart.getPlot().setBackgroundPaint(frameColor);

        BarRenderer options = (BarRenderer)chart.getCategoryPlot().getRenderer();
        
        for(int i = 0; i < barGraphSeries.getRowCount(); i++){
            options.setSeriesPaint(i, new Color(0, 120 + (i*3), 215));
        }
    }

    /*
     * Adds dateEpoch, y-value to XYseries.
     */
    protected void exportImg(){

        assert(chart != null);

        BufferedImage  raw = chart.createBufferedImage(500, 260);

        File outputfile = new File(fileName);
        try{
            ImageIO.write(raw, "png", outputfile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
