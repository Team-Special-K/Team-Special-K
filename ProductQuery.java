package master;

public class ProductQuery extends QueryBuilder{
	
	public ProductQuery(String keyWanted){
		super(keyWanted);
		
		KEYHEADER = "product_id";
		TABLENAME = "Products";
		TOTALCOLUMNS = 5;
		
		keys.put(KEYHEADER, null);
		keys.put("quantity", null);
		keys.put("wholesale_cost", null);
		keys.put("sale_price", null);
		keys.put("supplier_id", null);
		
	}
	public ProductQuery(String keyWanted, int quantity, 
			    double wholeCost, double salePrice, String Supplier) {
		
		super(keyWanted);
      
   
      		KEYHEADER = "product_id";
      		TABLENAME = "Products";
      		TOTALCOLUMNS = 5;
      
      		// puts inserted quantities from input into the product query constructor
		keys.put("quantity", quantity);
		keys.put("wholesale_cost", wholeCost);
		keys.put("sale_price", salePrice);
		keys.put("supplier_id", supplier);
		
	}
}
