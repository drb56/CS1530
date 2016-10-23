import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import entities.ChessBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import org.codehaus.groovy.runtime.powerassert.SourceText;
import services.AlgebraicNotationConversion;
import stockfish.Stockfish;
import entities.ChessBoard;

public class LaboonChessDocumentController implements Initializable {

    /* FXML references JavaFX GUI objects */
    @FXML private MenuBar mnuMain;          /* menu bar at the top of the application */
    @FXML private Label lblStatus;          /* TEMP, used as verbose output for testing */
    @FXML private Label lblTimer;           /* bottom-right, used to display the timer */
    @FXML private GridPane guiChessboard;   /* reference to the guiChessboard */

//    private char[][] chessboard;            /* 2D array reference of the chessboard (white=PNBRQK) (black=pnbrqk) */
    private int timer_count = 0;            /* used for game clock, as the counter */
    Timeline gameTimer = null;              /* used for game clock, counting up from the time game was started */
    private boolean isFirstClick = true;    /* determines if this is to be considered the "first" or "second" chess board click */
    private ImageView guiChessPiece = null; /* holds first chess piece clicked on */
    private Pane guiChessSquare = null;     /* holds square from which first chess piece was clicked */
    private String san = null;              /* holds standard algebraic notation of first square and second square */
    private Stockfish stockfish;
    private ChessBoard chessboard;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* start the Stockfish binary */
        stockfish = new Stockfish();
        if (stockfish.startEngine()) {
            System.out.println("Engine has started..");
        } else {
            System.out.println("Oops! I did it again..");
        }

        chessboard = new ChessBoard();
        /* build the chessboard */
//        chessboard = new char[][]{
//                { 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r' },
//                { 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p' },
//                { 0, 0, 0, 0, 0, 0, 0, 0 },
//                { 0, 0, 0, 0, 0, 0, 0, 0 },
//                { 0, 0, 0, 0, 0, 0, 0, 0 },
//                { 0, 0, 0, 0, 0, 0, 0, 0 },
//                { 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' },
//                { 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R' }
//        };

        System.out.println(chessboard.toFEN());
        System.out.println(AlgebraicNotationConversion.getTranslate(0,1));
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

    /**
     *  Handles the logic for when a user "plays" on the GUI chessboard. All "plays" are done by
     *      using mouseclick events. Each chessboard square (Pane) on the GUI is tied to this
     *      method to use for its mouseclick event.
     *
     *      Whenever a mouseclick occurs, there will be two distinct types:
     *          1. "First Click"
     *              - See if a chess piece exists on this square.
     *              - If no piece, then do nothing. Wait for "First Click" again.
     *              - If a piece exists, store it and wait for "Second Click".
     *
     *          2. "Second Click"
     *              - Determine if this second click location is a VALID chess move.
     *                  - Based off the "First Click" and what type of chess piece.
     *              - If valid, move the chess piece from the "First Click".
     *                  location to the "Second Click" location on the chess board.
     *
     * @param event The mouseclick event. Holds the GUI object that was clicked.
     */
    @FXML void handleChessboardClickAction(MouseEvent event) {
        Pane curSquare = (Pane) event.getSource();                              // get the source chess square that was clicked

        /*
            If this is the "first click", then we need to get
                the chess piece here and wait for a second click.

            If this is the "second click", then we need to place
                the previously-obtained chess piece here (if possible).
        */

        if (isFirstClick) {
            if (curSquare.getChildren().isEmpty()) {
                /* DO NOTHING: no chess piece here */
                isFirstClick = true;                                            // reset

            } else {
                /* FIRST-CLICK: set up for the second click */
                guiChessSquare = curSquare;                                     // hold reference to this square
                guiChessPiece = (ImageView) curSquare.getChildren().get(0);     // hold reference to this piece
                guiChessPiece.setOpacity(.6);                                   // set opacity to make it look "selected"
                isFirstClick = false;                                           // now wait for second-click
            }
        } else {
            /* SECOND-CLICK */
            String fromSquare = guiChessSquare.getId();
            String toSquare = curSquare.getId();
            if (chessboard.move(fromSquare, toSquare)) {
                // see if we can place the chess piece from the
                //      first click at this square on the board.
                if (curSquare.getChildren().isEmpty()) {
                /* EMPTY SQUARE */

                curSquare.getChildren().add(0, guiChessPiece);          // place the chess piece here
                san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)
            } else if (curSquare.getChildren().get(0).getId().matches("[a-z]")
                    != guiChessPiece.getId().matches("[a-z]")) {
                /* OPPONENT PIECE EXISTS HERE */

                    san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

                    curSquare.getChildren().remove(0);                  // remove the current chess piece
                    curSquare.getChildren().add(0, guiChessPiece);      // insert the first-click piece onto this square
                }
            }
            // finished with second-click
            isFirstClick = true;                                        // back to start
            guiChessPiece.setOpacity(1);        // opacity set back to show finished



            System.out.println(chessboard.toFEN());
        }
    }
//    public Pane getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
//        Pane result = null;
//        ObservableList<Node> children = gridPane.getChildren();
//
//        for (Node node : children) {
//            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
//                result = (Pane) node;
//                break;
//            }
//        }
//
//        return result;
//    }

    public static String sanToFenstring(String coordinates){
        return coordinates;
    }
//    public static boolean isLegalMove(String san){
//        if(stockfish.isLegalMove(san)){
//            return true;
//        }
//        else{
//            return false;
//        }
//
//    }

}
