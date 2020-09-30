import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class Leader{
    public static void main(String[] args){
        Leader start = new Leader();
        start.go();
    }

    public void go(){
        try{
        Db db = Db.getInstance();
        boolean dbReady = setupDatabase(db);
        //addEntries("orders_team5.csv", db);// LOAD CSV HERE
        while(dbReady){
            Runtime.getRuntime().exec("python send_email.py");
            Thread.sleep(15000);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean setupDatabase(Db db) throws SQLException {

        ProductQuery query = new ProductQuery();
        query.loadKeys();
        db.sendSqlStatement(query.dbUse()); //sendSql returns null on non table altering requests FIX

        ResultSet tableresult = db.sendSqlStatement(query.tableExist());
        if(!tableresult.next()) db.sendSqlStatement(query.createTable());

        OrderQuery orderQuery = new OrderQuery(); 
        tableresult = db.sendSqlStatement(orderQuery.tableExist());
        if(!tableresult.next()) db.sendSqlStatement(orderQuery.createTable());

        return true;
    }

/*
*  This method pulls the information from a text file and places it into a product query file
*  which is then added to the database
*  @param Db test takes in a database to add the information to
*  @param fileName is the name of the document you are using to add information
*/
    public static void addEntries(String fileName, Db test) throws FileNotFoundException, SQLSyntaxErrorException,
    SQLException {

        File inputDataFile = new File(fileName);
        Scanner inputFile = new Scanner(inputDataFile);

        OrderQuery pq;
        inputFile.nextLine();
        while(inputFile.hasNext()) {

            String line = inputFile.nextLine();
            String[] data = line.split("[,]", 0);

            String b = data[1];
            int c = Integer.parseInt(data[2]);
            String d = data[3];
            int e =  Integer.parseInt(data[4]);
            String f = data[5];
            String g = data[6];
                    
            pq = new OrderQuery(b,c,d,e,f,g);
            test.sendSqlStatement(pq.addRow());  
        } 
        inputFile.close();
    }

}