import javafx.application.Platform;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class RecordSelector {

    private ListView contactRecords = new ListView();
    private TitledPane contactRecordsPane = new TitledPane("Select a Name", contactRecords);

    private Button newBtn = new Button("New");
    private Button viewBtn = new Button("View");
    private Button deleteBtn = new Button("Delete");
    private Button editBtn = new Button("Edit");
    private Button importBtn = new Button("Import");
    private Button exportBtn = new Button("Export");
    private Button exitBtn = new Button("Exit");
    private HBox selectAction = new HBox( newBtn, viewBtn, deleteBtn, editBtn, importBtn, exportBtn, exitBtn );
    private TitledPane selectActionPane = new TitledPane("Select an Action", selectAction);

    VBox root = new VBox( contactRecordsPane, selectActionPane );

    RecordSelector(){

        selectActionPane.setCollapsible( false );
        contactRecordsPane.setCollapsible( false );

        disableButtons( true );
        contactRecords.setOnMouseClicked( actionEvent -> {
            if( !contactRecords.getSelectionModel().isEmpty() ){
                disableButtons( false );
            }
        });

        updateContactRecords();

        setButtonActions();
        setButtonIcons();
    }

    /**
     * This function refreshes the contactRecords ListView with items from the database
     */
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
     * This function initializes the logic for all of the buttons
     */
    private void setButtonActions(){

        newBtn.setOnAction( actionEvent -> new ContactRecord( ContactRecord.Option.NEW, 0, this ));

        /* TODO Change function to show contact record in same screen */
        viewBtn.setOnAction( actionEvent ->  new ContactRecord( ContactRecord.Option.READ, getSelectedPK(), this ) );

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
     * Sets the Icons for all buttons
     */
    private void setButtonIcons(){
        newBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/new.png"))));
        viewBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/view.png"))));
        deleteBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/delete.png"))));
        editBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/edit.png"))));
        importBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/import.png"))));
        exportBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/export.png"))));
        exitBtn.setGraphic( new ImageView( new Image( getClass().getResourceAsStream("png/exit.png"))));
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
