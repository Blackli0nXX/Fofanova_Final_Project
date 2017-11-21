import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

public class ContactRecord {

    private Label firstNameLbl = new Label("First Name");
    private TextField firstNameTxt = new TextField();
    private HBox firstNameBox = new HBox( firstNameLbl, firstNameTxt );

    private Label lastNameLbl = new Label("Last Name");
    private TextField lastNameTxt = new TextField();
    private HBox lastNameBox = new HBox( lastNameLbl, lastNameTxt );

    private Label emailLbl = new Label("Email");
    private TextField emailTxt = new TextField();
    private HBox emailBox = new HBox( emailLbl, emailTxt );

    private Label phoneNumberLbl = new Label("Phone Number");
    private TextField phoneNumberTxt = new TextField();
    private HBox phoneNumberBox = new HBox( phoneNumberLbl, phoneNumberTxt );

    private Label addressLbl = new Label("Address");
    private TextField addressTxt = new TextField();
    private HBox addressBox = new HBox( addressLbl, addressTxt );

    private Label birthdayLbl = new Label("Birthday");
    private TextField birthdayTxt = new TextField();
    private HBox birthdayBox = new HBox( birthdayLbl, birthdayTxt );

    private Label notesLbl = new Label("Notes");
    private TextField notesTxt = new TextField();
    private HBox notesBox = new HBox( notesLbl, notesTxt );

    private Button cancelBtn = new Button("Cancel");
    private Button saveBtn = new Button("Save");
    private HBox btnBox = new HBox( cancelBtn, saveBtn );

    private VBox root = new VBox( firstNameBox, lastNameBox, emailBox, phoneNumberBox, addressBox, birthdayBox,
            notesBox, btnBox );

    public enum Option{
        READ,
        WRITE,
        NEW
    }

    ContactRecord( Option option, Contact newContact ){

        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        cancelBtn.setOnAction( actionEvent -> {
            Platform.exit();
        });

        saveBtn.setOnAction( actionEvent -> {
            if( option == Option.NEW ){

                try {
                    Connection conn = RecordSelector.openDB();
                    conn.createStatement().executeQuery("");
                }
                catch( Exception ex ){ ex.printStackTrace(); }

            }
        });

        Scene scene = new Scene( root, 250, 150);
        Stage stage = new Stage();
        stage.setTitle("Contact Information");
        stage.setScene(scene);
        stage.show();
    }
}
