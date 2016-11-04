import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.RotateTransition;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import entities.ChessBoard;
import services.stockfish.Stockfish;

public class LaboonChessDocumentController implements Initializable {

    /* FXML references JavaFX GUI objects */
    @FXML private MenuBar mnuMain;          /* menu bar at the top of the application */
    @FXML private Label lblStatus;          /* TEMP, used as verbose output for testing */
    @FXML private Label lblTimer;           /* bottom-right, used to display the timer */
    @FXML private GridPane guiChessboard;    /* holds the GUI chessboard */

    private int timer_count = 0;            /* used for game clock, as the counter */
    Timeline gameTimer = null;              /* used for game clock, counting up from the time game was started */
    private boolean isFirstClick = true;    /* determines if this is to be considered the "first" or "second" chess board click */
    private ImageView guiChessPiece = null; /* holds first chess piece clicked on */
    private Pane guiChessSquare = null;     /* holds square from which first chess piece was clicked */
    private String san = null;              /* holds standard algebraic notation of first square and second square */
    private Stockfish stockfish;            /* chess API engine */
    private ChessBoard chessboard;          /* chessboard object model used to properly manipulate the GUI */


    /**
     * First-running method that builds the objects and dependencies needed to run the program. Here,
     *      the ChessBoard object is initialized and the Stockfish binary is loaded.
     *
     * TODO: Determine how to make Stockfish run in all environments (WIN, MAC, LINUX)
     *
     * @param url The location of the default fxml document for the program.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            chessboard = new ChessBoard();              // create a new chessboard instance
            System.out.println(chessboard.toFEN());     // DEBUG

            /* Start the Stockfish binary */
            stockfish = new Stockfish();
            if (stockfish.startEngine()) {
                System.out.println("Stockfish engine started!");
            } else {
                throw new RuntimeException("Could not start Stockfish engine...");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Opens the "About" modal dialog window.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleAboutAction(ActionEvent event) throws IOException {
        lblStatus.setText("About menu item clicked");           // DEBUG

        // pause the game timer (if it has been started)
        if (gameTimer != null) { gameTimer.pause(); }

        /* Determine the screen location to place the "About" dialog window */
        double x, y, length, width;
        x = mnuMain.getScene().getWindow().getX();
        y = mnuMain.getScene().getWindow().getY();
        length = mnuMain.getScene().getWindow().getHeight();
        width = mnuMain.getScene().getWindow().getWidth();

        /* Build the "About" modal dialog window */
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


    /**
     * Gracefully exits the program.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleExitAction(ActionEvent event) {
        Platform.exit();
    }


    /**
     * Opens a dialog to choose a saved-game in Portable Game Notation (PGN) format.
     *
     * TODO: Write the entire logic needed to Load an already-saved chess game in PGN format.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleLoadGameAction(ActionEvent event) {
        lblStatus.setText("Load Game clicked");                 // DEBUG
    }


    /**
     * Resets the chessboard to the starting layout, and also resets the timer.
     *
     * TODO: Reset the chess pieces on the GUI and also reset the ChessBoard object.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleNewGameAction(ActionEvent event) {
        lblStatus.setText("New game menu item clicked");        // DEBUG

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


    /**
     * Opens a dialog to save the current game in Portable Game Notation (PGN) format.
     *
     * TODO: Write the entire logic needed to Save the chess game in PGN format.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleSaveGameAction(ActionEvent event) {
        lblStatus.setText("Save Game clicked");                 // DEBUG
    }


    /**
     * Using a log of FEN moves, will undo the last move and revert
     *      the chessboard back to its previous state.
     *
     * TODO: Write the entire logic needed to Undo a chess move.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleUndoMoveAction(ActionEvent event) {
        lblStatus.setText("Undo Move menu item clicked");       // DEBUG
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
     *              - Determine if this second click location is a VALID chess move
     *                  (based off the "First Click" and what type of chess piece)
     *              - If valid, move the chess piece from the "First Click" location
     *                  to the "Second Click" location on the chess board.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML void handleChessboardClickAction(MouseEvent event) {
        Pane curSquare = (Pane) event.getSource();                          // get the source chess square that was clicked

        if (isFirstClick) {
            /* FIRST-CLICK */

            if (curSquare.getChildren().isEmpty()) {
                /* DO NOTHING: no chess piece here */
                isFirstClick = true;                                        // reset

            } else if ((chessboard.turn() == 'w' && ((ImageView)curSquare.getChildren().get(0)).getId().matches("[a-z]"))
                    || (chessboard.turn() == 'b' && ((ImageView)curSquare.getChildren().get(0)).getId().matches("[A-Z]"))) {
                /* DO NOTHING: user clicked on the opposing team's chess piece */
                isFirstClick = true;

            } else {
                /* FIRST-CLICK: set up for the second click */
                guiChessSquare = curSquare;                                 // hold reference to this square
                guiChessPiece = (ImageView) curSquare.getChildren().get(0); // hold reference to this piece
                guiChessPiece.setOpacity(.5);                               // set opacity to make it look "selected"
                isFirstClick = false;                                       // now wait for second-click
            }
        } else {
            /* SECOND-CLICK */
            String fromSquare = guiChessSquare.getId();                     // get the "first-click" chess square
            String toSquare = curSquare.getId();                            // get the "second-click" chess square

            if (guiChessSquare.equals(curSquare)) {
                /* USER CLICKED ON THEIR CURRENTLY HIGHLIGHTED PIECE */

                guiChessPiece.setOpacity(1);                // Unhighlight the currently highlighted piece
                isFirstClick = true;                        // back to start

            } else if (chessboard.move(fromSquare, toSquare)) {
                /*
                    See if we can place the chess piece from the
                    first click at this square on the board.
                */
                if (curSquare.getChildren().isEmpty()) {
                    /* EMPTY SQUARE HERE */

                    curSquare.getChildren().add(0, guiChessPiece);          // place the chess piece here
                    san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

                } else if (curSquare.getChildren().get(0).getId().matches("[a-z]")
                        != guiChessPiece.getId().matches("[a-z]")) {        // make sure to not overtake your own team's piece
                    /* OPPONENT PIECE EXISTS HERE */

                    san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

                    curSquare.getChildren().remove(0);                      // remove the current chess piece
                    curSquare.getChildren().add(0, guiChessPiece);          // insert the first-click piece onto this square
                }

                // finished with second-click
                isFirstClick = true;                        // back to start (wait for a "first-click" again)
                guiChessPiece.setOpacity(1);                // opacity set back to show finished

                System.out.println(chessboard.toFEN());     // DEBUG
            }
        }
    }


    /**
     * Handles logic for when the Flip Board menu item is clicked. When this occurs, the
     * chessboard is visually "flipped" around to show the black chess pieces on the bottom and
     * the white chess pieces on top (or vice versa).
     *
     * This actually occurs using JavaFX's rotation property built into the GridPane node
     * (aka the chessboard) and the Pane nodes (aka the chess squares). The rotation is set at
     * 180 degrees to simulate the flipping.
     *
     * The coordinates (Panes) that border the chessboard are also looked at and their
     * CSS properties are flipped to correspond with the flipping of the board.
     *
     * @param event The user action event that was used to trigger this method. Contains the Flip Board object.
     */
    @FXML void handleFlipBoardAction(ActionEvent event) {
        // rotate the chessboard
        guiChessboard.rotateProperty().setValue((guiChessboard.rotateProperty().getValue() + 180) % 360);

        // rotate the pieces in the chessboard
        for (Node square : guiChessboard.getChildren()) {
            // rotate
            square.rotateProperty().setValue((square.rotateProperty().getValue() + 180) % 360);

            // if this is a coordinate on the outside of the chessboard,
            //      then flip the border around to keep the border correct
            //      for the chessboard
            String id = square.getId();
            if (square.rotateProperty().getValue() == 0) {
                // default layout
                if (id != null && id.matches("[A-Z0-9]")) {
                    square.getStyleClass().remove("flipped");
                }
            } else {
                // flipped
                if (id != null && id.matches("[A-Z0-9]")) {
                    square.getStyleClass().add("flipped");
                }
            }
        }
    }
}
