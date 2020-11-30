
public class OrderQuery extends QueryBuilder{
    
	public OrderQuery(){	
        tableName = "Orders";
		totalColumns = 5;
        loadKeys();
        clearKeys();
    }

    public OrderQuery(String date, int quantity, String productId, 
    int confirmation, String shipAddress, String email) {
        this();
        keys.put("date", date);
		keys.put("cust_email", email);
		keys.put("cust_location", shipAddress);
		keys.put("product_id", productId);
        keys.put("product_quantity", quantity);
        keys.put("confirmation", confirmation);
    }

    public void loadKeys(){
        keys.put("date", "");
		keys.put("cust_email", "");
		keys.put("cust_location", "");
		keys.put("product_id", "");
        keys.put("product_quantity", 0);
        keys.put("confirmation", false);	
    }
}
