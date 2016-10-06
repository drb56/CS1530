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

    /* FXML references JavaFX GUI objects */
    @FXML private MenuBar mnuMain;          /* menu bar at the top of the application */
    @FXML private Label lblStatus;          /* TEMP, used as verbose output for testing */
    @FXML private Label lblTimer;           /* bottom-right, used to display the timer */
    @FXML private GridPane chessboard;      /* reference to the chessboard */

    private int timer_count = 0;            /* used for game clock, as the counter */
    Timeline gameTimer = null;              /* used for game clock, counting up from the time game was started */
    private boolean isFirstClick = true;    /* determines if this is to be considered the "first" or "second" chess board click */
    private ImageView chessPiece = null;    /* holds first chess piece clicked on */
    private Pane chessSquare = null;        /* holds square from which first chess piece was clicked */
    private String san = null;          /* holds standard algebraic notation of first square and second square */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

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
        Pane curSquare = (Pane) event.getSource();                          // get the source chess square that was clicked

        /*
            If this is the "first click", then we need to get
                the chess piece here and wait for a second click.

            If this is the "second click", then we need to place
                the previously-obtained chess piece here (if possible).
        */
        if (isFirstClick) {
            if (curSquare.getChildren().isEmpty()) {
                /* DO NOTHING: no chess piece here */
                isFirstClick = true;

            } else {
                /* FIRST-CLICK: set up for the second click */
                chessSquare = curSquare;                                    // hold reference to this square
                chessPiece = (ImageView) curSquare.getChildren().get(0);    // hold reference to this piece
                chessPiece.setOpacity(.6);                                  // set opacity to make it look "selected"
                isFirstClick = false;                                       // now wait for second-click
            }
        } else {
            /* SECOND-CLICK */

            // see if we can place the chess piece from the
            //      first click at this square on the board.
            if (curSquare.getChildren().isEmpty()) {
                /* EMPTY SQUARE */
                curSquare.getChildren().add(0, chessPiece);             // place the chess piece here
                san = chessSquare.getId() + curSquare.getId();      // get the move in terms of FEN (e.g. e3d6)
                System.out.println(san);
            } else if ((curSquare.getChildren().get(0).getId().contains("white") && chessPiece.getId().contains("black"))
                    || (curSquare.getChildren().get(0).getId().contains("black") && chessPiece.getId().contains("white"))) {
                /* OPPONENT PIECE */

                san = chessSquare.getId() + curSquare.getId();  // get the move in terms of FEN (e.g. e3d6)
                System.out.println(san);
                curSquare.getChildren().remove(0);                  // remove the current chess piece
                curSquare.getChildren().add(0, chessPiece);         // insert the first-click piece onto this square

                // logic to update the 2D array
            }

            // finished with second-click
            isFirstClick = true;            // back to start
            chessPiece.setOpacity(1);       // opacity set back to show finished
        }
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

    public static String sanToFenstring(String coordinates){
        return coordinates;
    }
    public static boolean isLegalMove(String san){

        return true;
    }
}
