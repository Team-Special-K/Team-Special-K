import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
    public double getAssets() throws SQLSyntaxErrorException, SQLException {

        ResultSet assetArray = getProducts(db);
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
    public ResultSet getProducts(Db db) throws SQLException, SQLSyntaxErrorException {
        String tableName = "Products";
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");
    }

    /*
     * Get all orders details
     * 
     * @param db the database handle
     * @return ResultSet of orders in DB
     */
    public ResultSet getOrders(Db db) throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        String tableName = "Orders";
        return db.sendSqlStatement("SELECT cust_email, product_id, product_quantity FROM " + tableName + ";");
    }

    /*
     * Filters array of orders by a date range
     * 
     * @param start the object from the start date text field
     * @param end the object from the end date text field
     * @param rows the object returned from the SQL query to the db
     * @return ArrayList of order columns
     */
    public static ArrayList<String[]> filterByDate(LocalDateTime start, LocalDateTime end, ResultSet rows) throws SQLException {

        final long startEpoch = start.toEpochSecond(ZoneOffset.UTC);
        final long endEpoch = end.toEpochSecond(ZoneOffset.UTC);
        long orderEpoch;
        
        int numColumns = rows.getMetaData().getColumnCount();
        var filtered = new ArrayList<String[]>();

        while(rows.next()){
            
            assert(rows.getString(4).length() < 11);
            LocalDateTime time = LocalDateTime.parse(rows.getString(4) + "T00:00:00.000000000");
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
