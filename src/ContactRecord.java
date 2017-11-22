import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;

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

    ContactRecord( Option option, int primaryKey ){

        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene( root, 250, 150);
        Stage stage = new Stage();
        stage.setTitle("Contact Information");
        stage.setScene(scene);
        stage.show();

        if( option == Option.READ || option == Option.WRITE ){

            if( option == Option.READ){ disableFields( true ); }

            try{

                Connection conn = ContactApp.openDB();

                String query = "SELECT * FROM contacts WHERE userID=\'" + primaryKey + "\';";

                ResultSet result = conn.createStatement().executeQuery( query );
                result.next();

                firstNameTxt.setText( result.getString("firstName") );
                lastNameTxt.setText( result.getString("lastName") );
                emailTxt.setText( result.getString("email") );
                phoneNumberTxt.setText( result.getString("email") );
                addressTxt.setText( result.getString("address") );
                birthdayTxt.setText( result.getString("birthday") );
                notesTxt.setText( result.getString("notes") );

            } catch( Exception ex ){ ex.printStackTrace(); }
        }

        cancelBtn.setOnAction( actionEvent -> stage.close() );

        saveBtn.setOnAction( actionEvent -> {
            if( option == Option.NEW ){

                try {
                    Connection conn = ContactApp.openDB();
                    conn.createStatement().executeUpdate("INSERT INTO contacts (firstName, lastName, email, phoneNumber, address, birthday, notes) VALUES (\'" +
                            firstNameTxt.getText() + "\', \'" + lastNameTxt.getText() + "\', \'" + emailTxt.getText() + "\', \'" + phoneNumberTxt.getText() + "\', \'" +
                            addressTxt.getText() + "\', \'" + birthdayTxt.getText() + "\', \'" + notesTxt.getText() + "\');" );

                    conn.close();

                    stage.close();
                }
                catch( Exception ex ){ ex.printStackTrace(); }

            }
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
