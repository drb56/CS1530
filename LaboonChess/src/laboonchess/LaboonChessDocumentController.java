package laboonchess;

import java.io.IOException;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Joe Meszar (jwm54@pitt.edu)
 */
public class LaboonChessDocumentController implements Initializable {
    
    @FXML private Label lblTestPiece;
    @FXML private Label lblStatus;
    @FXML private Label lblTimer;
    @FXML private GridPane chessboard;
    private int timer_count = 0;
    Timeline gameTimer = null;
    
    @FXML
    private void handleAboutAction(ActionEvent event) throws IOException {
        lblStatus.setText("About menu item clicked");

        // pause the game timer
        if (gameTimer != null) { gameTimer.pause(); }

        Stage aboutDialog;
        aboutDialog = new Stage();
        aboutDialog.setScene(new Scene(FXMLLoader.load(getClass().getResource("AboutDialog.fxml"))));
        aboutDialog.setTitle("About LaboonChess");
        aboutDialog.initModality(Modality.APPLICATION_MODAL);
        aboutDialog.initStyle(StageStyle.UTILITY);
        aboutDialog.showAndWait();

        // resume the game timer
        if (gameTimer != null) { gameTimer.play(); }
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
        
        // start or reset the game timer
        timer_count = 0;
        if (gameTimer == null) { // start
            this.gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event2) -> {
                lblTimer.setText(String.format("%d:%02d",
                    TimeUnit.SECONDS.toMinutes(++timer_count),
                    TimeUnit.SECONDS.toSeconds(timer_count) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timer_count))
                ));
            }));

            gameTimer.setCycleCount(Timeline.INDEFINITE);
            gameTimer.play();   
        } else { // reset
            timer_count = 0;
        }
    }
    
    @FXML
    private void handleSaveGameAction(ActionEvent event) {
        lblStatus.setText("Save Game clicked");
    }
    
    @FXML
    private void handleUndoMoveAction(ActionEvent event) {
        lblStatus.setText("Undo Move menu item clicked");
    }
    
    @FXML void handleChessboardClickAction(MouseEvent event) {
        // get the source of the click
        //Object source = event.getSource();
        ImageView source = (ImageView)event.getSource();
        Pane parent = (Pane)source.getParent();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // get the list of nodes for the chessboard
        ObservableList<Node> children = chessboard.getChildren();
        Node spot = getNodeByRowColumnIndex(0, 2, chessboard);
        Pane mypane = (Pane)getNodeByRowColumnIndex(0, 0, chessboard);
        //mypane.getChildren().add()
        
        
        //Scene myScene = (Scene) chessboard.getScene();
        //Stage myStage = (Stage) chessboard.getScene().getWindow();
        
        
        
//        myStage.focusedProperty().addListener(new ChangeListener<Boolean>()
//        {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
//            {
//                if (newPropertyValue)
//                {
//                    System.out.println("Textfield on focus");
//                }
//                else
//                {
//                    System.out.println("Textfield out focus");
//                }
//            }
//        });
        
    }
    
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
