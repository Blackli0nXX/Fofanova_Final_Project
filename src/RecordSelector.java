/**
 * CIS2337 Final Project
 * RecordSelector
 * Purpose: Form in which user can view, delete, import, and export contacts; Also has buttons to open new forms
 * to add new contacts or edit existing contacts
 *
 * @author Trevor Touchet, Dmitriy Karpunov
 * @version 1.0 22 November, 2017
 */

import javafx.application.Platform;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Pair;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

/**
 * Contains the elements required to make a functioning contact selection interface
 */
class RecordSelector {

    private ListView contactRecords = new ListView(); // ListView that will hold contact names
    private TitledPane contactRecordsPane = new TitledPane("Select a Name", contactRecords); // Pane to hold the contactRecords ListView

    private Button newBtn = new Button("New"); // Button that will create new contact
    private Button deleteBtn = new Button("Delete"); // Button that will delete selected contact
    private Button editBtn = new Button("Edit"); // Button that will edit selected contact
    private Button importBtn = new Button("Import"); // Button that will import contacts from CSV file
    private Button exportBtn = new Button("Export"); // Button that will export contacts to CSV file
    private Button exitBtn = new Button("Exit"); // Button that will exit the application
    private HBox selectActionBox = new HBox( newBtn, deleteBtn, editBtn, importBtn, exportBtn, exitBtn ); // HBox that will hold all buttons
    private TitledPane selectActionPane = new TitledPane( "Select an Action", selectActionBox ); // TitledPane that will hold HBox with buttons

    private Label viewOutputLbl = new Label(""); // Label that will display selected contact details

    VBox root = new VBox( contactRecordsPane, selectActionPane, viewOutputLbl ); // VBox that will hold top level GUI components

    /**
     * Constructor that will be run when the application starts
     * Performs startup functions
     */
    RecordSelector(){

        // Set the TitledPanes to not be collapsible
        selectActionPane.setCollapsible( false );
        contactRecordsPane.setCollapsible( false );

        // Initially disable edit and delete buttons
        disableButtons( true );

        // Set contactRecords to show contact information when contact is selected as well as enable buttons
        setContactRecordsAction();

        // Populate the contactRecords ListView with contacts from the database
        updateContactRecords();

        // Set the logic for each button
        setButtonActions();
        // Set the icon for each button
        setButtonIcons();
        // Set the tooltips for each button
        setButtonTooltips();
    }

    /**
     * Sets the contactRecords ListView to enable buttons when a contact is selected and show selected contact's information
     */
    private void setContactRecordsAction(){
        // Set logic when an item in contactRecords is selected
        contactRecords.setOnMouseClicked( actionEvent -> {
            // Execute if a contact is selected
            if( !contactRecords.getSelectionModel().isEmpty() ) {
                // Enable the delete and edit buttons
                disableButtons(false);

                try {
                    // Open a connection to the database and query all columns about the selected user
                    ResultSet result = ContactApp.openDB().createStatement().
                            executeQuery("SELECT * FROM contacts WHERE userID=\'" + getSelectedPK() + "\';");
                    // Move the cursor to the first field
                    result.next();

                    // Set the viewOutputLbl to show all columns about the selected user
                    viewOutputLbl.setText("Name:\t\t\t" + result.getString("firstName") + " " +
                            result.getString("lastName") + "\nEmail:\t\t\t" + result.getString("email") +
                            "\nPhone Number:\t" + result.getString("phoneNumber") + "\nAddress:\t\t\t" +
                            result.getString("address") + "\nBirthday:\t\t\t" + result.getString("birthday") +
                            "\nNotes:\t\t\t" + result.getString("notes"));

                } catch (Exception ex) { ex.printStackTrace(); }
            }
            // disable edit and delete buttons if an item isn't selected
            else disableButtons( true );
        });
    }

    /**
     * This function refreshes the contactRecords ListView with items from the database
     */
    public void updateContactRecords(){
        try{
            // Create an ObservableList to hold the entries from the database
            ObservableList<String> currentEntries = FXCollections.observableArrayList();

            // Query the database for all contacts
            ResultSet result = ContactApp.openDB().createStatement().executeQuery( "SELECT * FROM contacts" );

            // Iterate through each entry
            while( result.next() ){
                // Add the PK, first name, and last name as an entry to the ObservableList
                currentEntries.add( String.valueOf( result.getString("userID") + " " +
                        result.getString("firstName") + " " + result.getString("lastName") ) );
            }
            // Set the contactRecords ListView to have all contacts from the database
            contactRecords.setItems( currentEntries );

        } catch( Exception ex ){ ex.printStackTrace(); }
    }

    /**
     * Converts the ID of the selected contact to an integer, which will be the primary key in the database
     * @return integer of the selected item's userID
     */
    private int getSelectedPK(){

        // Convert the selected entry to a String
        String selectedItem = contactRecords.getSelectionModel().getSelectedItem().toString();

        // Get the index of the first space in the selected entry
        int substringIndex = selectedItem.indexOf(' ');

        // Convert the ID of the selected entry to an Integer
        int selectedPK = Integer.valueOf(selectedItem.substring(0, substringIndex));

        // return the selectedPK integer
        return selectedPK;
    }

    /**
     * This function initializes the logic for all of the buttons
     */
    private void setButtonActions(){

        newBtn.setOnAction( actionEvent -> new ContactRecord( ContactRecord.Option.NEW, 0, this ));

        deleteBtn.setOnAction( actionEvent -> {
            try{
                Connection conn = ContactApp.openDB();
                conn.createStatement().executeUpdate( "DELETE FROM contacts WHERE userID=\'" + getSelectedPK() + "\';" );
                conn.close();

            } catch( Exception ex ){ ex.printStackTrace(); }

            updateContactRecords();
        });

        editBtn.setOnAction( actionEvent -> new ContactRecord( ContactRecord.Option.WRITE, getSelectedPK(), this ) );

        importBtn.setOnAction( actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("CSV Selector");
                File csvFile = fileChooser.showOpenDialog(new Stage());

                Scanner csvScanner = new Scanner( csvFile );
                csvScanner.useDelimiter("(,|\n)");

                while( csvScanner.hasNext() ){
                    Connection conn = ContactApp.openDB();
                    String query = "INSERT INTO contacts (firstName, lastName, email, phoneNumber, address, birthday, notes) VALUES (\'" +
                            csvScanner.next() + "\', \'" + csvScanner.next() + "\', \'" + csvScanner.next() + "\', \'" + csvScanner.next() + "\', \'" +
                            csvScanner.next() + "\', \'" + csvScanner.next() + "\', \'" + csvScanner.next() + "\');";
                    debug( query );
                    conn.createStatement().executeUpdate( query );
                    conn.close();
                }
            } catch( Exception ex ){ ex.printStackTrace(); }

            updateContactRecords();
        });

        exportBtn.setOnAction( actionEvent -> {
           try{
               DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
               LocalDateTime now = LocalDateTime.now();

               FileChooser fileChooser = new FileChooser();
               fileChooser.setInitialFileName("contacts-" + dtf.format(now) );
               fileChooser.setTitle("CSV Exporter");
               File csvFile = fileChooser.showSaveDialog( new Stage() );
               PrintWriter csvWriter = new PrintWriter( csvFile );

               String query = "SELECT * FROM contacts ORDER BY lastName";
               ResultSet result = ContactApp.openDB().createStatement().executeQuery( query );

               while( result.next() ){
                   csvWriter.print( result.getString("firstName") + ", " + result.getString("lastName") + ", " +
                           result.getString("email") + ", " + result.getString("phoneNumber") + ", " +
                           result.getString("address") + ", " + result.getString("birthday") + ", " +
                           result.getString("notes") + "\n" );
               }

               csvWriter.close();

           } catch( Exception ex ){ ex.printStackTrace(); }
        });

        exitBtn.setOnAction( actionEvent -> Platform.exit());
    }

    /**
     * Sets the Icons for all Buttons
     */
    private void setButtonIcons(){
        newBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/new.png"))));
        deleteBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/delete.png"))));
        editBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/edit.png"))));
        importBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/import.png"))));
        exportBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/export.png"))));
        exitBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/exit.png"))));
    }

    /**
     * Sets Tooltips for all Buttons
     */
    private void setButtonTooltips(){
        newBtn.setTooltip( new Tooltip("Opens a form to add a new contact"));
        deleteBtn.setTooltip( new Tooltip("Deletes selected contact from the database"));
        editBtn.setTooltip( new Tooltip("Opens a form to edit the contents of the selected contact"));
        importBtn.setTooltip( new Tooltip("Opens a window to select a CSV file to import into the database"));
        exportBtn.setTooltip( new Tooltip("Opens a window to select a file to export the contents of the database to"));
        exitBtn.setTooltip( new Tooltip("Exits the application"));
    }

    /**
     * Disables or enables the buttons that should not be enabled when there is no item selected in the contactRecords
     * list
     * @param option true to disable, false to enable
     */
    private void disableButtons( boolean option ){
        deleteBtn.setDisable( option );
        editBtn.setDisable( option );
    }

    /**
     * This method is a shortcut for System.out.println(), and is used to quickly create debug messages
     * @param message String that will be printed to console
     */
    private void debug( String message ){ System.out.println( message ); }

    /**
     * This method is called by the main application to get the root VBox object
     * @return Returns the root VBox object
     */
    public VBox getRoot(){ return root; }
}
