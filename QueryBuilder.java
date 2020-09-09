
import java.util.HashMap;
import java.util.Map;

public abstract class QueryBuilder {
	
	static String KEYHEADER; 
	static String TABLENAME;
	static int TOTALCOLUMNS;
	static String DBNAME = "FIRSTDB";
	
	String keyWanted;
	
	Map<String, Object> keys = new HashMap<String, Object>();
	
	public QueryBuilder() {
		keyWanted = ""; 
	}
	
	public QueryBuilder(String keyWanted) {
		this.keyWanted = keyWanted;
	}

	/*
	 * Creates a SQL query to check if this DBNAME exists
	 * 
	 * @return the SQL query
	 */
	public String dbExist() {
		return "SHOW DATABASES LIKE '" + DBNAME + "';";
	}
	
	/*
	 * Creates a SQL query to check if this TABLENAME exists
	 * 
	 * @return the SQL query
	 */
	public String tableExist() {
		return "SHOW TABLES LIKE '" + TABLENAME + "';";
	}

	/*
	 * Creates a SQL query to get a row using this keyWanted value
	 * 
	 * @return the SQL query
	 */
	public String getRow(){
		String query = "SELECT * " + "FROM " + TABLENAME + " WHERE " + KEYHEADER + " = '" + keyWanted + "';";
		return query;
	}
	
	/*
	 * Creates a SQL query using this keyWanted to select a row and this keys to update the selected row values
	 * keys values needs to be cleaned!
	 * 
	 * @returns the SQL query
	 */
	public String updateRow() { 

		assert(keys.get(KEYHEADER) != null);
		
		String updateVals = "";
		
		for (Map.Entry<String, Object> set: keys.entrySet()) {
			Object value = set.getValue();
			if(value != null) {
			updateVals += set.getKey() + " = " + (String)value + ",";
			}
		}
		
		updateVals = updateVals.substring(0, updateVals.length() - 1);
		
		String query = "UPDATE " + TABLENAME + " SET " + updateVals + " WHERE " + KEYHEADER + " = " + keyWanted + ";";
		
		return query;
	}
	
	/*
	 * Creates a SQL query using this keys as the values for a new row
	 * 
	 * @returns the SQL query
	 */
	public String addRow() {
		
		assert(!keys.isEmpty());
		
		StringBuilder updateVals = new StringBuilder();
		updateVals.append("INSERT INTO " + TABLENAME + "(");

		keys.forEach((k,v) -> updateVals.append(k + ","));
		updateVals.deleteCharAt(updateVals.length() - 1);		
		updateVals.append(") VALUES (");
		
		keys.forEach((k,v) -> {
			if(v instanceof String){updateVals.append("'" + v + "',");}
			else{updateVals.append(v + ",");}
		});
		updateVals.deleteCharAt(updateVals.length() - 1);		

		updateVals.append(");");
		
		return updateVals.toString();
	}
	
	/*
	 * Creates a SQL query to delete a row using this keyWanted value
	 * 
	 * @return the SQL query
	 */
	public String deleteRow() {
		String query = "DELETE " + "FROM " + TABLENAME + " WHERE " + KEYHEADER + " = " + keyWanted + ";";
		return query;
	}
	
	/*
	 * Creates a SQL query to create a database using this dbname value
	 * 
	 * @return the SQL query
	 */
	public String createDb() {
		return "CREATE DATABASE " + DBNAME + ";";
	}
	
	/*
	 * Creates a SQL query to create a table using this tablename value
	 * 
	 * @return the SQL query
	 */
	public String createTable() {

		StringBuilder updateVals = new StringBuilder();
		updateVals.append("CREATE TABLE " + TABLENAME + " (");
		updateVals.append("id int AUTO_INCREMENT,");

		keys.forEach((k,v)->{
			updateVals.append(k);
			
			if(v instanceof String){updateVals.append(" varchar(255),");}
			else if(v instanceof Double){updateVals.append(" double(10,2),");}
			else if(v instanceof Integer){updateVals.append(" MEDIUMINT(255),");}
			else if(v == null){updateVals.append(" varchar(255),");}
		});
		
		//updateVals.deleteCharAt(updateVals.length() - 1);
		updateVals.append("PRIMARY KEY (id)");
		updateVals.append(");");
		
		return updateVals.toString();
	}
	
	/*
	 * Assigns null values to all of the keys in this key hashmap
	 */
	public void clearKeys() {
		keys.forEach((k, v) -> v = null);
	}
}
