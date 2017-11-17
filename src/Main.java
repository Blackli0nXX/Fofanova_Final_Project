import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Instructor's Record");
        primaryStage.setScene(new Scene( new RecordSelector().getRoot(), 300, 275 ) );
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
