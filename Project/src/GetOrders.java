import java.io.FileNotFoundException;

public class GetOrders {

    public static void main(String[] args) throws FileNotFoundException {
    Db database = Db.getInstance();
      OrderQuery o = new OrderQuery();
      database.sendSqlStatement("USE "+ QueryBuilder.DBNAME + ";");
      database.sendSqlStatement(o.createTable());
      database.loadCsv("customer_orders_A_team5.csv", o);
    }
}