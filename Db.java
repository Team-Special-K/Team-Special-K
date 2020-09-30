import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Db{
	
	static Connection connect = null;
    final static String dbAddress = "jdbc:mysql://localhost:3306?user=root";
    private static final Db instance = new Db();

    private Db(){
        getConnection();
    }

    /*
     * Returns the reference to the sole instance of the db
     */
    public static Db getInstance(){
        if(connect == null) getConnection();
        return instance;
    }

    /*
     * Gets the connection to an existing data MySql database
     * 
     * @return true if a successful connection was made
     */
    private static boolean getConnection(){
    	
        try {
        	connect = DriverManager.getConnection(dbAddress);        
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
    	}
    	catch(Exception e){}
    	return results;
    }    
}