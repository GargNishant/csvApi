# CSV API

A Java Application which can be used as an API to parse and store a CSV file to a database using JDBC.

## About
A CSV file is a file in which the data is seperated by commas. Each line will have particular number of Entries, which corresponds to a table.
In a CSV file the number of entries in all the rows must be same. This program is able to parse a given CSV file, extract the data from 
it and store it in a DATABASE. The database used in this Application is **SQLite** which is a local database, stores the data in a disk file instead of storing in a server or cloud.

## How It Works
1. The user have to first import the packages and has to make sure that both **csvApi.java** and **SQLiteHelper.java** are available.

2. When the user calls **setFilePath()** method, The API collects the path of file from Parameter and Tries to open the file to read it

3. If the file is present then The API reads it line by line and gets various information like **Number of Columns**,**Number of values**, seperate each value by commas, etc.

4. Then API tries to connect to the database. This is just to check wether there are some errors or not.

5. The next thing is to create SQL Queries which should be executed in order like **Create Database**, **Create Table**, **Insert the Values**. All these are acheived by the methods in the **csvApi.java**.

6. After the raw SQL Queries are created, then the Control transfers to **SQLiteHelper.java** class. This class is resposible with the management of database, executing various raw Queries.

7. By executing different types of SQL Queries in **SQLiteHelper.java**, we create, and Fill the tables and databases.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes and learning purpose. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Things you need to install the software

```
JAVA JDK on you Machine
An IDE will be helpful. NetBeans is recommended
SQLite in local Machine
```

## Deployment

To test the app in any Physical Device or Emulater follow the Installation Process mentioned Below.


### Installing and Run

A step by step series of examples that tell you have to get a development enviroment running

1. Download JAVA SE edition from the Link
[JAVA SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

2. Install using the Steps as mentioned during the installation process

3. Download this Repository by clicking on "Clone or Download Option" and use ZIP

4. Extract the package to a Desired Location 

5. Download an IDE, NetBeans Recommended
[NetBeans IDE](https://netbeans.org/downloads/)

6. Install the IDE by following the On-Screen instructions.  

7. Create a new Java Application and import my Package in it.

8. To run the application Create an Object of  **CsvApi** class in main Function as shown

```
class abc{
  .
  .
  .
  
  private static void main(String[] args){
    CsvApi object = new CsvApi();
  }

}
```
With this class object you will be able to call the methods

9. Replace the **DATBASE_PATH** variable in the  **SQLiteHelper.java** class, to a path you want
to store the Database. It has a Default Value which can be changed. A Database file is included in the Git Repo
named as **database.db**.

9. Now get a CSV file or use the **Sample.csv** from the repository or ZIP folder you downloaded

10. Call the methods as shown below. 

```
class abc{
  .
  .
  .
  
  private static void main(String[] args){
   object.setFilePath("C:\\Users\\Nishant Garg\\Downloads\\Sample.csv");
  }

}
```
* **Note:** The parameter under the * **setFilePath** method is an example. It should be replaced with the actual file Path

11. Now when you run the application, the code will run and you will see a bunch of Outputs like this

```
run:
Connection to SQLite has been established.
The driver name is SQLite JDBC
A new database has been created.
CREATE TABLE contentTable( Column1 TEXT, Column2 TEXT, Column3 TEXT, Column4 TEXT);
INSERT INTO contentTable VALUES ( "GroupName", "Groupcode ", "GroupOwner", "GroupCategoryID " );
Opened database successfully
Records created successfully
INSERT INTO contentTable VALUES ( "System Administrators", "sysadmin", "13456", "100" );
Opened database successfully
Records created successfully
INSERT INTO contentTable VALUES ( "Independence High Teachers", "HS Teachers", "123", "101" );
Opened database successfully
Records created successfully
INSERT INTO contentTable VALUES ( "John Glenn Middle Teachers", "MS Teachers", "13458", "102" );
Opened database successfully
Records created successfully

BUILD SUCCESSFUL (total time: 3 seconds)

```
All these calls are actually feed-backs when executing the programs. These are from **Sample.csv** File.

## Understanding the API
The CVS API uses a bunch of methods and Variables to Parse the data from the correct format and stores it in the Database. The backend Server being used is **SQLite** Database which is a complete lcal server, requires no installation. Just download, set the Enviromental Varaible and you will be able to perform SQL Queries.

As CVS files does not have some kind of standered format, Applications uses different Approaches. In this API, and during it devopment process we are **assuming** few Things. These are:
 
 ### Assumptions
 
 1. The programmer which is going to use this API has no IDEA about the number of Coloumns, schema of the table, Data Types of the Columns.
 
 2. It is the responsibility of the API to assume the Columns, their Datatypes, Create Table, Insert Values into them and Manage them
 
 3. A valid CSV file for this API will not have any kind of null values. The number of values in a Row will be constant throughout the file.
 
 4. Each row will end with CRLF or End of Line Character or simple "\n" character. Each Value in a Row will be seperated by commas only
 
 5. The API itself is not responsible to show the contents in the Database. The programmer must use different way to check. No SELECT queries will run on API
 
 6. The API can only perform selected SQL Queries. One for creating Database, then Create Table and last one will be to Insert Values into the Database.

## Built With

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - JAVA SE
* [SQLite](https://www.sqlite.org/download.html)- Back End Database

## Version

Latest Version: 1
## Authors

* [**Nishant Garg**](https://github.com/GargNishant)


## Acknowledgments

* Official Documentation 
