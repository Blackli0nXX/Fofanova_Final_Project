import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.*;

public class RecordSelector {

    private ListView contactRecords = new ListView();
    private TitledPane contactRecordsPane = new TitledPane("Select a Name", contactRecords);

    private Button newBtn = new Button("New");
    private Button viewBtn = new Button("View");
    private Button deleteBtn = new Button("Delete");
    private Button editBtn = new Button("Edit");
    private Button exitBtn = new Button("Exit");
    private HBox selectAction = new HBox( newBtn, viewBtn, deleteBtn, editBtn, exitBtn );
    private TitledPane selectActionPane = new TitledPane("Select an Action", selectAction);

    VBox root = new VBox( contactRecordsPane, selectActionPane );

    RecordSelector(){

        selectActionPane.setCollapsible( false );
        contactRecordsPane.setCollapsible( false );

        try{
            Connection conn = openDB();

            ObservableList<String> currentEntries = FXCollections.observableArrayList();
            ResultSet result = conn.createStatement().executeQuery( "SELECT * FROM contacts" );

            while( result.next() ){
                currentEntries.add( String.valueOf( result.getString("firstName") + " " + result.getString("lastName") ) );
            }

            contactRecords.setItems( currentEntries );

            conn.close();
        }
        catch( Exception ex ){ ex.printStackTrace(); }

        newBtn.setOnAction( actionEvent -> {
            new ContactRecord( ContactRecord.Option.NEW, new Contact() );
        });

        exitBtn.setOnAction( actionEvent -> {
            Platform.exit();
        });

    }

    public static Connection openDB(){

        Connection conn = null;

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://tstouchet17f.heyuhnem.com:3306/tstouche_contacts", "tstouche_contact", "OhMF?cJO!@}1");
        }
        catch( Exception ex ){ ex.printStackTrace(); }

        return conn;
    }

    public VBox getRoot(){ return root; }
}
