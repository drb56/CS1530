import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
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
    
    @FXML private MenuBar mnuMain;
    @FXML private Label lblTestPiece;
    @FXML private Label lblStatus;
    @FXML private Label lblTimer;
    @FXML private GridPane chessboard;
    private int timer_count = 0;
    Timeline gameTimer = null;
    private boolean isFirstClick = true;
    private ImageView chessPiece = null;
    
    @FXML
    private void handleAboutAction(ActionEvent event) throws IOException {
        lblStatus.setText("About menu item clicked");

        // pause the game timer
        if (gameTimer != null) { gameTimer.pause(); }
        double x, y, length, width;
        x = mnuMain.getScene().getWindow().getX();
        y = mnuMain.getScene().getWindow().getY();
        length = mnuMain.getScene().getWindow().getHeight();
        width = mnuMain.getScene().getWindow().getWidth();
        
        Stage aboutDialog;
        aboutDialog = new Stage();
        aboutDialog.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/AboutDialog.fxml"))));
        aboutDialog.setTitle("About LaboonChess");
        aboutDialog.initModality(Modality.APPLICATION_MODAL);
        aboutDialog.initStyle(StageStyle.UTILITY);
        aboutDialog.setX(mnuMain.getScene().getWindow().getX() + (mnuMain.getScene().getWindow().getHeight()/9));
        aboutDialog.setY(mnuMain.getScene().getWindow().getY() + (mnuMain.getScene().getWindow().getWidth()/3));
        aboutDialog.setResizable(false);
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
        // get the source chess square that was clicked
        Pane curSquare = (Pane) event.getSource();

        // if this is the "first click", then we need to get
        //      the chess piece here and wait for a second click.
        //
        // if this is the "second click", then we need to place
        //      the previously-obtained chess piece here (if possible).
        if (isFirstClick) {
            if (curSquare.getChildren().isEmpty()) {
                // do nothing. No chess piece here.
                isFirstClick = true;

            } else {
                // get the chess piece contained in this square
                chessPiece = (ImageView) curSquare.getChildren().get(0);
                isFirstClick = false;
            }
        } else {
            // second click. see if we can place the chess piece
            //      at this location.
            if (curSquare.getChildren().isEmpty()) {
                // good to go
                curSquare.getChildren().add(0, chessPiece);

            } else if ((curSquare.getChildren().get(0).getId().contains("white") && chessPiece.getId().contains("black"))
                    || (curSquare.getChildren().get(0).getId().contains("black") && chessPiece.getId().contains("white"))) {
                // logic to update the array or whatever we have. Show that
                // the piece on this square has been taken out of play.
                String removed = curSquare.getChildren().get(0).getId();

                // remove the current chess piece
                curSquare.getChildren().remove(0);

                // replace the piece on this square
                curSquare.getChildren().add(0, chessPiece);
            }

            // done with second-click
            isFirstClick = true;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // get the list of nodes for the chessboard
        ObservableList<Node> children = chessboard.getChildren();
        Node spot = getNodeByRowColumnIndex(0, 2, chessboard);
        Pane mypane = (Pane)getNodeByRowColumnIndex(0, 0, chessboard);
        //mypane.getChildren().add()
        
        
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
    
    public Pane getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Pane result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = (Pane) node;
                break;
            }
        }

        return result;
    }
}
