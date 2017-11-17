import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;

public class RecordSelector {

    private Button newBtn = new Button("New");
    private Button viewBtn = new Button("View");
    private Button deleteBtn = new Button("Delete");
    private Button editBtn = new Button("Edit");
    private Button exitBtn = new Button("Exit");
    private HBox selectAction = new HBox( newBtn, viewBtn, deleteBtn, editBtn, exitBtn );
    private TitledPane selectActionPane = new TitledPane("Test", selectAction);

    VBox root = new VBox( selectActionPane );

    RecordSelector(){

        selectActionPane.setCollapsible( false );
    }

    public VBox getRoot(){ return root; }

}
