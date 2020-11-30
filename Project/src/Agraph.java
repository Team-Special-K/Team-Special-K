import java.io.BufferedReader; 
import java.io.IOException; 
import java.nio.charset.StandardCharsets; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.nio.file.Paths; 
import java.util.ArrayList; 
import java.util.List;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
  
public class CSVReaderInJava { 
  public static void main(String... args) {
     List<Order> orders = readOrdersFromCSV("customer_orders_A_team5.csv"); 
     
     for (Order o : orders) { System.out.println(0);
     }
    }

    public static List<Order> readOrdersFromCSV(String fileName) { 
      List<Order> order = new ArrayList<>(); 
      Path pathToFile = Paths.get(fileName); 
      
      // create an instance of BufferedReader 
      try (BufferedReader br = Files.newBufferedReader(pathToFile, 
              StandardCharsets.US_ASCII)) { 
                
          // read the first line from the text file 
          String line = br.readLine(); 
          
          // loop until all lines are read 
          while (line != null) { 
            
            // use string.split to load a string array with the values from 
            // each line of 
            // the file, using a comma as the delimiter 
            String[] attributes = line.split(","); 
            
            Order order = createOrder(attributes); 
            
            // adding book into ArrayList 
            orders.add(order); 
            // read next line before looping 
            // if end of file reached, line would be null 
            line = br.readLine(); 
          } 
      } catch (IOException ioe) { 
        ioe.printStackTrace(); 
      } 
      return orders;

    }

    private static Order createOrder(String[] metadata) { 
      String date = metadata[0];
      String cust_email = metadata[1];
      String cust_location = metadata[2];
      int product_id = Integer.parseInt(metadata[3]);
      int quantity = Integer.parseInt(metadata[4]); 
       
      
      // create and return book of this metadata 
      return new Order(date, cust_email, cust_location, product_id, quantity); } }
    }
  }
  
class Order {
    private String date;
    private String cust_emial;
    private String cust_location;
    private int product_id;
    private int quantity;

    public Order(String date, String cust_email, String cust_location, int product_id, int quantity) {
        this.date = date;
        this.cust_email= cust_email;
        this.cust_location= cust_location;
        this.product_id= product_id;
        this.quantity= quantity;
    }

    public String getDate(){
      return date;
    }

    public void setDate(String date){
      this.date = date;
    }

    public String getCust_email(){
      return cust_emial;
    }

    public vod setCust_email(String cust_email){
      this.cust_emial = cust_email;
    }

    public String getCust_location(){
      return cust_location;
    }

    public void setCust_location(String cust_location){
      this.cust_location = cust_location;
    }

    public int getProduct_id(){
      return product_id;
    }  

    public void setProduct_id(int product_id){
      this.product_id = product_id;
    }

    public int getQuantity(){
      return quantity;
    }
    
    public void setQuantity(int quantity){
      this.quantity = quantity;
    }
}  



public class TimeSeriesChartExample extends JFrame {  
  
  
  private static final long serialVersionUID = 1L;  
  
  public TimeSeriesChartExample(String title) {  
    super(title);  
    // Create dataset  
    XYDataset dataset = createDataset();  
    // Create chart  
    JFreeChart chart = ChartFactory.createTimeSeriesChart(  
        "Companies' Financial Report", // Chart  
        "Date", // X-Axis Label  
        "Number", // Y-Axis Label  
        dataset);  
  
    //Changes background color  
    XYPlot plot = (XYPlot)chart.getPlot();  
    plot.setBackgroundPaint(new Color(255,228,196));  
      
    ChartPanel panel = new ChartPanel(chart);  
    setContentPane(panel);  
  }  
  
  private XYDataset createDataset() {  
    TimeSeriesCollection dataset = new TimeSeriesCollection();  
  
    TimeSeries series1 = new TimeSeries("Assets");   
    dataset.addSeries(series1);  
  
    TimeSeries series2 = new TimeSeries("Incoming Customer Orders");    
    dataset.addSeries(series2);  

    TimeSeries series3 = new TimeSeries("Total Dollar Amount of Customer Orders Recieved");    
    dataset.addSeries(series2);  
      
  
    return dataset;  
  }  
  
  public static void main(String[] args) {  
    SwingUtilities.invokeLater(() -> {  
      TimeSeriesChartExample example = new TimeSeriesChartExample("Time Series Chart");  
      example.setSize(800, 400);  
      example.setLocationRelativeTo(null);  
      example.setVisible(true);  
      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
    });  
  }  
}  