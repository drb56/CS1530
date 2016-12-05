import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LaboonChess extends Application {
    private Parent root;

    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/fxml/LaboonChessDocument.fxml"));

        Scene scene = new Scene(root);

        // set custom app icon
        stage.getIcons().add(new Image(LaboonChess.class.getResourceAsStream("/images/icon.png")));

        // set custom title
        stage.setTitle("Laboon Chess");

        // load the custom CSS file for the chessboard styling
        scene.getStylesheets().add(getClass().getResource("/css/chessboard.css").toExternalForm());
        
        // set the scene AKA the form
        stage.setScene(scene);
        
        // make the window not resizable
        stage.setResizable(false);

        // show the scene/stage AKA the form/window
        stage.show();


    }

    public Parent getRoot() {
        return root;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
