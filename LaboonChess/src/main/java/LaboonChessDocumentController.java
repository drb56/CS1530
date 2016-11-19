import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import entities.TimerAndMessages;
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
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.*;
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
    private int playerType = 0;             /* determines whether player is white or black */
    private int difficulty = 0;             /* AI Difficulty (0=Easy, 10=Medium, 20=hard) */

    /**
     * First-running method that builds the objects and dependencies needed to run the program. Here,
     *      the ChessBoard object is initialized and the Stockfish binary is loaded.
     *
     * TODO: Determine how to make Stockfish run in all environments (WIN, MAC, LINUX)
     *
     * @param url The location of the default fxml document for the program.
     * @param rb n/a
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
                // send commands manually

            } else {
                throw new RuntimeException("Could not start Stockfish engine...");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Move the chess piece using stock fish
     *
     * @param fen fen string
     * @param timeWait time for waiting, longer = more difficult
     */
    public void moveStockFish(String fen, int timeWait) {
        stockfish.sendCommand("");

        String difficultyCommand = "setoption name Skill Level value "+difficulty;
        stockfish.sendCommand(difficultyCommand);

        System.out.println(stockfish.getOutput(0));
        String move = stockfish.getBestMove(fen, timeWait);
        String fromSquareStr = move.substring(0, 2);
        String toSquareStr = move.substring(2, 4);

        System.out.println("AI moving from: " + fromSquareStr);
        System.out.println("To: " + toSquareStr);
        if (chessboard.move(fromSquareStr, toSquareStr)) {

            Pane fromSquare = getChessSquare(fromSquareStr);
            Pane toSquare = getChessSquare(toSquareStr);

            // Empty square
            guiChessPiece = (ImageView) fromSquare.getChildren().get(0); // hold reference to this piece
            fromSquare.getChildren().remove(0);                          // remove the chess piece in original square

            if (!toSquare.getChildren().isEmpty()) {
                toSquare.getChildren().remove(0);
            }

            toSquare.getChildren().add(0, guiChessPiece);                // place the chess piece here

        }
        chessboard.addToHistory(chessboard.toFEN());
    }

    /**
     *  Gets the square given algebraic coordinate from fxml board
     *
     *  @param coordinate Algebraic coordinate string that correlates with the pane
     */
    public Pane getChessSquare(String coordinate) {
        return (Pane) guiChessboard.getScene().lookup("#"+coordinate);
    }

    /**
     * Changes the color of the chess pieces to the option selected.
     *
     * @param event The color option selected.
     */
    @FXML
    private void handleColorChangeAction(ActionEvent event) throws IOException {
        // get the chosen color
        RadioMenuItem item = (RadioMenuItem) event.getSource();
        String color1 = "", color2 = "";

        switch (item.getId()) {
            case "deadpool":
                color1 = "#040603"; // black
                color2 = "#a5090c"; // firebrick
                break;
            case "election":
                color1 = "#49a2ce"; // steelblue
                color2 = "#ed4e31"; // tomato
                break;
            case "hulk":
                color1 = "#5b4862"; // purple
                color2 = "#70964b"; // green
                break;
            case "ironman":
                color1 = "#dc1405"; // red
                color2 = "#ffa700"; // yellow
                break;
            case "pitt":
                color1 = "#1c2957"; // blue
                color2 = "#cdb87d"; // gold
                break;
            case "wolverine":
                color1 = "#365382"; // saddlebrown
                color2 = "#f2c903"; // yellow
                break;
        }

        // loop through the chess board and change each piece's color
        ObservableList<Node> children = guiChessboard.getChildren();
        for (Node node : children) {
            Pane square = (Pane) node;

            if (!square.getChildren().isEmpty()) {
                Node nodeChild = square.getChildren().get(0);

                if (nodeChild instanceof ImageView) {
                    if (nodeChild.getId().matches("[a-z]")) {
                        // change "black" pieces
                        ImageView piece = (ImageView) nodeChild;
                        piece.setEffect(getPaintColor(piece, Color.web(color1)));
                    } else {
                        // change "white" pieces
                        ImageView piece = (ImageView) nodeChild;
                        piece.setEffect(getPaintColor(piece, Color.web(color2)));
                    }
                }
            }
        }
    }

    /**
     * Takes a chess piece and a given color and changes that chess piece to be
     *      that color by using a JavaFX Blend effect.
     *
     * @param piece The chess piece to change the color of.
     * @param color The color to change the chess piece.
     * @return The new Blended color to apply to the chess piece.
     */
    public Blend getPaintColor(ImageView piece, Color color) {
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(0);

        Blend blush = new Blend(
                BlendMode.SRC_ATOP,
                monochrome,
                new ColorInput(
                        0,
                        0,
                        piece.getImage().getWidth(),
                        piece.getImage().getHeight(),
                        color
                )
        );

        return blush;
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

        /* Build the "About" modal dialog window */
        Stage aboutDialog;
        aboutDialog = new Stage();
        aboutDialog.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/AboutDialog.fxml"))));
        aboutDialog.setTitle("About LaboonChess");
        aboutDialog.initModality(Modality.APPLICATION_MODAL);
        aboutDialog.initStyle(StageStyle.UTILITY);
        aboutDialog.setX(mnuMain.getScene().getWindow().getX() + (mnuMain.getScene().getWindow().getHeight()/12));
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(guiChessboard.getScene().getWindow());
        ArrayList<String> fenList = new ArrayList<>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                fenList.add(line);
            }
            reader.close();
        }
        catch (Exception e) {

        }
        chessboard = new ChessBoard(fenList);
    }

    @FXML
    private void handleDifficultyChangeAction(ActionEvent event) {
        switch (((RadioMenuItem)event.getSource()).getId()) {
            case "easy":
                difficulty = 0;
                lblStatus.setText("Game AI set to easy");
                break;

            case "medium":
                difficulty = 10;
                lblStatus.setText("Game AI set to medium");
                break;

            case "hard":
                difficulty = 20;
                lblStatus.setText("Game AI set to hard");
                break;
        }
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

        switch (((MenuItem)event.getSource()).getId()) {
            case "cpublack":
                playerType = 1;
                break;

            case "cpuwhite":
                playerType = 2;
                moveStockFish("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 100);
                break;

            case "multiplayer":
                // default
                break;

        }

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
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Save Your Game");
        dialog.setContentText("Enter File Name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            chessboard.saveGame(result.get());
        }
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

            } else if ((chessboard.turn() == 'w' && playerType == 2) || (chessboard.turn() == 'b' && playerType == 1)) {
                /* DO NOTHING: not player's designated turn */
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
                chessboard.addToHistory(chessboard.toFEN());
                // finished with second-click
                isFirstClick = true;                        // back to start (wait for a "first-click" again)
                guiChessPiece.setOpacity(1);                // opacity set back to show finished

                if(playerType > 0) {
                    moveStockFish(chessboard.toFEN(), 100);
                }

                // System.out.println("FEN: " + chessboard.toFEN());     // DEBUG
                System.out.println(chessboard.toFEN());     // DEBUG
                System.out.println(chessboard.reverseFEN());
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
