import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

public final class Db{
	
	private static Connection connect = null;
    private final static String dbAddress = "jdbc:mysql://localhost:3306?user=root";
    private static final Db instance = new Db();
    private final String dbName = "Firstdb";
 
    private Db(){
        getConnection();
    }

    /*
     * Returns the reference to the sole instance of the db
     */
    public static Db getInstance(){
        if(connect == null){
            instance.getConnection();
        }
        return instance;
    }

    /*
     * Gets the connection to an existing data MySql database
     * 
     * @return true if a successful connection was made
     */
    private boolean getConnection(){
    	
        try {
            connect = DriverManager.getConnection(dbAddress);
            sendSqlStatement("USE " + dbName + ";");        
        } 
        catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        }
    	return true;
    }
   
    /*
     * Executes a formatted SQL query on the database
     * 
     * @param sqlCommands a formatted SQL query
     * @return true if query was run successfully 
     */
    public ResultSet sendSqlStatement(String sqlCommands) {
    	
    	assert(connect != null);
    	ResultSet results = null;
    	
    	try{
    		Statement state = connect.createStatement();
    		boolean sqlSuccess = state.execute(sqlCommands);
    		if(sqlSuccess) {results = state.getResultSet();}    		
        } catch(SQLSyntaxErrorException e){
            e.printStackTrace();
        } catch(SQLException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    	return results;
    } 
      
    /*
     * Uploads a CSV file using the given table obj
     * 
     * @param fileName the csv file name
     * @param obj the table query object 
     */
    public void loadCsv(String fileName, Object obj) throws FileNotFoundException {

        File inputDataFile = new File(fileName);
        Scanner inputFile = new Scanner(inputDataFile);

        var keyIndex = new HashMap<Integer, String>();
        String line = inputFile.nextLine();
        String[] headerRow = line.split("[,]", 0);

        for(int i = 0; i < headerRow.length; i++){
            String columnName = headerRow[i];
            keyIndex.put(i, columnName);
        }

        QueryBuilder tableObj = TableFactory.TableFactory(obj);

        while(inputFile.hasNext()) {

            line = inputFile.nextLine();
            String[] data = line.split("[,]", 0);
            
            for(int i = 0; i < data.length; i++){
                tableObj.keys.put(keyIndex.get(i), data[i]);
            }
            this.sendSqlStatement(tableObj.addRow());
        } 
        inputFile.close();
    }

    /*
     * Returns all the rows from the table using filters
     * 
     * @param tableType get rows from table 
     * @param useKeyFilter use table.keys as sql filters
     * @return ResultSet of sql execution
     */
    public ResultSet getAllRows(QueryBuilder table, boolean useKeyFilter){

        String query = "SELECT * from " + table.tableName;
        int filterCount = 0;
        
        for(Object v: table.keys.values()) 
            if(v != null){
                filterCount += 1;
            }
        
        if(useKeyFilter){
            int i = 1;
            for(String k : table.keys.keySet()){
                Object v = table.keys.get(k);
                if(v != null){
                    query += " WHERE " + k + " = '" + v + "' ";
                    if(i++ != filterCount) 
                        query += " AND ";
                    }
            }
        }
        query += ";";

        return this.sendSqlStatement(query);
    }
}