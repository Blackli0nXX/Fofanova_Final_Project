import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ContactRecord {

    private Label firstNameLbl = new Label("First Name");
    private TextField firstNameTxt = new TextField();
    private HBox firstNameBox = new HBox( firstNameLbl, firstNameTxt );

    private Label lastNameLbl = new Label("Last Name");
    private TextField lastNameTxt = new TextField();
    private HBox lastNameBox = new HBox( lastNameLbl, lastNameTxt );

    private Button cancelBtn = new Button("Cancel");
    private Button saveBtn = new Button("Save");
    private HBox btnBox = new HBox( cancelBtn, saveBtn );

    private VBox root = new VBox( firstNameBox, lastNameBox, btnBox );

    ContactRecord(){

        Stage stage = new Stage();

        stage.setTitle("Contact Information");

        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene( root, 250, 150);
        stage.setScene(scene);
        stage.show();
    }
}
