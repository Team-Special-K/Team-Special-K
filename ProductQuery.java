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
}
