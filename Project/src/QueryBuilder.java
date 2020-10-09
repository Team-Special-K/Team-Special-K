import java.util.HashMap;
import java.util.Map;

public abstract class QueryBuilder {
	
	static String DBNAME = "FIRSTDB";

	String keyHeader; 
	String keyWanted;
	String tableName;
	int totalColumns;
	
	Map<String, Object> keys = new HashMap<String, Object>();
	
	public QueryBuilder() {
		keyWanted = "";
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
	 * Creates a SQL query to use this DBNAME
	 * 
	 * @return the SQL query
	 */
	public String dbUse() {
		return "USE " + DBNAME + ";";
	 }

	
	/*
	 * Creates a SQL query to check if this tableName exists
	 * 
	 * @return the SQL query
	 */
	public String tableExist() {
		return "SHOW TABLES LIKE '" + tableName + "';";
	}

	/*
	 * Creates a SQL query to get a row using this keyWanted value
	 * 
	 * @return the SQL query
	 */
	public String getRow(){
		String query = "SELECT * " + "FROM " + tableName + " WHERE " + keyHeader + " = '" + keyWanted + "';";
		return query;
	}
	
	/*
	 * Creates a SQL query using this keyWanted to select a row and this keys to update the selected row values
	 * keys values needs to be cleaned!
	 * 
	 * @returns the SQL query
	 */
	public String updateRow() { 

		assert(keys.get(keyHeader) != null);
		
		String updateVals = "";
		
		for (Map.Entry<String, Object> set: keys.entrySet()) {
			Object value = set.getValue();
			if(value != null) {
			updateVals += set.getKey() + " = " + (String)value + ",";
			}
		}
		
		updateVals = updateVals.substring(0, updateVals.length() - 1);
		
		String query = "UPDATE " + tableName + " SET " + updateVals + " WHERE " + keyHeader + " = " + keyWanted + ";";
		
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
        updateVals.append("INSERT INTO " + tableName + "(");
 
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
		String query = "DELETE " + "FROM " + tableName + " WHERE " + keyHeader + " = " + keyWanted + ";";
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

		if(keys.get(keyHeader) == null){loadKeys();}

		StringBuilder updateVals = new StringBuilder();
		updateVals.append("CREATE TABLE " + tableName + " (");
		updateVals.append("id int AUTO_INCREMENT,");

		keys.forEach((k,v)->{
			updateVals.append(k);
			if(v instanceof String){updateVals.append(" varchar(255),");}
			else if(v instanceof Double){updateVals.append(" double(10,2),");}
			else if(v instanceof Integer){updateVals.append(" MEDIUMINT(255),");}
			else if(v instanceof Boolean){updateVals.append(" TINYINT(1),");}
			else if(v == null){updateVals.append(" varchar(255),");}
		});
		
		updateVals.append(" PRIMARY KEY (id)");
		updateVals.append(");");

		clearKeys();
		
		return updateVals.toString();
	}
	
	/*
	 * Assigns null values to all of the keys in this key hashmap
	 */
	public void clearKeys() {
		var newHashMap = new HashMap<String, Object>();
        keys.forEach((k, v) -> newHashMap.put(k, null));
        this.keys = newHashMap;
	}

	/*
	 * Loads keys with sample values
	 * Val types used to create db table columns
	 */
	public abstract void loadKeys();


	public String getType(Object v){
        String returnType = null;
        if(v instanceof String || v == null){returnType = " varchar(255),";}
        else if(v instanceof Double){returnType = " double(10,2),";}
        else if(v instanceof Integer){returnType = " MEDIUMINT(255),";}
        else if(v instanceof Boolean){returnType = " TINYINT(1),";}
        return returnType;
    }




}
