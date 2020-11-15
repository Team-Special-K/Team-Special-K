import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import javax.imageio.ImageIO;


public class Graph {

    private XYSeries series;
    private JFreeChart chart;

    public Graph(String name){
        series = new XYSeries(new String(name));
    }
    
    /*
     * Adds xy points [dateEpoch, value] to XYseries
     */
    private void addPoints(int[][] valDates){
        for(var i: valDates){
            series.add(i[0], i[1]);
        }
    }
    private void create(){
        XYDataset data = new XYSeriesCollection(series);
        chart = ChartFactory.createTimeSeriesChart("", "x-axis","y-axis", data);
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setDateFormatOverride(DateFormat.getDateInstance());
    }
    private void exportImg(String fn) throws IOException {
        BufferedImage  raw = chart.createBufferedImage(500, 600);
        File outputfile = new File(fn);
        ImageIO.write(raw, "png", outputfile);
    }
}
