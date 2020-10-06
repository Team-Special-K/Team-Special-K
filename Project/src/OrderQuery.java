
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
		keys.put("email", email);
		keys.put("ship_address", shipAddress);
		keys.put("product_id", productId);
        keys.put("quantity", quantity);
        keys.put("confirmation", confirmation);
}

    public void loadKeys(){
        keys.put("date", "");
		keys.put("email", "");
		keys.put("ship_address", "");
		keys.put("product_id", "");
        keys.put("quantity", 0);
        keys.put("confirmation", false);	
    }


}
