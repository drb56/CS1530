import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;
import entities.ChessBoard;
import entities.ChessBoardGUIProperties;
import services.stockfish.Stockfish;

public class LaboonChessDocumentController implements Initializable {

    /* FXML objects directly reference JavaFX GUI objects */
    @FXML private MenuBar mnuMain;          /* menu bar at the top of the application */
    @FXML private Label lblStatus;          /* TEMP, used as verbose output for testing */
    @FXML private Label lblTimer;           /* bottom-right, used to display the timer */
    @FXML private GridPane guiChessboard;   /* holds the GUI chessboard */

    private int timer_count = 0;            /* used for game clock, as the counter */
    Timeline gameTimer = null;              /* used for game clock, counting up from the time game was started */
    private boolean isFirstClick = true;    /* determines if this is to be considered the "first" or "second" chess board click */
    private ImageView guiChessPiece = null; /* holds first chess piece clicked on */
    private Pane guiChessSquare = null;     /* holds square from which first chess piece was clicked */
    private String san = null;              /* holds standard algebraic notation of first square and second square */
    private Stockfish stockfish;            /* chess API engine */
    private ChessBoard chessboard;          /* chessboard object model used to properly manipulate the GUI */
    private int playerType = 0;             /* determines whether player is white or black */
    private int game_difficulty = 0;        /* AI Difficulty (0=Easy, 10=Medium, 20=hard) */
    private String chesspiece_color1;       /* Black chess piece color */
    private String chesspiece_color2;       /* White chess piece color */
    private Random rand = new Random();     /* Random number generator */
    private String team1name = "WHITE";     /* team name. Changes with color scheme */
    private String team2name = "BLACK";     /* team name. Changes with color scheme */

    /* Random strings to use for the Kibitzer */
    private String[] chess_comments = {"Hurry up! You are taking too long!", "Yes, if you take longer, your IQ will go up",
            "Oh wow you are horrible", "Whatever you do, don't touch that rook!", "Good Luck", "Now that's a power grab!",
            "Zip it up, and Zip it out", "NOOO PAPA!", "CARM, THERE'S NO FKIN SMOKED TURKEY IN HERE?!", "I didn't wanna get blood all ova ya floor",
            "Money isn't real George, it only seems like it is", "Derek Ferrell, DEREK FKIN FERRELL!!", "The cosine of the tangent is not the sine",
            "That's thirty minutes away. I'll be there in ten", "Why do I have to be Mr. Pink?", "John, Paul, George and Ringo",
            "Now that's a TASTY burger", "We are the musicmakers, and we are the dreamers of dreams", "What'samatter Colonel Sanders... chicken?!",
            "50 million Elvis fans can't be wrong", "Nail in my head, from my creator", "All that glitters is not gold", "He called the shit 'POOP' hahahahha"
    };

    private ChessBoardGUIProperties board_images = new ChessBoardGUIProperties(); /* Chessboard GUI property class for holding all imageviews */

    public GridPane getChessGridBoardGridPane() {
        return this.guiChessboard;
    }

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
                // good to send commands

            } else {
                throw new RuntimeException("Could not start Stockfish engine...");
            }

            /* set up game timer */
            resetGameTimer();

            /* set up thread for random messages */
            setupMessageTimer();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Shows a random message in the status section of the GUI, once
     * every one to five seconds.
     */
    public void setupMessageTimer() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // show random message
                            lblStatus.setText(chess_comments[rand.nextInt(chess_comments.length)]);
                        }
                    });
                    // show next message within 1-5 seconds
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
                }
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Move the chess piece using the Stockfish API.
     *
     * @param fen fen string
     * @param timeWait time for waiting, longer = more difficult
     */
    public void moveStockFish(String fen, int timeWait) {
        ChessBoard.returnStatus status = ChessBoard.returnStatus.INVALID;
        String fromSquareStr = "";
        String toSquareStr = "";
        int count = 0;
        while ((status == ChessBoard.returnStatus.INVALID) && (count < 10)) {
            count++;
            stockfish.sendCommand("");

            String difficultyCommand = "setoption name Skill Level value " + game_difficulty;
            stockfish.sendCommand(difficultyCommand);

            //System.out.println(stockfish.getOutput(0));
            String move = stockfish.getBestMove(fen, timeWait);
            fromSquareStr = move.substring(0, 2);
            toSquareStr = move.substring(2, 4);

            // DEBUG
            System.out.println("AI moving from: " + fromSquareStr);
            System.out.println("To: " + toSquareStr);

            status = chessboard.move(fromSquareStr, toSquareStr);
        }

        if ((status != ChessBoard.returnStatus.INVALID) &&
                (status != ChessBoard.returnStatus.CHECKMATE)) {

            Pane fromSquare = getChessSquare(fromSquareStr);
            Pane toSquare = getChessSquare(toSquareStr);

            // Empty square
            guiChessPiece = (ImageView) fromSquare.getChildren().get(0);    // hold reference to this piece
            fromSquare.getChildren().remove(0);                       // remove the chess piece in original square

            if (!toSquare.getChildren().isEmpty()) {
                toSquare.getChildren().remove(0);
            }

            toSquare.getChildren().add(0, guiChessPiece);             // place the chess piece here

            if (status == ChessBoard.returnStatus.CASTLING) {
                performCastling(toSquare);
            }
        } else if (status == ChessBoard.returnStatus.CHECKMATE) {
            // GRAY ALL OF THE PIECES
            performCheckmate();
        }

        // create a history of all moves that were made
        if (status != ChessBoard.returnStatus.INVALID) {
            chessboard.addToHistory(chessboard.toFEN());
        }
    }


    /**
     * Displays an alert as to what team has won.
     */
    public void performCheckmate() {
        String team;

        if (chessboard.turn() == 'w') {
            team = team2name;
        } else {
            team = team1name;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkmate");
        alert.setHeaderText(null);
        alert.setContentText(String.format("The %s team has won!", team));

        alert.showAndWait();
    }


    /**
     * Given the current chess square, will perform
     *      the special "Castling" move that can be made.
     *
     * @param curSquare The currently-chosen chess square.
     */
    public void performCastling(Pane curSquare) {
        // get and set the proper rook to complete the castle
        switch (curSquare.getId()) {
            case "c8": // black queen-side
                ((Pane)guiChessboard.lookup("#d8")).getChildren().add(((Pane)guiChessboard.lookup("#a8")).getChildren().get(0));
                break;
            case "g8": // black king-side
                ((Pane)guiChessboard.lookup("#f8")).getChildren().add(((Pane)guiChessboard.lookup("#h8")).getChildren().get(0));
                break;
            case "c1": // white queen-side
                ((Pane)guiChessboard.lookup("#d1")).getChildren().add(((Pane)guiChessboard.lookup("#a1")).getChildren().get(0));
                break;
            case "g1": // white king-side
                ((Pane)guiChessboard.lookup("#f1")).getChildren().add(((Pane)guiChessboard.lookup("#h1")).getChildren().get(0));
                break;
        }
    }

    /**
     *  Gets the square given algebraic coordinate from fxml board
     *
     *  @param coordinate Algebraic coordinate string that correlates with the pane
     *  @return The GUI chess square associated with the given coordinate; or null
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

        switch (item.getId()) {
            case "deadpool":
                chesspiece_color1 = "#040603"; // black
                chesspiece_color2 = "#a5090c"; // firebrick
                team2name = "BLACK";
                team1name = "RED";
                break;
            case "election":
                chesspiece_color1 = "#49a2ce"; // steelblue
                chesspiece_color2 = "#ed4e31"; // tomato
                team2name = "BLUE";
                team1name = "RED";
                break;
            case "hulk":
                chesspiece_color1 = "#4d004d"; // purple
                chesspiece_color2 = "#70964b"; // green
                team2name = "PURPLE";
                team1name = "GREEN";
                break;
            case "ironman":
                chesspiece_color1 = "#cc0000"; // red
                chesspiece_color2 = "#ffa700"; // yellow
                team2name = "RED";
                team1name = "YELLOW";
                break;
            case "pitt":
                chesspiece_color1 = "#1c2957"; // blue
                chesspiece_color2 = "#cdb87d"; // gold
                team2name = "BLUE";
                team1name = "GOLD";
                break;
            case "wolverine":
                chesspiece_color1 = "#365382"; // saddlebrown
                chesspiece_color2 = "#f2c903"; // yellow
                team2name = "BROWN";
                team1name = "YELLOW";
                break;
        }

        // set the color for each chess piece
        setChessPieceColors(chesspiece_color1, chesspiece_color2);
    }


    /**
     * Takes two hex colors and applies them to the GUI chess pieces.
     *
     * @param color1 A hex value representing the color to set for the "black" chess pieces.
     * @param color2 A hex value representing the color to set for the "white" chess pieces.
     */
    public void setChessPieceColors(String color1, String color2) {
        // loop through the chess board and change each piece's color
        //      if a color is set
        if (color1 == null || color2 == null) { return; }

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
     * Changes the game_difficulty of the AI in the game. The values chosen
     *      are lowest, mid-range, and highest according to the
     *      StockFish API.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleDifficultyChangeAction(ActionEvent event) {
        switch (((RadioMenuItem)event.getSource()).getId()) {
            case "easy":
                game_difficulty = 0;
                break;

            case "medium":
                game_difficulty = 10;
                break;

            case "hard":
                game_difficulty = 20;
                break;
        }
    }


    /**
     * Resets the chessboard to the starting layout, and also resets the timer.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleNewGameAction(ActionEvent event) {
        chessboard = new ChessBoard();
        updateGameBoardGUIFromFen(chessboard);

        switch (((MenuItem)event.getSource()).getId()) {
            case "cpublack":
                playerType = 1;
                break;

            case "cpuwhite":
                playerType = 2;
                moveStockFish("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 100);
                break;

            case "multiplayer":
                playerType = 0;
                break;
        }

        // reset team colors
        resetTeamColors();

        // reset the game timer
        resetGameTimer();

        // make sure the click action gets reset
        isFirstClick = true;
    }


    /**
     * Resets the team colors to their default values.
     */
    public void resetTeamColors() {
        // reset team colors
        chesspiece_color1 = null;
        chesspiece_color2 = null;
    }


    /**
     * Starts or Resets the game clock timer.
     */
    public void resetGameTimer() {
        timer_count = 0;
        if (gameTimer == null) { // start
            this.gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event2) -> {
                lblTimer.setText(String.format("%d:%02d",
                        TimeUnit.SECONDS.toMinutes(++timer_count),
                        TimeUnit.SECONDS.toSeconds(timer_count) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timer_count))
                ));
            }));

            // Keep the timer going as long as the game is being played
            gameTimer.setCycleCount(Timeline.INDEFINITE);
            gameTimer.play();

        } else { // reset
            timer_count = 0;
        }
    }


    /**
     * Opens a dialog to choose a saved chess game to load and continue.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleLoadGameAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(guiChessboard.getScene().getWindow());
        if (file != null) {
            ArrayList<String> fenList = new ArrayList<>();
            try {
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                line = bufferedReader.readLine();
                playerType = Integer.parseInt(line);
                while ((line = bufferedReader.readLine()) != null) {
                    fenList.add(line);
                }
                reader.close();
            } catch (Exception e) {

            }
            chessboard = new ChessBoard(fenList);
            String[] fen = fenList.get(fenList.size()-1).split(" ");
            if(fen.equals("b")) {
                chessboard.setTurn(1);
            }
        }

        // reset team colors
        resetTeamColors();

        // update the GUI to reflect the loaded game's current FEN
        updateGameBoardGUIFromFen(chessboard);

        // make sure the click action gets reset
        isFirstClick = true;
    }


    /**
     * Opens a dialog to save the current chess game for later playing.
     *
     * TODO: Save in Portable Game Notation (PGN) format.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleSaveGameAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        File file = fileChooser.showSaveDialog(guiChessboard.getScene().getWindow());
        if (file != null) {
            try {
                file.createNewFile();
                chessboard.saveGame(file, playerType);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    /**
     * Using a log of FEN moves, will undo the last move and revert
     *      the chessboard back to its previous state.
     *
     * @param event The mouseclick event that was used to trigger this method. Contains the GUI object clicked.
     */
    @FXML
    private void handleUndoMoveAction(ActionEvent event) {
        chessboard.undoMove(playerType);                            // undo the move
        updateGameBoardGUIFromFen(chessboard);                      // update the GUI to reflect the undo
        setChessPieceColors(chesspiece_color1, chesspiece_color2);  // keep the same color scheme
        isFirstClick = true;                                        // make sure the click action gets reset
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
                setFirstClick(curSquare);
            }
        } else {
            /* SECOND-CLICK */
            String fromSquare = guiChessSquare.getId();                     // get the "first-click" chess square
            String toSquare = curSquare.getId();                            // get the "second-click" chess square
            ChessBoard.returnStatus status;

            if (guiChessSquare.equals(curSquare)) {
                /* USER CLICKED ON THEIR CURRENTLY HIGHLIGHTED PIECE */

                guiChessPiece.setOpacity(1);                // Unhighlight the currently highlighted piece
                isFirstClick = true;                        // back to start

            } else if (((status = chessboard.move(fromSquare, toSquare)) != ChessBoard.returnStatus.INVALID) &&
                (status != ChessBoard.returnStatus.CHECKMATE)) {
                /*
                    Valid move. See if we can place the chess piece from the
                    first click at this square on the board.
                */
                performValidMove(curSquare, status);
            } else if (status == ChessBoard.returnStatus.CHECKMATE) {
                performCheckmate();
            }
        }
    }


    /**
     * Set up the logic to reflect the user has made their FIRST CLICK.
     *
     * @param curSquare The GUI square the user clicked on
     */
    public void setFirstClick(Pane curSquare) {
        guiChessSquare = curSquare;                                 // hold reference to this square
        guiChessPiece = (ImageView) curSquare.getChildren().get(0); // hold reference to this piece
        guiChessPiece.setOpacity(.5);                               // set opacity to make it look "selected"
        isFirstClick = false;                                       // now wait for second-click
    }


    /**
     * A valid move was issued so carry out what is needed to reflect it
     *      within the GUI chessboard.
     *
     * @param curSquare The GUI square the user clicked on
     * @param status The return status from checking to see what kind of valid move was made
     */
    public void performValidMove(Pane curSquare, ChessBoard.returnStatus status) {

        if (curSquare.getChildren().isEmpty()) {
            /* EMPTY SQUARE HERE */
            curSquare.getChildren().add(0, guiChessPiece);    // place the chess piece here
            san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

            // perform special operation if Castling just occurred
            if (status == ChessBoard.returnStatus.CASTLING) {
                // get and set the proper rook to complete the castle
                performCastling(curSquare);
            }
        } else if (curSquare.getChildren().get(0).getId().matches("[a-z]")
                != guiChessPiece.getId().matches("[a-z]")) {  // make sure to not overtake your own team's piece
            /* OPPONENT PIECE EXISTS HERE */

            san = guiChessSquare.getId() + curSquare.getId();       // get the move in terms of SAN (e.g. e3d6)

            curSquare.getChildren().remove(0);                // remove the current chess piece
            curSquare.getChildren().add(0, guiChessPiece);    // insert the first-click piece onto this square
        }

        // perform special operation if a pawn was promoted
        if (status == ChessBoard.returnStatus.PAWNPROMOTION) {
            // remove the pawn and place a queen
            curSquare.getChildren().remove(0);

            // place white or black queen, depending on the team
            if (curSquare.getId().contains("8")) {
                curSquare.getChildren().add(board_images.getWhiteQueen());
            } else {
                curSquare.getChildren().add(board_images.getBlackQueen());
            }
        }

        // create a history of all moves that were made
        chessboard.addToHistory(chessboard.toFEN());

        // finished with second-click
        isFirstClick = true;                        // back to start (wait for a "first-click" again)
        guiChessPiece.setOpacity(1);                // opacity set back to show finished

        // if playerType == cpu
        if (playerType > 0) {
            //if (chessboard.turn() > 0) {
            moveStockFish(chessboard.toFEN(), 100);
        }

        // DEBUG
        System.out.println(chessboard.toFEN());
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


    /**
     * Reset the GUI chessboard.
     */
    public void clearGuiBoard() {
        List children = guiChessboard.getChildren();
        for(int i = 0; i < children.size(); i++) {
            Pane child = (Pane) children.get(i);
            List pane_properties = child.getChildren();

            if(pane_properties.size() > 0 && pane_properties.get(0).getClass().toString().equals("class javafx.scene.image.ImageView")) {
                pane_properties.remove(0);
            }

        }
    }


    /**
     * Set the GUI chessboard to a certain state, given a board configuration.
     *
     * @param board The chessboard configuration to set the GUI to reflect.
     */
    public void updateGameBoardGUIFromFen(ChessBoard board) {

        // clear the board before setting it.
        clearGuiBoard();

        char[][] board_state = board.getBoardState();
        for(int i = 0; i < board_state.length; i++) {
            for(int j = 0; j < board_state[0].length; j++) {
                switch(board_state[i][j]) {
                    case 'p':
                        ImageView img = board_images.getBlackPawn();
                        String san = board.indexToSan(i, j);
                        String algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'r':
                        img = board_images.getBlackRook();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'b':
                        img = board_images.getBlackBishop();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'n':
                        img = board_images.getBlackKnight();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'k':
                        img = board_images.getBlackKing();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'q':
                        img = board_images.getBlackQueen();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'P':
                        img = board_images.getWhitePawn();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'R':
                        img = board_images.getWhiteRook();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'N':
                        img = board_images.getWhiteKnight();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'B':
                        img = board_images.getWhiteBishop();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'K':
                        img = board_images.getWhiteKing();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;
                    case 'Q':
                        img = board_images.getWhiteQueen();
                        san = board.indexToSan(i, j);
                        algebraic_id = "#" + san;
                        ((Pane)guiChessboard.lookup(algebraic_id)).getChildren().add(img);
                        break;

                    default:
                        break;
                }
            }
        }
    }
}
