import javafx.application.Platform;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class RecordSelector {

    private ListView contactRecords = new ListView();
    private TitledPane contactRecordsPane = new TitledPane("Select a Name", contactRecords);

    private Button newBtn = new Button("New");
    private Button viewBtn = new Button("View");
    private Button deleteBtn = new Button("Delete");
    private Button editBtn = new Button("Edit");
    private Button importBtn = new Button("Import");
    private Button exitBtn = new Button("Exit");
    private HBox selectAction = new HBox( newBtn, viewBtn, deleteBtn, editBtn, importBtn, exitBtn );
    private TitledPane selectActionPane = new TitledPane("Select an Action", selectAction);

    VBox root = new VBox( contactRecordsPane, selectActionPane );

    RecordSelector(){

        disableButtons( true );

        selectActionPane.setCollapsible( false );
        contactRecordsPane.setCollapsible( false );

        updateContactRecords();

        contactRecords.setOnMouseClicked( actionEvent -> {
            if( contactRecords.getSelectionModel().isEmpty() == false ){
                disableButtons( false );
            }
        });

        newBtn.setOnAction( actionEvent -> new ContactRecord( ContactRecord.Option.NEW, 0 ));

        viewBtn.setOnAction( actionEvent ->  new ContactRecord( ContactRecord.Option.READ, getSelectedPK() ) );

        deleteBtn.setOnAction( actionEvent -> {
            try{
                Connection conn = ContactApp.openDB();
                conn.createStatement().executeUpdate( "DELETE FROM contacts WHERE userID=\'" + getSelectedPK() + "\';" );
                conn.close();

            } catch( Exception ex ){ ex.printStackTrace(); }

            updateContactRecords();
        });

        editBtn.setOnAction( actionEvent -> new ContactRecord( ContactRecord.Option.WRITE, getSelectedPK() ) );

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

        exitBtn.setOnAction( actionEvent -> Platform.exit());
    }

    /**
     * Runs a query on the database to determine the userID of the currently selected contactRecords item based
     * on the firstName and lastName
     * @return integer of the selected item's userID
     */
    private int getSelectedPK(){

        int selectedPK = 0;

        try {
            String selectedItem = contactRecords.getSelectionModel().getSelectedItem().toString();
            int substringIndex = selectedItem.indexOf(' ');

            Connection conn = ContactApp.openDB();

            String query = "SELECT userID FROM contacts WHERE firstName=\'" +
                    selectedItem.substring(0, substringIndex) + "\' AND lastName=\'" +
                    selectedItem.substring( substringIndex + 1 ) + "\';";
            debug( query );

            ResultSet result = conn.createStatement().executeQuery( query );

            result.next();
            selectedPK = result.getInt("userID");
            debug("Selected item database PK: " + selectedPK );

            conn.close();

        } catch( Exception ex ){ ex.printStackTrace(); }

        return selectedPK;
    }

    /**
     * Disables or enables the buttons that should not be enabled when there is no item selected in the contactRecords
     * list
     * @param option true to disable, false to enable
     */
    private void disableButtons( boolean option ){
        viewBtn.setDisable( option );
        deleteBtn.setDisable( option );
        editBtn.setDisable( option );
    }

    public void updateContactRecords(){
        try{
            Connection conn = ContactApp.openDB();

            ObservableList<String> currentEntries = FXCollections.observableArrayList();
            ResultSet result = conn.createStatement().executeQuery( "SELECT * FROM contacts" );

            while( result.next() ){
                currentEntries.add( String.valueOf( result.getString("firstName") + " " + result.getString("lastName") ) );
            }

            contactRecords.setItems( currentEntries );

            conn.close();

        } catch( Exception ex ){ ex.printStackTrace(); }
    }

    /**
     * shortcut for System.out.println()
     * @param message String that will be printed to console
     */
    private void debug( String message ){ System.out.println( message ); }

    public VBox getRoot(){ return root; }
}
