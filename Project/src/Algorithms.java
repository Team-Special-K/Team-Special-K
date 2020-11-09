import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
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
    public ArrayList<Tup<Double,String>> getUsersSpent(ResultSet ordersTable) throws NumberFormatException, SQLException {

        CopyOnWriteArrayList<Tup<Double,String>> sumTotal = new CopyOnWriteArrayList<Tup<Double,String>>();
        while(ordersTable.next()){
            
            String email = ordersTable.getString(1);
            Integer quant = Integer.parseInt(ordersTable.getString(2));
            Double price = Double.parseDouble(ordersTable.getString(3));
            Double total = quant * price;

            var newline = new Tup<Double, String>(total,email);
            boolean found = false;

            for(int i = 0; i < sumTotal.size();i++) 
            {
                if(email.equals(sumTotal.get(i).y)){
                    Double newAddition =  total + sumTotal.get(i).x;
                    sumTotal.remove(i);
                    var reinsert = new Tup<Double, String>(newAddition, email);
                    sumTotal.add(i, reinsert);
                    found = true;
                    break;
                }
            }
            if(!found){sumTotal.add(newline);}
        }
        var sumTotalArr = new ArrayList<Tup<Double, String>>();
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
    public ArrayList<Tup<Double,String>> getSortedTuples(ArrayList<Tup<Double,String>> unsorted, int x) {

        ArrayList<Tup<Double,String>> sorted = new ArrayList <Tup<Double,String>>();

        for(Tup<Double, String> tuple: unsorted){

            int totalItems = sorted.size();
            if(totalItems == 0){
                sorted.add(0, tuple);
                continue;
            }
            else if(totalItems > x) 
                totalItems = x;

            for(int i = 0; i < totalItems; i++){
                if(tuple.x > sorted.get(i).x){
                    sorted.add(i, tuple);
                    break;
                }
            }
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
     * @return ResultSet of items in DB
     */
    public ResultSet getProducts(Db db) throws SQLException, SQLSyntaxErrorException {
        String tableName = "Products";
        return db.sendSqlStatement("SELECT quantity, wholesale_cost FROM " + tableName + ";");
    }

    /*
     * Get all orders details
     * 
     * @return ResultSet of orders in DB
     */
    public ResultSet getOrders(Db db) throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        String tableName = "Orders";
        return db.sendSqlStatement("SELECT cust_email, product_id, product_quantity FROM " + tableName + ";");
    }
}
