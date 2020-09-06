package master;

public class ProductQuery extends QueryBuilder{
	
	public ProductQuery(String keyWanted){
		super(keyWanted);
		
		KEYHEADER = "product_id";
		TABLENAME = "Products";
		TOTALCOLUMNS = 5;
		
		keys.put(KEYHEADER, "Sample");
		keys.put("quantity", 1);
		keys.put("wholesale_cost", 5.0);
		keys.put("sale_price", 3.0);
		keys.put("supplier_id", "a1");
		
	}
	public ProductQuery(String keyWanted, String productName, int quantity, 
			    double wholeCost, double salePrice, String Supplier) {
		
		super(keyWanted);
      
   
      		KEYHEADER = "product_id";
      		TABLENAME = "Products";
      		TOTALCOLUMNS = 5;
      
      		// puts inserted quantities from input into the product query constructor
      		keys.put(KEYHEADER, productName);
		keys.put("quantity", quantity);
		keys.put("wholesale_cost", wholeCost);
		keys.put("sale_price", salePrice);
		keys.put("supplier_id", supplier);
		
	}
}
