import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Algorithms {

    private final Db db = Db.getInstance();

    /*
     * Gets product quantity sold over dateRange.
     * 
     * @param results a KType object.
     * @return KTYPE of sets quantitySold, soldPrice, productId
     */
    public KType getMostOrderedProducts(KType results){

        var productSold = new HashMap<String, Double>();

        for(var result : results.data){
            String productID = result[1];
            Double existingSold = 0.0;
            Double totalSold = Double.parseDouble(result[0]);

            if(productSold.containsKey(productID)){
                existingSold += productSold.get(productID);
            }
            productSold.put(productID, existingSold + totalSold);
        }
        return KType.toKType(productSold);
    }

    /*
     * Gets daily number of orders per day.
     * 
     * @param results a KType object.
     * @return KTYPE of sets quantity, soldPrice, epoch
     */
    public KType getProductNumOrders(KType results){

        var numOrders = new HashMap<String, Double>();

        for(var result : results.data){
            String epoch = result[2];
            Double existingQuantity = 0.0;
                
            Double total = Double.parseDouble(result[0]);

            if(numOrders.containsKey(epoch)){
                existingQuantity += numOrders.get(epoch);
            }
            numOrders.put(epoch, existingQuantity + total);
        }
        return KType.toKType(numOrders);
    }

    /*
     * Gets daily number of orders per day.
     * 
     * @param results a KType object.
     * @return KTYPE of sets quantity, soldPrice, epoch
     */
    public KType getDailySales(KType results){

        var dailySales = new HashMap<String, Double>();

        for(var result : results.data){
            String epoch = result[2];
            Double existingTotal = 0.0;

            Double total = Double.parseDouble(result[0]) * Double.parseDouble(result[1]);

            if(dailySales.containsKey(epoch)){
                existingTotal += dailySales.get(epoch);
            }
            dailySales.put(epoch, existingTotal + total);
        }
        return KType.toKType(dailySales);
    }

    /*
     * Gets daily number of orders per day.
     * 
     * @param results a KType object.
     * @return KTYPE of sets quantity, soldPrice, epoch
     */
    public KType getCustomerSpent(KType results){

        var customerSpent = new HashMap<String, Double>();

        for(var result : results.data){
            String email = result[1];
            Double total = Double.parseDouble(result[0]);
            Double existingTotal = 0.0;

            if(customerSpent.containsKey(email)){
                existingTotal += customerSpent.get(email);
            }
            customerSpent.put(email, existingTotal + total);
        }
        return KType.toKType(customerSpent);
    }

    /*
     * Gets daily number of sales per day.
     * 
     * @param results a KType object.
     * @return KTYPE of sets numOrders, dateString, dateObj
     */
    public KType getDailyNumOrders(KType results){

        var numOrders = new HashMap<String, Double>();

        for(var result : results.data){
            String epoch = result[2];
            Double num = 0.0;
            if(numOrders.containsKey(epoch)){
                num = numOrders.get(epoch);
            }
            numOrders.put(epoch, num + 1);
        }
        return KType.toKType(numOrders);
    }
    
    /*
     * Gets unsorted list of assets sold daily.
     * 
     * @param results a sql query array
     * @return KTYPE of sets daySales, dateString, dateObj
     */
    public KType getDailyAssetsSold(KType results){

        CopyOnWriteArrayList<Tup<Double,Integer,Long>> sumTotal = new CopyOnWriteArrayList<Tup<Double,Integer, Long>>();

        for(var result : results.data){

            long epoch = Long.parseLong(result[2]);
            Integer quant = Integer.parseInt(result[1]);
            Double price = Double.parseDouble(result[0]);
            Double total = quant * price;

            var newline = new Tup<Double,Integer,Long>(total,0,epoch);
            boolean found = false;

            for(int i = 0; i < sumTotal.size();i++) 
            {
                if(epoch == sumTotal.get(i).z){
                    Double newAddition =  total + sumTotal.get(i).x;
                    sumTotal.remove(i);
                    var reinsert = new Tup<Double, Integer, Long>(newAddition, 0, epoch);
                    sumTotal.add(i, reinsert);
                    found = true;
                    break;
                }
            }
            if(!found){sumTotal.add(newline);}
        }
        return KType.toKType(sumTotal);
    }



    /*
     * Calculates assets total daily while iterating a time series .  
     * 
     * @param startAssets the dollar amount of assets at beginning of date range
     * @param dailySales a KType of daily sales
     * @return KType of sets representing daily (assets, x, epoch)
     */
    public KType calcAssetsFromStart(double startAssets, KType dailySales){

        Comparator<String[]> epochSort = (String[]a, String[]b) 
            -> Long.compare(Long.parseLong(a[2]), Long.parseLong(b[2]));

        dailySales.data.sort(epochSort);

        KType assets = new KType();
        double start = startAssets;

        for(var tuple : dailySales.data){
            start = start - Double.parseDouble(tuple[0]);
            assets.add(Double.toString(start), tuple[1], tuple[2]);
        }
        return assets;
    }

    /*
     * Calculates total assets of product inventory.
     * 
     * @return sumAssets sum of current inventory wholesalecost
     */
    public double getSumAssets(){

        ResultSet assetArray = getProducts();
        double sumAssets = 0;

        try{        
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

        }catch(SQLException e){
            sumAssets = 0;
            e.printStackTrace();
        }
        return sumAssets;
    }

    /*
     * Gets all select product details from the db.
     * 
     * @return ResultSet of items in DB
     */
    public ResultSet getProducts(){
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM Products;");
    }

    /*
     * Gets all orders fields from the db.
     * 
     * @return ResultSet the database response composed of email, product id, quantity
     */
    public ResultSet getOrders(){
        return db.sendSqlStatement("SELECT cust_email, product_id, product_quantity FROM Orders;");
    }


    /*
     * Gets sales fields from the db.
     * 
     * @return ResultSet the database response composed of date, quantity, sales_price
     */
    public ResultSet queryDailySales(){
        return db.sendSqlStatement("SELECT orders.date, orders.product_quantity, products.sale_price "
        + "FROM orders JOIN products ON orders.product_id = products.product_id;");
    }

    /*
     * Gets all num orders fields from the db.
     * 
     * @param db the database handle
     * @return ResultSet the database response composed of date, product id, quantity
     */
    public ResultSet queryNumOrders(){
        return db.sendSqlStatement("SELECT date, cust_email, product_quantity FROM Orders;");
    }

    /*
     * Gets all num orders fields from the db.
     * 
     * @param db the database handle
     * @return ResultSet the database response composed of date, product id, quantity
     */
    public ResultSet queryMostOrderedProducts(){
        return db.sendSqlStatement("SELECT date, product_id, product_quantity FROM Orders;");
    }

    /*
     * Gets all customer order fields.
     * 
     * @param db the database handle
     * @return ResultSet the database response composed of date, sales_total, wholesale_cost
     */
    public ResultSet queryTopCustomers(){
        String query = "SELECT orders.date, orders.cust_email, "
                     + "orders.product_quantity, products.sale_price "
                     + "FROM orders JOIN products ON orders.product_id = products.product_id;";
        return db.sendSqlStatement(query);
    }

    /*
     * Gets all asset fields from the db.
     * 
     * @param db the database handle
     * @return ResultSet the database response composed of date, quantity, wholesale_cost
     */
    public ResultSet getAssets(){
        String query = "SELECT orders.date, orders.product_quantity, products.wholesale_cost "
                     + "FROM orders JOIN products ON orders.product_id = products.product_id;";
        return db.sendSqlStatement(query);
    }

    /*
    * Converts ResultSet to KType.
    * 
    * @param results the db response in 3 column format
    * @return converted String date, String y, String epoch
    * @return converted String orderTotal, String email, String epoch if isFourFields
    */
    public static KType convertResultSetKType(ResultSet results, Boolean isFourFields){
    
        KType converted = new KType();
        try{
            while(results.next()){
                String key = results.getString(1);  
                key = Long.toString(LocalDateTime.parse(key + DateFields.EOL).
                                                    toEpochSecond(ZoneOffset.UTC));

                if(isFourFields != true){
                    converted.add(results.getString(3), results.getString(2), key);
                }else{      
                    Double quantity = Double.parseDouble(results.getString(3));
                    Double salesPrice = Double.parseDouble(results.getString(4));
                    String total = Double.toString(quantity * salesPrice);
                    converted.add(total, results.getString(2), key);
                }
        }
        }catch(SQLException e){
            converted = null;
            e.printStackTrace();
        }
        return converted;
    }

   /*
    * Gets the sum of all assets sold up until the start of the dateRange.
    * 
    * @param assetsDays the KType set of assets by day
    * @param dateRange 
    * @return the sum of assets sold from the start of dateRange
    */
    public static double getAssetsStartRange(KType assetsDays, DateFields dateRange){

        double assetsSold = 0;

        final long startEpoch = dateRange.getStartTimeEpoch();

        for(var set : assetsDays.data){
            long orderEpoch = Long.parseLong(set[2]);
            if(orderEpoch <= startEpoch){
                assetsSold += (Double.parseDouble(set[0]) * Double.parseDouble(set[1]));
            }
        }
        return assetsSold;
    }

    /*
     * Filters KType of orders by a date range.
     * 
     * @param start the object from the start date text field
     * @param end the object from the end date text field
     * @param rows the KType to filter 
     * @return KType of filtered sets
     */
    public static KType filterByDate(DateFields dateRange, KType rows){

        final long startEpoch = dateRange.getStartTimeEpoch();
        final long endEpoch = dateRange.getEndTimeEpoch();

        var filtered = new KType();

        for(var set : rows.data){
            long epoch = Long.parseLong(set[2]);
            if(epoch < startEpoch || epoch > endEpoch){
                continue;
            }
            filtered.add(set[0], set[1], set[2]);
        }
        return filtered;
    }
}
