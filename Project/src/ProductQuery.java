
public class ProductQuery extends QueryBuilder{

	public ProductQuery(){
		tableName = "Products";
		keyHeader = "product_id";
		totalColumns = 5;
		loadKeys();
		clearKeys();
	}

	public ProductQuery(String keyWanted){
		this();
		this.keyWanted = keyWanted;
	}

	public ProductQuery(String keyWanted, int quantity, 
			    double wholeCost, double salePrice, String supplier) {
			  
		this();
      	keys.put(keyHeader, keyWanted);
		keys.put("quantity", quantity);
		keys.put("wholesale_cost", wholeCost);
		keys.put("sale_price", salePrice);
		keys.put("supplier_id", supplier);
		
	}

	public void loadKeys(){
		keys.put(keyHeader, "");
		keys.put("quantity", 0);
		keys.put("wholesale_cost", 0.0);
		keys.put("sale_price", 0.0);
		keys.put("supplier_id", "");
	}
	
	public String buyerEvent() {
            
     		// statement that needs to be sent to database to update the quantity by 1 due to purchase
      		return "UPDATE " + tableName + " SET quantity = quantity - 1 WHERE " + keyHeader + " = " + keyWanted + ";"; 
      
   	}

   	public String supplierEvent() {
            
      		// statement that needs to be sent to database to update the quantity by 1 due to purchase
      		return "UPDATE " + tableName + " SET quantity = quantity + 1 WHERE " + keyHeader + " = " + keyWanted + ";"; 
      
   	}	
}
