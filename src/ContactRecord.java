/**
 * CIS2348 Final Project
 * ContactRecord.java
 * Purpose: Form which will allow user to edit an existing contact or create a new one and save the changes
 * to the online database
 *
 * @author Trevor Touchet, Dmitriy Karpunov
 * @version 1.0 22 November, 2017
 */

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;

class ContactRecord {

    private Label firstNameLbl = new Label("First Name"); // Label for first name TextField
    private TextField firstNameTxt = new TextField(); // TextField to input first name
    private HBox firstNameBox = new HBox( firstNameLbl, firstNameTxt ); // HBox to hold Label and TextField

    private Label lastNameLbl = new Label("Last Name"); // Label for last name TextField
    private TextField lastNameTxt = new TextField(); // TextField to input last name
    private HBox lastNameBox = new HBox( lastNameLbl, lastNameTxt ); // HBox to hold Label and TextField

    private Label emailLbl = new Label("Email"); // Label for email TextField
    private TextField emailTxt = new TextField(); // TextField to input email
    private HBox emailBox = new HBox( emailLbl, emailTxt ); // HBox to hold Label and TextField

    private Label phoneNumberLbl = new Label("Phone Number"); // Label for phone number TextField
    private TextField phoneNumberTxt = new TextField(); // TextField to input phone number
    private HBox phoneNumberBox = new HBox( phoneNumberLbl, phoneNumberTxt ); // HBox to hold Label and TextField

    private Label addressLbl = new Label("Address"); // Label for address TextField
    private TextField addressTxt = new TextField(); // TextField to input address
    private HBox addressBox = new HBox( addressLbl, addressTxt ); // HBox to hold Label and TextField

    private Label birthdayLbl = new Label("Birthday"); // Label for birthday TextField
    private TextField birthdayTxt = new TextField(); // TextField to input birthday
    private HBox birthdayBox = new HBox( birthdayLbl, birthdayTxt ); // HBox to hold Label and TextField

    private Label notesLbl = new Label("Notes"); // Label for notes TextField
    private TextField notesTxt = new TextField(); // TextField to input notes
    private HBox notesBox = new HBox( notesLbl, notesTxt ); // HBox to hold Label and TextField

    private Button cancelBtn = new Button("Cancel"); // Button to cancel the operation
    private Button saveBtn = new Button("Save"); //  Button to save the entry
    private HBox btnBox = new HBox( cancelBtn, saveBtn ); // Hbox to hold Buttons

    private VBox root = new VBox( firstNameBox, lastNameBox, emailBox, phoneNumberBox, addressBox, birthdayBox,
            notesBox, btnBox ); // VBox to hold all HBoxes

    // Enum that will hold the requested operation option
    // NEW will open a blank form, WRITE will open a form with data from the selected entry
    public enum Option{
        READ,
        WRITE,
        NEW
    }

    /**
     * Constructor for the ContactRecord form that sets preliminary data and shows the form
     * @param option Designated operation this the spawned form will perform
     * @param primaryKey Primary Key of the selected contact in the database
     * @param parent RecordSelector object that this form was spawned from
     */
    ContactRecord( Option option, int primaryKey, RecordSelector parent ){

        // Set padding and alignment settings for the root VBox
        root.setPadding(new Insets(10, 50, 50, 50));
        root.setAlignment(Pos.CENTER);

        // Create a new scene with the root VBox and stage with the created scene and show the stage
        Scene scene = new Scene( root, 625, 625);
        Stage stage = new Stage();
        stage.setTitle("Contact Information");
        stage.setScene(scene);
        stage.show();

        // Populate the fields with information from the selected contact if the READ or WRITE option was passed
        if( option == Option.READ || option == Option.WRITE ){

            // Disable the fields if READ was passed to emulate read-only capability
            if( option == Option.READ){ disableFields( true ); }

            try{
                // Open a connection to the database
                Connection conn = ContactApp.openDB();
                // Create a String query to get the selected contact
                String query = "SELECT * FROM contacts WHERE userID=\'" + primaryKey + "\';";
                // Execute the query and save the results in a ResultSet
                ResultSet result = conn.createStatement().executeQuery( query );
                result.next();

                // Set each TextField to the corresponding field in the selected entry
                firstNameTxt.setText( result.getString("firstName") );
                lastNameTxt.setText( result.getString("lastName") );
                emailTxt.setText( result.getString("email") );
                phoneNumberTxt.setText( result.getString("phoneNumber") );
                addressTxt.setText( result.getString("address") );
                birthdayTxt.setText( result.getString("birthday") );
                notesTxt.setText( result.getString("notes") );

                // Close the connection
                conn.close();

            } catch( Exception ex ){ ex.printStackTrace(); }
        }

        // Set all button logic
        setButtonLogic( stage, option, primaryKey, parent );
    }

    /**
     * Sets the button logic
     * @param stage stage that the form is being displayed on
     * @param option operation option that was passed to the form
     * @param primaryKey primary key of the selected contact
     * @param parent RecordSelector object that spawned this form
     */
    private void setButtonLogic( Stage stage, Option option, int primaryKey, RecordSelector parent ){

        // Set cancelBtn Button to close the form
        cancelBtn.setOnAction( actionEvent -> stage.close() );

        // Set saveBtn Button to act based on the passed in option
        saveBtn.setOnAction( actionEvent -> {
            // If the option was READ, just close the stage, since the data couldn't have been changed
            if( option == Option.READ ){ stage.close(); }

            // If the option was WRITE, update the selected record with data from each TextField
            if( option == Option.WRITE ){
                try {
                    // Open a connection with the database
                    Connection conn = ContactApp.openDB();

                    // Create a String query to update the record with data from each TextField
                    String query = "UPDATE contacts SET firstName=\'" + firstNameTxt.getText() + "\', lastName=\'" +
                            lastNameTxt.getText() + "\', email=\'" + emailTxt.getText() + "\', phoneNumber=\'" +
                            phoneNumberTxt.getText() + "\', address=\'" + addressTxt.getText() + "\', birthday=\'" +
                            birthdayTxt.getText() + "\', notes=\'" + notesTxt.getText() + "\' WHERE userID=" +
                            primaryKey;

                    // Execute the query to update the database
                    conn.createStatement().executeUpdate( query );

                    // Close the connection
                    conn.close();
                    // Close the form
                    stage.close();

                } catch( Exception ex ){ ex.printStackTrace(); }
            }
            // If the option was NEW, create a new record with data from the TextFields
            if( option == Option.NEW ){
                try {
                    // Open a connection with the database
                    Connection conn = ContactApp.openDB();
                    // Execute a statement inserts a new record into the database with data from the TextFields
                    conn.createStatement().executeUpdate("INSERT INTO contacts (firstName, lastName, email, phoneNumber, address, birthday, notes) VALUES (\'" +
                            firstNameTxt.getText() + "\', \'" + lastNameTxt.getText() + "\', \'" + emailTxt.getText() + "\', \'" + phoneNumberTxt.getText() + "\', \'" +
                            addressTxt.getText() + "\', \'" + birthdayTxt.getText() + "\', \'" + notesTxt.getText() + "\');" );

                    // Close the connection
                    conn.close();
                    // Close the stage
                    stage.close();
                }
                catch( Exception ex ){ ex.printStackTrace(); }
            }
            // Update the parents RecordSelector object to reflect the changes
            parent.updateContactRecords();
        });
    }

    /**
     * Set's all TextFields to be writable or only readable based on option parameter
     * @param option true disables, false enables
     */
    private void disableFields( boolean option ){
        firstNameTxt.setDisable( option );
        lastNameTxt.setDisable( option );
        emailTxt.setDisable( option );
        phoneNumberTxt.setDisable( option );
        addressTxt.setDisable( option );
        birthdayTxt.setDisable( option );
        notesTxt.setDisable( option );
    }
}
