/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvapi;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nishant Garg
 */
public class SQLiteHelper {
    
    
    SQLiteHelper(){
    }
    
    //Specified path of the database where it will be created and maniputaed and it's name
    private final String DATABASE_PATH = "jdbc:sqlite:C://sqlite3/" + "database.db";
    
    //Tries to connect to a database to see if JDBC is working or not
    void connect() {
        Connection conn = null;
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(DATABASE_PATH);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                /*Closses the connection regardless of succes or failure*/
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * Creates a new Database in the specified path of the database
     */
    public void createNewDatabase() {
        
        try (Connection conn = DriverManager.getConnection(DATABASE_PATH)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
 
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Creates a new table in the database in specified path of the database
     */
    public void createNewTable(String createTableQuery) {        
        
        try (Connection conn = DriverManager.getConnection(DATABASE_PATH);
                Statement stmt = conn.createStatement()) {
            System.out.println(createTableQuery);
            // create a new table
            stmt.execute(createTableQuery);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Insert he data into the database. Takes a raw SQL insert Query and 
     * executes it, resulting in inserting all the data into the Database
     */
    public  void insertIntoDB( String insertQuery) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(DATABASE_PATH);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            
            stmt.executeUpdate(insertQuery);
            stmt.close();
            c.commit();
            c.close();
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }   
}