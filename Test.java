package master;

import java.sql.*;

public class Test {

	public static void main(String[] args) throws SQLException {

		Db sears = new Db();
		ProductQuery q = new ProductQuery("sku123");
		
		//create db if it doesnt exist 
		ResultSet result = sears.sendSqlStatement(q.dbExist());//sears.sendSqlStatement(q.dbExist());
		if(!result.next()){sears.sendSqlStatement(q.createDb());}
		
		 //select db after creation
		sears.sendSqlStatement("USE " + q.DBNAME + ";");//add to querybuilder?
		
		//create table if it doesnt exist
		result = sears.sendSqlStatement(q.tableExist());//sears.sendSqlStatement(q.dbExist());
		if(!result.next()){sears.sendSqlStatement(q.createTable());}
		
        //add a new row
        q.keys.put("product_id", "abcZZZ");
		sears.sendSqlStatement(q.addRow());
			
		//get a row(s)
		q.keyWanted = "abcZZZ";
        ResultSet a = sears.sendSqlStatement(q.getRow()); //iterate resultset to get values of row
       
        //iterate results - row location
        while(a.next()){System.out.println(a.getRow());}
		
	}

}
