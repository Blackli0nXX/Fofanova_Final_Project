import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class ContactApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Instructor's Record");
        primaryStage.setScene(new Scene( new RecordSelector().getRoot(), 300, 275 ) );
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
}
