package laboonchess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Joe Meszar (jwm54@pitt.edu)
 */
public class LaboonChessDocumentController implements Initializable {
    
    @FXML private Label lblStatus;
    @FXML private Label lblTimer;
    @FXML private GridPane chessboard;
    private int timer_count = 0;
    
    @FXML
    private void handleAboutAction(ActionEvent event) throws IOException {
        lblStatus.setText("About menu item clicked");
        
        Stage aboutDialog; 

        aboutDialog = new Stage();
        aboutDialog.setScene(new Scene(FXMLLoader.load(getClass().getResource("AboutDialog.fxml"))));
        aboutDialog.setTitle("About LaboonChess");
        aboutDialog.initModality(Modality.APPLICATION_MODAL);
        aboutDialog.initStyle(StageStyle.UTILITY);
        aboutDialog.showAndWait();
    }
    
    @FXML
    private void handleExitAction(ActionEvent event) {
        Platform.exit();
    }
    
    @FXML
    private void handleLoadGameAction(ActionEvent event) {
        lblStatus.setText("Load Game clicked");
    }
    
    @FXML
    private void handleNewGameAction(ActionEvent event) {
        lblStatus.setText("New game menu item clicked");
        timer_count = 0;
    }
    
    @FXML
    private void handleSaveGameAction(ActionEvent event) {
        lblStatus.setText("Save Game clicked");
    }
    
    @FXML
    private void handleUndoMoveAction(ActionEvent event) {
        lblStatus.setText("Undo Move menu item clicked");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // get the list of nodes for the chessboard
        //ObservableList<Node> children = chessboard.getChildren();
        
        // start the timer for the game.
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            lblTimer.setText(String.format("%d", ++timer_count));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }       
}
