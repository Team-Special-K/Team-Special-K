import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Algorithms {

    private final Db db = Db.getInstance();

    /*
     * Calculates total spent by each user email
     * 
     * @param ordersTable a sql query resultSet of all orders
     * @return array of tuples <totalspent, email>
     */
    public ArrayList<Tup<Double, String, LocalDateTime>> getUsersSpent(ArrayList<String[]> ordersTable) {

        CopyOnWriteArrayList<Tup<Double,String, LocalDateTime>> sumTotal = new CopyOnWriteArrayList<Tup<Double,String, LocalDateTime>>();

        for (var row : ordersTable){

            String email = row[0];
            Integer quant = Integer.parseInt(row[1]);
            Double price = Double.parseDouble(row[2]);


            assert(row[3].length() < 11);
            LocalDateTime time = LocalDateTime.parse(row[3] + "T00:00:00.000000000");
            Double total = quant * price;

            var newline = new Tup<Double, String, LocalDateTime>(total,email,time);
            boolean found = false;

            for(int i = 0; i < sumTotal.size();i++) 
            {
                if(email.equals(sumTotal.get(i).y)){
                    Double newAddition =  total + sumTotal.get(i).x;
                    sumTotal.remove(i);
                    var reinsert = new Tup<Double, String, LocalDateTime>(newAddition, email, time);
                    sumTotal.add(i, reinsert);
                    found = true;
                    break;
                }
            }
            if(!found){sumTotal.add(newline);}
        }
        var sumTotalArr = new ArrayList<Tup<Double, String, LocalDateTime>>();
        sumTotalArr.addAll(sumTotal);
        return sumTotalArr;
    }

    /*
     * Returns unsorted list of daily assets sold 
     * 
     * @param results a sql query resultSet of db query results
     * @return ArrayList of tuples <daySales, dateString, dateObj>
     */
    public ArrayList<Tup<Double, String, LocalDateTime>> getDailyAssets(ArrayList<String[]> results){

        CopyOnWriteArrayList<Tup<Double,String, LocalDateTime>> sumTotal = new CopyOnWriteArrayList<Tup<Double,String, LocalDateTime>>();
        final String EOL = "T00:00:00.000000000";

        for(var result : results){

            String date = result[0];
            assert(date.length() < 11);
            LocalDateTime time = LocalDateTime.parse(date + EOL);

            Integer quant = Integer.parseInt(result[1]);
            Double price = Double.parseDouble(result[2]);
            Double total = quant * price;

            var newline = new Tup<Double, String, LocalDateTime>(total,date,time);
            boolean found = false;

            for(int i = 0; i < sumTotal.size();i++) 
            {
                if(date.equals(sumTotal.get(i).y)){
                    Double newAddition =  total + sumTotal.get(i).x;
                    sumTotal.remove(i);
                    var reinsert = new Tup<Double, String, LocalDateTime>(newAddition, date, time);
                    sumTotal.add(i, reinsert);
                    found = true;
                    break;
                }
            }
            if(!found){sumTotal.add(newline);}
        }
        var sumTotalArr = new ArrayList<Tup<Double, String, LocalDateTime>>();
        sumTotalArr.addAll(sumTotal);
        return sumTotalArr;
    }

    /*
     * Calculates assets on a time series
     * 
     * @param startAssets the dollar amount of sales starting with
     * @param dailySales an ArrayList of sales 
     * @return array of <assetsTotal, epoch>
     */
    public ArrayList<double[]> calcAssetsFromStart(double startAssets, ArrayList<Tup<Double, String, LocalDateTime>> dailySales){

        Comparator<Tup<Double, String, LocalDateTime>> epochSort = 
                (Tup<Double, String, LocalDateTime>a, Tup<Double, String, LocalDateTime>b) 
                    -> Long.compare(a.z.toEpochSecond(ZoneOffset.UTC),b.z.toEpochSecond(ZoneOffset.UTC));

        dailySales.sort(epochSort);

        ArrayList<double[]> assets = new ArrayList<>(); //double assets state in dollars, double epoch
        double start = startAssets;

        for(var tuple : dailySales){
            double[] salesEpoch = new double[]{
                start - tuple.x, tuple.z.toEpochSecond(ZoneOffset.UTC)
            };
            assets.add(salesEpoch);
        }
        return assets;
    }

    /*
     * Calculates total spent by each user email
     * 
     * @param ordersTable a sql query resultSet of all orders
     * @return array of tuples <totalspent, email>
     */
    public ArrayList<Tup<Double, String, LocalDateTime>> getUsersSpent(ResultSet ordersTable) throws NumberFormatException, SQLException {

        CopyOnWriteArrayList<Tup<Double,String, LocalDateTime>> sumTotal = new CopyOnWriteArrayList<Tup<Double,String, LocalDateTime>>();
        while(ordersTable.next()){
            
            String email = ordersTable.getString(1);
            Integer quant = Integer.parseInt(ordersTable.getString(2));
            Double price = Double.parseDouble(ordersTable.getString(3));
            assert(ordersTable.getString(4).length() < 11);
            LocalDateTime time = LocalDateTime.parse(ordersTable.getString(4) + "T00:00:00.000000000");
            Double total = quant * price;

            var newline = new Tup<Double, String, LocalDateTime>(total,email,time);
            boolean found = false;

            for(int i = 0; i < sumTotal.size();i++) 
            {
                if(email.equals(sumTotal.get(i).y)){
                    Double newAddition =  total + sumTotal.get(i).x;
                    sumTotal.remove(i);
                    var reinsert = new Tup<Double, String, LocalDateTime>(newAddition, email, time);
                    sumTotal.add(i, reinsert);
                    found = true;
                    break;
                }
            }
            if(!found){sumTotal.add(newline);}
        }
        var sumTotalArr = new ArrayList<Tup<Double, String, LocalDateTime>>();
        sumTotalArr.addAll(sumTotal);
        return sumTotalArr;
    }

    /*
     * Sorts array of tuples by first Double field
     * Only x initial items are guaranteed to be sorted
     * 
     * @param unsorted the array of tuples
     * @param x the number of sorted items to return
     * @return sorted array of tuples <totalspent, email>
     */
    public ArrayList<Tup<Double,String,LocalDateTime>> getSortedTuples(ArrayList<Tup<Double,String,LocalDateTime>> unsorted, int x) {

        ArrayList<Tup<Double,String,LocalDateTime>> sorted = new ArrayList<Tup<Double,String,LocalDateTime>>();

        for(Tup<Double,String,LocalDateTime> tuple: unsorted){

            int totalItems = sorted.size();
            if(totalItems == 0){
                sorted.add(0, tuple);
                continue;
            }
            else if(totalItems > x) 
                totalItems = x;

            boolean found = false;
            for(int i = 0; i < totalItems; i++){
                if(tuple.x > sorted.get(i).x){
                    sorted.add(i, tuple);
                    found = true;
                    break;
                }
            }
            if(!found){sorted.add(tuple);}
        }
        return sorted;
    }

    /*
     * Calculates total assets of product inventory
     * 
     * @return sumAssets sum of current inventory wholesalecost
     */
    public double getSumAssets() throws SQLSyntaxErrorException, SQLException {

        ResultSet assetArray = getProducts();
        double sumAssets = 0;

        ResultSetMetaData metadata = assetArray.getMetaData();
        int columnCount = metadata.getColumnCount();

        while (assetArray.next()) {
            for (int i = 1; i < columnCount; i++) {
                double currentQuantity = Double.parseDouble(assetArray.getString(i));
                double currentCost = Double.parseDouble(assetArray.getString(i + 1));
                double currentAsset = currentQuantity * currentCost;
                sumAssets += currentAsset;
            }
        }
        return sumAssets;
    }

    /*
     * Get all items quantity & wholesalecost
     * 
     * @param db the database handle
     * @return ResultSet of items in DB
     */
    public ResultSet getProducts() throws SQLException, SQLSyntaxErrorException {
        String tableName = "Products";
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");
    }

    /*
     * Get all orders details
     * 
     * @param db the database handle
     * @return ResultSet of orders in DB
     */
    public ResultSet getOrders() throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        String tableName = "Orders";
        return db.sendSqlStatement("SELECT cust_email, product_id, product_quantity FROM " + tableName + ";");
    }

    /*
     * Get all asset fields
     * 
     * @param db the database handle
     * @return ResultSet the database response composed of date, quantity, wholesale_cost
     */
    public ResultSet getAssets() throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        String query = "SELECT orders.date, orders.product_quantity, products.wholesale_cost "
                     + "FROM orders JOIN products ON orders.product_id = products.product_id;";
        return db.sendSqlStatement(query);
    }


    /*
     * Filters array of orders by a date range
     * 
     * @param start the object from the start date text field
     * @param end the object from the end date text field
     * @param rows the object returned from the SQL query to the db
     * @return ArrayList of order columns
     */
    public static ArrayList<String[]> filterByDate(LocalDateTime start, LocalDateTime end, ResultSet rows, int dateColumn) throws SQLException {

        final long startEpoch = start.toEpochSecond(ZoneOffset.UTC);
        final long endEpoch = end.toEpochSecond(ZoneOffset.UTC);
        long orderEpoch;
        
        int numColumns = rows.getMetaData().getColumnCount();
        var filtered = new ArrayList<String[]>();

        while(rows.next()){
            
            assert(rows.getString(dateColumn).length() < 11);
            LocalDateTime time = LocalDateTime.parse(rows.getString(dateColumn) + "T00:00:00.000000000");
            orderEpoch = time.toEpochSecond(ZoneOffset.UTC);

            if(orderEpoch < startEpoch || orderEpoch > endEpoch) continue;

            var line = new String[numColumns];

            for(int i = 0; i < numColumns; i++){
                line[i] = rows.getString(i + 1);
            }
            filtered.add(line);
        }
        return filtered;
    }
}
