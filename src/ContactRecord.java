import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContactRecord {

    ContactRecord(){

        Stage stage = new Stage();

        VBox box = new VBox();
        box.setPadding(new Insets(10));

        box.setAlignment(Pos.CENTER);

        Scene scene = new Scene(box, 250, 150);
        stage.setScene(scene);
        stage.show();
    }
}
