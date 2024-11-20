package com.example.aurobaahmad_assignment4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;

/**
 * This class contains methods for the application to function.
 *
 * @author Auroba Ahmad
 */
public class MovieController {
    //FXML annotations to add elements defined in FXML file
    @FXML
    private TableView tableview;

    @FXML
    private TableColumn<Movie,String> titleColumn;

    @FXML
    private TableColumn<Movie,Integer> yearColumn;

    @FXML
    private TableColumn<Movie,Double> salesColumn;

    @FXML
    private Label status;

    @FXML
    private TextField titleField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField salesField;

    /**
     * Creates a table and updates the status.
     */
    @FXML
    protected void onCreateTableClick() {
       ObservableList<Movie> info = tableview.getItems();
       //clear the tableview
       info.clear();
       createDBandTable();
       status.setText("Movie table created");
    }

    /**
     * Allows user to choose a JSON file and displays the data.
     */
    @FXML
    protected void onImportJSONClick() {
        //create FileChooser and set title to open
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        //get and set directory
        File f = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(f);
        //filter for only JSON Files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            //insert data to the database
            insertJSONDataToDB(file);
            ObservableList<Movie> info = tableview.getItems();
            //clear the tableview
            info.clear();
            //display data on tableview
            showDataFromDB();
            tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            //update the status
            status.setText("Import data from " + file.getPath());
        }
    }

    /**
     * Allows user to select a JSON File to write to.
     */
    @FXML
    protected void onExportJSONClick() {
        //create FileChooser and set title to save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        //get and set directory
        File f = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(f);
        //filter for only JSON Files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //write to file
            try (FileWriter writer = new FileWriter(file)) {
                ObservableList<Movie> info = tableview.getItems();
                gson.toJson(info, writer);
                //update status
                status.setText("Export data to " + file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Closes the application.
     */
    @FXML
    protected void onExitClick() {
        Platform.exit();
    }

    /**
     * Displays Alert Dialog.
     */
    @FXML
    protected void onAboutClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Movie Database");
        alert.setHeaderText("Name and Integrity Statement");
        alert.setContentText("Auroba Ahmad" + "\n\n" + "I certify that this submission is my original work.");
        status.setText("Integrity Statement Displayed");
        alert.showAndWait();
    }

    /**
     * Displays data from movie table on tableview.
     */
    @FXML
    protected void onListRecordsClick() {
        ObservableList<Movie> info = tableview.getItems();
        //clear tableview
        info.clear();
        //display data and adjust columns
        showDataFromDB();
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //update status
        status.setText("Movie table displayed");
    }

    /**
     * Adds record to database and validates user data.
     */
    @FXML
    protected void onAddRecordClick() {
        String dbFilePath = ".//MovieDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        Connection conn;
        //if connection to db fails it throws an exception
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //get user input
        String title = titleField.getText();
        String year = yearField.getText();
        String sales = salesField.getText();
        //validate the input
        String validateTitle = Validation.validateTitle(title);
        String validateYear = Validation.validateYear(year);
        String validateSales = Validation.validateSales(sales);
        //if the input is valid add to database and display on tableview
        if((validateTitle == "") && (validateYear == "") && (validateSales == "")) {
            int yearAsInt = Integer.valueOf(year);
            double salesAsDouble = Double.valueOf(sales);
            insertDataToDB(conn, title, yearAsInt, salesAsDouble);
            //clear text fields
            titleField.clear();
            yearField.clear();
            salesField.clear();
            //update status
            status.setText("A movie has been inserted: " + title);
            System.out.println("Movie Inserted: " + title + ", " + year + ", " + sales);
            ObservableList<Movie> info = tableview.getItems();
            info.clear();
            showDataFromDB();
            tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        //if the input is not valid show alert dialog with validation information
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input");
            alert.setContentText(validateTitle + "\n" + validateYear + "\n" + validateSales);
            alert.showAndWait();
        }
    }

    /**
     * Deletes selected record from database.
     */
    @FXML
    protected void onDeleteRecordClick() {
        String dbFilePath = ".//MovieDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        Connection conn;
        //if connection to db fails it throws an exception
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //get the selected record
        Object selected = tableview.getSelectionModel().getSelectedItem();
        if(selected != null) {
            Movie movieSelected = (Movie) selected;
            //delete record
            deleteRowFromDB(conn, movieSelected.Title, movieSelected.Year, movieSelected.Sales);
            //clear record from tableview
            tableview.getItems().remove(selected);
            System.out.println("Movie Deleted: " + movieSelected.toString());
            //update status
            status.setText("A movie has been deleted: " + movieSelected.Title);
        }
    }

    /**
     * This is the first method called after the application loads.
     */
    public void initialize() {
        //set tableView columns to show data from Movie
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<Movie,String>("Title"));
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<Movie,Integer>("Year"));
        salesColumn.setCellValueFactory(
                new PropertyValueFactory<Movie,Double>("Sales"));
    }

    /**
     * This method creates the database if it does not already exist and creates a table called Movie.
     */
    public void createDBandTable() {
        //database file path and URL
        String dbFilePath = ".//MovieDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        File dbFile = new File(dbFilePath);
        //if database file does not exist create a file and table
        if (!dbFile.exists()) {
            //create file using file path
            try (Database db =
                         DatabaseBuilder.create(Database.FileFormat.V2010, new File(dbFilePath))) {
                System.out.println("The database file has been created.");
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        }
            //drop table Movie
            try {
                Connection conn = DriverManager.getConnection(databaseURL);
                String sql;
                sql = "DROP TABLE Movie";
                Statement createTableStatement = conn.createStatement();
                createTableStatement.execute(sql);
                conn.commit();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            //create table Movie with columns Title, Year, and Sales
            try {
                Connection conn = DriverManager.getConnection(databaseURL);
                String sql;
                sql = "CREATE TABLE Movie (Title nvarchar(255), Year int, Sales double)";
                Statement createTableStatement = conn.createStatement();
                createTableStatement.execute(sql);
                conn.commit();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

    }

    /**
     * This method inserts the data into the table.
     * @param conn variable for connection
     * @param title variable to hold the title
     * @param year variable to hold the year
     * @param sales variable to hold the sale
     */
    public void insertDataToDB(Connection conn, String title, int year, double sales) {
        String sql = "INSERT INTO Movie (title, year, sales) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, year);
            preparedStatement.setDouble(3, sales);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method deletes any existing rows in the table.
     * @param conn variable for connection
     */
    public void deleteAllRowsFromDB(Connection conn){
        String sql = "DELETE FROM Movie";
        PreparedStatement preparedStatement = null;
        //if connection to db fails it throws an exception
        try {
            preparedStatement = conn.prepareStatement(sql);
            int rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method deletes a row from the table.
     * @param conn variable for connection
     * @param title variable for title
     * @param year variable for year
     * @param sales variable for sale
     */
    public void deleteRowFromDB(Connection conn, String title, int year, double sales) {
        String sql = "DELETE FROM Movie WHERE title = ? AND year = ? AND sales = ?";
        PreparedStatement preparedStatement = null;
        //if connection to db fails it throws an exception
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, year);
            preparedStatement.setDouble(3, sales);
            int rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This displays the data onto the tableview from the database.
     */
    public void showDataFromDB(){
        String dbFilePath = ".//MovieDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        Connection conn;
        //if connection to db fails it throws an exception
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            String tableName = "Movie";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);
            //goes through the rows of the table and retrieves the information
            while (result.next()) {
                String title = result.getString("Title");
                int year = result.getInt("Year");
                double sales = result.getDouble("Sales");
                //display the information on the tableview
                ObservableList<Movie> info = tableview.getItems();
                Movie m = new Movie(title, year, sales);
                info.add(m);
            }
        } catch (SQLException except) {
            except.printStackTrace();
        }
    }

    /**
     * This method adds the JSON data to the table in the database.
     * @param file variable holds file that was passed in.
     */
    public void insertJSONDataToDB(File file) {
        String dbFilePath = ".//MovieDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        Connection conn;
        //if connection to db fails it throws an exception
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //calls the method to delete existing rows in the db
        deleteAllRowsFromDB(conn);
        //builds gson
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileReader fr;
        //if file is not found it throws an exception
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Movie[] m = gson.fromJson(fr,Movie[].class);
        //go through movie data
        for(Movie info : m){
            String title = info.getTitle();
            int year = info.getYear();
            double sales = info.getSales();
            //insert data to database
            insertDataToDB(conn, title, year, sales);
        }
    }
}