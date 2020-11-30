import java.awt.*;
import javax.swing.*;


public class Gui {

    public static final Color BG_COLOR = new Color(64, 64, 64);

    public static void main(String[] args){
        initDb();
        Gui gui = new Gui();
        gui.buildFrame();
    }

    private static void initDb() {

        Db db = Db.getInstance();

        if (db.sendSqlStatement(QueryBuilder.dbExist()) == null) {
            db.sendSqlStatement(QueryBuilder.dbCreate());
        }

        db.sendSqlStatement(QueryBuilder.dbUse());

        ProductQuery product = new ProductQuery();
        if (db.sendSqlStatement(product.tableExist()) == null) {
            db.sendSqlStatement(product.createTable());
        }

        OrderQuery order = new OrderQuery();
        if (db.sendSqlStatement(order.tableExist()) == null) {
            db.sendSqlStatement(order.createTable());
        }
    }
 
    private void buildFrame() {

        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(BG_COLOR);

        String [] setBlack = new String[]{"background", "focus", "contentAreaColor", 
                                          "selected", "selectedBackground"};                                    
        String [] setGray = new String[]{"borderHightlightColor"};

        for(var set : setBlack){
            UIManager.put("TabbedPane." + set, BG_COLOR);
        }
        for(var set : setGray){
            UIManager.put("TabbedPane." + set, new Color(104, 104, 104));
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(new Color(87,87,87)); 
        tabs.setForeground(Color.WHITE);

        JPanel firstTab = new JPanel();
        firstTab.setLayout(new BoxLayout(firstTab, BoxLayout.X_AXIS));
        JPanel menu = new JPanel(new GridLayout(10, 1, 0, 3));
        menu.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        menu.setBackground(BG_COLOR);

        firstTab.add(menu);

        JPanel results = new JPanel(new GridLayout(2, 1));
        results.setBackground(Color.WHITE);

        
        JPanel sampleGraph = new JPanel();
        sampleGraph.setPreferredSize(new Dimension(500, 350));
        results.add(sampleGraph);   

        JPanel textResults = new JPanel(new GridLayout(10, 1));
        textResults.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        results.add(textResults);

        firstTab.add(results);

        JPanel datePicker = new JPanel(new GridLayout(2, 1));        
        datePicker.setBackground(BG_COLOR);

        JLabel dateLabel = new JLabel("Date Range:");
        dateLabel.setForeground(new Color(220, 239, 252));
        dateLabel.setHorizontalAlignment(JLabel.LEFT);
        datePicker.add(dateLabel);

        JPanel dateRangeBox = new JPanel(new GridLayout(1, 2));
        DateFields dateRange = new DateFields(); 
        JPanel dateRangeLine = new JPanel(new GridLayout(1, 2, 4, 5));
        dateRangeLine.setBackground(BG_COLOR);
        dateRangeLine.add(dateRange.getStartField());
        dateRangeLine.add(dateRange.getEndField());
        dateRangeBox.add(dateRangeLine);
        datePicker.add(dateRangeBox);

        menu.add(datePicker);

        menu = addButtons(menu, sampleGraph, textResults, dateRange);

        tabs.addTab("Reports", firstTab);
        tabs.addTab("Inventory", new JPanel());

        frame.add(tabs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel addButtons(JPanel menu, JPanel graphArea, JPanel outputArea, DateFields dateRange) {

        Algorithms assets = new Algorithms();
        var buttonAssets = new ButtonReportAssets(outputArea, graphArea, assets, 
                                                    "Total Assets", dateRange);
        menu.add(buttonAssets.button);

        Algorithms numOrders = new Algorithms();
        var buttonNumOrders= new ButtonReportNumOrders(outputArea, graphArea, numOrders, 
                                                        "Total Orders", dateRange); 
        menu.add(buttonNumOrders.button);

        Algorithms sales = new Algorithms();
        var buttonSales= new ButtonReportSales(outputArea, graphArea, sales, 
                                                "Total Sales", dateRange); 
        menu.add(buttonSales.button);

        Algorithms topProducts = new Algorithms();
        var buttonTopProducts= new ButtonReportMostOrderedProducts(outputArea, graphArea, topProducts, 
                                                            "Top Products", dateRange); 
        menu.add(buttonTopProducts.button);

        return menu;
    }
}

