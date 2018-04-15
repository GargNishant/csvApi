package csvapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * In this API we are accepting only comma seperated Fields. Each row must end
 * in CRLF or line break and not with Comma. It does not accept the double
 * quotation. The number of entries should be constant meaning all column should
 * be filled. The programmer will have to pass the Path of the file. The flow of the 
 * file is as Follows:
 * 
 * 1. 
 * 
 */
public class CsvApi {

    /**
     * This is the seperator string. In CSV file the values are seperated by 
     * commas. So we will use the Variable to split the string that we got from
     * reading the File.
     */
    private static final String FIELD_DELIMITER = ",";

    
    /**
     * This API will replace all the Double Quotation with "". So when we get the
     * final String from Reading the whole file, we will use it to replaceAll 
     */
    private static final String REGEX_QUOTES = "\"";

    
    /**
     * This String is the replacement for the Double Quotation. We cannot make a
     * blank space in place of Double Quotations, so we are removing them completely
     */
    private static final String REPLACE_QUOTES = "";

    
    /**
     * As we have to assume the columns, thus it is Necessary to get the number of
     * columns. This Variable is responsible for holding the Number of Columns in 
     * the CSV file
     */
    private int NUMBER_OF_COLUMNS = Integer.MIN_VALUE;

    
    /**
     * When we read the whole file, we will split all the values in it by using commas
     * This variable is responsible for Holding all the values in array.
     */
    private String[] TABLE_CONTENTS;

    
    /**
     * We are assuming that the name of Column in our Database will start from "Column"
     * This variable holds the name of the Column 
     */
    private static final String TEMP_COLUMN_NAME = "Column";

    
    
    /**
     * Name of the Table that we are going to create will be Stored
     * in this variable
     */
    static final String TABLE_NAME = "contentTable";

    
    /**
     * Name of the Database will be stored inside this variable
     */
    static final String DATABASE_NAME = "database.db";

    /**
     * The path of the CSV file along with its Name.
     * If the path is wrong then the program will not execute
     */
    private String mFilePath;

    /**
     * When Reading the values and contents from the file with the help of 
     * BufferedReader, this String will be used to store all the contents of
     * a single line
     */
    private String inputLine = "";

    
    /**
     * This variable will be responsible of add all the contents from the inputLine
     * and append them by adding commas, as End of line does not have commas, 
     * and we are splitting values based on commas.
     */
    private StringBuilder validInputLine = new StringBuilder();

    
    /**
     * This variable will be used to create Insert Queries. It holds the current
     * position of the field from TABLE_CONTENTS array. Through this variable
     * we are making sure that we will never read data from same index again
     */
    private static int CURRENT_INDEX_OF_CONTENTS = 0;

    
    /**
     * This is the object of the SQLite Helper Class which is responsible for opening
     * inserting, creating and closing the database and it's connections
    */
    private SQLiteHelper dbHelper;

    CsvApi() {
        dbHelper = new SQLiteHelper();
    }

    /**
     * This method is used to set the path of the CSV file.
     */
    public void setFilePath(String mFilePath) throws IOException {
        
        //mFilePath = "C:\\Users\\Nishant Garg\\Downloads\\Sample.csv";
        
        /**
         * First Read the file, get the Number of Columns, Get values inside an 
         * array. Store them one by one
        */
        readFile(mFilePath);
        
        /**
         * Open Connection to check whether the JDBC is working or not
         */
        dbHelper.connect();
        
        
        /**
         * Sending a complete SQL Query to SQLiteHelper class so that it can
         * execute it and create a table
         */
        createTableQry();
        
        /**
         * We need to run the Insert Queries till we fill all the values or
         * elements in the array to the Database. This loop will run until
         * Current Index of array being used is not equal to Total number of
         * elements in the TABLE_CONTENTS array.
         */
        while (CURRENT_INDEX_OF_CONTENTS < TABLE_CONTENTS.length) {
            
            /**
             * Sends a raw SQL insert query to the SQLite Helper class which
             * handle various Database Related operations
             */
            insertValuesQry();
        }

    }

    
    /**
    * Responsible for reading the File from the path. Replaces the Double Quotes 
    * and seperates the Input based on comma and store each individual word in 
    * different index of an Array named as TABLE_CONTENTS
    */
    private void readFile(String path) {
        
        
        /**
         * A variable which gives us the row Number we are on. Mainly used to
         * calculate the number of columns in the CSV file
         */
        int rowNumber = 0;
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            while ((inputLine = bf.readLine()) != null) {
                
                if (rowNumber == 0) {
                   /**
                    * When reading for the 1st time we will extract the number of words
                    * split by commas, thus indicating the number of Columns
                    */ 
                    String[] temp = inputLine.split(FIELD_DELIMITER);
                    NUMBER_OF_COLUMNS = temp.length;
                }
                
                /**
                 * If any Double Quotations are found in the current Line, it will
                 * be replaced by "". Thus removing the Double Quotations Completely
                 */
                if (inputLine.contains(REGEX_QUOTES)) {
                    inputLine = inputLine.replaceAll(REGEX_QUOTES, REPLACE_QUOTES);
                }
                
                /**
                 * Append the current Line which we got from the file to a String
                 * Builder Variable which after whole completion, will be used to find the number
                 * of words and store them in an array.
                 */
                validInputLine.append(inputLine + ",");
                
                rowNumber++;
            }
            /**
             * Getting each value by using the commas and String Builder and 
             * storing them in Array of Strings.
             */
            TABLE_CONTENTS = validInputLine.toString().split(FIELD_DELIMITER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    /**
     * Creates the Raw SQL Query which can be used to create the table. It takes
     * several input and does the calculation to properly produces a raw query
     * that can be executed in SQL
     */
    private void createTableQry() {

        String createTableQry = "CREATE TABLE " + TABLE_NAME + "( ";

        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            createTableQry = createTableQry + TEMP_COLUMN_NAME + String.valueOf(i + 1) + " TEXT";

            if (i <= NUMBER_OF_COLUMNS - 2) {
                createTableQry = createTableQry + ", ";
            }
        }
        createTableQry = createTableQry + ");";
        
        dbHelper.createNewDatabase();
        dbHelper.createNewTable(createTableQry);

    }

    /**
     * Creates the Raw SQL Query which can be used to insert values into the
     * table. It takes several input and does the calculation to properly
     * produces a raw query that can be executed in SQL.
     */
    private void insertValuesQry() {

        StringBuilder statementString = new StringBuilder();
        statementString.append("INSERT INTO " + TABLE_NAME);

        
        /**
         * Now the String Builder object will contain the Value for example
         * INSERT INTO table_name (Column1 TEXT, Column2 TEXT,Column1 TEXT,
         * Column3 TEXT,....., ColumnN TEXT) The next step will be to append it
         * with the rest of statement
         */
        statementString.append(" VALUES ( ");

        /**
         * Now the following loop will be responsible for appending the existing
         * with following: (VALUE1, VALUE2, VALUE3, ........ , VALUE N); When
         * Appended successfully our Raw SQL query will be Complete;
         */
        for (int i = CURRENT_INDEX_OF_CONTENTS; i < CURRENT_INDEX_OF_CONTENTS + NUMBER_OF_COLUMNS; i++) {
            statementString.append("\"" + TABLE_CONTENTS[i] + "\"");
            /**
             * With this condition we can add the commas in SQL Queries and make
             * sure that at the end there are no commas or it will give an error
             */
            if (i <= CURRENT_INDEX_OF_CONTENTS + NUMBER_OF_COLUMNS - 2) {
                statementString.append(", ");
            }

        }
        
        /**
         * It is important to keep the track of all the elements we have already
         * inserted into database or we will keep inserting same data indefinitely
         */
        CURRENT_INDEX_OF_CONTENTS = CURRENT_INDEX_OF_CONTENTS + NUMBER_OF_COLUMNS;
        
        /**
         * Completing the SQL Query by appending the closing brackets and semi-colon
         * then we can send it to execute.
         */
        statementString.append(" );");
        String statement = statementString.toString();
        
        /**
         * Passing the whole raw SQL Query to the helper class which will be
         * responsible for managing the database
         */
        System.out.println(statement);
        dbHelper.insertIntoDB(statement);

    }

    public static void main(String[] args) throws IOException {
        CsvApi obj = new CsvApi();
        obj.setFilePath("C:\\Users\\Nishant Garg\\Downloads\\Sample.csv");
        // TODO code application logic here
    }
}
