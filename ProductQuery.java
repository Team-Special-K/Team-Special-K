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
}
