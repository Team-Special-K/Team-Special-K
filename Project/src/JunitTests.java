import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.junit.Test;  

public class JunitTests {

    @Test
    public void assertKeysAreNull() {
        
        ProductQuery products = new ProductQuery();
        OrderQuery orders = new OrderQuery();

        products.loadKeys();
        products.keys.put("ABC", 1);
        products.clearKeys();

        orders.loadKeys();
        orders.keys.put("ABC", 1);
        orders.clearKeys();

        var combined = new HashMap<>();
        combined.putAll(products.keys);
        combined.putAll(orders.keys);

        Boolean valsNull = true;

        for (Object v : combined.values()) {
            if (v != null) {
                valsNull = false;
                break;
            }
        }
        assertEquals(valsNull, true);
    }

    @Test
    public void assertDeleteRow() throws SQLException {

        Db db = Db.getInstance();
        ProductQuery products = new ProductQuery();
        db.sendSqlStatement(products.dbUse());

        String keySearchingFor = "123";
        products.keys.put("product_id", keySearchingFor);
        db.sendSqlStatement(products.addRow());

        products.keyWanted = keySearchingFor;
        ResultSet rs = db.sendSqlStatement(products.getRow());
        assert(rs != null && rs.next());

        db.sendSqlStatement(products.deleteRow());
        rs = db.sendSqlStatement(products.getRow());
        assert(rs != null && rs.next() == false);
    }

    @Test
    public void assertDbSingleton(){
        Db db = Db.getInstance();
        Db secondDb = Db.getInstance();
        assert(db == secondDb);
    }
}