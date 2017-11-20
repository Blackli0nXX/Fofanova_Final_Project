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

        newBtn.setOnAction( actionEvent -> {
            new ContactRecord();
        });

        exitBtn.setOnAction( actionEvent -> {
            Platform.exit();
        });

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://tstouchet17f.heyuhnem.com:3306/tstouche_contacts", "tstouche_contact", "OhMF?cJO!@}1");


        }
        catch( Exception ex ){
            ex.printStackTrace();
        }

        /*
        ObservableList<Contact> data = FXCollections.observableArrayList();

        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();

            String sqlStatement = "SELECT Description, ProdNum, Price FROM Coffee";

            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                System.out.println( result.getString("Description") );

                data.add( new Contact( result.getString("Description"),
                        result.getString("ProdNum"),
                        result.getString("Price")) );
            }
            conn.close();
        }
        catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        */

    }

    public VBox getRoot(){ return root; }

}
