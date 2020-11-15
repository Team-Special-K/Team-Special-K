import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

public class PasswordHash {

    public static void readFile()
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        // Here we will use a salt to enhance our encryption
        byte[] salt = genSalt();

        Scanner scnr = new Scanner(System.in);
        // read in a password input file of the users choice
        String fileName = scnr.next();

        File inputDataFile = new File(fileName);
        Scanner inputFile = new Scanner(inputDataFile);

        while (inputFile.hasNext()) {

            String password = inputFile.nextLine();
            System.out.println("Password: " + password);
            // Next we will implement our PBKDF2 and SecretKeyFactory
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            System.out.println(Arrays.hashCode(hash));

        }
        scnr.close();
        inputFile.close();

    }

    public static ArrayList<String> readDatabase() throws IOException, SQLSyntaxErrorException, SQLException,
            NoSuchAlgorithmException, InvalidKeySpecException {

        // generate salt
        byte[] salt = genSalt();
        // Get database instance
        Db db = Db.getInstance();

        db.sendSqlStatement("USE " + QueryBuilder.DBNAME + ";");

        ResultSet emails = getEmails(db);

        ArrayList<String> hashedEmails = new ArrayList<String>();

        while(emails.next()) {
            String email = emails.getString(1);

            KeySpec spec = new PBEKeySpec(email.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            hashedEmails.add(Arrays.hashCode(hash));

        }
        
        return hashedEmails;

    }

    public static ResultSet getEmails(Db db) throws SQLException, SQLSyntaxErrorException, FileNotFoundException {
        String tableName = "Orders";
        return db.sendSqlStatement("SELECT cust_email FROM " + tableName + ";");
    }

    // Method that generates the salt for our code and stores it into a file

    public static byte[] genSalt() throws IOException {

        //write information to file if needed 
        final String OUTPUT_FILE = "Salt.txt";

        FileWriter outputDataFile = new FileWriter(OUTPUT_FILE);
        PrintWriter outputFile = new PrintWriter(outputDataFile);

        // Create salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String s = new String(salt, StandardCharsets.UTF_8);

        for(int i = 0; i < salt.length; i++ ) {
            outputFile.print(salt[i]);
        }

        outputFile.print("\n");

        outputFile.println(salt);
        outputFile.println(s);

        // Close output file
        outputFile.close();

        return salt;
    }

    



    // Method that compares user input to a stored password in the table using our same hash and salt
    // Simple method returns true if the hashed email or password in question matches that of one stored in the database.  
    public static boolean comparePass(String hashedPassword, String databaseVal) {
        if(hashedPassword.equals(databaseVal)) {
            return true;
        } else {
            return false;
        }
    }


    public static ArrayList<String> getHash(String email){

        byte[] salt = genSalt();
        ArrayList<String> hashedEmails = new ArrayList<String>();

        KeySpec spec = new PBEKeySpec(email.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        hashedEmails.add(Arrays.hashCode(hash));

        return hashedEmails;

    }


    
}