import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import stockfish.Stockfish;

/**
 *
 * @author Joe Meszar (jwm54@pitt.edu)
 */
public class LaboonChess extends Application {
    @FXML Label lblTimer = null;
    long timer_count = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LaboonChessDocument.fxml"));
        
        Scene scene = new Scene(root);

        Stockfish stockfish = new Stockfish();
        if (stockfish.startEngine()) {
            System.out.println("Engine has started..");
        } else {
            System.out.println("Oops! Something went wrong..");
        }
        
        // set custom app icon
        stage.getIcons().add(new Image(LaboonChess.class.getResourceAsStream("/images/icon.png")));
        
        // set custom title
        stage.setTitle("Laboon Chess");
        
        // import custom chess font
        // usage: -fx-font-family: PIXymbolsChessW95-Regular
        //Font.loadFont(LaboonChess.class.getResource("/font/chess.ttf").toExternalForm(), 10);
        
        // set the scene AKA the form
        stage.setScene(scene);
        
        // make the window not resizable
        stage.setResizable(false);

        // show the scene/stage AKA the form/window
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static String getTranslate(String coordinates){
        return coordinates;
    }
}