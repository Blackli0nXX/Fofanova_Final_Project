import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

/**
 * CIS2337 Final Project
 * ContactApp.java
 * Purpose: Entry point for the Contact Manager Application
 *
 * @author Trevor Touchet, Dmitriy Karpunov
 * @version 1.0 22 November, 2017
 */

/**
 * This class contains the main function to start the application as well as preliminary startup functions
 * and helper functions
 */
public class ContactApp extends Application {

    /**
     * Executes immediately after the main function and creates a Stage on which a RecordSelector form is shown
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Instructor's Record");
        primaryStage.setScene(new Scene( new RecordSelector().getRoot(), 625, 625 ) );
        primaryStage.show();
    }

    /**
     * First executed function, and launches the GUI
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns a Connection object pointing to the online database.
     * Here as a shortcut to be reused by other classes/functions.
     * @return Returns a Connection object pointing to the online database.
     */
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
