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
import services.AlgebraicNotationConversion;
import services.FENStringConversion;
import stockfish.Stockfish;

public class LaboonChessDocumentController implements Initializable {

    /* FXML references JavaFX GUI objects */
    @FXML private MenuBar mnuMain;          /* menu bar at the top of the application */
    @FXML private Label lblStatus;          /* TEMP, used as verbose output for testing */
    @FXML private Label lblTimer;           /* bottom-right, used to display the timer */
    @FXML private GridPane guiChessboard;   /* reference to the guiChessboard */

    private char[][] chessboard;            /* 2D array reference of the chessboard (white=PNBRQK) (black=pnbrqk) */
    private int timer_count = 0;            /* used for game clock, as the counter */
    Timeline gameTimer = null;              /* used for game clock, counting up from the time game was started */
    private boolean isFirstClick = true;    /* determines if this is to be considered the "first" or "second" chess board click */
    private ImageView guiChessPiece = null; /* holds first chess piece clicked on */
    private Pane guiChessSquare = null;     /* holds square from which first chess piece was clicked */
    private String san = null;              /* holds standard algebraic notation of first square and second square */
    private Stockfish stockfish;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* start the Stockfish binary */
        stockfish = new Stockfish();
        if (stockfish.startEngine()) {
            System.out.println("Engine has started..");
        } else {
            System.out.println("Oops! I did it again..");
        }

        /* build the chessboard */
        chessboard = new char[][]{
                { 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r' },
                { 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p' },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' },
                { 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R' }
        };

        System.out.println(FENStringConversion.chessboardToFEN(chessboard));
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

            // see if we can place the chess piece from the
            //      first click at this square on the board.
            if (curSquare.getChildren().isEmpty()) {
                /* EMPTY SQUARE */

                curSquare.getChildren().add(0, guiChessPiece);          // place the chess piece here
                san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

                update2DArrayChessboard(guiChessSquare.getId(), curSquare.getId());     // keep 2D chessboard array updated
            } else if (curSquare.getChildren().get(0).getId().matches("[a-z]")
                    != guiChessPiece.getId().matches("[a-z]")) {

                /* OPPONENT PIECE EXISTS HERE */
                String fromSquare = guiChessSquare.getId();
                String toSquare = curSquare.getId();
                san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

                //if (stockfish.isLegalMove()) {
                if (true) {
                    curSquare.getChildren().remove(0);                  // remove the current chess piece
                    curSquare.getChildren().add(0, guiChessPiece);      // insert the first-click piece onto this square

                    update2DArrayChessboard(fromSquare, toSquare);      // keep 2D chessboard array updated
                }
            }

            // finished with second-click
            isFirstClick = true;                                        // back to start
            guiChessPiece.setOpacity(1);        // opacity set back to show finished
            System.out.println(FENStringConversion.chessboardToFEN(chessboard));
        }
    }

    /**
     * Takes two SAN coordinates and moves the chess piece from the start square to
     *      the end square within the 2D chessboard array. Because of the way a chessboard
     *      is "called", things get tricky due to [0][0] actually being referenced
     *      as [a][8] in USCF terms.
     *
     * @param sanFromSquare The ID or SAN of the starting chess square. (e.g. 'a1')
     * @param sanToSquare The ID or SAN of the ending chess square. (e.g. 'd6')
     */
    private void update2DArrayChessboard(final String sanFromSquare, final String sanToSquare) {
        /*
            Transform the SAN strings into 2D array coordinates, being careful due
                to the USCF column layouts and 2D array column layouts are reversed.
         */
        int fromRow = sanTo2DRow(sanFromSquare);
        int fromCol = sanTo2DCol(sanFromSquare);
        int toRow = sanTo2DRow(sanToSquare);
        int toCol = sanTo2DCol(sanToSquare);

        chessboard[toRow][toCol] = chessboard[fromRow][fromCol];        // move piece from start to end square
        chessboard[fromRow][fromCol] = 0;                               // the start square should now be empty
    }

    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array ROW coordinate (e.g. '0'). Subtracted from (8) to
     *      give the proper USCF layout.
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square ROW within the 2D chessboard array. (e.g. '7')
     */
    private int sanTo2DRow (final String sanSquare) {
        return (8 - ((int) sanSquare.charAt(1)) % 48);    // ASCII char (48) is '0'
    }

    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array COLUMN coordinate (e.g. '0').
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square COLUMN within the 2D chessboard array. (e.g. '0')
     */
    private int sanTo2DCol (final String sanSquare) {
        return ((int) sanSquare.toLowerCase().charAt(0)) % 97;     // ASCII chart (97) is 'a'
    }

    /**
     * Given a SAN coordinate (e.g. 'a4'), will return the contents of the proper
     *      2D array location (chess square).
     *
     * @param sanSquare The chessboard square, in SAN, to get the 2D array coordinates of. (e.g. 'a1')
     * @return The contents of the given chess square; '0' if nothing exists on that square.
     */
    private char get2DArrayChessboardPiece(final String sanSquare) {

        return chessboard[sanTo2DRow(sanSquare)][sanTo2DCol(sanSquare)];
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


//    public String chessboardToFEN(char[][] chessboard){
//        char[] letterLocs = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
//        char[] numberLocs = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
//        String currBoard = "";
//
//        for(int i=0; i<chessboard.length; i++){
//            for(int j=0; j<chessboard.length; j++){
//                if(chessboard[i][j] != 0){
//                    currBoard = currBoard + chessboard[i][j] + letterLocs[j] + numberLocs[i] + " ";
//                }
//            }
//        }
//        return currBoard;
//    }
}
